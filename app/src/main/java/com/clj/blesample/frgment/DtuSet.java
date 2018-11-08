package com.clj.blesample.frgment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clj.blesample.R;
import com.clj.blesample.ariset.BasePager;

/**
 * Created by ruiao on 2018/10/31.
 */

public class DtuSet extends BasePager {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.activity_dtu, container, false);
        return v;
    }

    @Override
    public void initData() {

    }

    @Override
    public void getMsg(String res) {

    }
    @Override
    protected void onInvisible() {
        super.onInvisible();
        Log.d("vis","DtuSet onInvisible");
    }
}
