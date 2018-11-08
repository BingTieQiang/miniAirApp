package com.clj.blesample.ariset;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AirSetActivity extends AppCompatActivity implements View.OnClickListener {
    public boolean canPageChange = true;
    boolean startCmd = false;
    public int type = 100; //100 没选择气体  0"二氧化碳",1 "二氧化氮",2 "一氧化碳",3"臭氧"
    public String cmd;

    static {
        System.loadLibrary("strtool");
    }
    BleManager bleManager;
    @BindView(R.id.iv_first_home)
    ImageView ivFirstHome;
    @BindView(R.id.iv_second_match)
    ImageView ivSecondMatch;
    @BindView(R.id.iv_third_recommend)
    ImageView ivThirdRecommend;
    @BindView(R.id.iv_four_mine)
    ImageView ivFourMine;
    @BindView(R.id.iv_fif_mine)
    ImageView ivFifMine;

    public native String getStr(byte[] data);

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blewrite((String) msg.obj, true);

        }
    };

    private String totalStr = "";
    Timer timer = new Timer();
    RelativeLayout r1, r2, r3, r4, r5;
    public static final String KEY_DATA = "key_data";
    private ArrayList<Byte> totalbyte = new ArrayList<>();
    //    RadioGroup group;
    private Context context;
    public BleDevice bleDevice;
    private Button[] btnArgs;
    MyFragmentPagerAdapter adapter;
    String uuid_service;
    String uuid_characteristic_write;
    String uuid_characteristic_notify;
    BluetoothGattCharacteristic xcharacteristic;
    private MyRollPager myviewpager;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_set);
        ButterKnife.bind(this);
        context = this;
        initView();
        initData();
        bleManager = BleManager.getInstance();
        Window window = AirSetActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startNotice();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        BleManager.getInstance().

    }

    private void initView() {
        myviewpager = (MyRollPager) this.findViewById(R.id.myviewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new DataFragment());
        fragments.add(new SetFragment());
        fragments.add(new DtuFragment());
        fragments.add(new RangeFragment());
        fragments.add(new ZeroCal());
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        myviewpager.setAdapter(adapter);
        myviewpager.setScroll(false);
        r1 = findViewById(R.id.rl_first_layout);
        r1.setOnClickListener(this);
        r2 = findViewById(R.id.rl_second_layout);
        r2.setOnClickListener(this);
        r3 = findViewById(R.id.rl_third_layout);
        r3.setOnClickListener(this);
        r4 = findViewById(R.id.rl_four_layout);
        r4.setOnClickListener(this);
        r5 = findViewById(R.id.rl_fif_layout);
        r5.setOnClickListener(this);


    }


    private void startNotice() {
        BleManager.getInstance().notify(
                bleDevice,
                uuid_service,
                uuid_characteristic_notify,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {

                        Log.d("Ble----", "onNotifySuccess");
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Log.d("Ble----", "onNotifyFailure");
                    }

                    @Override
                    public void onCharacteristicChanged(final byte[] data) {
                        Log.d("Ble----", "onCharacteristicChanged   data");
                        String temp = getStr(data);

                        if (!temp.endsWith("\n")) {
                            totalStr = totalStr + temp;
                        } else {
                            totalStr = totalStr + temp;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   String news =  totalStr.replace("\r\n", "");
                                    MsgComeListener frg = (MsgComeListener) adapter.currentFragment;
                                    Log.d("Ble----", "getMSG " + news);
                                    frg.getMsg(news);
                                }
                            });
                            totalStr = "";
                        }

                    }
                });
    }

    private void initData() {
        bleDevice = getIntent().getParcelableExtra(KEY_DATA);
        if (bleDevice == null) {
            finish();
        }
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        for (BluetoothGattService service : gatt.getServices()) {

            String tempuuid = service.getUuid().toString();

            if (tempuuid.startsWith("0000fff0")) {
                uuid_service = tempuuid;
                List<BluetoothGattCharacteristic> ccc = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : ccc) {
                    String temCar = characteristic.getUuid().toString();
                    if (temCar.startsWith("0000fff6")) {
                        xcharacteristic = characteristic;
                        uuid_characteristic_write = temCar;
                    }
                    if (temCar.startsWith("0000fff7")) {

                        uuid_characteristic_notify = temCar;
                    }
                }
            }

        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (startCmd) {
                    Log.d("TimerTask", "run");
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = cmd;
                    handler.sendMessage(msg);
                }

            }
        }, 0, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().clearCharacterCallback(bleDevice);
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }


    public void blewrite(final String str, boolean needTail) {
        final StringBuffer sp = new StringBuffer(str);
        if (needTail) {
            sp.append("\r\n");
        }
        byte[] data = HexUtil.strToByteArray(sp.toString());
        bleManager.write(
                bleDevice,
                uuid_service,
                uuid_characteristic_write,
                data,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.d("Ble----","onWriteSuccess()::"+HexUtil.byteArrayToStr(justWrite));
//                        Log.d("Ble----", uuid_service + "==" + uuid_characteristic_write +":::::::"+ str);
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.d("Ble----", exception.toString());
                    }
                });
    }

    public void blewrite_withdelay(final ArrayList<String> arrayList) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                for(int i = 0;i<arrayList.size();i++){
                    try {
                        Message msg = Message.obtain();
                        if(i == (arrayList.size()-1)){
                            Thread.sleep(400);
                        }
                        msg.obj = arrayList.get(i);
                        Thread.sleep(200);
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (startCmd) {
            Toast.makeText(context, "请先关闭设定开关", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (id) {
            case R.id.rl_first_layout:

                myviewpager.setCurrentItem(0);
                break;
            case R.id.rl_second_layout:

                myviewpager.setCurrentItem(1);
                break;
            case R.id.rl_third_layout:

                myviewpager.setCurrentItem(2);
                break;
            case R.id.rl_four_layout:

                myviewpager.setCurrentItem(3);
                break;
            case R.id.rl_fif_layout:

                myviewpager.setCurrentItem(4);
                break;

        }
    }




}
