package com.ysy.student.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.student.dao.StudentDao;
import com.ysy.student.db.StudentDBHelper;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;

public class AddStudentActivity extends Activity implements OnClickListener {

	private static final String TAG = "AddStudentActivity";
	private final static int DATE_DIALOG = 1;
	private static final int DIALOG_DATE = 0;

	private TextView idText;
	private EditText nameText;
	private EditText apartmentText;
	private EditText classesText;
//	private CheckBox footBox;
	private RadioGroup sexGroup;
	private EditText dateText;
	private EditText phoneText;
	private Button comfirmButton;
	private Button clearButton;
	private StudentDao dao;

	private boolean isAdd = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences dayornight = AddStudentActivity.this.getSharedPreferences("dayornight", AddStudentActivity.this.MODE_PRIVATE);
		if (dayornight.getBoolean("dayornight",false) == false) {
			this.setTheme(R.style.AppTheme);
		}
		else if (dayornight.getBoolean("dayornight",false) == true) {
			this.setTheme(R.style.AppTheme_Night);
			StudentListActivity.setBrightness(this, 0.0f);
		}
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.add_student);
		idText = (TextView) findViewById(R.id.tv_stu_id);
		nameText = (EditText) findViewById(R.id.et_name);
		apartmentText = (EditText) findViewById(R.id.et_apartment);
		classesText = (EditText) findViewById(R.id.et_classes);
//		footBox = (CheckBox) findViewById(R.id.cb_likes_fb);
		sexGroup = (RadioGroup) findViewById(R.id.rg_sex);
		dateText = (EditText) findViewById(R.id.et_birthday);
		phoneText = (EditText) findViewById(R.id.et_phone);
		comfirmButton = (Button) findViewById(R.id.btn_save);
		clearButton = (Button) findViewById(R.id.btn_clear);

		dao = new StudentDao(new StudentDBHelper(this));

		comfirmButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		dateText.setOnClickListener(this);
		
		checkIsAddStudent();
	}

    /**
     * 检查此时Activity是否用于添加学员信息
     */
	private void checkIsAddStudent() {
		Intent intent = getIntent();
		Serializable serial = intent.getSerializableExtra(TableContanst.STUDENT_TABLE);
		if (serial == null) {
			isAdd = true;
			dateText.setText(getCurrentDate());
		} else {
			isAdd = false;
			Student s = (Student) serial;
			showEditUI(s);
		}
	}

    /**
     * 显示学员信息更新的UI
     */
	private void showEditUI(Student student) {
		// 先将Student携带的数据还原到student的每一个属性中去
		idText.setText(student.getId() + "");
		nameText.setText(student.getName());
		apartmentText.setText(student.getApartment());
		String sex = student.getSex();
		for (int i = 0; i < sexGroup.getChildCount(); i++) {
			RadioButton rb = (RadioButton) sexGroup.getChildAt(i);
			if (sex != null && sex.equals(rb.getText().toString())) {
				rb.setChecked(true);
				break;
			}
		}
		classesText.setText(student.getClasses());
//		String likes = student.getLike();
//		if (likes != null && !"".equals(likes)) {
//			if (basketBox.getText().toString().indexOf(likes) >= 0) {
//				basketBox.setChecked(true);
//			}
//			if (footBox.getText().toString().indexOf(likes) >= 0) {
//				footBox.setChecked(true);
//			}
//		}
		dateText.setText(student.getBirthDay());
		phoneText.setText(student.getPhoneNumber());
		setTitle("成员信息更新");
		comfirmButton.setText("更  新");
	}

	public void onClick(View v) {
		// 收集数据
		if (v == comfirmButton) {

			if (!checkUIInput()) {// 界面输入验证
				return;
			}
			Student student = getStudentFromUI();
			long id = 0;
			int count = 0;
			if (isAdd) {
				id = dao.addStudent(student);
			} else {
				count = dao.updateStudent(student);
			}
			dao.closeDB();
			if (isAdd) {
				if (id > 0) {
					Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(this, "未知错误，添加失败！", Toast.LENGTH_SHORT).show();
				}
			} else {
				if (count > 0) {
					Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent();
//					intent.putExtra(TableContanst.STUDENT_TABLE, getStudentFromUI());
//					setResult(2, intent);
					Intent intent = new Intent(this, ShowStudentActivity.class);
					intent.putExtra(TableContanst.STUDENT_TABLE, getStudentFromUI());
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(this, "未知错误，修改失败！", Toast.LENGTH_SHORT).show();
				}
			}
		} else if (v == clearButton) {
			clearUIData();
		} else if (v == dateText) {
			Log.e(TAG, "click date"); 
			showDialog(DIALOG_DATE);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		
		Dialog dialog = null;
		if (id == DIALOG_DATE) { // ����һ�����ڲ����ؼ�
			Log.e(TAG, "click date----"); 
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateText.getText().toString()));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					dateText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		return dialog;
		//return super.onCreateDialog(id, args);
	}

    /**
     * 清空界面的数据
     */
	private void clearUIData() {
		nameText.setText("");
		apartmentText.setText("");
		phoneText.setText("");
		dateText.setText("");
//		basketBox.setChecked(false);
//		footBox.setChecked(false);
		classesText.setText("");
		sexGroup.clearCheck();
	}

    /**
     * 收集界面输入的数据，并将封装成Student对象
     */
	private Student getStudentFromUI() {

		String name = nameText.getText().toString();
		String apartment = apartmentText.getText().toString();
		String sex = ((RadioButton) findViewById(sexGroup.getCheckedRadioButtonId())).getText()
				.toString();
		String classes = classesText.getText().toString();
//		String likes = "";
//		if (basketBox.isChecked()) { // basketball, football football
//			likes += basketBox.getText();
//		}
//		if (footBox.isChecked()) {
//			if (likes.equals("")) {
//				likes += footBox.getText();
//			} else {
//				likes += "," + footBox.getText();
//			}
//		}
		String birthDay = dateText.getText().toString();
		String phoneNumber = phoneText.getText().toString();
		String modifyDateTime = getCurrentDateTime();
		Student s = new Student(name, apartment, sex, classes, phoneNumber, birthDay, modifyDateTime);
		if(!isAdd) {
			s.setId(Integer.parseInt(idText.getText().toString()));
		}
		return s;
	}

    /**
     * 得到当前的日期时间
     */
	private String getCurrentDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
	
    /**
     * 得到当前的日期
     */
	private String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}


    /**
     * 验证用户是否按要求输入了数据
     */
	private boolean checkUIInput() { // name, apartment, sex
		String name = nameText.getText().toString();
		String apartment = apartmentText.getText().toString();
		int id = sexGroup.getCheckedRadioButtonId();
		String message = null;
		View invadView = null;
		if (name.trim().length() == 0) {
			message = "请填写名字！";
			invadView = nameText;
		} else if (apartment.trim().length() == 0) {
			message = "请填写部门！";
			invadView = apartmentText;
		} else if (id == -1) {
			message = "还请至少填写性别！";
		}
		if (message != null) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			if (invadView != null)
				invadView.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, ShowStudentActivity.class);
			intent.putExtra(TableContanst.STUDENT_TABLE, getStudentFromUI());
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
