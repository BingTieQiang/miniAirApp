package com.clj.blesample.splash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clj.blesample.MainActivity;
import com.clj.blesample.R;
import com.clj.blesample.login.LoginActivity;
import com.clj.blesample.tools.HttpUtil;
import com.clj.blesample.tools.SPUtils;
import com.clj.blesample.tools.ToastHelper;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        Boolean haslogin = (Boolean) SPUtils.get(this,"haslogin",false);
        if(haslogin){
            RequestParams pa = new RequestParams();
            pa.put("username",(String)SPUtils.get(context,"username","") );
            HttpUtil.get("http://110.249.145.94:33333/activity.asp",pa,new HttpUtil.SimpJsonHandle(context){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        String success = response.getString("success");
                        if(success.equals("true")){
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }else {
                            ToastHelper.shortToast(context,(String)response.get("message"));
                            SPUtils.put(context,"username","");
                            SPUtils.put(context,"haslogin",false);
                            startActivity(new Intent(context, LoginActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    ToastHelper.shortToast(context,"网络错误");
                }
            });

        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }
}
