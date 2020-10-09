package com.clj.blesample.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.clj.blesample.MainActivity;
import com.clj.blesample.R;
import com.clj.blesample.tools.HttpUtil;
import com.clj.blesample.tools.SPUtils;
import com.clj.blesample.tools.ToastHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.et_usename)
    EditText etUsename;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        final String name = etUsename.getText().toString();
        String password = etPassword.getText().toString();
        RequestParams pa = new RequestParams();
        pa.add("username",name.trim());
        pa.add("password",password.trim());
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        pa.add("uid",ANDROID_ID);
        HttpUtil.get("http://27.128.180.188:33333/login.asp",pa,new JsonHttpResponseHandler("GB2312"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 try {
                        String success = response.getString("success");
                        if(success.equals("true")){

                            SPUtils.put(context,"haslogin",true);
                            SPUtils.put(context,"username",name.trim());
                            SPUtils.put(context,"quanxian",response.getString("quanxian"));
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }else {
                            ToastHelper.shortToast(context,(String)response.get("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                super.onSuccess(statusCode, headers, response);
            }
        });
    }
}
