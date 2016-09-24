package com.ysy.sourcer_slidingmenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.ysy.pattern.LockPatternActivity;
import com.ysy.student.activity.Option;
import com.ysy.student.activity.StudentListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Window;

/**
 * 
 * @{#} SplashActivity.java Create on 2013-5-2 下午9:10:01
 * 
 * class desc: 启动画面
 *
 * <p>
 * Copyright: Copyright(c) 2013
 * </p>
 * 
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 * 
 *
 */
public class SplashActivity extends Activity {

	SharedPreferences preferences;
	SharedPreferences PW;
	SharedPreferences SS;

	public static int OpenlockFrom;

	// boolean isFirstIn = false;

	// 延迟
	private static final long SPLASH_DELAY_MILLIS = 1000;
	//获取版本号
	public int getVersionCode(){
	    try {
	        return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
	    }catch(PackageManager.NameNotFoundException e){
	        return 0;
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences dayornight = SplashActivity.this.getSharedPreferences("dayornight", SplashActivity.this.MODE_PRIVATE);
		if (dayornight.getBoolean("dayornight",false) == false) {
//			this.setTheme(R.style.AppTheme); // 此处不注释掉的话，会和Mainfest里的theme冲突造成fullscreen失效
		}
		else if (dayornight.getBoolean("dayornight",false) == true) {
//			this.setTheme(R.style.AppTheme_Night); // 同上
			StudentListActivity.setBrightness(this, 0.0f);
		}
		setContentView(R.layout.splash);

		// SharedPreferences preferences = getSharedPreferences("first_pref",
		// MODE_PRIVATE);
		// isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 使用Handler的postDelayed方法，1秒后执行跳转到MainActivity
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// if (isFirstIn) {
				// InitDB();
				// }

				// 读取SharedPreferences中需要的数据
				preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
				int count = preferences.getInt("count", 0);
				// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
				if (count < getVersionCode()) {
				//每次更新App都会在配置文件中versionCode加1，如此保证每次更新不管是新用户还是老用户都可以重置数据库
					InitDB();
					Editor editor = preferences.edit();
					// 存入数据
					editor.putInt("count", getVersionCode());
					// 提交修改
					editor.commit();
				}

				goHome();
			}
		}, SPLASH_DELAY_MILLIS);
	}

	protected void InitDB() {
		String DB_PATH = "/data/data/com.ysy.sourcer_slidingmenu/databases/";
		String DB_NAME = "student_manager.db";
		// 检查 SQLite 数据库文件是否存在
		// if ((new File(DB_PATH + DB_NAME)).exists() == false) {
		// 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
		File f = new File(DB_PATH);
		// 如 database 目录不存在，新建该目录
		// if (!f.exists()) {
		f.mkdir();
		// }

		try {
			// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
			InputStream is = getBaseContext().getAssets().open(DB_NAME);
			// 输出流
			OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

			// 文件写入
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}

			// 关闭文件流
			os.flush();
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// }

	private void goHome() {
		// SharedPreferences preferences = getSharedPreferences("first_pref",
		// MODE_PRIVATE);
		// Editor editor = preferences.edit();
		// editor.putBoolean("isFirstIn", false);
		// editor.commit();
		OpenlockFrom = 0;

		PW = getSharedPreferences("password", this.MODE_PRIVATE);
		SS = getSharedPreferences("switch_state", this.MODE_PRIVATE);
		String passWord = PW.getString("password", null);
		boolean switch_state = SS.getBoolean("switch_state", false);
		if (passWord == null || switch_state == false) {
			Intent intent = new Intent(SplashActivity.this, StudentListActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
		else if (passWord != null && switch_state == true) {
			Intent intent = new Intent(SplashActivity.this, LockPatternActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}

}
