package com.clj.blesample.ariset;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by ruiao on 2018/10/11.
 */

public class BaseFragment extends Fragment {
    AirSetActivity activity;
    public MsgComeListener listener = null;
    public void setListener(MsgComeListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AirSetActivity) getActivity();
    }
}
