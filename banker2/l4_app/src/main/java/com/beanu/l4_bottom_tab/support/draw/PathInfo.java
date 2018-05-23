package com.beanu.l4_bottom_tab.support.draw;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PathInfo implements Serializable {

    List<SerPath> mSerPaths = new ArrayList<>();
    List<SerPath> mDeletePaths = new ArrayList<>();

    private SerPath mCurPath;

    public PathInfo() {
    }

    public void lineStart(float x, float y) {
        mCurPath = new SerPath();
        mCurPath.mPoints.add(new SerPoint(x, y));
    }

    public void lineMove(float x, float y) {
        mCurPath.mPoints.add(new SerPoint(x, y));
    }

    public void lineEnd(float x, float y, int color) {
        mCurPath.mPoints.add(new SerPoint(x, y));
        mCurPath.mColor = color;
        mSerPaths.add(mCurPath);
    }

    public void removeLine() {
        if (mSerPaths.size() > 0) {
            mSerPaths.remove(mSerPaths.size() - 1);
        }
    }

    public void undo() {
        if (mDeletePaths.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            SerPath dp = mDeletePaths.get(mDeletePaths.size() - 1);
            mSerPaths.add(dp);

            //将该路径从删除的路径列表中去除
            mDeletePaths.remove(mDeletePaths.size() - 1);
        }
    }

    public void clear() {
        mSerPaths.clear();
    }

    //-0-0--------------------------------------------
    private Paint transferPaint(SerPath sp) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(sp.mColor);
        paint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        paint.setStrokeCap(Paint.Cap.ROUND);// 形状
        paint.setDither(true);
        return paint;
    }

    private Path transferPath(SerPath sp) {
        Path path = new Path();
        SerPoint p;
        int size = sp.mPoints.size();

        if (size < 3) {
            return path;
        }

        p = sp.mPoints.get(0);
        path.moveTo(p.x, p.y);

        float ox = p.x;
        float oy = p.y;

        for (int i = 1; i < size - 1; i++) {
            p = sp.mPoints.get(i);
            path.quadTo(ox, oy, (ox + p.x) / 2, (oy + p.y) / 2);
            ox = p.x;
            oy = p.y;
        }

        p = sp.mPoints.get(size - 1);
        path.lineTo(p.x, p.y);

        return path;
    }

    public List<DrawPath> transfer() {
        List<DrawPath> pps = new ArrayList<>();
        for (SerPath sp : mSerPaths) {
            Paint paint = transferPaint(sp);
            Path path = transferPath(sp);
            pps.add(new DrawPath(path, paint));
        }
        return pps;
    }

    //内部类
    class SerPoint implements Serializable {
        private float x;
        private float y;

        public SerPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    class SerPath implements Serializable {
        private int mColor = Color.BLACK;
        private List<SerPoint> mPoints = new ArrayList<>();
    }
}
