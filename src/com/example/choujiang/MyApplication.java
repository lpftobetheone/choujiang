/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:SJS
 */
package com.example.choujiang;


import android.app.Application;

import com.example.choujiang.exception.CrashHandler;


/**
 *@Title:
 *@Description:
 *@Author:liupf5
 *@Since:2015-10-19
 *@Version:1.1.0
 */
public class MyApplication extends Application{
	
	/* 
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initErrorHandler();
	}
	
	/**
	 * 捕获全局异常
	 * 
	 * @Description:
	 */
	private void initErrorHandler() {
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(this);
	}

}
