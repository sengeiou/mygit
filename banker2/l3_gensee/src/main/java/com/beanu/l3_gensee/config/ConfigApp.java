package com.beanu.l3_gensee.config;

import android.os.Environment;

import java.io.File;

public class ConfigApp {
	public static final String PARAMS_DOMAIN = "PARAMS_DOMAIN";
	public static final String PARAMS_TYPE = "PARAMS_TYPE";
	public static final String PARAMS_NUMBER = "PARAMS_NUMBER";
	public static final String PARAMS_ACCOUNT = "PARAMS_ACCOUNT";
	public static final String PARAMS_PWD = "PARAMS_PWD";
	public static final String PARAMS_NICKNAME = "PARAMS_NICKNAME";
	public static final String PARAMS_JOINPWD = "PARAMS_JOINPWD";
	public static final String PARAMS_JOINSUCCESS = "PARAMS_JOINSUCCESS";
	public static final String PARAMS_VIDEO_FULLSCREEN = "PARAMS_VIDEO_FULLSCREEN";
	
	
	public static final String WEBCAST = "WEBCAST";
	public static final String TRAINING = "TRAINING";
	
	
	
	public static String ROOTPAHT = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "playersdkdemo" + File.separator;
	public static String LOGPATH = ROOTPAHT + "log" + File.separator;
	
}
