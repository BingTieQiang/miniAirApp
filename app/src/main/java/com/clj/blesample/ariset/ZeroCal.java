package com.clj.blesample.ariset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.ui.LineEngine;
import com.github.mikephil.charting.charts.LineChart;
import com.clj.blesample.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/10/30.
 */

public class ZeroCal extends BaseFragment implements MsgComeListener {
    //    String[] arr = {"二氧化流", "二氧化氮", "一氧化碳", "臭氧"};
    int dele = 2; //图像对比度
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.tv_rg)
    RadioGroup tvRg;
    @BindView(R.id.ll_switch)
    LinearLayout llSwitch;
    private LineEngine engine;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.switch1)
    SwitchButton aSwitch;
    @BindView(R.id.tv_type)
    TextView gas_type;  //气体类型
    @BindView(R.id.num)
    TextView num;  //微型空气站上来的数

    @BindView(R.id.lineChart)
    LineChart lineChart;  //微型空气站上来的数
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.fragment_zerocal, container, false);
        unbinder = ButterKnife.bind(this, v);
//        aSwitch.setClickable();
        aSwitch.setOnCanNotListener(new SwitchButton.OnCanNotListener() {
            @Override
            public void onCheckedChanged(boolean cando) {
                Toast.makeText(getContext(), "请先选择设定的气体", Toast.LENGTH_SHORT).show();
            }
        });
        aSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton compoundButton, boolean b) {

                if (b) {
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
                }
                activity.startCmd = b;

            }
        });
        init();
        return v;
    }

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


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_set11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set11:
                String str1 = et1.getText().toString();
                if (str1.trim().equals("")) {
                    Toast.makeText(getContext(), "值不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String cmd = "";
                switch (activity.type) {
                    case 0:
                        cmd = "atmozeor_config so2=" + str1;
                        break;
                    case 1:
                        cmd = "atmozeor_config no2=" + str1;
                        break;
                    case 2:
                        cmd = "atmozeor_config co=" + str1;
                        break;
                    case 3:
                        cmd = "atmozeor_config o3=" + str1;
                        break;
                }

                if (activity.startCmd) {
                    activity.blewrite(cmd, true);
                }
//                    activity.blewrite("sys_config exit", true);
//                    activity.blewrite("sys_config enter", true);
//                } else {
//                    Toast.makeText(getContext(), "请先打开设定开关", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.btn_set2:
//                String str2 = et1.getText().toString();
//                if (str2.trim().endsWith("")) {
//                    Toast.makeText(getContext(), "值不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                break;

            default:

                break;
        }
    }

    private void init() {
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
        tvRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View checkView = tvRg.findViewById(checkedId);
                if (!checkView.isPressed()) {
                    return;
                }
                aSwitch.setCanTog(true);
                switch (checkedId) {
                    case R.id.id1:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gas_type.setText("现在设定二氧化硫");
                        activity.type = 0;
                        break;
                    case R.id.id2:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gas_type.setText("现在设定二氧化氮");
                        activity.type = 1;
                        break;
                    case R.id.id3:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gas_type.setText("现在设定一氧化碳");
                        activity.type = 2;

                        break;
                    case R.id.id4:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gas_type.setText("现在设定臭氧");
                        activity.type = 3;

                        break;
                }
            }

        });
        engine = new LineEngine("");
        engine.setView(getActivity(), 100, lineChart);

    }
}
