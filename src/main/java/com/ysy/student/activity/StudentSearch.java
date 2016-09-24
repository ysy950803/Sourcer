package com.ysy.student.activity;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.student.dao.StudentDao;
import com.ysy.student.db.StudentDBHelper;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentSearch extends Activity implements OnClickListener, AdapterView.OnItemClickListener {
    private EditText nameText;
    private TextView tipText;
    private Button button;
    private Button button1;
    private Button reButton;
    private Cursor cursor;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private StudentDao dao;
    private Button returnButton;
    private LinearLayout layout;
    private Button btnWhosBirthday;

    private static int STATE_SEARCHBYWHICH = 0;
    private static String NAME = null;
    private static String PHONE_NUMBER = null;
    private static String BIRTHDAY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = StudentSearch.this.getSharedPreferences("dayornight", StudentSearch.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight", false) == false) {
            this.setTheme(R.style.AppTheme);
        } else if (dayornight.getBoolean("dayornight", false) == true) {
            this.setTheme(R.style.AppTheme_Night);
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.search);
        nameText = (EditText) findViewById(R.id.et_search);
        tipText = (TextView) findViewById(R.id.textView5);
        layout = (LinearLayout) findViewById(R.id.linersearch);
        button = (Button) findViewById(R.id.bn_sure_search);
        button1 = (Button) findViewById(R.id.bn_sure1_search);
        btnWhosBirthday = (Button) findViewById(R.id.btn_whosbirthday);
        reButton = (Button) findViewById(R.id.bn_return);
        listView = (ListView) findViewById(R.id.searchListView);
        returnButton = (Button) findViewById(R.id.return_id);
        dao = new StudentDao(new StudentDBHelper(this));


        reButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        btnWhosBirthday.setOnClickListener(this);

    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            STATE_SEARCHBYWHICH = 1;
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            btnWhosBirthday.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            tipText.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            String name = nameText.getText().toString();
            NAME = name;
            cursor = dao.findStudent(name);
            if (!cursor.moveToFirst()) {
//                listView.setVisibility(View.INVISIBLE);
                listView.setAdapter(null);
                Toast.makeText(this, "未搜索到相关信息或未按姓名搜索！", Toast.LENGTH_SHORT).show();
            } else {
                //如果有所查询的信息，则将查询结果显示出来
                List<Map<String, Object>> data = dao.getAllStudents(0, name, null, null, null);
                String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                        TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                        TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                        TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
                int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                        R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
                SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
                listView.setAdapter(adapter);
            }
        }
        if (v == button1) {
            STATE_SEARCHBYWHICH = 2;
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            btnWhosBirthday.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            tipText.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            String phone_number = nameText.getText().toString();
            PHONE_NUMBER = phone_number;
            cursor = dao.findStudent1(phone_number);
            if (!cursor.moveToFirst()) {
//                listView.setVisibility(View.INVISIBLE);
                listView.setAdapter(null);
                Toast.makeText(this, "未搜索到相关信息或未按号码搜索！", Toast.LENGTH_SHORT).show();
            } else {
                //如果有所查询的信息，则将查询结果显示出来
                List<Map<String, Object>> data = dao.getAllStudents(1, null, phone_number, null, null);
                String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                        TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                        TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                        TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
                int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                        R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
                SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
                listView.setAdapter(adapter);
            }
        }
        if (v == btnWhosBirthday) {
            STATE_SEARCHBYWHICH = 3;
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            btnWhosBirthday.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            tipText.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);

            Calendar c = Calendar.getInstance();
            String year = Integer.toString(c.get(Calendar.YEAR));
            String month = Integer.toString(c.get(Calendar.MONTH));
            String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));

//            Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
//            t.setToNow(); // 取得系统时间
//            String year1 = Integer.toString(t.year);
//            String month1 = Integer.toString(t.month);
//            String day1 = Integer.toString(t.monthDay);

            String birthday = getCurrentDate();
            birthday = birthday.replace("/0", "/");
//            Log.d("Test", birthday);
            BIRTHDAY = birthday;

            cursor = dao.findStudent3(birthday);
            if (!cursor.moveToFirst()) {
//                listView.setVisibility(View.INVISIBLE);
                listView.setAdapter(null);
                tipText.setVisibility(View.GONE);
                Toast.makeText(this, "今天还没有谁过生日哦！", Toast.LENGTH_SHORT).show();
            } else {
                //如果有所查询的信息，则将查询结果显示出来
                List<Map<String, Object>> data = dao.getAllStudents(3, null, null, null, birthday);
                String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                        TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                        TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                        TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
                int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                        R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
                SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
                listView.setAdapter(adapter);
            }
        } else if (v == returnButton) {
            finish();
//        	startActivity(new Intent(this, StudentSearch.class));
        } else if (v == reButton) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student s = getStudentByPos(position);
        Log.d("ysy950803", "Source Science Technology Association");
        Intent intent = new Intent(StudentSearch.this, ShowStudentActivity.class);
        intent.putExtra(TableContanst.STUDENT_TABLE, s);
        startActivity(intent);
    }

    protected Student getStudentByPos(int position) {
        Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);
        long id = (Long) data.get(TableContanst.StudentColumns.ID);
        String name = (String) data.get(TableContanst.StudentColumns.NAME);
        String apartment = (String) data.get(TableContanst.StudentColumns.APARTMENT);
        String sex = (String) data.get(TableContanst.StudentColumns.SEX);
        String classes = (String) data.get(TableContanst.StudentColumns.CLASSES);
        String phoneNumber = (String) data.get(TableContanst.StudentColumns.PHONE_NUMBER);
        String birthDay = (String) data.get(TableContanst.StudentColumns.BIRTHDAY);
        String modifyTime = (String) data.get(TableContanst.StudentColumns.MODIFY_TIME);
        Student s = new Student(id, name, apartment, sex, classes, phoneNumber, birthDay, modifyTime);
        return s;
    }

    public class StudentAdpter extends SimpleAdapter {

        public StudentAdpter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                             int[] to) {
            super(context, data, resource, from, to);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);

            TextView idView = (TextView) view.findViewById(R.id.tv_item_id);
            long id = Long.parseLong(idView.getText().toString());
            return view;
        }
    }

    // 每次启动或返回主界面时刷新ListView
    @Override
    protected void onStart() {
        // 调用load()方法将数据库中的所有记录显示在当前页面
        super.onStart();
        showListView();
    }

    private void showListView() {
        if (STATE_SEARCHBYWHICH == 1) {
            List<Map<String, Object>> data = dao.getAllStudents(0, NAME, null, null, null);
            String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                    TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                    TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                    TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
            int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                    R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
            SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
            listView.setAdapter(adapter);
        } else if (STATE_SEARCHBYWHICH == 2) {
            List<Map<String, Object>> data = dao.getAllStudents(1, null, PHONE_NUMBER, null, null);
            String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                    TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                    TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                    TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
            int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                    R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
            SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
            listView.setAdapter(adapter);
        } else if (STATE_SEARCHBYWHICH == 3) {
            List<Map<String, Object>> data = dao.getAllStudents(3, null, null, null, BIRTHDAY);
            String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                    TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                    TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                    TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
            int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                    R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
            SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
            listView.setAdapter(adapter);
        }
    }
}