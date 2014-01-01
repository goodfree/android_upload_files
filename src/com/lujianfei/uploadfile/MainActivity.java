package com.lujianfei.uploadfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lujianfei.uploadfile.HttpConnHelper.IProgressListener;

public class MainActivity extends Activity implements OnClickListener{
	private static final String tag = "MainActivity";
	TextView  txt_progress;
	Button bt_do;
	List<String> files = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bt_do = (Button)findViewById(R.id.bt_do);
		txt_progress = (TextView)findViewById(R.id.txt_progress);
		
		bt_do.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		files.add("/mnt/sdcard/file.apk");
		txt_progress.setText("Uploading...");
		new Thread(){
			public void run() {
				String result = HttpConnHelper.doPost("http://192.168.1.107/test_upload.php", 
						null,
						files,
						"attach",
						mIProgressListener);
				log(result);
			};
		}.start();
		//log(getSDPath());
		// /mnt/sdcard
	}
	IProgressListener mIProgressListener = new IProgressListener() {
		@Override
		public void onProgressReceive(final int currentsize, final int totalsize) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
				txt_progress.setText(String.format("%s/%s", currentsize,totalsize));
				}
			});
		}
	};
	protected String getSDPath() {
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		  if (sdCardExist) {//有SD卡
		   sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		   return sdDir.toString();
		  }
		  return "";
	 }
	
	void log(String msg){
		Log.d(tag, msg);
	}
}
