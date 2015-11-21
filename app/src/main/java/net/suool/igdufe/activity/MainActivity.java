package net.suool.igdufe.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.suool.igdufe.R;
import net.suool.igdufe.application.MyApplication;
import net.suool.igdufe.fragment.DrawerItem;
import net.suool.igdufe.fragment.DrawerItemAdapter;
import net.suool.igdufe.fragment.DrawerMenu;
import net.suool.igdufe.fragment.SimpleFramgment;
import net.suool.igdufe.model.Course;
import net.suool.igdufe.model.Grade;
import net.suool.igdufe.model.LinkNode;
import net.suool.igdufe.model.StudentInfo;
import net.suool.igdufe.service.CourseService;
import net.suool.igdufe.service.GradeService;
import net.suool.igdufe.service.LinkService;
import net.suool.igdufe.service.StudentInfoService;
import net.suool.igdufe.util.CommonUtil;
import net.suool.igdufe.util.GlobalDataUtil;
import net.suool.igdufe.util.HttpUtil;
import net.suool.igdufe.util.LinkUtil;
import net.suool.igdufe.util.SharedPreferenceUtil;
import net.suool.igdufe.view.CustomDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Manifest;

import cz.msebera.android.httpclient.Header;

/**
 * @author SuooL
 */

public class MainActivity extends AppCompatActivity {

    private long firstTime;
    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

    private FrameLayout fl_content;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TextView  tvMoney, tvTips;

    public SharedPreferenceUtil sharedPreferenceUtil, util;
    private LinkService linkService;
    private StudentInfoService studentInfoService;
    private CourseService courseService;
    private GradeService gradeService;

    private AlertDialog notifyDialog;
    private View notifyView;

    private Toolbar toolbar;

    private int hourOfDay, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValue();
        // getBasicInfo();
        getJWC();
        initView();

        if (savedInstanceState == null) setupFragment(new SimpleFramgment());
        initEvent();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        drawerLayout.closeDrawers();
        if (secondTime - firstTime > 2000) {
//                Snackbar sb = Snackbar.make(fl_content, "再按一次退出", Snackbar.LENGTH_SHORT);
//                sb.getView().setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
//                sb.show();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    private void setupFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Fragment currentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        // if (true) {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                .commit();
        // }
        // currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())
    }

    private void onDrawerMenuSelected(int position) {
        drawerLayout.closeDrawers();
    }

    private void initValue() {
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext(), "accountInfo");
        util = new SharedPreferenceUtil(getApplicationContext(), "LinkInfo");
        MyApplication application = ((MyApplication) getApplicationContext());
        linkService = application.getLinkService();
        studentInfoService = application.getStudentInfoService();
        courseService = application.getCourseService();
        gradeService = application.getGradeService();

        Calendar calendar = Calendar.getInstance();
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        // TODO 根据是否登陆过判断是否要重新获取信息
//        if ( ! sharedPreferenceUtil.getBooleanData("userID")){
//
//        }
    }

    private void initEvent() {

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("校园助手");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        RecyclerView drawerOptions = (RecyclerView) findViewById(R.id.drawer_options);
        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        notifyView = layoutInflater.inflate(R.layout.notify_dialog, (ViewGroup) findViewById(R.id.notify_view));
        tvMoney = (TextView) notifyView.findViewById(R.id.tvMoney);
        tvTips = (TextView) notifyView.findViewById(R.id.tvTips);

        tvMoney.setText("截止到昨天,您的饭卡余额还有"+GlobalDataUtil.blanace+"元.");
        Log.d("zafu", GlobalDataUtil.blanace+"元.");
        try {
            if (Float.parseFloat(GlobalDataUtil.blanace) <= 30) {
                tvTips.setText("饭卡余额不足,请及时充值!");
            }
            else if (Float.parseFloat(GlobalDataUtil.blanace) >=23333)
                tvTips.setText("抱歉!饭卡余额暂时不能获取!");
        } catch (NumberFormatException ne){
            Log.d("zafu", GlobalDataUtil.blanace + "元");
        }



        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        List<DrawerItem> drawerItems = Arrays.asList(
                new DrawerItem(DrawerItem.Type.HEADER).setText(sharedPreferenceUtil.getKeyData("Name")),
                new DrawerMenu().setIconRes(R.drawable.ic_group).setText(getString(R.string.menu_kcb)),
                new DrawerMenu().setIconRes(R.drawable.ic_map).setText(getString(R.string.menu_cjb)),
                new DrawerMenu().setIconRes(R.drawable.ic_person).setText(getString(R.string.menu_djb)),
                new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.drawable.ic_settings).setText(getString(R.string.menu_set)),
                new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.drawable.ic_person).setText(getString(R.string.menu_about)));
        drawerOptions.setLayoutManager(new LinearLayoutManager(this));
        DrawerItemAdapter adapter = new DrawerItemAdapter(drawerItems);
        adapter.setOnItemClickListener(new DrawerItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                onDrawerMenuSelected(position);
                Log.d("zafu", "You Clicked " + position);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        getKB();
                        toolbar.setTitle("我的课表");
                        break;
                    case 2:
                        // getViewState();
                        // CustomDialog pickerDialog = new CustomDialog(MainActivity.this, R.style.pickerViewDialog, R.layout.pickerview_dialog);
                        // pickerDialog.show();
                        toolbar.setTitle("校园助手");
                        Intent mainToGrade = new Intent(MainActivity.this, GradeActivity.class);
                        startActivity(mainToGrade);
                        //finish();
                        break;
                    case 3:
                        toolbar.setTitle("校园助手");
                        Intent mainToReGrade = new Intent(MainActivity.this, ReGradeActivity.class);
                        startActivity(mainToReGrade);
                        break;
                    case 4:
                        break;
                    case 5:
                        Intent mainToSet = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(mainToSet);
                        break;
                    case 6:

                        break;
                    case 7:
                        Intent mainToAbout = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(mainToAbout);
                        break;
                    case 8:

                        break;
                    default:
                }
            }
        });
        drawerOptions.setAdapter(adapter);
        drawerOptions.setHasFixedSize(true);

        notifyDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("温馨提示:")
                .setView(notifyView)
                .setPositiveButton("确定", onclick)
                .create();
        notifyDialog.show();


    }

    /**
     * 选项的事件监听器
     */
    DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Log.d("zafu", "选择了:" + GlobalDataUtil.yearSel + GlobalDataUtil.semesterSel);
            notifyDialog.dismiss();
        }

    };

    private boolean getJWC() {
        final ProgressDialog dialog = CommonUtil.getProcessDialog(
                MainActivity.this, "正在登陆教务处网站！");
        dialog.show();

        HttpUtil.getClient().addHeader("Referer", HttpUtil.URL_REFER);
        String url = util.getKeyData("教务系统");
        Log.d("zafu", "教务处的链接是：" + url);

        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dialog.dismiss();
                Log.d("zafu", "statusCode = " + arg0);
                Log.d("zafu", "这个是真实的地址" + HttpUtil.getURL_JWXTRE());
                Log.d("zafu", "这个是真实的中间地址" + HttpUtil.getUrlJwxtremid());
                DataSupport.deleteAll(LinkNode.class);

                String resultContent;
                try {
                    resultContent = new String(arg2, "gb2312");
                    //Log.d("zafu", resultContent);
                    Document content = Jsoup.parse(resultContent);

                    //StringBuilder result = new StringBuilder();
                    Elements elements = content.select("ul.nav a[target=zhuti]");
                    for (Element element : elements) {
                        // 将菜单选项的链接存储下来
                        LinkNode linkNode = new LinkNode();
                        linkNode.setTitle(element.html());
                        linkNode.setLink(HttpUtil.getUrlJwxtremid() + element.attr("href"));
                        Log.d("zafu", "链接" + linkNode.getTitle() + "\n" + linkNode.getLink() + "\n\n");
                        linkService.save(linkNode);
                    }


                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                util.setKeyData("JWCResult", true);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                Toast.makeText(getApplicationContext(), "教务处进入失败！！！",
                        Toast.LENGTH_SHORT).show();

                util.setKeyData("JWCResult", false);
            }
        });

        return util.getBooleanData("JWCResult");
    }

    /**
     * 获取课表，存入数据库
     */
    private void getKB() {
        final ProgressDialog dialog = CommonUtil.getProcessDialog(
                MainActivity.this, "正在获取课表！！！");
        dialog.show();

        // String link = util.getKeyData("教务系统")+util.getKeyData(LinkUtil.XSGRKB);
        String link = DataSupport.where("title = ?", LinkUtil.XSGRKB).find(LinkNode.class).get(0).getLink();
        Log.d("zafu", "完整的重定向后的教务系统课程表地址：" + link);
        if (!link.equals("")) {
            HttpUtil.URL_QUERY = link;
        } else {
            Toast.makeText(getApplicationContext(), "链接出现错误",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtil.getClient().setURLEncodingEnabled(true);

        HttpUtil.get(HttpUtil.URL_QUERY, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dialog.dismiss();
                String resultContent;
                try {
                    resultContent = new String(arg2, "gb2312");
                    DataSupport.deleteAll(Course.class);
                    courseService.parseCourse(resultContent);
                    Log.d("zafu", "课程表内容：\n" + resultContent);
                    //Document content = Jsoup.parse(resultContent);

                    Toast.makeText(getApplicationContext(), "课表获取成功！！！",
                            Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                setupFragment(new SimpleFramgment());
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "课表获取失败！！！",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}