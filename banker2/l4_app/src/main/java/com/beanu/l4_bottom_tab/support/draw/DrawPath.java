package com.beanu.l4_bottom_tab.support.draw;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 画图 元数据
 * Created by Beanu on 2017/4/25.
 */

public class DrawPath {
    public Path path;// 路径
    public Paint paint;// 画笔

    public DrawPath() {
    }

    public DrawPath(Path path, Paint paint) {
        this.path = path;
        this.paint = paint;
    }
}
