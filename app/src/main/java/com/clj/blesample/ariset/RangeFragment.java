package com.clj.blesample.ariset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
 * Created by ruiao on 2018/10/31.
 */

public class RangeFragment extends BaseFragment implements MsgComeListener {

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
    @BindView(R.id.ll_switch)
    LinearLayout llSwitch;
    private LineEngine engine;

    @Override
    public void getMsg(String res) {

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
        switch1.setOnCanNotListener(new SwitchButton.OnCanNotListener() {
            @Override
            public void onCheckedChanged(boolean cando) {
                Toast.makeText(getContext(), "请先选择设定的气体", Toast.LENGTH_SHORT).show();
            }
        });
        switch1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton compoundButton, boolean isChecked) {
//                if (activity.type == 100) {
//                    Toast.makeText(getContext(), "请先选择设定的气体", Toast.LENGTH_SHORT).show();
////                    compoundButton.setChecked(false);
//                    llSwitch.setVisibility(View.GONE);
//                    return;
//                }
                if (isChecked) {
                    activity.blewrite("sys_config enter", true);
                } else {
                    activity.blewrite("sys_config exit", true);
                    activity.type = 100;
                }

//                activity.cmd = "r+realdata?";
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
                        activity.cmd = "tvocrange_config readad?";
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
                        tvType.setText("现在设定二氧化硫");
                        activity.type = 0;
                        break;
                    case R.id.rb_main_002:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定二氧化氮");
                        activity.type = 1;
                        break;
                    case R.id.rb_main_003:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定一氧化碳");
                        activity.type = 2;

                        break;
                    case R.id.rb_main_004:
                        if (activity.startCmd) {
                            Toast.makeText(getContext(), "请先关闭设定", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvType.setText("现在设定臭氧");
                        activity.type = 3;

                        break;
                    case R.id.rb_main_005:
                        tvType.setText("现在设定TVOC");
                        activity.type = 4;
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

    @OnClick({R.id.btn_set1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set1:
                if (activity.type == 0) {
                    activity.blewrite("atmorange_config r_so2flg=n0 r_so2=12.2 m_so2=10.0", true);
                } else if (activity.type == 1) {
                    activity.blewrite("atmorange_config r_no2flg=n0 r_no2=12.2 m_no2=10.0", true);
                } else if (activity.type == 2) {
                    activity.blewrite("atmorange_config r_so2flg=n0 r_so2=12.2 m_so2=10.0", true);
                } else if (activity.type == 3) {
                    activity.blewrite("atmorange_config r_so2flg=n0 r_so2=12.2 m_so2=10.0", true);
                } else if (activity.type == 4) {
                    activity.blewrite("atmorange_config r_so2flg=n0 r_so2=12.2 m_so2=10.0", true);
                }
                break;

        }
    }
}
