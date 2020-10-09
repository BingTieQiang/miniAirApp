package com.clj.blesample.ariset;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2018/9/6.
 */

public class DataFragment extends BaseFragment implements MsgComeListener {

    //    Timer timer = new Timer();
    TextView mac_add;
    TextView tv_mac;
    @BindView(R.id.tv_so2)
    TextView tvSo2;
    @BindView(R.id.tv_no2)
    TextView tvNo2;
    @BindView(R.id.tv_co)
    TextView tvCo;
    @BindView(R.id.tv_o3)
    TextView tvO3;
    @BindView(R.id.tv_voc)
    TextView tvVoc;
    @BindView(R.id.tv_pm25)
    TextView tvPm25;
    @BindView(R.id.tv_pm10)
    TextView tvPm10;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_drict)
    TextView tvDrict;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_press)
    TextView tvPress;
    @BindView(R.id.tv_wet)
    TextView tvWet;
    @BindView(R.id.tv_pm100)
    TextView pm100;
    @BindView(R.id.switch1)
    SwitchButton switch1;
    Unbinder unbinder;
    @BindView(R.id.tv_sound)
    TextView tvSound;

    //    ThreadSafe threadSafe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.layout_first, container, false);
        tv_mac = v.findViewById(R.id.tv_mac);
        mac_add = v.findViewById(R.id.mac_add);


        Drawable drawable = getResources().getDrawable(R.drawable.lanya);
        drawable.setBounds(0, 0, 40, 40);
        tv_mac.setCompoundDrawables(drawable, null, null, null);
        String name = activity.bleDevice.getName();

        if(name == null){
            tv_mac.setText("匿名蓝牙设备");
        }
        else if(name.startsWith("RO")){
            String newName = name.replace("RO","ENV");
            tv_mac.setText("设备名称:"+newName);
        }else {
            tv_mac.setText("设备名称:"+name);
        }

        Drawable drawable2 = getResources().getDrawable(R.drawable.wangka);
        drawable2.setBounds(0, 0, 40, 40);
        mac_add.setCompoundDrawables(drawable2, null, null, null);
        String add = activity.bleDevice.getMac();
        mac_add.setText("设备MAC:" + add);

        unbinder = ButterKnife.bind(this, v);

        switch1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                activity.cmd = "r+realdata?";
                activity.startCmd = isChecked;
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
//        activity.blewrite("r+realdata?", true);
//        String aa = "realdata:so2=20.00;no2=73.94;co=0.54;o3=192.87;tvoc=0.00;pm25=69.00;pm10=86.00;windspeed=0.00;winddir=0.00;temp=0.00;humi=0.00;pres=45.71";
//        aha(aa);
//        activity.running = true;
//        activity.setAutoCmd("r+realdata?",2000);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void getMsg(String res) {

        shownon(res);
        Log.d("msg", res);
//        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();

    }

    private void shownon(String res) {
        if (res.startsWith("realdata:")) {
            String newStr = res.replace("realdata:", "");
            newStr = newStr.replace("\r\n", "");
            String[] arr = newStr.split(";");
            String temp;
            for (int i = 0; i < arr.length; i++) {
                temp = arr[i];
                if (temp.startsWith("so2=")) {
                    temp = temp.replace("so2=", "");
                    tvSo2.setText(temp);
                } else if (temp.startsWith("no2=")) {
                    temp = temp.replace("no2=", "");
                    tvNo2.setText(temp);
                } else if (temp.startsWith("co=")) {
                    temp = temp.replace("co=", "");
                    tvCo.setText(temp);
                } else if (temp.startsWith("o3=")) {
                    temp = temp.replace("o3=", "");
                    tvO3.setText(temp);
                } else if (temp.startsWith("tvoc=")) {
                    temp = temp.replace("tvoc=", "");
                    tvVoc.setText(temp);
                } else if (temp.startsWith("pm25=")) {
                    temp = temp.replace("pm25=", "");
                    tvPm25.setText(temp);
                } else if (temp.startsWith("pm10=")) {
                    temp = temp.replace("pm10=", "");
                    tvPm10.setText(temp);
                } else if (temp.startsWith("windspeed=")) {
                    temp = temp.replace("windspeed=", "");
                    tvSpeed.setText(temp);
                } else if (temp.startsWith("winddir=")) {
                    temp = temp.replace("winddir=", "");
                    tvDrict.setText(temp);
                } else if (temp.startsWith("temp=")) {
                    temp = temp.replace("temp=", "");
                    tvTemp.setText(temp);
                } else if (temp.startsWith("humi=")) {
                    temp = temp.replace("humi=", "");
                    tvWet.setText(temp);
                } else if (temp.startsWith("pres=")) {
                    temp = temp.replace("pres=", "");
                    tvPress.setText(temp);
                } else if (temp.startsWith("zs=")) {
                    temp = temp.replace("zs=", "");
                    tvSound.setText(temp);
                }else if (temp.startsWith("pm100=")) {
                    temp = temp.replace("pm100=", "");
                    pm100.setText(temp);
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    class ThreadSafe extends Thread {
        public volatile boolean exit = true;

        public void run() {
            while (exit) {
                //do something
                try {
                    Log.d("ThreadSafe", "try");
                    Thread.sleep(2000);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = "r+realdata?";
                    activity.handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
