package com.ysy.sourcer_apartments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.student.activity.ShowStudentActivity;
import com.ysy.student.activity.StudentListActivity;
import com.ysy.student.dao.StudentDao;
import com.ysy.student.db.StudentDBHelper;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;

import java.util.List;
import java.util.Map;

public class Media extends Activity implements OnClickListener, AdapterView.OnItemClickListener {
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private StudentDao dao;
    private Button returnButton;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = Media.this.getSharedPreferences("dayornight", Media.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight",false) == false) {
            this.setTheme(R.style.AppTheme);
        }
        else if (dayornight.getBoolean("dayornight",false) == true) {
            this.setTheme(R.style.AppTheme_Night);
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.apart_media);
        layout = (LinearLayout) findViewById(R.id.linersearch1);
        listView = (ListView) findViewById(R.id.searchListView1);
        returnButton = (Button) findViewById(R.id.return_id1);
        dao = new StudentDao(new StudentDBHelper(this));
        listView.setOnItemClickListener(this);
        returnButton.setOnClickListener(this);
        layout.setVisibility(View.VISIBLE);
        cursor = dao.findStudent2("宣传部");
        List<Map<String, Object>> data = dao.getAllStudents(2, null, null, "宣传部", null);
        String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
        int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
        SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.find_student_list_item, from, to);
        listView.setAdapter(adapter);
    }

    public void onClick(View v) {
        if (v == returnButton) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student s = getStudentByPos(position);
        Log.d("ysy950803", "Source Science Technology Association");
        Intent intent = new Intent(this, ShowStudentActivity.class);
        intent.putExtra(TableContanst.STUDENT_TABLE, s);
        startActivity(intent);
    }

    private Student getStudentByPos(int position) {

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

    private void showListView(){
        List<Map<String, Object>> data = dao.getAllStudents(2, null, null, "宣传部", null);
        String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
        int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
        SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.find_student_list_item, from, to);
        listView.setAdapter(adapter);
    }

}