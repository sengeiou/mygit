package com.beanu.l4_clean.model;


import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.bean.GlobalConfig;
import com.beanu.l3_common.model.bean.SiteClass;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l4_clean.model.bean.Anchor;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.BillItem;
import com.beanu.l4_clean.model.bean.Commodity;
import com.beanu.l4_clean.model.bean.Coupon;
import com.beanu.l4_clean.model.bean.CrawlRecord;
import com.beanu.l4_clean.model.bean.DeliverOrder;
import com.beanu.l4_clean.model.bean.DeliverOrderDetail;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.DollsMachine;
import com.beanu.l4_clean.model.bean.FirendDollsHeader;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.beanu.l4_clean.model.bean.HelpUser;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.model.bean.LiveRoom2Audience;
import com.beanu.l4_clean.model.bean.Message;
import com.beanu.l4_clean.model.bean.MyDolls;
import com.beanu.l4_clean.model.bean.MyDollsHeader;
import com.beanu.l4_clean.model.bean.PKDetail;
import com.beanu.l4_clean.model.bean.PKMatch;
import com.beanu.l4_clean.model.bean.PKMatchDetail;
import com.beanu.l4_clean.model.bean.RankPeople;
import com.beanu.l4_clean.model.bean.RechargeOption;
import com.beanu.l4_clean.model.bean.Rewards;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.model.bean.Task;
import com.beanu.l4_clean.model.bean.Winners;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jam on 16-5-17
 * Description:
 */
public interface APIService {

    /**
     * 1.01初始化
     */
    @GET("initProgram")
    Observable<HttpModel<GlobalConfig>> getConfig();

    /**
     * 1.03上传文件
     */
    @GET("uploadFile")
    Observable<HttpModel<String>> uploadImgFile();

    /**
     * 1.05获取七牛token
     */
    @GET("getQNToken")
    Observable<HttpModel<String>> getQNToken();

    /**
     * 1.06 banner
     *
     * @param number 位置 0首页 1我要抓娃娃
     */
    @GET("advList")
    Observable<HttpModel<List<BannerItem>>> bannerList(@Query("position") int number);

    /**
     * 1.07 分享
     */
    @GET("shareUrl")
    Observable<HttpModel<Map<String, String>>> shareUrl();

    /**
     * 2.02 用户登录
     *
     * @param type   登录类型 0 账号登录 1ＱＱ登录　２微信登录
     * @param mobile 账号
     * @param pwd    密码
     */
    @FormUrlEncoded
    @POST("login")
    Observable<HttpModel<User>> login(@Field("loginType") String type, @Field("mobile") String mobile, @Field("password") String pwd);


    /**
     * 2.05 修改个人信息
     */
    @FormUrlEncoded
    @POST("updateUsersInfo")
    Observable<HttpModel<String>> update_user_info(@Field("icon") String icon, @Field("nickname") String nickName, @Field("motto") String motto, @Field("sex") String sex);

    /**
     * 2.06更新融云的token
     */
    @FormUrlEncoded
    @POST("updateUsersRongYunToken")
    Observable<HttpModel<String>> updateRongCloudToken(@Field("userId") String userId);


    /**
     * 2.07 通过ID获取用户简要信息
     */
    @Headers("Cache-Control:public,max-age=86400")
    @GET("userInfoById")
    Observable<HttpModel<Friend>> getFriendInfo(@Query("userId") String userId);


//    /**
//     * 2.09未发货订单
//     */
//    @FormUrlEncoded
//    @POST("myDollCaughtIn")
//    Observable<HttpModel<PageModel<MyDolls>>> myDollCaughtIn(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 2.09未发货订单
     */
    @GET("myDollCaughtIn")
    Observable<HttpModel<MyDollsHeader>> myDollCaughtIn2();


    /**
     * 2.10 确认订单
     */
    @FormUrlEncoded
    @POST("confirmOrder")
    Observable<HttpModel<String>> confirmOrder(@Field("logId") String logId, @Field("remark") String remark, @Field("addressId") String addressId);


    /**
     * 2.14已发货订单
     */
    @FormUrlEncoded
    @POST("myCashPrizeList")
    Observable<HttpModel<PageModel<DeliverOrder>>> myDollFinish(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 2.15 订单详情
     */
    @FormUrlEncoded
    @POST("orderDetails")
    Observable<HttpModel<DeliverOrderDetail>> orderDetail(@Field("orderId") String orderId);

//    /**
//     * 2.16 对战记录列表
//     */
//    @FormUrlEncoded
//    @POST("battleRecordList")
//    Observable<HttpModel<PageModel<Commodity>>> pkRecordList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 2.17 对战详情
     */
    @FormUrlEncoded
    @POST("battleRecordDetails")
    Observable<HttpModel<PKDetail>> pkRecordDetail(@Field("matchId") String battleRecordId);

    /**
     * 2.19 充值列表
     */
    @GET("rechargeList")
    Observable<HttpModel<List<RechargeOption>>> rechargeList();

    /**
     * 2.22 获取最近使用的地址
     */
    @GET("latelyAddress")
    Observable<HttpModel<Map<String, String>>> myDefaultAddress();

    /**
     * 2.23 反馈
     */
    @FormUrlEncoded
    @POST("feedback")
    Observable<HttpModel<String>> feedBack(@Field("content") String content);

    /**
     * 2.24 我的消息列表
     */
    @FormUrlEncoded
    @POST("myMessageList")
    Observable<HttpModel<PageModel<Message>>> messageList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 2.25 开心币账单
     */
    @FormUrlEncoded
    @POST("conisLogList")
    Observable<HttpModel<PageModel<BillItem>>> coinList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 2.26 当前用户余额
     */
    @GET("getUserCoins")
    Observable<HttpModel<Map<String, Integer>>> userBalance();

    /**
     * 2.27 预签到
     */
    @GET("preSign")
    Observable<HttpModel<Rewards>> rewardsInfo();

    /**
     * 2.28 签到领取
     */
    @GET("userSign")
    Observable<HttpModel<Object>> userRewards();

    /**
     * 2.29 兑换积分
     */
    @FormUrlEncoded
    @POST("dollToScore")
    Observable<HttpModel<Object>> dollToScore(@Field("logId") String logId);


    /**
     * 3.01 娃娃列表
     */
    @GET("dollTypeList")
    Observable<HttpModel<PageModel<SiteClass>>> siteClassList();

    /**
     * 3.02 娃娃机列表
     */
    @FormUrlEncoded
    @POST("dollMachineListAll")
    Observable<HttpModel<PageModel<Dolls>>> dollMachineList(@Field("id") String id, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 3.03 娃娃机前三个
     */
    @GET("dollMachineTopThree")
    Observable<HttpModel<PageModel<Dolls>>> dollMachineTOPList();


    /**
     * 3.04 娃娃机详情
     */
    @FormUrlEncoded
    @POST("dollMachineDetail")
    Observable<HttpModel<DollsMachine>> dollMachineDetail(@Field("dollMaId") String dollMaId);


    /**
     * 3.06 抓娃娃达人
     */
    @FormUrlEncoded
    @POST("dollCaughtTotal")
    Observable<HttpModel<PageModel<Winners>>> winnerList(@Field("dollMaId") String dollMaId, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 3.07抢占机器    @POST("dollStartPrize")
     */
    @FormUrlEncoded
    @POST("dollStartPrize")
    Observable<HttpModel<SeizeResult>> seizeMachine(@Field("dollId") String dollId);

    /**
     * 3.08获取抓娃娃结果
     */
    @FormUrlEncoded
    @POST("isSuccess")
    Observable<HttpModel<Map<String, String>>> gameResult(@Field("id") String id);


    /**
     * 4.01 当前是否有我发起的比赛
     */
    @GET("isExistenceMatch")
    Observable<HttpModel<PKMatch>> isExistenceMath();

    /**
     * 4.02 接受邀请
     */
    @FormUrlEncoded
    @POST("acceptInvitation")
    Observable<HttpModel<Map<String, String>>> invite_accept(@Field("code") String code);

    /**
     * 4.03 PK比赛用的机器
     */
    @GET("dollMachineListFree")
    Observable<HttpModel<List<Dolls>>> pkMachineList();

    /**
     * 4.04比赛确认
     */
    @FormUrlEncoded
    @POST("confirmReady")
    Observable<HttpModel<Map<String, String>>> pkConfirmReady(@Field("dollMaId") String dollMaId, @Field("id") String matchId);


    /**
     * 4.05发起PK
     */
    @GET("putCompetition")
    Observable<HttpModel<Map<String, String>>> invite2PK();

    /**
     * 4.07结束游戏(异常退出的情况 调用一下)
     */
    @FormUrlEncoded
    @POST("gameOver")
    Observable<HttpModel<String>> pkMatchOver(@Field("id") String matchId);

    /**
     * 4.08根据比赛ID获取双方娃娃机
     */
    @FormUrlEncoded
    @POST("machineInfoByMatchId")
    Observable<HttpModel<PKMatchDetail>> pkMatchDetail(@Field("id") String matchId);

    /**
     * 4.09比赛继续抓
     */
    @FormUrlEncoded
    @POST("goOnGame")
    Observable<HttpModel<String>> pkMatchGoOn(@Field("dollMaId") String dollMaId, @Field("id") String id);

    /**
     * 5.01 在线主播列表
     */
    @FormUrlEncoded
    @POST("anchorListPage")
    Observable<HttpModel<PageModel<Anchor>>> anchorList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 5.02进入直播间
     */
    @FormUrlEncoded
    @POST("startLive")
    Observable<HttpModel<LiveRoom>> livingRoomDetail(@Field("machineId") String machineId, @Field("cover") String cover, @Field("title") String title);

    /**
     * 5.03主播帮我抓
     */
    @FormUrlEncoded
    @POST("anchorHelpDraw")
    Observable<HttpModel<Object>> anchorHelpMeDraw(@Field("logId") String logId);

    /**
     * 5.04主播帮我抓列表
     */
    @FormUrlEncoded
    @POST("helpDrawList")
    Observable<HttpModel<List<HelpMeCraw>>> anchorHelpMeList(@Field("logId") String logId);

    /**
     * 5.05 主播接受OR拒绝 帮抓娃娃
     */
    @FormUrlEncoded
    @POST("anchorUpdateLog")
    Observable<HttpModel<HelpUser>> anchorAccepted(@Field("id") String id, @Field("isAccept") String isAccept);

    /**
     * 5.06主播开始游戏
     */
    @FormUrlEncoded
    @POST("anchorStartPrize")
    Observable<HttpModel<SeizeResult>> anchorBeginGame(@Field("dollId") String dollId);

    /**
     * 5.07获取房间详情
     */
    @FormUrlEncoded
    @POST("roomDetail")
    Observable<HttpModel<LiveRoom2Audience>> audienceLiveRoom(@Field("roomId") String roomId, @Field("logId") String logId);

    /**
     * 5.08主播退出游戏
     */
    @FormUrlEncoded
    @POST("finishLive")
    Observable<HttpModel<Object>> anchorEndGame(@Field("roomId") String roomId, @Field("logId") String logId);

    /**
     * 5.09申请成为主播
     */
    @FormUrlEncoded
    @POST("anchorApply")
    Observable<HttpModel<Object>> anchorApply(@FieldMap Map<String, String> params);


    /**
     * 5.30 分享游戏
     */
    @FormUrlEncoded
    @POST("shareGame")
    Observable<HttpModel<Map<String, String>>> shareGame(@Field("id") String gameId);


    //==================================新接口======================================================

    /**
     * 1.09 任务完成的情况
     */
    @GET("completeTask")
    Observable<HttpModel<Map<String, Integer>>> taskProgress();

    /**
     * 1.10 分享朋友圈获得积分
     */
    @GET("shareSucceed")
    Observable<HttpModel<Object>> shareRewards();


    /**
     * 2.04我的免费卡列表
     */
    @FormUrlEncoded
    @POST("myFreeCardListAll")
    Observable<HttpModel<PageModel<Coupon>>> couponList(@Field("type") int type, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 2.05 我的奖品
     */
    @FormUrlEncoded
    @POST("myPrizeList")
    Observable<HttpModel<PageModel<MyDolls>>> myPrizeList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 2.19 登录领取免费卡
     */
    @GET("receiveFreeCard")
    Observable<HttpModel<Map<String, String>>> receiveFreeCard();

    /**
     * 2.20 免费卡数量
     */
    @GET("freeCardNum")
    Observable<HttpModel<Map<String, Integer>>> freeCardNum();

    /**
     * 2.21 获取用户信息
     */
    @GET("getUsersInfo")
    Observable<HttpModel<User>> loginWhitId();


    /**
     * 2.24 绑定邀请码
     */
    @FormUrlEncoded
    @POST("bindCode")
    Observable<HttpModel<Object>> userBinding(@Field("code") String code);


    /**
     * 3.05排行榜
     */
    @FormUrlEncoded
    @POST("rankingList")
    Observable<HttpModel<PageModel<RankPeople>>> rankList(@Field("type") int type, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 3.06获取同款娃娃机
     */
    @FormUrlEncoded
    @POST("sameDollMachine")
    Observable<HttpModel<PageModel<Dolls>>> sameDollMachine(@Field("typeId") String typeId, @Field("dollId") String dollId);


    /**
     * 3.07 抓中记录
     */
    @FormUrlEncoded
    @POST("dollCaughtIn")
    Observable<HttpModel<PageModel<Winners>>> winnerRecordList(@Field("dollMaId") String dollMaId, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 3.08好友 抓到的娃娃列表
     */
    @FormUrlEncoded
    @POST("dollCaughtInUser")
    Observable<HttpModel<PageModel<MyDolls>>> myFriendCaughtList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);

    /**
     * 3.08好友 抓到的娃娃列表
     */
    @FormUrlEncoded
    @POST("dollCaughtInUser")
    Observable<HttpModel<FirendDollsHeader>> myFriendCaughtHeader(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 3.09 用户  抓取记录
     *
     * @param type 0 全部 1抓中
     */
    @FormUrlEncoded
    @POST("dollCaughtListUser")
    Observable<HttpModel<PageModel<CrawlRecord>>> crawlRecordList(@Field("type") int type, @Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 3.10 申述
     */
    @FormUrlEncoded
    @POST("gameAppeal")
    Observable<HttpModel<Object>> recordReport(@FieldMap Map<String, String> params);


    /**
     * 4.01 积分商城
     */
    @FormUrlEncoded
    @POST("scoreProductList")
    Observable<HttpModel<PageModel<Commodity>>> shopList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 4.02 商品详情
     */
    @GET("scoreProductDetail")
    Observable<HttpModel<Commodity>> shopDetail(@Query("id") String id);

    /**
     * 4.03 兑换记录
     */
    @FormUrlEncoded
    @POST("myScoreOrderList")
    Observable<HttpModel<PageModel<Commodity>>> exchangeRecordList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    /**
     * 4.06 积分兑换商品
     */
    @FormUrlEncoded
    @POST("scoreCashProduct")
    Observable<HttpModel<Object>> scoreToProduct(@Field("id") String id, @Field("addressId") String addressId);


    /**
     * 任务列表
     */
    @FormUrlEncoded
    @POST("anchorListPage")
    Observable<HttpModel<PageModel<Task>>> taskList(@Field("pageNumber") int pageNumber, @Field("pageSize") int pageSize);


    //
//
//    /**
//     * 6.03微信签名
//     */
//    @GET("wxSign")
//    Observable<HttpModel<Map<String, String>>> requestWePaySign(@Query("id") String orderId);


}