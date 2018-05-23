package com.beanu.l4_clean.support.mina.manager;

import android.content.Context;
import android.util.Log;

import com.beanu.arad.Arad;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_clean.support.mina.client.ConnectUtils;
import com.beanu.l4_clean.support.mina.client.FrameCodecFactory;
import com.beanu.l4_clean.support.mina.client.HeartBeatHandler;
import com.beanu.l4_clean.support.mina.client.HeartBeatListener;
import com.beanu.l4_clean.support.mina.client.HeartBeatMessageFactory;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 客户端  连接管理
 * Created by Beanu on 2017/11/14.
 */

public class ClientConnectManager {
    private static ClientConnectManager instance;


    public static ClientConnectManager getInstance() {
        if (null == instance) {
            instance = new ClientConnectManager();
        }
        return instance;
    }

    private ClientConnectManager() {

    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public void connect(final Context context) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                NioSocketConnector mSocketConnector = new NioSocketConnector();
                //设置协议封装解析处理
                mSocketConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new FrameCodecFactory()));
                //设置心跳包
                KeepAliveFilter heartFilter = new KeepAliveFilter(new HeartBeatMessageFactory());
                //每 5 分钟发送一个心跳包
                heartFilter.setRequestInterval(5 * 60);
                //心跳包超时时间 10s
                heartFilter.setRequestTimeout(10);
                // 获取过滤器链
                DefaultIoFilterChainBuilder filterChain = mSocketConnector.getFilterChain();
                filterChain.addLast("encoder", new ProtocolCodecFilter(new FrameCodecFactory()));
                // 添加编码过滤器 处理乱码、编码问题
                filterChain.addLast("decoder", new ProtocolCodecFilter(new FrameCodecFactory()));
                mSocketConnector.getFilterChain().addLast("heartbeat", heartFilter);
                //设置 handler 处理业务逻辑
                mSocketConnector.setHandler(new HeartBeatHandler(context));
                mSocketConnector.addListener(new HeartBeatListener(mSocketConnector));
                //配置服务器地址
                InetSocketAddress mSocketAddress = new InetSocketAddress(ConnectUtils.HOST, ConnectUtils.PORT);
                //发起连接
                ConnectFuture mFuture = mSocketConnector.connect(mSocketAddress);
                mFuture.awaitUninterruptibly();
                IoSession mSession = mFuture.getSession();
                Log.d("", "======连接成功" + mSession.toString());
                e.onNext(mSession);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        IoSession mSession = (IoSession) o;
                        Log.d("MainActivity", "======连接成功了吗====" + mSession.isConnected());
                        SessionManager.getInstance().setSeesion(mSession);
//                        SessionManager.getInstance().writeToServer("Connection Success\n");

                        //发送连接成功广播
                        if (mSession.isConnected()) {
                            Arad.bus.post(new EventModel.SocketConnect(true));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void rePeatConnect() {
        final boolean[] isRepeat = {false};
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                // 执行到这里表示Session会话关闭了，需要进行重连,我们设置每隔3s重连一次,如果尝试重连5次都没成功的话,就认为服务器端出现问题,不再进行重连操作
                int count = 0;// 记录尝试重连的次数
                NioSocketConnector mSocketConnector = null;
                while (!isRepeat[0] && count < 10) {
                    try {
                        count++;
                        if (mSocketConnector == null) {
                            mSocketConnector = new NioSocketConnector();
                        }
                        //设置协议封装解析处理
                        mSocketConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new FrameCodecFactory()));
                        //设置心跳包
                        KeepAliveFilter heartFilter = new KeepAliveFilter(new HeartBeatMessageFactory());
                        //每 5 分钟发送一个心跳包
                        heartFilter.setRequestInterval(5 * 60);
                        //心跳包超时时间 10s
                        heartFilter.setRequestTimeout(10);
                        // 获取过滤器链
                        DefaultIoFilterChainBuilder filterChain = mSocketConnector.getFilterChain();
                        filterChain.addLast("encoder", new ProtocolCodecFilter(new FrameCodecFactory()));
                        // 添加编码过滤器 处理乱码、编码问题
                        filterChain.addLast("decoder", new ProtocolCodecFilter(new FrameCodecFactory()));
                        mSocketConnector.getFilterChain().addLast("heartbeat", heartFilter);
                        //设置 handler 处理业务逻辑
                        mSocketConnector.setHandler(new HeartBeatHandler(context));
                        mSocketConnector.addListener(new HeartBeatListener(mSocketConnector));
                        //配置服务器地址
                        InetSocketAddress mSocketAddress = new InetSocketAddress(ConnectUtils.HOST, ConnectUtils.PORT);
                        //发起连接
                        ConnectFuture mFuture = mSocketConnector.connect(mSocketAddress);
                        mFuture.awaitUninterruptibly();
                        IoSession mSession = mFuture.getSession();
                        if (mSession.isConnected()) {
                            isRepeat[0] = true;
                            Log.d("", "======连接成功" + mSession.toString());
                            e.onNext(mSession);
                            e.onComplete();
                            break;
                        }
                    } catch (Exception e1) {
                        if (count == ConnectUtils.REPEAT_TIME) {
                            System.out.println(ConnectUtils.stringNowTime() + " : 断线重连"
                                    + ConnectUtils.REPEAT_TIME + "次之后仍然未成功,结束重连.....");
                            break;
                        } else {
                            System.out.println(ConnectUtils.stringNowTime() + " : 本次断线重连失败,1s后进行第" + (count + 1) + "次重连.....");
                            try {
                                Thread.sleep(1000);
                                System.out.println(ConnectUtils.stringNowTime() + " : 开始第" + (count + 1) + "次重连.....");
                            } catch (InterruptedException e12) {
                                Log.e("rePeatConnect ", "rePeatConnect e12" + e12);
                            }
                        }
                    }

                }

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        IoSession mSession = (IoSession) o;
                        Log.d("MainActivity", "======连接成功了吗====" + mSession.isConnected());
                        SessionManager.getInstance().setSeesion(mSession);
//                        SessionManager.getInstance().writeToServer("重新连接发起的请求\n");

                        //发送连接成功广播
                        Arad.bus.post(new EventModel.SocketConnect(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
