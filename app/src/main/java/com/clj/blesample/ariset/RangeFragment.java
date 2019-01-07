package com.clj.blesample.ariset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.tools.ToastHelper;
import com.clj.blesample.ui.LineEngine;
import com.clj.blesample.widget.SwitchButton;
import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/10/31.
 */

public class RangeFragment extends BaseFragment implements MsgComeListener {
    int isalldone = 0;
    int dele = 2; //图像对比度
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    Unbinder unbinder;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.et_2)
    EditText et2;
    @BindView(R.id.switch1)
    SwitchButton switch1;
    @BindView(R.id.rg_gas)
    RadioGroup rgGas;
    @BindView(R.id.et_3)
    EditText et3;
    @BindView(R.id.et_4)
    EditText et4;
    @BindView(R.id.ll_switch)
    LinearLayout llSwitch;
    @BindView(R.id.btn_set1)
    Button btnSet1;
    @BindView(R.id.btn_set2)
    Button btnSet2;
    @BindView(R.id.btn_set3)
    Button btnSet3;
    @BindView(R.id.btn_set4)
    Button btnSet4;
    @BindView(R.id.btn_over)
    Button btnOver;
    private LineEngine engine;

    @Override
    public void getMsg(String res) {
        if (res.startsWith("zr_cfg")) {
            String[] arr = res.split("=");
            String nums = arr[1];
            num.setText(nums);

            engine.update(Float.parseFloat(nums), dele);
        } else if (res.endsWith("zeroBase Suc")) {
            Toast.makeText(getContext(), "设定成功", Toast.LENGTH_SHORT).show();
        }

        if (res.endsWith("_ok")) {
            String ne1 = res.replace("_ok", "校准:本次成功");
            ToastHelper.shortToast(getContext(), ne1);
        }

        if (res.startsWith("range_cfg")) {
            String[] arr = res.split(" ");
            String arr1 = arr[1];
            if (arr1.contains("Suc")) {
                String ne1 = arr1.replace("Suc", "校准成功");
                ToastHelper.shortToast(getContext(), ne1);
            }
            if (arr1.contains("Fail")) {
                String ne1 = arr1.replace("Fail", "校准失败");
                ToastHelper.shortToast(getContext(), ne1);
            }

            ToastHelper.shortToast(getContext(), "");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.frgment_range, container, false);
        activity = (AirSetActivity) getActivity();

        unbinder = ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {

        engine = new LineEngine("");
        engine.setView(getActivity(), 100, lineChart);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dele = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        switch1.setOnCanNotListener(new SwitchButton.OnCanNotListener() {
            @Override
            public void onCheckedChanged(boolean cando) {
                Toast.makeText(getContext(), "请先选择设定的气体", Toast.LENGTH_SHORT).show();
            }
        });
        switch1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    activity.blewrite("sys_config enter", true);
                } else {
                    activity.blewrite("sys_config exit", true);
                    activity.type = 100;
                }
                switch (activity.type) {
                    case 0:
                        activity.cmd = "atmozr_config read_so2?";
                        break;
                    case 1:
                        activity.cmd = "atmozr_config read_no2?";
                        break;
                    case 2:
                        activity.cmd = "atmozr_config read_co?";
                        break;
                    case 3:
                        activity.cmd = "atmozr_config read_o3?";
                        break;
                    case 4:
                        activity.cmd = "atmozr_config read_adtvoc?";
                        break;
                }
                activity.startCmd = isChecked;

            }
        });
        rgGas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View checkView = rgGas.findViewById(checkedId);
                if (!checkView.isPressed()) {
                    return;
                }
                switch1.setCanTog(true);
                switch (checkedId) {
                    case R.id.rb_main_001:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        btnOver.setEnabled(true);
                        tvType.setText("现在设定二氧化硫");
                        isalldone = 0;
                        activity.type = 0;
                        btnSet1.setText("设置");
                        btnSet2.setText("设置");
                        btnSet3.setText("设置");
                        btnSet4.setText("设置");
                        break;
                    case R.id.rb_main_002:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定二氧化氮");
                        isalldone = 0;
                        activity.type = 1;
                        btnSet1.setText("设置");
                        btnSet2.setText("设置");
                        btnSet3.setText("设置");
                        btnSet4.setText("设置");
                        break;
                    case R.id.rb_main_003:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定一氧化碳");
                        isalldone = 0;
                        activity.type = 2;
                        btnSet1.setText("设置");
                        btnSet2.setText("设置");
                        btnSet3.setText("设置");
                        btnSet4.setText("设置");
                        break;
                    case R.id.rb_main_004:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定臭氧");
                        isalldone = 0;
                        activity.type = 3;
                        btnSet1.setText("设置");
                        btnSet2.setText("设置");
                        btnSet3.setText("设置");
                        btnSet4.setText("设置");
                        break;
                    case R.id.rb_main_005:
                        tvType.setText("现在设定TVOC");
                        isalldone = 0;
                        activity.type = 4;
                        btnSet1.setText("设置");
                        btnSet2.setText("设置");
                        btnSet3.setText("设置");
                        btnSet4.setText("设置");
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 添加数据
     */
    private void upadta() {

    }

    @OnClick({R.id.btn_set1, R.id.btn_set2, R.id.btn_set3, R.id.btn_set4, R.id.btn_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set1:  //n0
                if (et1.getText().toString().isEmpty()) {
                    ToastHelper.shortToast(getContext(), "请输入值");
                    return;
                }
                if (activity.type == 0) {   //
                    activity.blewrite("atmorange_config r_so2flg=n0 r_so2=" + et1.getText().toString().trim() + " m_so2=" + num.getText(), true);
                } else if (activity.type == 1) {  //
                    activity.blewrite("atmorange_config r_no2flg=n0 r_no2=" + et1.getText().toString().trim() + " m_no2=" + num.getText(), true);
                } else if (activity.type == 2) {  //
                    activity.blewrite("atmorange_config r_coflg=n0 r_co=" + et1.getText().toString().trim() + " m_co=" + num.getText(), true);
                } else if (activity.type == 3) {  //
                    activity.blewrite("atmorange_config r_o3flg=n0 r_o3=" + et1.getText().toString().trim() + " m_o3=" + num.getText(), true);
                } else if (activity.type == 4) {  //
                    activity.blewrite("tvocrange_config r_tvocflg=n0 tvocppm=" + et1.getText().toString().trim() + " tvocad=" + num.getText(), true);
                }

                break;
            case R.id.btn_set2:  //n1
                if (et2.getText().toString().isEmpty()) {
                    ToastHelper.shortToast(getContext(), "请输入值");
                    return;
                }
                if (activity.type == 0) {   //
                    activity.blewrite("atmorange_config r_so2flg=n1 r_so2=" + et2.getText().toString().trim() + " m_so2=" + num.getText(), true);
                } else if (activity.type == 1) {  //
                    activity.blewrite("atmorange_config r_no2flg=n1 r_no2=" + et2.getText().toString().trim() + " m_no2=" + num.getText(), true);
                } else if (activity.type == 2) {  //
                    activity.blewrite("atmorange_config r_coflg=n1 r_co=" + et2.getText().toString().trim() + " m_co=" + num.getText(), true);
                } else if (activity.type == 3) {  //
                    activity.blewrite("atmorange_config r_o3flg=n1 r_o3=" + et2.getText().toString().trim() + " m_o3=" + num.getText(), true);
                } else if (activity.type == 4) {  //
                    activity.blewrite("tvocrange_config r_tvocflg=n1 tvocppm=" + et2.getText().toString().trim() + " tvocad=" + num.getText(), true);
                }


                break;
            case R.id.btn_set3:  //n2
                if (et3.getText().toString().isEmpty()) {
                    ToastHelper.shortToast(getContext(), "请输入值");
                    return;
                }
                if (activity.type == 0) {   //
                    activity.blewrite("atmorange_config r_so2flg=n2 r_so2=" + et3.getText().toString().trim() + " m_so2=" + num.getText(), true);
                } else if (activity.type == 1) {  //
                    activity.blewrite("atmorange_config r_no2flg=n2 r_no2=" + et3.getText().toString().trim() + " m_no2=" + num.getText(), true);
                } else if (activity.type == 2) {  //
                    activity.blewrite("atmorange_config r_coflg=n2 r_co=" + et3.getText().toString().trim() + " m_co=" + num.getText(), true);
                } else if (activity.type == 3) {  //
                    activity.blewrite("atmorange_config r_o3flg=n2 r_o3=" + et3.getText().toString().trim() + " m_o3=" + num.getText(), true);
                } else if (activity.type == 4) {  //
                    activity.blewrite("tvocrange_config r_tvocflg=n2 tvocppm=" + et3.getText().toString().trim() + " tvocad=" + num.getText(), true);
                }


                break;
            case R.id.btn_set4:  //n3
                if (et4.getText().toString().isEmpty()) {
                    ToastHelper.shortToast(getContext(), "请输入值");
                    return;
                }
                if (activity.type == 0) {   //
                    activity.blewrite("atmorange_config r_so2flg=n3 r_so2=" + et4.getText().toString().trim() + " m_so2=" + num.getText(), true);
                } else if (activity.type == 1) {  //
                    activity.blewrite("atmorange_config r_no2flg=n3 r_no2=" + et4.getText().toString().trim() + " m_no2=" + num.getText(), true);
                } else if (activity.type == 2) {  //
                    activity.blewrite("atmorange_config r_coflg=n3 r_co=" + et4.getText().toString().trim() + " m_co=" + num.getText(), true);
                } else if (activity.type == 3) {  //
                    activity.blewrite("atmorange_config r_o3flg=n3 r_o3=" + et4.getText().toString().trim() + " m_o3=" + num.getText(), true);
                } else if (activity.type == 4) {  //
                    activity.blewrite("tvocrange_config r_tvocflg=n3 tvocppm=" + et4.getText().toString().trim() + " tvocad=" + num.getText(), true);
                }


                break;
            case R.id.btn_over:
                if (activity.type == 0) {   //
                    activity.blewrite("atmorange_config so2cail_end=0.0", true);
                } else if (activity.type == 1) {  //
                    activity.blewrite("atmorange_config no2cail_end=0.0", true);
                } else if (activity.type == 2) {  //
                    activity.blewrite("atmorange_config cocail_end=0.0", true);
                } else if (activity.type == 3) {  //
                    activity.blewrite("atmorange_config o3cail_end=0.0", true);
                } else if (activity.type == 4) {  //
                    activity.blewrite("tvocrange_config tvocend=0.0", true);
                }

                btnOver.setEnabled(false);
        }
    }

}
