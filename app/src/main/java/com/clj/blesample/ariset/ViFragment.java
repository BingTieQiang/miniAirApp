package com.clj.blesample.ariset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.blesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/9/6.
 */

public class ViFragment extends BaseFragment implements MsgComeListener {
    AirSetActivity activity;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_change_city)
    TextView tvChangeCity;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.tv_get)
    TextView tvGet;
    @BindView(R.id.et_send)
    EditText etSend;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.vi, container, false);
        activity = (AirSetActivity) getActivity();
        unbinder = ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void getMsg(String res) {
        tvGet.setText(res);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        String str = etSend.getText().toString();
        activity.blewrite(str,true);
    }
}
