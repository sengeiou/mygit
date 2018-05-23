package com.beanu.l4_bottom_tab.ui.module1_exam;

import java.util.List;

/**
 * 作答反馈接口
 * Created by Beanu on 2017/4/12.
 */

public interface IExamResponseListener {

    //单选
    void onRadio(String selectedId, int whichOne);

    //多选
    void onMutil(List<String> selectedID, int whichOne);

    //判断
    void onJudge(String selectId, int whichOne);

}
