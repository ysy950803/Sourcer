package com.ysy.student.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysy.pattern.SetPatternActivity;
import com.ysy.sourcer_apartments.Academic;
import com.ysy.sourcer_apartments.Electrician;
import com.ysy.sourcer_apartments.Media;
import com.ysy.sourcer_apartments.Office;
import com.ysy.sourcer_apartments.Presidium;
import com.ysy.sourcer_apartments.Software;
import com.ysy.sourcer_slidingmenu.R;
import com.ysy.sourcer_slidingmenu.SlidingMenu;
import com.ysy.student.dao.StudentDao;
import com.ysy.student.db.StudentDBHelper;
import com.ysy.student.entity.Student;
import com.ysy.student.entity.TableContanst;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StudentListActivity extends ListActivity
        implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

    private ImageView btnOption;
    private SlidingMenu mMenu;
    private Button btnPresidium;
    private Button btnAcademic;
    private Button btnElectrician;
    private Button btnMedia;
    private Button btnOffice;
    private Button btnSoftware;
    private ImageButton btnLoginSchoolNet;
    private ImageButton btnNightmode;

    ArrayList<String> packagNameList;
    private MyReceiver receiver;

    private static final String TAG = "StudentListActivity";
    private static final int DIALOG_DELETE_ALL_COMFIRM = 2;
    private static final int DIALOG_DELETE_COMFIRM = 1;
    protected static final int DIALOG_DELETE_PROGRESS = 3;
    private static final int WHAT_FRESH_PROGRESS = 1;
    private static final int WHAT_DISMISS_DIALOG = 2;

    private StudentDao dao;
    private Student student, student2;
    private LinearLayout layout;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    private int checkTime;
    // 此变量作用:在按下选择按钮后,所有item对应的checkbox显示出来,被勾选则计一次数

    private View searchButton;
    private Button addStudent;
    private ImageView multiSelect;
    private Button cancelBtn;
    private Button deleteBtn;
    private CheckBox selectAllBox;
    private RelativeLayout headLayout;
    private LinearLayout bottomLayout;
    private ListView listView;
    private boolean isListDelete = false;
    private HashMap<Long, Boolean> checkBoxStatus;
    private List<Long> selectIds, selectIds2;
    private ProgressBar mProgressBar;
    private AlertDialog mDialog;
    private TextView mPercentSizeView;
    private int mDeletedSize;
    private static int mTotalSize;
    private boolean delete = false;

    private View selectedView = null;

    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }

        return osVersion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = StudentListActivity.this.getSharedPreferences("dayornight", StudentListActivity.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight", false) == false) {
            this.setTheme(R.style.AppTheme);
        } else if (dayornight.getBoolean("dayornight", false) == true) {
            setBrightness(this, 0.0f);
            this.setTheme(R.style.AppTheme_Night);
        }
        setContentView(R.layout.activity_main);
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);

        dao = new StudentDao(new StudentDBHelper(this)); // this instanceof

        // Context true
        btnNightmode = (ImageButton) findViewById(R.id.night_mode);
        searchButton = findViewById(R.id.ib_search);
        addStudent = (Button) findViewById(R.id.btn_add_student);
        multiSelect = (ImageView) findViewById(R.id.iv_multi_select);
        cancelBtn = (Button) findViewById(R.id.btn_cancel_delete);
        deleteBtn = (Button) findViewById(R.id.btn_comfirm_delete);
        selectAllBox = (CheckBox) findViewById(R.id.cb_select_all);
        headLayout = (RelativeLayout) findViewById(R.id.rl_header_layout);
        bottomLayout = (LinearLayout) findViewById(R.id.ll_bottom);
        listView = getListView();

        btnOption = (ImageView) findViewById(R.id.btn_option);
        btnOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent option = new Intent(StudentListActivity.this, Option.class);
                startActivity(option);
            }
        });

        // initpackagNameList(); // 下面有不在此处初始化的原因
        // 监听系统新安装程序的广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);// 注册广播机制
        filter.addDataScheme("package"); // 必须添加这项，否则拦截不到广播
        registerReceiver(receiver, filter);

        btnLoginSchoolNet = (ImageButton) findViewById(R.id.btn_loginschoolnet);
        btnLoginSchoolNet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent loginschoolnet = new Intent();
                // loginschoolnet.setClassName("com.example.login",
                // "com.example.login.Activity1");
                // startActivity(loginschoolnet);

                initpackagNameList(); // 初始化列表放在此位置可防止主程序正在运行时删除login插件后再次点击登录出现的闪退问题
                // 检查是否已经安装
                Log.d("time", "clicked start " + System.currentTimeMillis() + "");
                boolean installed = detectApk("com.example.login");

                if (installed) {// 已经安装直接起动
                    Log.d("time", "getPackageManager start " + System.currentTimeMillis() + "");

                    Intent intent = new Intent();
                    // 组件名称，第一个参数是包名，也是主配置文件Manifest里设置好的包名 第二个是类名，要带上包名
                    intent.setComponent(new ComponentName("com.example.login", "com.example.login.Activity1"));
                    intent.setAction(Intent.ACTION_VIEW);

                    Log.d("time", "setAction start " + System.currentTimeMillis() + "");
                    startActivity(intent);

                    // Intent mIntent = new Intent();
                    // ComponentName comp = new
                    // ComponentName("com.example.login",
                    // "com.example.login.Activity1");
                    // mIntent.setComponent(comp);
                    // mIntent.setAction("android.intent.action.VIEW");
                    // startActivity(mIntent);

                    // Intent i = new Intent();
                    // ComponentName cn = new ComponentName("com.example.login",
                    // "com.example.login.Activity1");
                    // i.setComponent(cn);
                    // i.setAction("android.intent.action.MAIN");
                    // startActivityForResult(i, RESULT_OK);

                } else {// 未安装先安装
                    //
                    // get the cacheDir.
                    if (getAndroidOSVersion() <= 20) {
                        File fileDir = getFilesDir();
                        final String cachePath = fileDir.getAbsolutePath() + "/login_schoolnet_K.apk";
                        retrieveApkFromAssets(StudentListActivity.this, "login_schoolnet_K.apk", cachePath);
                        showInstallConfirmDialog(StudentListActivity.this, cachePath);
                    } else {
                        File fileDir = getFilesDir();
                        final String cachePath = fileDir.getAbsolutePath() + "/login_schoolnet_L.apk";
                        retrieveApkFromAssets(StudentListActivity.this, "login_schoolnet_L.apk", cachePath);
                        showInstallConfirmDialog(StudentListActivity.this, cachePath);
                    }
                }
            }

            // 捆绑安装
            public boolean retrieveApkFromAssets(Context context, String fileName, String path) {
                boolean bRet = false;

                try {
                    File file = new File(path);
                    if (file.exists()) {
                        return true;
                    } else {
                        file.createNewFile();
                        InputStream is = context.getAssets().open(fileName);
                        FileOutputStream fos = new FileOutputStream(file);

                        byte[] temp = new byte[1024];
                        int i = 0;
                        while ((i = is.read(temp)) != -1) {
                            fos.write(temp, 0, i);
                        }
                        fos.flush();
                        fos.close();
                        is.close();

                        bRet = true;
                    }

                } catch (IOException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Builder builder = new Builder(context);
                    builder.setMessage(e.getMessage());
                    builder.show();
                    e.printStackTrace();
                }

                return bRet;
            }

            /**
             * 提示用户安装程序
             */
            public void showInstallConfirmDialog(final Context context, final String filePath) {
                AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
                // tDialog.setIcon(R.drawable.info);
                tDialog.setTitle("安装插件");
                tDialog.setMessage("尚未安装“一键登录校园网（无5s等候时间）”插件，确定安装吗？\n" + "成功登录后出现“您已登录”蓝色字样即已连接校园网，连续返回到主界面即可，无需重复登录。\n"
                        + "（由于Android L系统机制改动，目前内嵌登录暂时仅支持5.0以下的系统，5.0及以上则采取双应用调用。）");

                tDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // 修改apk权限
                        try {
                            String command = "chmod " + "777" + " " + filePath;
                            Runtime runtime = Runtime.getRuntime();
                            runtime.exec(command);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // install the apk.
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.parse("file://" + filePath),
                                "application/vnd.android.package-archive");
                        context.startActivity(intent);

                    }
                });

                tDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alert = tDialog.create();
                alert.show();

                //设置透明度
                Window window = alert.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 0.75f;
                window.setAttributes(lp);

            }

            /**
             * 检测是否已经安装
             *
             * @param packageName
             * @return true已安装 false未安装
             */
            private boolean detectApk(String packageName) {
                return packagNameList.contains(packageName.toLowerCase());
            }

        });

        btnNightmode.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        addStudent.setOnClickListener(this);
        multiSelect.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        selectAllBox.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnCreateContextMenuListener(this);
        listView.setOnItemLongClickListener(this);

        InputDB();
        showListView(); // 刷新ListView

        btnPresidium = (Button) findViewById(R.id.btnPresidium);
        btnPresidium.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zero = new Intent(StudentListActivity.this, Presidium.class);
                startActivity(zero);
            }
        });
        btnAcademic = (Button) findViewById(R.id.btnAcademic);
        btnAcademic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent three = new Intent(StudentListActivity.this, Academic.class);
                startActivity(three);
            }
        });
        btnOffice = (Button) findViewById(R.id.btnOffice);
        btnOffice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent one = new Intent(StudentListActivity.this, Office.class);
                startActivity(one);
            }
        });
        btnMedia = (Button) findViewById(R.id.btnMedia);
        btnMedia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent two = new Intent(StudentListActivity.this, Media.class);
                startActivity(two);
            }
        });
        btnSoftware = (Button) findViewById(R.id.btnSoftware);
        btnSoftware.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent five = new Intent(StudentListActivity.this, Software.class);
                startActivity(five);
            }
        });
        btnElectrician = (Button) findViewById(R.id.btnElectrician);
        btnElectrician.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent four = new Intent(StudentListActivity.this, Electrician.class);
                startActivity(four);
            }
        });

        birthdayTips();
    }

    private void initpackagNameList() {
        // 初始化小模块列表
        packagNameList = new ArrayList<String>();
        PackageManager manager = this.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            packagNameList.add(pI.packageName.toLowerCase());
        }
    }

    /**
     * 设置广播监听
     */
    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {

                String packName = intent.getDataString().substring(8);

                Log.e(intent.getDataString() + "====", packName);
                // package:cn.oncomm.activity cn.oncomm.activity
                // packName为所安装的程序的包名
                packagNameList.add(packName.toLowerCase());

                // 删除file目录下的所有以安装的apk文件
                File file = getFilesDir();
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.getName().endsWith(".apk")) {
                        f.delete();
                    }
                }

            }
        }
    }

    // 载入已做好的数据库文件
    public void InputDB() {
        String DB_PATH = "/data/data/com.ysy.sourcer_slidingmenu/databases/";
        String DB_NAME = "student_manager.db";
        // 检查 SQLite 数据库文件是否存在
        if ((new File(DB_PATH + DB_NAME)).exists() == false) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

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

        // 下面测试 /data/data/com.test.db/databases/ 下的数据库是否能正常工作
        // SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH
        // + DB_NAME, null);
        // Cursor cursor = database.rawQuery("select * from test", null);
        //
        // if (cursor.getCount() > 0) {
        // cursor.moveToFirst();
        // try {
        // // 解决中文乱码问题
        // byte test[] = cursor.getBlob(0);
        // String strtest = new String(test, "utf-8").trim();
        //
        // // 看输出的信息是否正确
        // System.out.println(strtest);
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // cursor.close(); //这段代码会造成数据库的未知错误(no such table:test)
    }

    public void toggleMenu(View view) {
        mMenu.toggle();
    }

	/*
     * public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
	 * menuInfo) { menu.add(1, 1, 1, "1��ϸ��Ϣ"); menu.add(1, 2, 1, "2ɾ����Ϣ");
	 * menu.add(1, 3, 1, "3�޸���Ϣ"); }
	 */

    // 创建功能键菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

//	/*
//     * @Override public boolean onContextItemSelected(MenuItem item) { int
//	 * itemId = item.getItemId(); Student s = (Student) listView.getTag();
//	 * Intent intent = null; switch (itemId) { // int ���п���ת����int���� shot
//	 * byte char case 1: intent = new Intent(this, ShowStudentActivity.class);
//	 * intent.putExtra(TableContanst.STUDENT_TABLE, s); startActivity(intent);
//	 * break; case 2: Bundle b = new Bundle(); b.putLong("id", s.getId());
//	 * showDialog(1, b); break; case 3: intent = new Intent(this,
//	 * AddStudentActivity.class); intent.putExtra(TableContanst.STUDENT_TABLE,
//	 * s); // startActivity(intent); startActivityForResult(intent, 1); break;
//	 * default: break; } return super.onContextItemSelected(item); }
//	 */

    // 为长按item时弹出菜单中的按钮添加响应事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        student = (Student) listView.getTag();
        Log.v(TAG, "TestSQLite++++student+" + listView.getTag() + "");
        final long student_id = student.getId();
        Intent intent = new Intent();
        // Log.v(TAG, "TestSQLite+++++++id"+student_id);
        switch (item_id) {
            // 添加
            case R.id.add:
                startActivity(new Intent(this, AddStudentActivity.class));
                break;
            // 删除
            case R.id.delete:
                deleteStudentInformation(student_id);
                break;
            case R.id.look:
                // 查看学生信息
                // Log.v(TAG, "TestSQLite+++++++look"+student+"");
                intent.putExtra("student", student);
                intent.setClass(this, ShowStudentActivity.class);
                this.startActivity(intent);
                break;
            case R.id.write:
                // 修改学生信息
                intent.putExtra("student", student);
                intent.setClass(this, AddStudentActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            Log.v(TAG, "onActivityResult");
            Student student = (Student) data.getSerializableExtra(TableContanst.STUDENT_TABLE);
            TextView nameView = (TextView) selectedView.findViewById(R.id.tv_item_name);
            nameView.setText(student.getName());
            // showListView();
        }
    }

//	/*
//	 * @Override protected Dialog onCreateDialog(int code, Bundle args) { Dialog
//	 * dialog; if (code == DIALOG_DELETE_COMFIRM) { final long id =
//	 * args.getLong("id"); dialog = new AlertDialog.Builder(this)
//	 * .setTitle("ѧԱ��Ϣ����ɾ��") .setMessage("ȷ��ɾ����ЩѧԱ��Ϣ��")
//	 * .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { public
//	 * void onClick(DialogInterface dialog, int which) { // ȥ���ݿ�ɾ������ int
//	 * raws = dao.deleteStudentById(id); String message = null; if (raws > 0) {
//	 * message = "idΪ" + id + "�ļ�¼��ɾ���ɹ�!"; showListView(); } else { message
//	 * = "ɾ��ʧ�ܣ�������!"; } Toast.makeText(StudentListActivity.this, message,
//	 * 0).show(); } }).setNegativeButton("ȡ��", null).show(); } else if (code ==
//	 * DIALOG_DELETE_ALL_COMFIRM) { // } return super.onCreateDialog(code,
//	 * args); }
//	 */

    public void onClick(View v) {
        if (v == addStudent) {
            startActivity(new Intent(this, QASuosiWebActivity.class));
        } else if (v == multiSelect) {
            //
            showOrHiddenMultSelectUI(true);
        } else if (v == cancelBtn) {
            showOrHiddenMultSelectUI(false);
        } else if (v == deleteBtn) {
            deleteSeleteData();
            // showDialog(DIALOG_DELETE_ALL_COMFIRM);
        } else if (v == selectAllBox) {
            checkOrcancelAllbox(selectAllBox.isChecked());
            Log.v(TAG, "select box count=" + selectIds.size());
            Log.v(TAG, "select box status count=" + checkBoxStatus.size());
        } else if (v == searchButton) {
            // 跳转到查询界面
            startActivity(new Intent(this, StudentSearch.class));
        } else if (v == btnNightmode) {
            SharedPreferences dayornight = StudentListActivity.this.getSharedPreferences("dayornight", StudentListActivity.this.MODE_PRIVATE);
            if (dayornight.getBoolean("dayornight", false) == false) {
                SharedPreferences.Editor editor = dayornight.edit();
                editor.putBoolean("dayornight", true);
                editor.commit();
                startActivity(new Intent(this, StudentListActivity.class));
                this.finish();
            } else if (dayornight.getBoolean("dayornight", false) == true) {
                SharedPreferences.Editor editor = dayornight.edit();
                editor.putBoolean("dayornight", false);
                editor.commit();
                startActivity(new Intent(this, StudentListActivity.class));
                this.finish();
            }
        }

    }

    // 刷新ListView
    public void showListView() {
        // StudentDBHelper studentDBHelper = new StudentDBHelper(
        // StudentListActivity.this);
        // SQLiteDatabase database = studentDBHelper.getWritableDatabase();
        // cursor = database.query(TableContanst.STUDENT_TABLE, null, null,
        // null,
        // null, null, TableContanst.StudentColumns.MODIFY_TIME + " desc");
        // startManagingCursor(cursor);
        List<Map<String, Object>> data = dao.getAllStudents(0, "", null, null, null);
        String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
        int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
        SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
        listView.setAdapter(adapter);
        // StudentDBHelper studentDBHelper = new StudentDBHelper(
        // StudentListActivity.this);
        // SQLiteDatabase database = studentDBHelper.getWritableDatabase();
        // cursor = database.query(TableContanst.STUDENT_TABLE, null, null,
        // null,
        // null, null, TableContanst.StudentColumns.MODIFY_TIME + " desc");
        // startManagingCursor(cursor);
        // adapter = new SimpleCursorAdapter(this, R.layout.student_list_item2,
        // cursor, new String[] { TableContanst.StudentColumns.ID,
        // TableContanst.StudentColumns.NAME,
        // TableContanst.StudentColumns.AGE,
        // TableContanst.StudentColumns.SEX,
        // TableContanst.StudentColumns.LIKES,
        // TableContanst.StudentColumns.PHONE_NUMBER,
        // TableContanst.StudentColumns.TRAIN_DATE }, new int[] {
        // R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
        // R.id.tv_stu_sex, R.id.tv_stu_likes, R.id.tv_stu_phone,
        // R.id.tv_stu_traindate });
        // listView.setAdapter(adapter);

    }

    // 有参刷新ListView
    public void showListView(Cursor cursor) {

        List<Map<String, Object>> data = dao.getAllStudents(0, "", null, null, null);
        String[] from = {TableContanst.StudentColumns.ID, TableContanst.StudentColumns.NAME,
                TableContanst.StudentColumns.SEX, TableContanst.StudentColumns.CLASSES,
                TableContanst.StudentColumns.PHONE_NUMBER, TableContanst.StudentColumns.BIRTHDAY,
                TableContanst.StudentColumns.APARTMENT, TableContanst.StudentColumns.MODIFY_TIME};
        int[] to = {R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_sex, R.id.tv_item_classes, R.id.tv_item_phone,
                R.id.tv_item_birthday, R.id.tv_item_apartment, R.id.tv_item_modify_date};
        SimpleAdapter adapter = new StudentAdpter(this, data, R.layout.student_list_item, from, to);
        listView.setAdapter(adapter);

        // adapter = new SimpleCursorAdapter(this, R.layout.student_list_item2,
        // cursor, new String[] { TableContanst.StudentColumns.ID,
        // TableContanst.StudentColumns.NAME,
        // TableContanst.StudentColumns.AGE,
        // TableContanst.StudentColumns.SEX,
        // TableContanst.StudentColumns.LIKES,
        // TableContanst.StudentColumns.PHONE_NUMBER,
        // TableContanst.StudentColumns.TRAIN_DATE }, new int[] {
        // R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
        // R.id.tv_stu_sex, R.id.tv_stu_likes, R.id.tv_stu_phone,
        // R.id.tv_stu_traindate });
        // listView.setAdapter(adapter);

    }

    // 删除信息
    private void deleteSeleteData() {
        if (selectIds.size() > 0) {
            for (int i = 0; i < selectIds.size(); i++) {
                long id = selectIds.get(i);
                Log.e(TAG, "------delete id=" + id);
                dao.deleteStudentById(id);
            }
            dao.closeDB();
            showOrHiddenMultSelectUI(false);
            selectIds.clear();
            checkBoxStatus.clear();
            showListView();
        }
    }

    // 自定义一个利用对话框形式进行数据的删除
    private void deleteStudentInformation(final long delete_id) {
        // 利用对话框的形式删除数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("成员信息删除").setMessage("确定要删除所选记录?").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int raws = dao.deleteStudentById(delete_id);
                        // layout.setVisibility(View.GONE);
                        // isDeleteList = !isDeleteList;
                        showListView();
                        if (raws > 0) {
                            Toast.makeText(StudentListActivity.this, "删除成功!", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(StudentListActivity.this, "未知错误，删除失败!", Toast.LENGTH_LONG).show();
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

    // 按下选择图标时是否显示底部自定义菜单
    public void showOrHiddenMultSelectUI(boolean isShow) {

        checkBoxStatus.clear();
        int visible = isShow ? View.GONE : View.VISIBLE;
        headLayout.setVisibility(visible);
        visible = isShow ? View.VISIBLE : View.GONE;
        bottomLayout.setVisibility(visible);
        showOrHiddenAllBox(isShow);
        selectAllBox.setChecked(false);
        clearAllboxs();
        checkTime = 0;
    }

    // 在上述方法中被调用
    private void clearAllboxs() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            if (view != null) {

                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_item_delete);

                cb.setChecked(false);
                Log.v(TAG, "clearAllboxs i=" + cb.isChecked());
            }
        }
        deleteBtn.setEnabled(false);
        deleteBtn.setTextColor(getResources().getColor(R.color.white)); // 保证在按下选择按钮时删除按钮也不可按
    }

    // 按下选择图标时显示或隐藏所有的checkbooks
    private void showOrHiddenAllBox(boolean isShow) {
        int itemCount = listView.getCount(); // List<Map<String Obejct>> data
        // size
        int childCount = listView.getChildCount();
        Log.v(TAG, "itemCount=" + itemCount + " childCount=" + childCount);
        for (int i = 0; i < itemCount; i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox box = (CheckBox) view.findViewById(R.id.cb_item_delete);
                int visibility = isShow ? View.VISIBLE : View.GONE;
                box.setVisibility(visibility);
            } else {
                Log.e(TAG, "itemview is null" + i);
            }
        }
    }

    // 全选或不全选
    private void checkOrcancelAllbox(boolean checked) {
        int willsetColor_red = 0, willsetColor_white = 0;

        int itemCount = listView.getCount();// mdata size
        int childCount = listView.getChildCount(); //
        selectIds.clear();
        checkBoxStatus.clear();

        for (int i = 0; i < itemCount; i++) {
            View view = listView.getChildAt(i);
            Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(i);
            long id = (Long) data.get(TableContanst.StudentColumns.ID);
//            long id = (Long) data.get(TableContanst.StudentColumns.PHONE_NUMBER);
            if (view != null) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_item_delete);
                TextView idView = (TextView) view.findViewById(R.id.tv_item_id);
                cb.setChecked(checked);
                // deleteBtn.setEnabled(true); //此处或者if(checked)处任意存在一处都能正确实现

                // checkBoxStatus
                // selectIds
            }
            checkBoxStatus.put(id, checked);
            if (checked) {
                checkTime = itemCount;
                deleteBtn.setEnabled(true); // 如果全选checkbox被选中,则删除按钮可按
                willsetColor_red++;
                // deleteBtn.setText(getResources().getColor(R.color.red));
                selectIds.add(id);
                Log.v(TAG, "check id=" + id);
            } else {
                checkTime = 0;
                deleteBtn.setEnabled(false);// 如果全选checkbox未被选中,则删除按钮不可按
                willsetColor_white++;
                // deleteBtn.setTextColor(getResources().getColor(R.color.white));
            }
        }
        if (willsetColor_red > 0) {
            // 经过测试发现对按钮设置颜色不能放在for循环中,因此对item勾选次数进行计数,在循环结束后判断置色
            deleteBtn.setTextColor(getResources().getColor(R.color.red));
        } else if (willsetColor_white > 0) {
            deleteBtn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    // 点击item时触发的事件
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemCount2 = listView.getCount();
        // 此处获取item的数目是为了下面被checkTime参照值
        // 其中checkTime在每一次按下选择时,恒不大于itemCount2

        if (bottomLayout.getVisibility() != View.VISIBLE) {
            Log.d("ysy950803", "Source Science Technology Association");
            Student s = getStudentByPos(position);
            Intent intent = new Intent(this, ShowStudentActivity.class);
            intent.putExtra(TableContanst.STUDENT_TABLE, s);
            startActivity(intent);
        } else {
            CheckBox box = (CheckBox) view.findViewById(R.id.cb_item_delete);
            TextView idView = (TextView) view.findViewById(R.id.tv_item_id);
//            TextView idView = (TextView) view.findViewById(R.id.tv_item_phone);
            id = Long.parseLong(idView.getText().toString());
            box.toggle();
            if (box.isChecked()) {
                // deleteBtn.setEnabled(true);
                checkTime++; // 每选中一个item,便计一次数
                selectIds.add(id);
                checkBoxStatus.put(id, true);
            } else {
                // deleteBtn.setEnabled(false);
                checkTime--; // 每取消选中一个item,便减一次数
                selectIds.remove(id);
                checkBoxStatus.put(id, false);
            }
            Log.v(TAG, "idS=" + selectIds.size());
        }
        if (checkTime > 0) {
            selectAllBox.setChecked(false);
            deleteBtn.setEnabled(true);
            deleteBtn.setTextColor(getResources().getColor(R.color.red));
            if (checkTime == itemCount2) {
                // 手动勾选至全选时,全选checkbox自动选中并改变删除按钮的可按状态与颜色
                // CheckBox cb2 = (CheckBox)
                // view.findViewById(R.id.cb_select_all);
                selectAllBox.setChecked(true);
            }
        } else if (checkTime == 0) {
            deleteBtn.setEnabled(false);
            deleteBtn.setTextColor(getResources().getColor(R.color.white));
            // CheckBox cb3 = (CheckBox) view.findViewById(R.id.cb_select_all);
            selectAllBox.setChecked(false);
            // 手动取消至不全选时,全选checkbox自动取消选中并改变删除按钮的可按状态与颜色
        }
    }

    // 长按item触发的事件
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Student s = getStudentByPos(position);
        selectedView = view;
        listView.setTag(s);
        return false;
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
            selectIds = new ArrayList<Long>();
            checkBoxStatus = new HashMap<Long, Boolean>();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);
            // Log.v(TAG, view.toString()+position);
            CheckBox box = (CheckBox) view.findViewById(R.id.cb_item_delete);
            box.setVisibility(bottomLayout.getVisibility());

            TextView idView = (TextView) view.findViewById(R.id.tv_item_id);
            long id = Long.parseLong(idView.getText().toString());
            Log.v(TAG, "getView() " + checkBoxStatus.containsKey(id));
            if (checkBoxStatus.containsKey(id)) {
                box.setChecked(checkBoxStatus.get(id));
            } else {
                box.setChecked(selectAllBox.isChecked());
            }
            // Ҫ��ס��Щid����Ӧ��itemview��ѡ����
            // Map<id, boolean>
            return view;
        }
    }

    // 创建一个按钮菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.add(1, 1, 1, "按入学日期排序");
        // menu.add(1, 2, 1, "按姓名进行排序");
        // menu.add(1, 5, 1, "按学号进行排序");
        menu.add(1, 3, 1, "搜索成员");
        menu.add(1, 4, 1, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    // 对功能键菜单中的按钮添加响应事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // // 排序
            // case 1:
            // cursor = dao.sortByTrainDate();
            // showListView(cursor);
            // break;
            //
            // // 排序
            // case 2:
            // cursor = dao.sortByName();
            // showListView(cursor);
            // break;
            // 查找
            case 3:
                startActivity(new Intent(this, StudentSearch.class));
                break;
            // 退出
            case 4:
                finish();
                break;
            // case 5:
            // cursor = dao.sortByID();
            // showListView(cursor);
            // break;
            // default:
            // break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 每次启动或返回主界面时刷新ListView
    @Override
    protected void onStart() {
        // 调用load()方法将数据库中的所有记录显示在当前页面
        super.onStart();
        showListView();
    }

    // 连续按两次退出
    private long exitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                // Object mHelperUtils;
                Toast.makeText(this, "请再按一次以退出！", Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), "请再按一次以退出！",
                // Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // save();
                finish();
                System.exit(0);
            }
            return false; // 此处返回true达到的效果相同，但若不返回值，会出现按一次就显示提示并直接退出
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setBrightness(Activity activity, float brightnessValue) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (brightnessValue > 1.0f) {
            lp.screenBrightness = 1.0f;
        } else if (brightnessValue <= 0.0f) {
            lp.screenBrightness = 0.0f;
        } else {
            lp.screenBrightness = brightnessValue;
        }
        activity.getWindow().setAttributes(lp);
    }

    private void birthdayTips() {
        String birthday = getCurrentDate();
        birthday = birthday.replace("/0", "/");
        cursor = dao.findStudent3(birthday);
        if (!cursor.moveToFirst()) {

        } else {
            // 利用对话框的形式删除数据
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Happy Birthday").setMessage("今天有人过生日哦！快去“放大镜”里面看看吧！").setCancelable(false)
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
    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }

}
