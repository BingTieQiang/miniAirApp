package com.clj.blesample.ariset;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.clj.blesample.R;
import com.clj.blesample.tools.HttpUtil;
import com.clj.blesample.tools.SPUtils;
import com.clj.blesample.tools.ToastHelper;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ruiao on 2018/9/6.
 */

public class SetFragment extends BaseFragment implements MsgComeListener {

    @BindView(R.id.ll_1)
    LinearLayout ll1;
    private String pass;
    TimePickerView pvTime;
    Context context;
    AlertDialog dialog;
    AlertDialog dialog1;
    @BindView(R.id.ll_mn)
    LinearLayout llMn;
    @BindView(R.id.ll_zhouqi)
    LinearLayout llZhouqi;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.jizhunxian)
    LinearLayout jizhunxian;
    @BindView(R.id.klingdianjiaozhun)
    LinearLayout klingdianjiaozhun;
    @BindView(R.id.liangchengjiaozhun)
    LinearLayout liangchengjiaozhun;
    @BindView(R.id.so2)
    LinearLayout so2;
    @BindView(R.id.no2)
    LinearLayout no2;
    @BindView(R.id.co)
    LinearLayout co;
    @BindView(R.id.o3)
    LinearLayout o3;
    @BindView(R.id.ll_set2)
    LinearLayout llSet2;
    Unbinder unbinder;
    @BindView(R.id.ll_set1)
    LinearLayout llSet3;
    @BindView(R.id.ll_set3)
    LinearLayout llSet1;
    @BindView(R.id.ll_set4)
    LinearLayout llSet4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.set_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        context = getContext();
        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//            Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                ArrayList<String> list = new ArrayList();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                list.clear();
                list.add("sys_config enter");


                list.add("settime_config time=" + sdf.format(date));
                list.add("sys_config exit");
                activity.blewrite_withdelay(list);
            }
        }).setType(new boolean[]{true, true, true, true, true, true})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setOutSideCancelable(false)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.setTitleText("设定时间");
        pass = getRandomString(3);
        View view5 = LayoutInflater.from(context).inflate(R.layout.edit, null);
        dialog1 = new AlertDialog.Builder(getContext()).setTitle("请输入密码")
                .setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText et1 = (EditText) dialog1.findViewById(R.id.edit);
                        String pass1 = et1.getEditableText().toString().trim();
                        if (pass1.equals(getBase64(pass))) {
                            llSet1.setVisibility(View.VISIBLE);
                            llSet2.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setMessage(pass)
                .setNegativeButton("取消", null).create();

        String str  = (String) SPUtils.get(context,"quanxian","");
        if(str.equals("xiao")){
            long time = Long.parseLong((String)SPUtils.get(context,"time","0"));
            long systime =  System.currentTimeMillis();
            if((systime - time)< (1 * 60 * 60 * 1000)){
                llSet1.setVisibility(View.VISIBLE);
                llSet2.setVisibility(View.VISIBLE);
                llSet3.setVisibility(View.VISIBLE);
                llSet4.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.VISIBLE);
            }
        }else if(str.equals("da")){
            llSet2.setVisibility(View.VISIBLE);
            llSet3.setVisibility(View.VISIBLE);
            llSet4.setVisibility(View.VISIBLE);
            llSet1.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.VISIBLE);
        }
        return v;
    }

    @Override
    public void getMsg(String res) {
        Log.d("msg",res.toString());
        String ss = res;
        if (ss.contains("sjzro")) {
            Toast.makeText(getContext(), "进入设置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ss.contains("exit ok")) {
            Toast.makeText(getContext(), "退出设置", Toast.LENGTH_SHORT).show();
            return;
        }


        if (ss.contains("Suc")) {
            ss = ss.replace("Suc", "成功");
        } else if (ss.contains("Fail")) {
            ss = ss.replace("Fail", "失败");
        }

        if (ss.contains("_cfg")) {
            ss = ss.replace("_cfg", "设置");
        }
        if (ss.contains("cyc")) {
            ss = ss.replace("cyc", "上传周期");
        }
        if (ss.contains("settime")) {
            ss = ss.replace("settime", "时间");
        }
        if (ss.contains("targ")) {
            ss = ss.replace("targ", "传感器目标值校准");
        }
        if (ss.contains("Rest_Fac")) {
            ss = ss.replace("Rest_Fac", "传感器回复出厂");
        }
        if (ss.contains("satzeor")) {
            ss = ss.replace("satzeor", "零点校准");
        }

        if (ss.contains("ble")) {
            ss = ss.replace("ble", "设置设备名称");
            if(ss.contains("成功")){
                AirSetActivity activity = (AirSetActivity) getActivity();
                activity.disconet();
            }

        }
        Toast.makeText(getContext(), ss, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnClick({R.id.ll_mn, R.id.ll_zhouqi, R.id.ll_time, R.id.jizhunxian,
            R.id.klingdianjiaozhun, R.id.liangchengjiaozhun, R.id.so2,
            R.id.no2, R.id.co, R.id.o3,  R.id.ll_gaoji,
            R.id.ll_so2, R.id.ll_co, R.id.ll_no2, R.id.ll_o3,R.id.ll_mima,
            R.id.so2mubiao,R.id.no2mubiao,R.id.comubiao,R.id.o3mubiao,
            R.id.so2chuchang,R.id.no2chuchang,R.id.cochuchang,R.id.o3chuchang,
            R.id.so2lindian,R.id.no2lindian,R.id.colindian,R.id.o3lindian,R.id.ll_mingcehng

    })
    public void onViewClicked(View view) {
        View view4 = LayoutInflater.from(context).inflate(R.layout.edit2, null);
        final ArrayList<String> list = new ArrayList();
        switch (view.getId()) {
            case R.id.ll_mn: //设置MN
                View view1 = LayoutInflater.from(context).inflate(R.layout.edit, null);
                dialog = new AlertDialog.Builder(getContext()).setTitle("MN号码（非空，不超过24位）").setView(view1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
//                        Toast.makeText(context, rt.getText().toString(), Toast.LENGTH_SHORT).show();
                        String ss = rt.getEditableText().toString().trim();
                        if (ss.length() > 24 || ss.length()==0 ) {
                            Toast.makeText(context, "MN不能为空，小于等于24位！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        list.add("sys_config enter");
                        list.add("mn_config num=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
                case R.id.ll_mima: //设置密码
                View view12 = LayoutInflater.from(context).inflate(R.layout.edit, null);
                dialog = new AlertDialog.Builder(getContext()).setTitle("密码(非空，不超过6位)").setView(view12).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
//                        Toast.makeText(context, rt.getText().toString(), Toast.LENGTH_SHORT).show();
                        String ss = rt.getEditableText().toString().trim();
                        if (ss.length() > 6 || ss.length()==0 ) {
                            Toast.makeText(context, "密码不能为空，小于等于6位！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        list.add("sys_config enter");
                        list.add("pw_config pw=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_zhouqi: //设置周期
                View view2 = LayoutInflater.from(context).inflate(R.layout.edit, null);
                dialog = new AlertDialog.Builder(getContext()).setTitle("输入周期").setView(view2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("cyc_config cyc=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;

            case R.id.ll_time: //设置时间
                View view3 = LayoutInflater.from(context).inflate(R.layout.edit, null);
                pvTime.show();
                break;
            case R.id.jizhunxian:  //pm 2.5
//                llSet2.setVisibility(View.VISIBLE);

                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入PM2.5 K/B值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("pm25kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.klingdianjiaozhun:  //PM10
//                llSet2.setVisibility(View.VISIBLE);
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入PM10 K/B值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("pm10kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.liangchengjiaozhun:  //TVOC
//                llSet2.setVisibility(View.VISIBLE);
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入Tvoc K/B值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("tvockb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.so2: //二氧化硫 kb值
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入二氧化硫 kb值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();


                        list.clear();
                        list.add("sys_config enter");
                        list.add("so2kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.no2://二氧化氮氧 kb值
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入二氧化氮氧 kb值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("no2kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.co://一氧化碳 kb值
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入一氧化碳 kb值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();

                        list.clear();
                        list.add("sys_config enter");
                        list.add("cokb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.o3://臭氧kb值
                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入臭氧kb值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et2.getEditableText().toString().trim();


                        list.clear();
                        list.add("sys_config enter");
                        list.add("o3kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);


                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;

            case R.id.ll_gaoji:
                showGaoji();
                break;
            //R.id.ll_00no2,R.id.ll_00co,R.id.ll_00no2,R.id.ll_00o3
            case R.id.ll_so2:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化硫零点基线").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmozeorbase_cfg nso2=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_co:
                dialog = new AlertDialog.Builder(getContext()).setTitle("一氧化碳零点基线").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmozeorbase_cfg nco=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_no2:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化氮零点基线").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmozeorbase_cfg nno2=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_o3:
                dialog = new AlertDialog.Builder(getContext()).setTitle("臭氧零点基线").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmozeorbase_cfg no3=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;

            case R.id.so2mubiao:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化硫目标值").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmotarg_config so2=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.no2mubiao:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化氮目标值").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmotarg_config no2=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.comubiao:
                dialog = new AlertDialog.Builder(getContext()).setTitle("一氧化碳目标值").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmotarg_config co=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.o3mubiao:
                dialog = new AlertDialog.Builder(getContext()).setTitle("臭氧目标值").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmotarg_config o3=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;

            case R.id.so2chuchang:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化硫传感器回复出厂").setMessage("是否恢复出厂").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmorest_config so2?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.no2chuchang:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化碳传感器回复出厂").setMessage("是否恢复出厂").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmorest_config no2?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.cochuchang:
                dialog = new AlertDialog.Builder(getContext()).setTitle("一氧化碳传感器回复出厂").setMessage("是否恢复出厂").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmorest_config co?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.o3chuchang:
                dialog = new AlertDialog.Builder(getContext()).setTitle("臭氧传感器回复出厂").setMessage("是否恢复出厂").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("atmorest_config o3?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
                //R.id.so2lindian,R.id.no2lindian,R.id.colindian,R.id.o3lindian
            case R.id.so2lindian:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化硫传感器校准零点").setMessage("是否校准零点").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmozeor_config so2?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.no2lindian:
                dialog = new AlertDialog.Builder(getContext()).setTitle("二氧化氮传感器校准零点").setMessage("是否校准零点").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmozeor_config no2?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.colindian:
                dialog = new AlertDialog.Builder(getContext()).setTitle("一氧化碳传感器校准零点").setMessage("是否校准零点").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmozeor_config co?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.o3lindian:
                dialog = new AlertDialog.Builder(getContext()).setTitle("臭氧传感器校准零点").setMessage("是否校准零点").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.clear();
                        list.add("sys_config enter");
                        list.add("satmozeor_config o3?=");
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
                case R.id.ll_mingcehng:  //设置微型空气站密码
                    dialog = new AlertDialog.Builder(getContext()).setTitle("设置微型空气站名称").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText rt = (EditText) dialog.findViewById(R.id.edit);
                            String ss = rt.getEditableText().toString().trim();
                            list.clear();
                            list.add("sys_config enter");
                            list.add("ble_config name=" + ss);
                            list.add("sys_config exit");
                            activity.blewrite_withdelay(list);
                        }
                    }).setNegativeButton("取消", null).create();
                    dialog.show();
                break;
        }
    }

    private void showGaoji() {
        RequestParams pa = new RequestParams();
        pa.put("username",(String) SPUtils.get(context,"username","") );
        HttpUtil.get("http://110.249.145.94:33333/quanxian.asp",pa,new HttpUtil.SimpJsonHandle(context){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String success = response.getString("yunxu");
                    if(success.equals("0")){
                        llSet1.setVisibility(View.VISIBLE);
                        llSet2.setVisibility(View.VISIBLE);
                        llSet3.setVisibility(View.VISIBLE);
                        llSet4.setVisibility(View.VISIBLE);
                        ll1.setVisibility(View.VISIBLE);
                        SPUtils.put(context,"time",""+System.currentTimeMillis());
                    }else {

                        ToastHelper.shortToast(context,"无权限");
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
    }

    public static String getBase64(String base1) {
        String s1 = base1;
        String res = Base64.encodeToString(s1.getBytes(), Base64.DEFAULT);
        return res.subSequence(1, 4).toString().toUpperCase();
    }

    public static String getRandomString(int length) {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(25);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


}
