package com.bokecc.sdk.mobile.demo.drm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bokecc.sdk.mobile.demo.drm.model.DownloadInfo;

import javax.crypto.spec.DESKeySpec;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("SimpleDateFormat")
public class DataSet {
    private final static String DOWNLOADINFO = "downloadinfo";
    private final static String VIDEOPOSITION = "videoposition";
    private final static String VIDEOINDEX = "videosindex";
    private static Map<String, DownloadInfo> downloadInfoMap;
    private static SQLiteOpenHelper sqLiteOpenHelper;

    public static void init(Context context) {

        sqLiteOpenHelper = new SQLiteOpenHelper(context, "demo", null, 2) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                String sql = "CREATE TABLE IF NOT EXISTS downloadinfo(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "videoId VERCHAR, " +
                        "title VERCHAR, " +
                        "progress INTEGER, " +
                        "progressText VERCHAR, " +
                        "status INTEGER, " +
                        "createTime DATETIME, " +
                        "definition INTEGER)";

                String videoSql = "CREATE TABLE IF NOT EXISTS uploadinfo(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "uploadId VERCHAR, " +
                        "status INTEGER, " +
                        "progress INTEGER, " +
                        "progressText VERCHAR, " +
                        "videoId VERCHAR, " +
                        "title VERCHAR, " +
                        "tags VERCHAR, " +
                        "description VERCHAR, " +
                        "filePath VERCHAR," +
                        " fileName VERCHAR, " +
                        "fileByteSize VERCHAR, " +
                        "md5 VERCHAR, " +
                        "uploadServer VERCHAR, " +
                        "serviceType VERCHAR, " +
                        "priority VERCHAR, " +
                        "encodeType VERCHAR, " +
                        "uploadOrResume VERCHAR," +
                        " createTime DATETIME)";

                String videoPositionSql = "CREATE TABLE IF NOT EXISTS videoposition(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "videoId VERCHAR, " +
                        "position INTEGER)";


                String videosIndexSql = "CREATE TABLE IF NOT EXISTS videosindex(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "videoId VERCHAR, " +
                        "videoIndex INTEGER)";

                db.execSQL(sql);
                db.execSQL(videoSql);
                db.execSQL(videoPositionSql);
                db.execSQL(videosIndexSql);

            }
        };

        downloadInfoMap = new HashMap<String, DownloadInfo>();
        reloadData();
    }



    private static void reloadData() {

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 重载下载信息
            synchronized (downloadInfoMap) {
                cursor = db.rawQuery("SELECT * FROM ".concat(DOWNLOADINFO), null);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = buildDownloadInfo(cursor);
                        downloadInfoMap.put(downloadInfo.getTitle(), downloadInfo);

                    } catch (ParseException e) {
                        Log.e("Parse date error", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            Log.e("cursor error", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //保存数据
    public static void saveData() {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        db.beginTransaction();

        try {
            //清除当前数据
            db.delete(DOWNLOADINFO, null, null);

            for (DownloadInfo downloadInfo : downloadInfoMap.values()) {
                ContentValues values = new ContentValues();
                values.put("videoId", downloadInfo.getVideoId());
                values.put("title", downloadInfo.getTitle());
                values.put("progress", downloadInfo.getProgress());
                values.put("progressText", downloadInfo.getProgressText());
                values.put("status", downloadInfo.getStatus());
                values.put("definition", downloadInfo.getDefinition());

                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                values.put("createTime", formater.format(downloadInfo.getCreateTime()));

                db.insert(DOWNLOADINFO, null, values);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("db error", e.getMessage());
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    public static List<DownloadInfo> getDownloadInfos() {

        return new ArrayList<DownloadInfo>(downloadInfoMap.values());
    }

    public static boolean hasDownloadInfo(String Title) {
        return downloadInfoMap.containsKey(Title);
    }

    public static DownloadInfo getDownloadInfo(String Title) {
        return downloadInfoMap.get(Title);
    }

    public static void addDownloadInfo(DownloadInfo downloadInfo) {
        synchronized (downloadInfoMap) {
            if (downloadInfoMap.containsKey(downloadInfo.getTitle())) {
                return;
            }

            downloadInfoMap.put(downloadInfo.getTitle(), downloadInfo);
        }
    }

    public static void removeDownloadInfo(String title) {
        synchronized (downloadInfoMap) {
            downloadInfoMap.remove(title);
        }
    }

    public static void updateDownloadInfo(DownloadInfo downloadInfo) {
        synchronized (downloadInfoMap) {
            downloadInfoMap.put(downloadInfo.getTitle(), downloadInfo);
        }

    }

    private static DownloadInfo buildDownloadInfo(Cursor cursor) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createTime = formater.parse(cursor.getString(cursor.getColumnIndex("createTime")));
        DownloadInfo downloadInfo = new DownloadInfo(cursor.getString(cursor.getColumnIndex("videoId")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getInt(cursor.getColumnIndex("progress")),
                cursor.getString(cursor.getColumnIndex("progressText")),
                cursor.getInt(cursor.getColumnIndex("status")),
                createTime,
                cursor.getInt(cursor.getColumnIndex("definition")));
        return downloadInfo;
    }

    //我添加的
    //保存退出课时详情界面时，最后播放的视频  index

    public static void insertVideosIndex(int index){
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("videosIndex", index);
            database.insert(VIDEOINDEX, null, values);
            Log.e("demo", "insert " + index );
            database.close();
        }
    }

    public static String getVideosIndex() {
        int index = 0;
        String videoid = "";
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        if (database.isOpen()) {
            //Cursor cursor = database.query(VIDEOINDEX, new String[]{"videosIndex"}, "videoId=?", new String[]{videoId}, null, null, null);
            Cursor cursor = database.query(VIDEOINDEX, new String[]{"videoId"}, null, null, null, null, "id desc", "0,1");
            if (cursor.moveToFirst()) {
                //index = cursor.getInt(cursor.getColumnIndex("videosIndex"));
                videoid =  cursor.getString(cursor.getColumnIndex("videoId"));
            }
            cursor.close();
            database.close();
        }
        return videoid;
    }
    //添加以上


    //保存视频最后播放到的位置到数据库
    public static void insertVideoPosition(String videoId, int position) {

        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("videoId", videoId);
            values.put("position", position);
            database.insert(VIDEOPOSITION, null, values);
            Log.e("demo", "insert " + videoId + " position" + position);
            database.close();
        }
    }

    //根据视频ID从数据库取出上次播放到的位置
    //该position是个int值，毫秒数
    public static int getVideoPosition(String videoId) {
        int position = 0;
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        if (database.isOpen()) {
            Cursor cursor = database.query(VIDEOPOSITION, new String[]{"position"}, "videoId=?", new String[]{videoId}, null, null, null);
            if (cursor.moveToFirst()) {
                position = cursor.getInt(cursor.getColumnIndex("position"));
            }
            cursor.close();
            database.close();
        }
        return position;
    }


    //更改播放课时的position （播放另一个视频）
    public static void updateVideoPosition(String videoId, int position) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("position", position);
            database.update(VIDEOPOSITION, values, "videoId=?", new String[]{videoId});
            Log.e("demo", "update " + videoId + " position" + position);
            database.close();
        }
    }
}
