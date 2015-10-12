package net.suool.igdufe.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import net.suool.igdufe.R;
import net.suool.igdufe.application.MyApplication;
import net.suool.igdufe.model.LinkNode;
import net.suool.igdufe.model.StudentInfo;
import net.suool.igdufe.service.CourseService;
import net.suool.igdufe.service.LinkService;
import net.suool.igdufe.service.StudentInfoService;
import net.suool.igdufe.util.CommonUtil;
import net.suool.igdufe.util.GlobalDataUtil;
import net.suool.igdufe.util.HttpUtil;
import net.suool.igdufe.util.SharedPreferenceUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.tablemanager.Connector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password, secrectCode;// 账号，密码，验证码
    private ImageView code;// 验证码
    private Button flashCode, login;//刷新验证码，登录
    private PersistentCookieStore cookie;
    private CheckBox rememberPW;
    private SQLiteDatabase db;
    private LinkService linkService;
    private SharedPreferenceUtil util;

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.getCode:
                    getCode();
                    break;
                case R.id.login:
                    if (username.getText().toString().equals(util.getKeyData("username"))){
                        util.setKeyData("userID",true);
                    } else {
                        util.setKeyData("userID",false);
                    }
                    login();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initValue();//变量初始化
        initView();//视图初始化
        initEvent();// 事件初始化
        initCookie(this);// cookie初始化
        initDatabase();// 数据库初始化
    }

    private void initValue() {
        MyApplication application = ((MyApplication) getApplicationContext());
        linkService = application.getLinkService();
        util = new SharedPreferenceUtil(getApplicationContext(), "accountInfo");
    }

    /**
     * 初始化View
     */
    private void initView() {

        secrectCode = (EditText) findViewById(R.id.secrectCode);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        flashCode = (Button) findViewById(R.id.getCode);
        login = (Button) findViewById(R.id.login);
        code = (ImageView) findViewById(R.id.codeImage);
        rememberPW = (CheckBox) findViewById(R.id.rememberPw);

        if (util.getBooleanData("remember")) {
            username.setText(util.getKeyData("username"));
            password.setText(util.getKeyData("password"));
            rememberPW.setChecked(true);
        } else {
            username.setText(util.getKeyData("username"));
        }
    }

    /**
     * 初始事件
     */
    private void initEvent() {
        // 一些列点击事件的初始化
        flashCode.setOnClickListener(listener);
        login.setOnClickListener(listener);
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        db = Connector.getDatabase();

        // 在assets目录下的litepal.xml 里配置数据库名，版本，映射关系
    }

    /**
     * 初始化Cookie
     */
    private void initCookie(Context context) {
        //必须在请求前初始化
        cookie = new PersistentCookieStore(context);
        HttpUtil.getClient().setCookieStore(cookie);
    }

    private void jump2Main(){

        GlobalDataUtil.studentID = HttpUtil.Token1;
        // 保存用户名和密码
        if (rememberPW.isChecked()) {
            util.setKeyData("remember", true);
            util.setKeyData("username", HttpUtil.Token1);
            util.setKeyData("password", HttpUtil.Token2);
        } else {
            util.setKeyData("remember", false);
            util.setKeyData("username", HttpUtil.Token1);
        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    /**
     * 登录
     */
    private void login() {
        HttpUtil.Token1 = username.getText().toString().trim();
        HttpUtil.Token2 = password.getText().toString().trim();
        //需要时打开验证码注释
        //HttpUtil.txtSecretCode = secrectCode.getText().toString().trim();
        if (TextUtils.isEmpty(HttpUtil.Token1)
                || TextUtils.isEmpty(HttpUtil.Token2)) {
            Toast.makeText(getApplicationContext(), "账号或者密码不能为空!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = CommonUtil.getProcessDialog(LoginActivity.this, "正在登录中！！！");
        dialog.show();
        RequestParams params = HttpUtil.getLoginRequestParams();// 获得请求参数
        HttpUtil.post(HttpUtil.URL_LOGIN, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        try {
                            String resultContent = new String(arg2, "gb2312");
                            Log.d("zafu", resultContent);
                            if (linkService.isLogin(resultContent) != null) {
                                String ret = linkService.parseMenu(resultContent);
                                Log.d("zafu", "login success:" + ret);
                                Toast.makeText(getApplicationContext(),
                                        "登录成功！！！", Toast.LENGTH_SHORT).show();
                                getBasicInfo();
                                //jump2Main();

                            } else {
                                Toast.makeText(getApplicationContext(), "账号或者密码错误！！！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        Toast.makeText(getApplicationContext(), "登录失败！！！！",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


    }

    /**
     * 获取个人基本信息并存储
     *
     */
    private void getBasicInfo(){
        final ProgressDialog dialog = CommonUtil.getProcessDialog(
                LoginActivity.this, "正在获取个人基本信息！！！");
        dialog.show();

        HttpUtil.getClient().addHeader("Referer", HttpUtil.URL_REFER);

        HttpUtil.get(HttpUtil.URL_MAIN, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dialog.dismiss();

                SharedPreferenceUtil util = new SharedPreferenceUtil(getApplicationContext(), "accountInfo");
                String resultContent;
                try {
                    resultContent = new String(arg2, "UTF-8");
                    // Log.d("zafu", resultContent);
                    Document content = Jsoup.parse(resultContent);

                    Elements info = content.select("div.composer > ul > li");

                    StudentInfo studentInfo = new StudentInfo();

                    studentInfo.setName(info.get(0).text().split("，")[0]);
                    studentInfo.setStudentID(info.get(2).text().split("：")[1]);
                    studentInfo.setIdentity(info.get(3).text().split("：")[1]);
                    studentInfo.setDepartment(info.get(4).text().split("：")[1]);

                    // studentInfoService.save(studentInfo);

                    Log.d("zafu", "个人姓名 " + studentInfo.getName());
                    util.setKeyData("Name", studentInfo.getName());

                    SharedPreferenceUtil linkUtil = new SharedPreferenceUtil(
                            getApplicationContext(), "LinkInfo");

                    Elements elements = content.select("div#pf301").select("tbody > tr"); // 获取表格
                    LinkNode linkNode;
                    for (Element elementLink : elements) {
                        if (!elementLink.select("td").toString().equals("")) {
                            for (Element element : elementLink.select("td")) {
                                if (!element.select("a").text().equals("")) {
                                    String url = element.select("td").select("a").attr("href");
                                    String text = element.select("td").select("a").text();
                                    Log.d("zafu", text + "的地址是：" + url);
                                    // Log.d("zafu", text);
                                    linkNode = new LinkNode();
                                    linkNode.setTitle(text);
                                    linkNode.setLink(url);
                                    linkService.save(linkNode);
                                    linkUtil.setKeyData(text, url);
                                }
                            }
                        }
                    }
                    jump2Main();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "获取失败！！！",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获得验证码
     */
    private void getCode() {
        final ProgressDialog dialog = CommonUtil.getProcessDialog(LoginActivity.this, "正在获取验证码");
        dialog.show();
        HttpUtil.get(HttpUtil.URL_CODE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                InputStream is = new ByteArrayInputStream(arg2);
                Bitmap decodeStream = BitmapFactory.decodeStream(is);
                code.setImageBitmap(decodeStream);
                Toast.makeText(getApplicationContext(), "验证码获取成功！！！", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {

                Toast.makeText(getApplicationContext(), "验证码获取失败！！！",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }

}
