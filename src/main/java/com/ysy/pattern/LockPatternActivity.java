package com.ysy.pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ysy.sourcer_slidingmenu.R;
import com.ysy.sourcer_slidingmenu.SplashActivity;
import com.ysy.sourcer_utils.GeneralHashFunctionLibrary;
import com.ysy.student.activity.StudentListActivity;

import org.w3c.dom.Text;

import java.util.List;

public class LockPatternActivity extends Activity {

    private Button btnForgetPassword;
    private EditText edtInputname;
    private Button btnBack;
    private Button btnClearPattern;
    private LinearLayout clearPatternLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = LockPatternActivity.this.getSharedPreferences("dayornight", LockPatternActivity.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight",false) == false) {
            this.setTheme(R.style.AppTheme);
        }
        else if (dayornight.getBoolean("dayornight",false) == true) {
            this.setTheme(R.style.AppTheme_Night);
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.activity_lock_pattern);


        final SharedPreferences sp = getSharedPreferences("password", this.MODE_PRIVATE);
        final SharedPreferences switch_state = getSharedPreferences("switch_state", this.MODE_PRIVATE);
        final String password = sp.getString("password", null);

        final GestureLock lock = (GestureLock) findViewById(R.id.LockView);

        edtInputname = (EditText) findViewById(R.id.input_name);


        clearPatternLayout = (LinearLayout) findViewById(R.id.clear_pattern_layout);

        btnClearPattern = (Button) findViewById(R.id.btn_clear_pattern);
        btnClearPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String president_name = edtInputname.getText().toString();
                int j;
                String[] president = {"李东微", "陈文聪", "苏伟", "李思敏", "杨光", "郑智予", "柴铎"};
                for (j = 0; j < 7; j++) {
                    if (president_name.equals(president[j])) {
                        j = 16;
                    }
                }
                if (j == 17) { // 注意上面的后自增
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", null);
                    editor.commit();

                    SharedPreferences.Editor editor2 = switch_state.edit();
                    editor2.putBoolean("switch_state", false);
                    editor2.commit();

                    if (SplashActivity.OpenlockFrom == 0) {
                        Intent i = new Intent(LockPatternActivity.this, StudentListActivity.class);
                        startActivity(i);
                        LockPatternActivity.this.finish();
                    } else if (SplashActivity.OpenlockFrom == 1) {
                        Intent i2 = new Intent(LockPatternActivity.this, SetPatternActivity.class);
                        startActivity(i2);
                        LockPatternActivity.this.finish();
                    }
                } else {
                    Toast.makeText(LockPatternActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPatternLayout.setVisibility(View.GONE);
            }
        });

        lock.setOnDrawFinishedListener(new GestureLock.onDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                StringBuilder sb = new StringBuilder();
                for (Integer i : passList) {
                    sb.append(i);
                }

                String hash_password = Long.toString(GeneralHashFunctionLibrary.BKDRHash(sb.toString()));

                if (hash_password.equals(password)) {
//                    Toast.makeText(LockPatternActivity.this, "正确", Toast.LENGTH_SHORT).show();
                    if (SplashActivity.OpenlockFrom == 0) {
                        Intent i = new Intent(LockPatternActivity.this, StudentListActivity.class);
                        startActivity(i);
                        LockPatternActivity.this.finish();
                    } else if (SplashActivity.OpenlockFrom == 1) {
                        Intent i2 = new Intent(LockPatternActivity.this, SetPatternActivity.class);
                        startActivity(i2);
                        LockPatternActivity.this.finish();
                    }
                    return true;
                } else {
//                    Toast.makeText(LockPatternActivity.this, "错误", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        btnForgetPassword = (Button) findViewById(R.id.forgetpassword);
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPatternLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
