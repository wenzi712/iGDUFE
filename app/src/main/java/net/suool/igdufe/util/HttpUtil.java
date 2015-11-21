package net.suool.igdufe.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.*;

import net.suool.igdufe.service.LinkService;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SuooL on 15/9/24.
 */
public class HttpUtil {
    private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
    // Host地址
    public static final String HOST = "my.gdufe.edu.cn";
    // 基础地址
    public static final String URL_BASE = "http://jwxt2.gdufe.edu.cn:8080/";
    // 验证码地址
    public static final String URL_CODE = "http://my.gdufe.edu.cn/captchaGenerate.portal";
    // 登陆地址
    public static final String URL_LOGIN = "http://my.gdufe.edu.cn/userPasswordValidate.portal";
    // refer
    public static final String URL_REFER = "http://my.gdufe.edu.cn/";
    // 登录成功的首页
    public static String URL_MAIN = "http://my.gdufe.edu.cn/index.portal";
    public static String URL_MONEY = "http://my.gdufe.edu.cn/pnull.portal?.f=f385&.pmn=view&action=informationCenterAjax&.ia=false&.pen=pe344";
    // 请求地址
    public static String URL_QUERY = "http://jwxt2.gdufe.edu.cn/QUERY";
    // 教务处跳转后的地址
    private static String URL_JWXTRE = "http://jwxt2.gdufe.edu.cn:8080/";
    // 教务处新的中间地址变化字段
    private static String URL_JWXTREMID = "";

    public static String getUrlJwxtremid() {
        return "http://jwxt2.gdufe.edu.cn:8080/"+URL_JWXTREMID;
    }


    public static  String getURL_JWXTRE() {
        return URL_JWXTRE;
    }

    public static void setURL_JWXTRE(String rejwxt) {
        String[] midURL = rejwxt.split("/");
        URL_JWXTREMID = midURL[3]+"/";
        URL_JWXTRE = rejwxt;
    }

    /**
     * 在教务处获取成绩单的的请求参数
     */
    public static String __EVENTTARGET = null;
    public static String __EVENTARGUMENT = null;
    public static String __LASTFOCUS = null;
    public static String __VIEWSTATE ;
    public static String __EVENTVALIDATION ;
    public static String ddlxn ;
    public static String ddlxq ;
    public static String btnCx ;

    /**
     * 登陆信息门户的请求参数
     */
    public static String Token1 = null;
    public static String Token2 = null;
    public static String captchaField = null;
    public static String goTo = "http://my.gdufe.edu.cn/loginSuccess.portal";
    public static String gotoOnFail = "http://my.gdufe.edu.cn/loginFailure.portal";

    // 静态初始化
    static {
        //client.setEnableRedirects(true);
        client.setRedirectHandler(new CustomRedirectHandler(true));
        client.setTimeout(10000); // 设置链接超时，如果不设置，默认为10s
        // 设置请求头
        client.addHeader("Host", HOST);
        client.addHeader("Referer", URL_REFER);
        client.addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:39.0) Gecko/20100101 Firefox/39.0");
    }

    /**
     * get,用一个完整url获取一个string对象
     *
     * @param urlString
     * @param res
     */
    public static void get(String urlString, AsyncHttpResponseHandler res) {
        client.get(urlString, res);
    }

    /**
     * get,url里面带参数
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void get(String urlString, RequestParams params,
                           AsyncHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    /**
     * get,不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param res
     */
    public static void get(String urlString, JsonHttpResponseHandler res) {
        client.get(urlString, res);
    }

    /**
     * get,带参数，获取json对象或者数组
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void get(String urlString, RequestParams params,
                           JsonHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    /**
     * get,下载数据使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void get(String uString, BinaryHttpResponseHandler bHandler) {
        client.get(uString, bHandler);
    }

    /**
     * post,不带参数
     *
     * @param urlString
     * @param res
     */
    public static void post(String urlString, AsyncHttpResponseHandler res) {
        client.post(urlString, res);
    }

    /**
     * post,带参数
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(String urlString, RequestParams params,
                            AsyncHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    /**
     * post,不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param res
     */
    public static void post(String urlString, JsonHttpResponseHandler res) {
        client.post(urlString, res);
    }

    /**
     * post,带参数，获取json对象或者数组
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(String urlString, RequestParams params,
                            JsonHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    /**
     * post,返回二进制数据时使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void post(String uString, BinaryHttpResponseHandler bHandler) {
        client.post(uString, bHandler);
    }

    /**
     * 返回请求客户端
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        return client;
    }

    /**
     * 获得登录时所需的请求参数
     *
     * @return
     */
    public static RequestParams getLoginRequestParams() {
        // 设置请求参数
        RequestParams params = new RequestParams();
        params.add("Login.Token1", Token1);
        params.add("Login.Token2", Token2);
        params.add("captchaField", captchaField);
        params.add("goto", goTo);
        params.add("gotoOnFail", gotoOnFail);
        return params;
    }

    /**
     * 获得查询教务处信息时所需的请求参数
     *
     * @return
     */
    public static RequestParams getResultRequestParams() {
        // 设置请求参数
        RequestParams params = new RequestParams();
//        params.remove("Login.Token1");
//        params.remove("Login.Token2");
//        params.remove("goto");
//        params.remove("gotoOnFail");
        params.add("__EVENTARGUMENT", __EVENTARGUMENT);
        params.add("__LASTFOCUS",__LASTFOCUS );
        params.add("__EVENTTARGET",__EVENTTARGET);
        params.add("__VIEWSTATE",__VIEWSTATE);
        params.add("__EVENTVALIDATION",__EVENTVALIDATION);
        params.add("ddlxn",ddlxn);
        params.add("ddlxq",ddlxq);
        params.add("btnCx", btnCx);
        return params;
    }



    public interface QueryCallback {
        public String handleResult(String result);
    }
    public static void getQuery(final Context context,LinkService linkService,final String urlName,final QueryCallback callback) {
        final ProgressDialog dialog = CommonUtil.getProcessDialog(
                context, "正在获取"+urlName);
        dialog.show();
        String link = linkService.getLinkByName(urlName);
        if (link != null) {
            HttpUtil.URL_QUERY = HttpUtil.URL_QUERY.replace("QUERY", link);
        } else {
            Toast.makeText(context, "链接出现错误",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        HttpUtil.getClient().addHeader("Referer", HttpUtil.URL_MAIN);
        HttpUtil.getClient().setURLEncodingEnabled(true);
        HttpUtil.get(HttpUtil.URL_QUERY, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                String resultContent=null;
                try {
                    resultContent = new String(arg2, "gb2312");
                    if(callback!=null){
                        callback.handleResult(resultContent);
                    }
                    Toast.makeText(context, urlName+"获取成功！！！",Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                dialog.dismiss();
                Toast.makeText(context, urlName+"获取失败！！！",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
