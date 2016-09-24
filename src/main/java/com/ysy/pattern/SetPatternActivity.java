package com.ysy.pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.sourcer_utils.GeneralHashFunctionLibrary;
import com.ysy.student.activity.StudentListActivity;

import java.util.List;

public class SetPatternActivity extends Activity {

    List<Integer> passList;
    List<Integer> passList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = SetPatternActivity.this.getSharedPreferences("dayornight", SetPatternActivity.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight",false) == false) {
            this.setTheme(R.style.AppTheme);
        }
        else if (dayornight.getBoolean("dayornight",false) == true) {
            this.setTheme(R.style.AppTheme_Night);
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.activity_set_pattern);



        final GestureLock lock = (GestureLock) findViewById(R.id.SetView);
        final GestureLock lock2 = (GestureLock) findViewById(R.id.SetView2);

//        Button btn_reset = (Button) findViewById(R.id.reset);
//        Button btn_save = (Button) findViewById(R.id.save);
        Button btn_back = (Button) findViewById(R.id.backtooption);

        lock.setOnDrawFinishedListener(new GestureLock.onDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (passList.size() < 3 && passList.size() > 0) {
                    Toast.makeText(SetPatternActivity.this, "密码不能小于3个点", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (passList.size() == 0){
                    return false;
                }
                else {
                    SetPatternActivity.this.passList = passList; // 将绘制的点存入此Activity开辟的List中
                    lock.setVisibility(View.INVISIBLE);
                    lock2.setVisibility(View.VISIBLE);
                    Toast.makeText(SetPatternActivity.this, "请再次绘制图案", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        });

        lock2.setOnDrawFinishedListener(new GestureLock.onDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList2) {
                if (passList2.size() < 3 && passList2.size() > 0) {
                    Toast.makeText(SetPatternActivity.this, "密码不能小于3个点", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (passList2.size() == 0){
                    return false;
                }
                else {
                    SetPatternActivity.this.passList2 = passList2; // 将绘制的点存入此Activity开辟的List中

                    StringBuilder sb = new StringBuilder();
                    for (Integer i : passList) {
                        sb.append(i);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    for (Integer j : passList2) {
                        sb2.append(j);
                    }

                    if (sb.toString().equals(sb2.toString())) {
                        // 错误写法：if ( sb.toString() == sb2.toString() )
                        SharedPreferences sp = SetPatternActivity.this.getSharedPreferences("password", SetPatternActivity.this.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        String hash_password = Long.toString(GeneralHashFunctionLibrary.BKDRHash(sb.toString()));

                        editor.putString("password", hash_password);
                        editor.commit();

                        Toast.makeText(SetPatternActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        SharedPreferences switch_state = getSharedPreferences("switch_state", SetPatternActivity.this.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = switch_state.edit();
                        editor2.putBoolean("switch_state", true);
                        editor2.commit();

                        finish();

                    } else {
                        Toast.makeText(SetPatternActivity.this, "两次绘制图案不同，请重新绘制", Toast.LENGTH_SHORT).show();
                        lock.resetPoints();
                        lock.setVisibility(View.VISIBLE);
                        lock2.resetPoints();
                        lock2.setVisibility(View.INVISIBLE);

                    }
                    return true;
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        btn_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (lock.getVisibility() == View.VISIBLE) {
//                    lock.resetPoints();
//                } else if (lock2.getVisibility() == View.VISIBLE) {
//                    lock2.resetPoints();
//                }
//            }
//        });

//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (passList != null) {
//                    StringBuilder sb = new StringBuilder();
//                    for (Integer i : passList) {
//                        sb.append(i);
//                    }
//                    SharedPreferences sp = SetPatternActivity.this.getSharedPreferences("password", SetPatternActivity.this.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("password",sb.toString());
//                    editor.commit();
//
//                    Toast.makeText(SetPatternActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
