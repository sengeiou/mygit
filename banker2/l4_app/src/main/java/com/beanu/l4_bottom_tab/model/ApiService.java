package com.beanu.l4_bottom_tab.model;

import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.bean.Version;
import com.beanu.l4_bottom_tab.adapter.provider.ExchangeNetOrder;
import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLesson;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsHistory;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordJson;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.BookImg;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.Comment;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.model.bean.ExamHistory;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.model.bean.LiveLessonTimeTable;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;
import com.beanu.l4_bottom_tab.model.bean.NewsTitle;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.OrderItem;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.model.bean.ToolsReport;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 业务接口
 */
public interface ApiService {

    /**
     * 1.1 初始化
     */
    @GET("initProgram")
    Observable<HttpModel<Version>> initProgram(@HeaderMap Map<String, String> header);


    /**
     * 1.3 获取轮播图
     *
     * @param position 轮播图位置 0 首页 1直播课 2高清网课
     */
    @GET("advList")
    Observable<HttpModel<List<BannerItem>>> banner_list(@HeaderMap Map<String, String> header, @Query("position") int position);

    /**
     * 1.4 获取七牛的TOKEN
     */
    @GET("getQNToken")
    Observable<HttpModel<String>> qn_token(@HeaderMap Map<String, String> header);

    /**
     * 1.6 反馈
     */
    @FormUrlEncoded
    @POST("feedBack")
    Observable<HttpModel<String>> feed_back(@HeaderMap Map<String, String> header, @Field("content") String content, @Field("title") String title);


    /**
     * 2.6 创建课程订单
     */
    @FormUrlEncoded
    @POST("createNetOrder")
    Observable<HttpModel<Map<String, String>>> createLessonOrder(@HeaderMap Map<String, String> header, @Field("type") int type, @Field("id") String lessonId, @Field("addressId") String addressId, @Field("remark") String remark);

    /**
     * 2.8 我的订单列表
     * <p>
     * type  0直播课 1高清网课 2图书
     */
    @GET("ordersList")
    Observable<HttpModel<PageModel<OrderItem>>> my_order_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> params, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 2.9 删除订单
     */
    @GET("delOrders")
    Observable<HttpModel<String>> delete_order(@HeaderMap Map<String, String> header, @Query("id") String orderId);

    /**
     * 2.10 个人练习报告
     */
    @GET("practiceReport")
    Observable<HttpModel<ToolsReport>> tools_practice_report(@HeaderMap Map<String, String> header);

    /**
     * 2.12 收藏题目
     */
    @GET("collectionQuestion")
    Observable<HttpModel<Integer>> collect_question(@HeaderMap Map<String, String> header, @Query("questionId") String questionId, @Query("type") int type, @Query("questionType") int questionType, @Query("subjectId") String subjectId, @Query("courseId") String courseId);

    /**
     * 2.13 练习历史列表
     *
     * @param type 0 练习 1试卷
     */
    @GET("answerRecordList")
    Observable<HttpModel<PageModel<ToolsHistory>>> tools_history_list(@HeaderMap Map<String, String> header, @Query("type") int type, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 2.14 错题本科目列表
     *
     * @param parentId 目录父类ID  第一级为0
     */
    @GET("wrongQuestionList")
    Observable<HttpModel<List<Course>>> tools_wrong_course_list(@HeaderMap Map<String, String> header, @Query("parentId") String parentId);

    /**
     * 2.15 错题本 题目列表
     */
    @GET("wrongQuestion")
    Observable<HttpModel<List<ExamQuestion>>> tools_wrong_question_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("courseId") String courseId);


    /**
     * 2.16 收藏 科目列表
     *
     * @param parentId 目录父类ID  第一级为0
     */
    @GET("userCollectCourseList")
    Observable<HttpModel<List<Course>>> tools_collect_course_list(@HeaderMap Map<String, String> header, @Query("parentId") String parentId);

    /**
     * 2.17 收藏 题目列表
     */
    @GET("userCollectionList")
    Observable<HttpModel<List<ExamQuestion>>> tools_collect_question_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("courseId") String courseId);

    /**
     * 2.18 删除错题（答对的题目也将中错题本中去除掉）
     *
     * @param ids 错题记录ID数组，逗号隔开
     */
    @FormUrlEncoded
    @POST("delWrongQuestion")
    Observable<HttpModel<String>> tools_delete_wrong_question(@HeaderMap Map<String, String> header, @Field("ids") String ids);


    /**
     * 2.19 笔记 科目列表
     *
     * @param parentId 目录父类ID  第一级为0
     */
    @GET("userNoteCourseList")
    Observable<HttpModel<List<Course>>> tools_note_course_list(@HeaderMap Map<String, String> header, @Query("parentId") String parentId);

    /**
     * 2.20 笔记 题目列表
     */
    @GET("userNoteQuestionList")
    Observable<HttpModel<List<ExamQuestion>>> tools_note_question_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("courseId") String courseId);

    /**
     * 2.21 修改个人信息
     */
    @FormUrlEncoded
    @POST("updateUsersInfo")
    Observable<HttpModel<String>> update_user_info(@HeaderMap Map<String, String> header, @Field("icon") String icon, @Field("nickname") String nickName, @Field("motto") String sign, @Field("sex") String sex, @Field("school") String school);

    /**
     * 2.22 修改密码
     */
    @FormUrlEncoded
    @POST("updateUsersPassword")
    Observable<HttpModel<String>> change_pwd(@HeaderMap Map<String, String> header, @Field("userId") String userId, @Field("password") String password, @Field("newPassword") String newPassword);


    /**
     * 2.26 向购物车中添加商品
     */
    @GET("addShoppingCart")
    Observable<HttpModel<Void>> addShopToCart(@HeaderMap Map<String, String> header, @Query("productId") String productId, @Query("num") int num);

    /**
     * 2.29 购物车中商品的数量
     */
    @GET("cartNum")
    Observable<HttpModel<Integer>> numOfShopCart(@HeaderMap Map<String, String> header);


    /**
     * 3.1 获取学科列表（缓存）
     */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("subjectList")
    Observable<HttpModel<List<Subject>>> subject_list(@HeaderMap Map<String, String> header);

    /**
     * 3.2 更新用户选的学科ID
     */
    @FormUrlEncoded
    @POST("updateSubjectId")
    Observable<HttpModel<String>> updateUserSelectedSubject(@HeaderMap Map<String, String> header, @Field("subjectId") String subjectId);


    /**
     * 3.3 获取科目列表(缓存一天)
     */
    @Headers("Cache-Control:public,max-age=86400")
    @GET("courseList")
    Observable<HttpModel<List<Course>>> course_list(@HeaderMap Map<String, String> header, @Query("id") String subjectId);

    /**
     * 4.1 获取资讯分类
     */
    @GET("infoTypeList")
    Observable<HttpModel<List<NewsTitle>>> news_type_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId);

    /**
     * 4.2 获取资讯内容
     */
    @GET("infoListByTypeId")
    Observable<HttpModel<PageModel<NewsItem>>> news_content_list(@HeaderMap Map<String, String> header, @Query("id") String id, @Query("provinceId") String provinceId, @Query("subjectId") String subjectId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 4.3 获取资讯详情
     */
    @GET("infoDetailById")
    Observable<HttpModel<NewsItem>> news_detail(@HeaderMap Map<String, String> header, @Query("id") String id);


    /**
     * 5.1 获取历年真题列表
     */
    @GET("paperList")
    Observable<HttpModel<PageModel<ExamHistory>>> test_paper_list(@HeaderMap Map<String, String> header, @Query("id") String id, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 5.2 获取真题的 练习题
     */
    @GET("questionList")
    Observable<HttpModel<List<ExamQuestion>>> test_paper_questions(@HeaderMap Map<String, String> header, @Query("id") String pagerId);


    /**
     * 5.3 提交真题答案
     */
    @FormUrlEncoded
    @POST("savePaperAnswerRecord")
    Observable<HttpModel<String>> post_test_paper_result(@HeaderMap Map<String, String> header, @Field("recordJson") String resultJson);

    /**
     * 5.4 获得真题的答题报告
     *
     * @param recordId 做题记录ID
     */
    @GET("answerPaperLog")
    Observable<HttpModel<AnswerRecordJson>> test_paper_report_detail(@HeaderMap Map<String, String> header, @Query("id") String recordId);

    /**
     * 5.5 真题错题分析
     *
     * @param recordId 做题记录ID
     * @param source   不传为全部解析 1错误解析
     */
    @GET("answerPaperLogList")
    Observable<HttpModel<List<ExamQuestion>>> test_paper_analysis_list(@HeaderMap Map<String, String> header, @Query("id") String recordId, @Query("source") int source);

    /**
     * 6.1 获取图书列表
     */
    @GET("bookList")
    Observable<HttpModel<PageModel<BookItem>>> book_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> param, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 6.2 获取图书相册
     */
    @GET("bookImgList")
    Observable<HttpModel<List<BookImg>>> book_img_list(@HeaderMap Map<String, String> header, @Query("id") String bookId);

    /**
     * 6.3 获取图书评论列表
     */
    @GET("bookEvaluateListMap")
    Observable<HttpModel<PageModel<Comment>>> book_comment_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> param, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 6.4 发表图书的评论
     */
    @FormUrlEncoded
    @POST("putBookEvaluate")
    Observable<HttpModel<String>> postBookComment(@HeaderMap Map<String, String> header, @Field("id") String bookId, @Field("content") String content, @Field("starRating") int starRating);


    /**
     * 6.5 推荐图书列表
     */
    @GET("bookRecommendList")
    Observable<HttpModel<PageModel<BookItem>>> recommend_book_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);


    /**
     * 6.6 获取图书详情
     */
    @GET("bookDetailById")
    Observable<HttpModel<BookItem>> book_detail(@HeaderMap Map<String, String> header, @Query("id") String id);


    /**
     * 7.1 根据科目ID获得练习题
     */
    @GET("exercisesList")
    Observable<HttpModel<List<ExamQuestion>>> random_exercises_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("courseId") String courseId);

    /**
     * 7.2 智能练习 练习题
     */
    @GET("intelligentPaperList")
    Observable<HttpModel<List<ExamQuestion>>> random_ai_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId);

    /**
     * 7.3 提交答案
     */
    @FormUrlEncoded
    @POST("saveAnswerRecord")
    Observable<HttpModel<String>> postExamResult(@HeaderMap Map<String, String> header, @Field("recordJson") String resultJson);

    /**
     * 7.4 获得答题报告
     *
     * @param id   做题记录ID
     * @param type 0 练习 1智能练习
     */
    @GET("answerLog")
    Observable<HttpModel<AnswerRecordJson>> exam_report_detail(@HeaderMap Map<String, String> header, @Query("id") String id, @Query("type") int type);


    /**
     * 7.5 答题详情
     *
     * @param id     做题记录ID
     * @param type   0 练习 1智能练习
     * @param source 不传为全部解析 1错误解析
     * @return
     */
    @GET("answerLogList")
    Observable<HttpModel<List<ExamQuestion>>> exam_analysis_list(@HeaderMap Map<String, String> header, @Query("id") String id, @Query("type") int type, @Query("source") int source);

    /**
     * 7.6 创建或更新笔记
     */
    @FormUrlEncoded
    @POST("createNote")
    Observable<HttpModel<String>> post_note(@HeaderMap Map<String, String> header, @Field("id") String recordId, @Field("type") int type, @Field("questionType") int questionType, @Field("courseId") String courseId, @Field("content") String content, @Field("imgOne") String imgOne, @Field("imgTwo") String imgTwo, @Field("imgThree") String imgThree, @Field("imgFour") String imgFour);

    /**
     * 7.7 获取笔记
     */
    @GET("getQuestionNote")
    Observable<HttpModel<ExamNote>> request_question_note(@HeaderMap Map<String, String> header, @Query("id") String questionId, @Query("type") int type, @Query("questionType") int questionType, @Query("courseId") String courseId);


    /**
     * 8.1 高清网课列表
     */
    @GET("recordingList")
    Observable<HttpModel<PageModel<OnlineLesson>>> online_lesson_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 8.2 高清网课详情
     */
    @GET("recordingDetail")
    Observable<HttpModel<OnlineLesson>> online_lesson_detail(@HeaderMap Map<String, String> header, @Query("id") String lessonId);

    /**
     * 8.3 高清网课教师列表
     */
    @GET("recordTeacherList")
    Observable<HttpModel<List<TeacherIntro>>> online_lesson_teacher_list(@HeaderMap Map<String, String> header, @Query("id") String lessonId);

    /**
     * 8.4 高清网课／直播课 评论列表
     */
    @GET("recordingCommentList")
    Observable<HttpModel<PageModel<Comment>>> comment_list(@HeaderMap Map<String, String> header, @Query("id") String lessonId, @Query("type") int type, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 8.5 高清网课课时列表
     */
    @GET("recordingDetailList")
    Observable<HttpModel<List<Period>>> online_lesson_period_list(@HeaderMap Map<String, String> header, @Query("id") String lessonId);

    /**
     * 8.6 发表评论
     */
    @FormUrlEncoded
    @POST("putNetEvaluate")
    Observable<HttpModel<String>> postComment(@HeaderMap Map<String, String> header, @Field("id") String lessonId, @Field("content") String content, @Field("starRating") int starRating);


    /**
     * 8.7 直播课列表
     */
    @GET("directSeedingList")
    Observable<HttpModel<PageModel<LiveLesson>>> live_lesson_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> params, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 8.8 直播课详情
     */
    @GET("seedingDetail")
    Observable<HttpModel<LiveLesson>> live_lesson_detail(@HeaderMap Map<String, String> header, @Query("id") String lessonId);

    /**
     * 8.9 直播课 教师列表
     */
    @GET("seedTeacherList")
    Observable<HttpModel<List<TeacherIntro>>> live_lesson_teacher_list(@HeaderMap Map<String, String> header, @Query("id") String lessonId);

    /**
     * 8.10 直播课 课时列表
     */
    @GET("directDetailList")
    Observable<HttpModel<List<LiveLessonTimeTable>>> live_lesson_time_table_list(@HeaderMap Map<String, String> header, @Query("id") String liveLessonId);

    /**
     * 8.11 推荐直播课列表
     */
    @GET("directRecommendList")
    Observable<HttpModel<PageModel<LiveLesson>>> recommend_live_lesson_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 8.12 推荐高清网课列表
     */
    @GET("recordingRecommendList")
    Observable<HttpModel<PageModel<OnlineLesson>>> recommend_online_lesson_list(@HeaderMap Map<String, String> header, @Query("subjectId") String subjectId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);


    /**
     * 8.13 我的直播课
     *
     * @param state 状态 0未开课 1直播中 2回放
     * @param time  查询时间
     * @param month 2017-05
     */
    @GET("myDirectSeedingList")
    Observable<HttpModel<List<MyLiveLesson>>> my_live_lesson_list(@HeaderMap Map<String, String> header, @Query("state") String state, @Query("time") String time, @Query("month") String month);


    /**
     * 8.14 我的高清网课
     */
    @GET("myRecordingList")
    Observable<HttpModel<List<OnlineLesson>>> my_online_lesson_list(@HeaderMap Map<String, String> header);


    /**
     * 8.15 热门高清网课
     */
    @GET("hotRecording")
    Observable<HttpModel<Online_HotLesson>> online_lesson_hot(@Query("subjectId") String subjectId);

    /**
     * 9.1 获取教师详情
     */
    @GET("teacherDetail")
    Observable<HttpModel<TeacherIntro>> teacher_detail(@HeaderMap Map<String, String> header, @Query("id") String teacherId);

    /**
     * 9.2 教师 正在出售课程列表
     */
    @GET("beingSaleList")
    Observable<HttpModel<PageModel<LiveLesson>>> teacher_sale_lesson_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> param, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    /**
     * 9.3 发表评论
     */
    @FormUrlEncoded
    @POST("putTeacherEvaluate")
    Observable<HttpModel<String>> postTeacherComment(@HeaderMap Map<String, String> header, @Field("id") String lessonId, @Field("content") String content, @Field("starRating") int starRating);

    /**
     * 9.4 获取教师评论列表
     */
    @GET("teacherEvaluateListMap")
    Observable<HttpModel<PageModel<Comment>>> teacher_comment_list(@HeaderMap Map<String, String> header, @QueryMap Map<String, Object> param, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);


    /**
     * 10.1 我的--兑换码
     */

    @GET("exchangeNetOrder")
    Observable<HttpModel<Object>> exchange_netorder(@Query("code") String code);


}





