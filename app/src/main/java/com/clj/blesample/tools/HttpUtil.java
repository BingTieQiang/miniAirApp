package com.clj.blesample.tools;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ruiao on 2018/9/20.
 */

public class HttpUtil {
    //先定义一个String类型来接收接口相同的部分
//    private static final String BASE_URL = "http://192.168.1.101:8890/type/jason/action/";
    //建立静态的AsyncHttpClient
    public static AsyncHttpClient client = new AsyncHttpClient();
    //AsyncHttpClient中有get和post方法，需要用到public方法来修饰，以便调用
    public  static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        Log.d("HTTP_GET",url+"?"+params.toString());
        client.get(url, params, responseHandler);
    }


    public static void httppost(String url,RequestParams params, AsyncHttpResponseHandler responseHandler){
        Log.d("HTTP_POST",url);
        client.setTimeout(100000);
        client.post(url,params,responseHandler);
    }
    public static class SimpJsonHandle extends JsonHttpResponseHandler{
        Context context;
        public SimpJsonHandle(Context context){
            super("GB2312");
            this.context = context;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            ToastHelper.shortToast(context,"网络错误");
        }

        @Override
        public void onFinish() {
            super.onFinish();

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            super.onSuccess(statusCode, headers, response);
        }
    }

}
