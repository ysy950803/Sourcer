package com.ysy.student.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;



public class ShowStudentActivity extends Activity implements OnClickListener{
	private Button btnBack;
	private Button btnUpdate;
	private Student student;
	private ImageView btnCall;
	private ImageView btnMsg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences dayornight = ShowStudentActivity.this.getSharedPreferences("dayornight", ShowStudentActivity.this.MODE_PRIVATE);
		if (dayornight.getBoolean("dayornight",false) == false) {
			this.setTheme(R.style.AppTheme);
		}
		else if (dayornight.getBoolean("dayornight",false) == true) {
			this.setTheme(R.style.AppTheme_Night);
			StudentListActivity.setBrightness(this, 0.0f);
		}
		setContentView(R.layout.student_info);
		Intent intent = getIntent();
		final Student student = (Student) intent.getSerializableExtra(TableContanst.STUDENT_TABLE);
		((TextView)findViewById(R.id.tv_info_id)).setText(student.getId()+"");
		((TextView)findViewById(R.id.tv_info_name)).setText(student.getName());
		((TextView)findViewById(R.id.tv_info_apartment)).setText(student.getApartment()+"");
		((TextView)findViewById(R.id.tv_info_sex)).setText(student.getSex());
		((TextView)findViewById(R.id.tv_info_classes)).setText(student.getClasses());
		((TextView)findViewById(R.id.tv_info_birthday)).setText(student.getBirthDay());
		((TextView)findViewById(R.id.tv_info_phone)).setText(student.getPhoneNumber());
		
		btnBack = (Button) findViewById(R.id.back_to_list_id);
		btnUpdate = (Button) findViewById(R.id.btn_update);
		btnCall = (ImageView) findViewById(R.id.btn_call);
		btnMsg = (ImageView) findViewById(R.id.btn_msg);

		btnBack.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowStudentActivity.this);
				builder.setTitle("Make a Call").setMessage("即将与[" + student.getName().toString() + "]通话，确定要拨打吗？\n" + "若系统提示需要获取拨号权限，请选择允许。").setCancelable(false)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String PN = student.getPhoneNumber().toString();
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PN));
								startActivity(intent);
							}

						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				//设置透明度
				Window window = alert.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				lp.alpha = 0.75f;
				window.setAttributes(lp);

			}
		});

		btnMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				send(student.getPhoneNumber().toString(),"【索思科技协会】");
			}
		});

	}

	private void send(String number, String message){
		Uri uri = Uri.parse("smsto:" + number);
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", message);
		startActivity(sendIntent);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v == btnBack){
			finish();
		} else if(v == btnUpdate){
			Intent intent = getIntent();
			Student student = (Student) intent.getSerializableExtra(TableContanst.STUDENT_TABLE);
			Intent intent2 = new Intent();
			intent2.putExtra("student", student);
			intent2.setClass(this, AddStudentActivity.class);
			this.startActivity(intent2);
			finish();
		}
	}
	
//	public void goBack(View view) {
//		finish();
//	}
}
