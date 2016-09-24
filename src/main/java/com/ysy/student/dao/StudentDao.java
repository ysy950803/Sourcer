package com.ysy.student.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.student.db.StudentDBHelper;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;

public class StudentDao {

    private StudentDBHelper dbHelper;
    private Cursor cursor;

    public StudentDao(StudentDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // 添加一个Student对象数据到数据库表
    public long addStudent(Student s) {

        ContentValues values = new ContentValues();
        values.put(TableContanst.StudentColumns.NAME, s.getName());
        values.put(TableContanst.StudentColumns.APARTMENT, s.getApartment());
        values.put(TableContanst.StudentColumns.SEX, s.getSex());
        values.put(TableContanst.StudentColumns.CLASSES, s.getClasses());
        values.put(TableContanst.StudentColumns.PHONE_NUMBER,
                s.getPhoneNumber());
        values.put(TableContanst.StudentColumns.BIRTHDAY, s.getBirthDay());
        values.put(TableContanst.StudentColumns.MODIFY_TIME,
                s.getModifyDateTime());
        return dbHelper.getWritableDatabase().insert(
                TableContanst.STUDENT_TABLE, null, values);

    }

    // 删除一个id所对应的数据库表student的记录
    public int deleteStudentById(long id) {

        return dbHelper.getWritableDatabase().delete(
                TableContanst.STUDENT_TABLE,
                TableContanst.StudentColumns.ID + "=?",
                new String[]{id + ""});
    }

    // 更新一个id所对应数据库表student的记录
    public int updateStudent(Student s) {

        ContentValues values = new ContentValues();
        values.put(TableContanst.StudentColumns.NAME, s.getName());
        values.put(TableContanst.StudentColumns.APARTMENT, s.getApartment());
        values.put(TableContanst.StudentColumns.SEX, s.getSex());
        values.put(TableContanst.StudentColumns.CLASSES, s.getClasses());
        values.put(TableContanst.StudentColumns.PHONE_NUMBER,
                s.getPhoneNumber());
        values.put(TableContanst.StudentColumns.BIRTHDAY, s.getBirthDay());
        values.put(TableContanst.StudentColumns.MODIFY_TIME,
                s.getModifyDateTime());
        return dbHelper.getWritableDatabase().update(
                TableContanst.STUDENT_TABLE, values,
                TableContanst.StudentColumns.ID + "=?",
                new String[]{s.getId() + ""});

    }

    // 查询所有的记录
    public List<Map<String, Object>> getAllStudents(int infoID, String name, String phone_number, String apartment, String birthday) { // modify_time desc
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//        Cursor cursor = dbHelper.getWritableDatabase().query(
//                TableContanst.STUDENT_TABLE, null, null, null, null, null,
//                TableContanst.StudentColumns.MODIFY_TIME + " desc");
        if (infoID == 0) {
            Cursor cursor = dbHelper.getWritableDatabase().query(
                    TableContanst.STUDENT_TABLE, null, "name like ?",
                    new String[]{"%" + name + "%"}, null, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>(8);
                long id = cursor.getInt(cursor
                        .getColumnIndex(TableContanst.StudentColumns.ID));
                map.put(TableContanst.StudentColumns.ID, id);
                name = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.NAME));
                map.put(TableContanst.StudentColumns.NAME, name);
                apartment = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.APARTMENT));
                map.put(TableContanst.StudentColumns.APARTMENT, apartment);
                String sex = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.SEX));
                map.put(TableContanst.StudentColumns.SEX, sex);
                String classes = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.CLASSES));
                map.put(TableContanst.StudentColumns.CLASSES, classes);
                phone_number = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.PHONE_NUMBER));
                map.put(TableContanst.StudentColumns.PHONE_NUMBER, phone_number);
                birthday = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.BIRTHDAY));
                map.put(TableContanst.StudentColumns.BIRTHDAY, birthday);
                String modify_time = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.MODIFY_TIME));
                map.put(TableContanst.StudentColumns.MODIFY_TIME, modify_time);
                data.add(map);
            }
            cursor.close();
        } else if (infoID == 1) {
            Cursor cursor = dbHelper.getWritableDatabase().query(
                    TableContanst.STUDENT_TABLE, null, "phone_number like ?",
                    new String[]{"%" + phone_number + "%"}, null, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>(8);
                long id = cursor.getInt(cursor
                        .getColumnIndex(TableContanst.StudentColumns.ID));
                map.put(TableContanst.StudentColumns.ID, id);
                name = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.NAME));
                map.put(TableContanst.StudentColumns.NAME, name);
                apartment = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.APARTMENT));
                map.put(TableContanst.StudentColumns.APARTMENT, apartment);
                String sex = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.SEX));
                map.put(TableContanst.StudentColumns.SEX, sex);
                String classes = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.CLASSES));
                map.put(TableContanst.StudentColumns.CLASSES, classes);
                phone_number = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.PHONE_NUMBER));
                map.put(TableContanst.StudentColumns.PHONE_NUMBER, phone_number);
                birthday = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.BIRTHDAY));
                map.put(TableContanst.StudentColumns.BIRTHDAY, birthday);
                String modify_time = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.MODIFY_TIME));
                map.put(TableContanst.StudentColumns.MODIFY_TIME, modify_time);
                data.add(map);
            }
            cursor.close();
        } else if (infoID == 2) {
            Cursor cursor = dbHelper.getWritableDatabase().query(
                    TableContanst.STUDENT_TABLE, null, "apartment like ?",
                    new String[]{"%" + apartment + "%"}, null, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>(8);
                long id = cursor.getInt(cursor
                        .getColumnIndex(TableContanst.StudentColumns.ID));
                map.put(TableContanst.StudentColumns.ID, id);
                name = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.NAME));
                map.put(TableContanst.StudentColumns.NAME, name);
                apartment = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.APARTMENT));
                map.put(TableContanst.StudentColumns.APARTMENT, apartment);
                String sex = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.SEX));
                map.put(TableContanst.StudentColumns.SEX, sex);
                String classes = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.CLASSES));
                map.put(TableContanst.StudentColumns.CLASSES, classes);
                phone_number = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.PHONE_NUMBER));
                map.put(TableContanst.StudentColumns.PHONE_NUMBER, phone_number);
                birthday = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.BIRTHDAY));
                map.put(TableContanst.StudentColumns.BIRTHDAY, birthday);
                String modify_time = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.MODIFY_TIME));
                map.put(TableContanst.StudentColumns.MODIFY_TIME, modify_time);
                data.add(map);
            }
            cursor.close();
        }else if (infoID == 3) {
            Cursor cursor = dbHelper.getWritableDatabase().query(
                    TableContanst.STUDENT_TABLE, null, "birthday like ?",
                    new String[]{"%" + birthday + "%"}, null, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>(8);
                long id = cursor.getInt(cursor
                        .getColumnIndex(TableContanst.StudentColumns.ID));
                map.put(TableContanst.StudentColumns.ID, id);
                name = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.NAME));
                map.put(TableContanst.StudentColumns.NAME, name);
                apartment = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.APARTMENT));
                map.put(TableContanst.StudentColumns.APARTMENT, apartment);
                String sex = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.SEX));
                map.put(TableContanst.StudentColumns.SEX, sex);
                String classes = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.CLASSES));
                map.put(TableContanst.StudentColumns.CLASSES, classes);
                phone_number = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.PHONE_NUMBER));
                map.put(TableContanst.StudentColumns.PHONE_NUMBER, phone_number);
                birthday = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.BIRTHDAY));
                map.put(TableContanst.StudentColumns.BIRTHDAY, birthday);
                String modify_time = cursor.getString(cursor
                        .getColumnIndex(TableContanst.StudentColumns.MODIFY_TIME));
                map.put(TableContanst.StudentColumns.MODIFY_TIME, modify_time);
                data.add(map);
            }
            cursor.close();
        }

        return data;
    }

    // 模糊查询一条记录
    public Cursor findStudent(String name) {

        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, "name like ?",
                new String[]{"%" + name + "%"}, null, null, null, null);
        return cursor;
    }

    public Cursor findStudent1(String phone_number) {

        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, "phone_number like ?",
                new String[]{"%" + phone_number + "%"}, null, null, null, null);
        return cursor;
    }

    public Cursor findStudent2(String apartment) {

        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, "apartment like ?",
                new String[]{"%" + apartment + "%"}, null, null, null, null);
        return cursor;
    }

    public Cursor findStudent3(String birthday) {

        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, "birthday like ?",
                new String[]{"%" + birthday + "%"}, null, null, null, null);
        return cursor;
    }

    // 按姓名进行排序
    public Cursor sortByName() {
        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, null, null, null, null,
                TableContanst.StudentColumns.NAME);
        return cursor;
    }

    // 按生日进行排序
    public Cursor sortByBirthDay() {
        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, null, null, null, null,
                TableContanst.StudentColumns.BIRTHDAY);
        return cursor;
    }

    // 按学号进行排序
    public Cursor sortByID() {
        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableContanst.STUDENT_TABLE, null, null, null, null, null,
                TableContanst.StudentColumns.ID);
        return cursor;
    }

    public void closeDB() {
        dbHelper.close();
    }

    // 自定义的方法通过View和Id得到一个student对象
//    public Student getStudentFromView(View view, long id) {
//        TextView nameView = (TextView) view.findViewById(R.id.tv_stu_name2);
//        TextView apartmentView = (TextView) view.findViewById(R.id.tv_stu_apartment2);
//        TextView sexView = (TextView) view.findViewById(R.id.tv_stu_sex2);
//        TextView classesView = (TextView) view.findViewById(R.id.tv_stu_classes2);
//        TextView phoneView = (TextView) view.findViewById(R.id.tv_stu_phone2);
//        TextView dataView = (TextView) view.findViewById(R.id.tv_stu_birthday2);
//        String name = nameView.getText().toString();
//        String apartment = apartmentView.getText().toString();
//        String sex = sexView.getText().toString();
//        String classes = classesView.getText().toString();
//        String phone = phoneView.getText().toString();
//        String date = dataView.getText().toString();
//        Student student = new Student(id, name, apartment, sex, classes, phone, date,
//                null);
//        return student;
//    }
}
