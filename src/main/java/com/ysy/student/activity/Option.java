package com.ysy.student.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ysy.pattern.LockPatternActivity;
import com.ysy.pattern.SetPatternActivity;
import com.ysy.sourcer_slidingmenu.R;
import com.ysy.sourcer_slidingmenu.SplashActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Option extends Activity {

    private static final String LOG_TAG = "Emmagee-" + Option.class.getSimpleName();

    private LinearLayout btnRepair;
    private Button btnAbout;
    private Button btnUpdateLog;
    private LinearLayout optionLayout;
    private LinearLayout updatelogLayout;
    private Button btnUpdateLogBack;

    private TextView appVersion;

    SharedPreferences PW;
    SharedPreferences SS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = Option.this.getSharedPreferences("dayornight", Option.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight",false) == false) {
            this.setTheme(R.style.AppTheme);
        }
        else if (dayornight.getBoolean("dayornight",false) == true) {
            this.setTheme(R.style.AppTheme_Night);
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.option);

        optionLayout = (LinearLayout) findViewById(R.id.option_layout);
        updatelogLayout = (LinearLayout) findViewById(R.id.updatelog_layout);

        // 以下基本为布局中各类控件与Activity的绑定并进行强制类型转换
        appVersion = (TextView) findViewById(R.id.app_versions);
        appVersion.setText(getVersion());

        btnRepair = (LinearLayout) findViewById(R.id.btn_repair);
        btnRepair.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tips();
            }
        });

        btnAbout = (Button) findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tips2();
            }
        });

        btnUpdateLog = (Button) findViewById(R.id.btn_update_log);
        btnUpdateLog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                optionLayout.setVisibility(View.GONE);
                updatelogLayout.setVisibility(View.VISIBLE);
            }
        });

        btnUpdateLogBack = (Button) findViewById(R.id.btn_update_log_back);
        btnUpdateLogBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                optionLayout.setVisibility(View.VISIBLE);
                updatelogLayout.setVisibility(View.GONE);
            }
        });

        final Button btn_setPattern = (Button) findViewById(R.id.btn_setPattern);
        final Switch swt_pattern = (Switch) findViewById(R.id.swt_pattern);

        SharedPreferences switch_state = Option.this.getSharedPreferences("switch_state", Option.this.MODE_PRIVATE);

        if (switch_state.getBoolean("switch_state", false) == true) {
            swt_pattern.setChecked(true);
            btn_setPattern.setEnabled(true);

        } else if (switch_state.getBoolean("switch_state", false) == false) {
            swt_pattern.setChecked(false);
            btn_setPattern.setEnabled(false);
        }

        btn_setPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSetPattern();
            }
        });

        swt_pattern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    SharedPreferences switch_state = Option.this.getSharedPreferences("switch_state", Option.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = switch_state.edit();
                    editor.putBoolean("switch_state", isChecked);
                    editor.commit();
                    btn_setPattern.setEnabled(true);
                } else if (isChecked == false) {
                    SharedPreferences switch_state = Option.this.getSharedPreferences("switch_state", Option.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = switch_state.edit();
                    editor.putBoolean("switch_state", isChecked);
                    editor.commit();
                    btn_setPattern.setEnabled(false);
                }
            }
        });

    }

    // TextView title = (TextView)findViewById(R.id.nb_title);
    // title.setText(R.string.about);

    // ImageView btnSave = (ImageView) findViewById(R.id.btn_set);
    // btnSave.setVisibility(ImageView.INVISIBLE);


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (optionLayout.getVisibility() == View.GONE && updatelogLayout.getVisibility() == View.VISIBLE) {
                optionLayout.setVisibility(View.VISIBLE);
                updatelogLayout.setVisibility(View.GONE);
            } else if (optionLayout.getVisibility() == View.VISIBLE && updatelogLayout.getVisibility() == View.GONE)
                finish();
            return false; // 此处返回true达到的效果相同，但若不返回值，会出现按一次就显示提示并直接退出
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * get app version
     *
     * @return app version
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            version.hashCode();
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "-";
        }
    }

    @Override
    public void finish() {
        // 结束Activity，即关闭当前最前端的Activity
        super.finish();
    }

    private void Tips() {
        // 利用对话框的形式删除数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("恢复成员信息").setMessage("即将重置所有数据至默认状态，确定要恢复吗？").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RepairDB();
                        Toast.makeText(Option.this, "恢复成功！", Toast.LENGTH_LONG).show();
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

    private void Tips2() {
        // 利用对话框的形式删除数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About").setMessage("        Thank you for using this App.\n" +
                "        Sourcer is an Android application made by Technology Apartment of Source that provides our members with access to their information when searching, adding, updating or deleting the relative content more easily. That's a lot remaining to be improved and we appreciate any feedback you may have.").setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
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

    private void RepairDB() {
        String DB_PATH = "/data/data/com.ysy.sourcer_slidingmenu/databases/";
        String DB_NAME = "student_manager.db";
        // 检查 SQLite 数据库文件是否存在
        // if ((new File(DB_PATH + DB_NAME)).exists() == false) {
        // // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
        // File f = new File(DB_PATH);
        // // 如 database 目录不存在，新建该目录
        // if (!f.exists()) {
        // f.mkdir();
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

    private void goSetPattern() {
        // SharedPreferences preferences = getSharedPreferences("first_pref",
        // MODE_PRIVATE);
        // Editor editor = preferences.edit();
        // editor.putBoolean("isFirstIn", false);
        // editor.commit();
        SplashActivity.OpenlockFrom = 1;

        PW = getSharedPreferences("password", this.MODE_PRIVATE);
        SS = getSharedPreferences("switch_state", this.MODE_PRIVATE);
        String passWord = PW.getString("password", null);
        boolean switch_state = SS.getBoolean("switch_state", false);
        if (passWord == null || switch_state == false) {
            Intent intent = new Intent(Option.this, SetPatternActivity.class);
            Option.this.startActivity(intent);
//			Option.this.finish();
        } else if (passWord != null && switch_state == true) {
            Intent intent = new Intent(Option.this, LockPatternActivity.class);
            Option.this.startActivity(intent);
//			Option.this.finish();
        }
    }
}
