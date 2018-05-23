package com.bokecc.sdk.mobile.demo.drm.util;

public class ConfigUtil {

	// 配置API KEY
	public final static String API_KEY = "zdBhaUTLxJkGyXPSSp9AwZbZPSyDAScq";

	// 配置帐户ID
	public final static String USERID = "C2D19C4B5920B4E8";

	// 配置下载文件路径
	public final static String DOWNLOAD_DIR = "CCDownload";
	
	// 配置视频回调地址
	public final static String NOTIFY_URL = "http://www.example.com";
	
	/** Fragment */
	
	public final static int MAIN_FRAGMENT_MAX_TAB_SIZE = 2;
	public final static int PLAY_TAB = 0;
	public final static int DOWNLOAD_TAB = 1;
	
	public final static int DOWNLOAD_FRAGMENT_MAX_TAB_SIZE = 2;
	public final static int DOWNLOADED = 0;
	public final static int DOWNLOADING = 1;
	
	/** Service Action */
	
	public final static String ACTION_DOWNLOADED = "demo.service.downloaded";
	public final static String ACTION_DOWNLOADING = "demo.service.downloading";
	
	/** Download Group ID */
	public final static int DOWNLOADING_MENU_GROUP_ID = 20000000;
	public final static int DOWNLOADED_MENU_GROUP_ID = 20000001;

}
