package com.clj.blesample.ariset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/9/6.
 */

public class DtuFragment extends BaseFragment implements MsgComeListener {

    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.et_apn)
    EditText etApn;
    @BindView(R.id.et_apn_center)
    EditText etApnCenter;
    @BindView(R.id.tv_bord)
    TextView tvBord;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tv_chuankoubao)
    TextView tvChuankoubao;
    @BindView(R.id.spr_type)
    Spinner sprType;
    Unbinder unbinder;

    AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.dtu, container, false);

        unbinder = ButterKnife.bind(this, v);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ss = getActivity().getResources().getStringArray(R.array.spingarr)[i];
                tvBord.setText(ss);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sprType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ss = getActivity().getResources().getStringArray(R.array.type)[i];
                tvChuankoubao.setText(ss);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    @Override
    public void getMsg(String res) {
        if(res.equals("sjzro#")){
            Toast.makeText(getContext(),"DTU进入设置",Toast.LENGTH_SHORT).show();
        }

        if(res.equals("dtu_cfg Suc")){
            Toast.makeText(getContext(),"DTU设定成功",Toast.LENGTH_SHORT).show();
            activity.blewrite("sys_config exit",true);
        }
        if(res.equals("exit ok")){
            Toast.makeText(getContext(),"DTU设置退出",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_ip, R.id.btn_port, R.id.btn_apn, R.id.btn_apn_center, R.id.tpye, R.id.boaurd, R.id.btn_send})
    public void onViewClicked(View view) {
        final ArrayList<String> list = new ArrayList();
        switch (view.getId()) {
            case R.id.btn_ip:
//                String ip = etIp.getText().toString().trim();
//                if (!"".equals(ip)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config ip=" + ip );
//                    activity.blewrite_withdelay(list);
//                }
                break;
            case R.id.btn_port:
//                String port = etPort.getText().toString().trim();
//                if (!"".equals(port)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config port=" + port );
//
//                    activity.blewrite_withdelay(list);
//                }
                break;
            case R.id.btn_apn:
//                String apn = etPort.getText().toString().trim();
//                if (!"".equals(apn)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config wireapn=" + apn );
//
//                    activity.blewrite_withdelay(list);
//                }
                break;
            case R.id.btn_apn_center:
//                String apn_center= etPort.getText().toString().trim();
//                if (!"".equals(apn_center)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config apndial=" + apn_center );
////                    list.add("sys_config exit" );
//                    activity.blewrite_withdelay(list);
//                }

                break;
            case R.id.tpye:
                //tvChuankoubao
//                String chuankou = tvChuankoubao.getText().toString();
//                if (!"".equals(chuankou)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config dscd=" + chuankou );
////                    list.add("sys_config exit" );
//                    activity.blewrite_withdelay(list);
//                }
                break;
            case R.id.boaurd:
//                String bord = tvBord.getText().toString();
//                if (!"".equals(bord)) {
//                    list.clear();
//                    list.add("sys_config enter" );
//                    list.add("dtu_config baud=" + bord );
////                    list.add("sys_config exit" );
//                    activity.blewrite_withdelay(list);
//                }
                break;

            case R.id.btn_send:
                list.clear();
                String cmd="dtu_config";
                String ip = etIp.getText().toString().trim();
                if (!"".equals(ip)) {
                    cmd = cmd + " ip=" +ip;
                }
                String port = etPort.getText().toString().trim();
                if (!"".equals(port)) {
                    cmd = cmd + " port="+port;
                }
                String apn = etApn.getText().toString().trim();
                if (!"".equals(apn)) {
                    cmd = cmd + " wireapn="+apn;
                }
                String apn_center= etApnCenter.getText().toString().trim();
                if (!"".equals(apn_center)) {
                    cmd = cmd + " apndial="+apn_center;
                }
                String chuankou = tvChuankoubao.getText().toString();
                if (!"".equals(chuankou)) {
                    cmd = cmd + " dscd="+chuankou;
                }

                String bord = tvBord.getText().toString();
                if (!"".equals(bord)) {
                    cmd = cmd + " baud="+bord;
                }
                list.add("sys_config enter");
                list.add(cmd);


                if(cmd.equals("dtu_config")){
                    Toast.makeText(getContext(),"未设置",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new AlertDialog.Builder(getContext()).setTitle("确认发送设置")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("发送设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.blewrite_withdelay(list);
                            }
                        })
                        .show();


                break;
        }
    }


}
