package com.clj.blesample.ariset;

import android.app.AlertDialog;
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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/9/6.
 */

public class SetFragment extends BaseFragment implements MsgComeListener {
    private String pass;
    TimePickerView pvTime ;
    Context context;
    AlertDialog dialog;
    AlertDialog dialog1;
    @BindView(R.id.ll_mn)
    LinearLayout llMn;
    @BindView(R.id.ll_zhouqi)
    LinearLayout llZhouqi;
    @BindView(R.id.ll_dtuset)
    LinearLayout llDtuset;
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
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_set1)
    LinearLayout llSet1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.set_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        context = getContext();
        etPassword.setHint(getRandomString(3));
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
                .setLabel("年","月","日","时","分","秒")
                .build();
        pvTime.setTitleText("设定时间");
        pass = getRandomString(3);
        View view5= LayoutInflater.from(context).inflate(R.layout.edit, null);
        dialog1 = new AlertDialog.Builder(getContext()).setTitle("请输入密码")
                .setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText et1 = (EditText) dialog1.findViewById(R.id.edit);
                        String pass1 = et1.getEditableText().toString().trim();
                        if(pass1.equals(getBase64(pass))){
                            llSet1.setVisibility(View.VISIBLE);
                            llSet2.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setMessage(pass)
                .setNegativeButton("取消", null).create();
//        EditText et1 =  (EditText) dialog.findViewById(R.id.edit);
//        et1.setHint(getRandomString(3));
        return v;
    }

    @Override
    public void getMsg(String res) {
        String ss = res;
        if(ss.contains("sjzro")){
            Toast.makeText(getContext(),"进入设置",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ss.contains("exit ok")){
            Toast.makeText(getContext(),"退出设置",Toast.LENGTH_SHORT).show();
            return;
        }


        if(ss.contains("Suc")){
            ss = ss.replace("Suc","成功");
        }else if(ss.contains("Fail")){
            ss = ss.replace("Fail","失败");
        }

        if(ss.contains("_cfg")){
            ss = ss.replace("_cfg","设置");
        }
        if(ss.contains("cyc")){
            ss = ss.replace("cyc","上传周期");
        }
        if(ss.contains("settime")){
            ss = ss.replace("settime","时间");
        }

        Toast.makeText(getContext(),ss,Toast.LENGTH_SHORT).show();



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

    @OnClick({R.id.ll_mn, R.id.ll_zhouqi, R.id.ll_dtuset, R.id.ll_time, R.id.jizhunxian,
            R.id.klingdianjiaozhun, R.id.liangchengjiaozhun, R.id.so2,
            R.id.no2, R.id.co, R.id.o3, R.id.btn_showkb
    ,R.id.ll_gaoji})
    public void onViewClicked(View view) {
        View view4 = LayoutInflater.from(context).inflate(R.layout.edit2, null);
        final ArrayList<String> list = new ArrayList();
        switch (view.getId()) {
            case R.id.ll_mn: //设置MN
                View view1 = LayoutInflater.from(context).inflate(R.layout.edit, null);
                dialog = new AlertDialog.Builder(getContext()).setTitle("输入MN号码(24位)").setView(view1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
//                        Toast.makeText(context, rt.getText().toString(), Toast.LENGTH_SHORT).show();
                        String ss = rt.getEditableText().toString().trim();
                        if(ss.length()!=24){
                            Toast.makeText(context,"MN为24位，请检查！",Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("mn_config num=" + ss, true);
//                        activity.blewrite("sys_config exit", true);
                        list.clear();
                        list.add("sys_config enter");
                        list.add("mn_config num=" + ss);
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
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("cyc_config cyc=" + ss, true);
//                        activity.blewrite("sys_config exit", true);

                        list.clear();
                        list.add("sys_config enter");
                        list.add("cyc_config cyc=" + ss);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);

                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_dtuset: //设置零点基准线

                dialog = new AlertDialog.Builder(getContext()).setTitle("零点基线").setView(R.layout.edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
                        String ss = rt.getEditableText().toString().trim();
                        activity.blewrite("sys_config enter", true);
                        activity.blewrite("cyc_config cyc=" + ss, true);
                        activity.blewrite("sys_config exit", true);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.ll_time: //设置时间
                View view3 = LayoutInflater.from(context).inflate(R.layout.edit, null);

                pvTime.show();
//                dialog = new AlertDialog.Builder(getContext()).setTitle("输入时间").setView(view3).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        EditText rt = (EditText) dialog.findViewById(R.id.edit);
//                        String ss = rt.getEditableText().toString();
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("settime_config time=" + ss, true);
//                        activity.blewrite("sys_config exit", true);


//
//                    }
//                }).setNegativeButton("取消", null).create();
//                dialog.show();
                break;
            case R.id.jizhunxian:  //pm 2.5
//                llSet2.setVisibility(View.VISIBLE);

                dialog = new AlertDialog.Builder(getContext()).setTitle("请输入PM2.5 K/B值").setView(view4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) dialog.findViewById(R.id.edit1);
                        EditText et2 = (EditText) dialog.findViewById(R.id.edit2);
                        String ss1 = et1.getEditableText().toString().trim();
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
////                        activity.blewrite("so2kb_config k=1.0 b=0.0"+ss1,true);
//                        activity.blewrite("pm25kb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);


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
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
////                        activity.blewrite("so2kb_config k=1.0 b=0.0"+ss1,true);
//                        activity.blewrite("pm10kb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);


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
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("tvockb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);

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
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("so2kb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);


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
                        String ss2 = et1.getEditableText().toString().trim();
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
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("cokb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);
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
                        String ss2 = et1.getEditableText().toString().trim();
//                        activity.blewrite("sys_config enter", true);
//                        activity.blewrite("o3kb_config  k=" + ss1 + " " + "b=" + ss2, true);
//                        activity.blewrite("sys_config exit", true);

                        list.clear();
                        list.add("sys_config enter");
                        list.add("o3kb_config k=" + ss1 + " " + "b=" + ss2);
                        list.add("sys_config exit");
                        activity.blewrite_withdelay(list);


                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.btn_showkb:
                String pass = etPassword.getEditableText().toString().trim();
                String hint =  etPassword.getHint().toString();

                if(pass.equals(getBase64(hint))){
                    llSet1.setVisibility(View.VISIBLE);
                    llSet2.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_gaoji:
                dialog1.show();
                break;
        }
    }
    public static String getBase64(String base1){
        String s1 =  base1;
        String res  = Base64.encodeToString(s1.getBytes(),Base64.DEFAULT);
        return res.subSequence(1,4).toString().toUpperCase();
    }
    public static String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(25);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


}
