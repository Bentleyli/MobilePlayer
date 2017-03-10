package com.ljh.mobileplayer.pager;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ljh.mobileplayer.base.BasePager;

/**
 * Created by Bentley on 2017/3/10.
 */
public class NetAudioPager extends BasePager {

    private TextView textView;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);
        return textView;

    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("网络音频页面");
        Log.d("TAG","网络音频页面被初始化");
    }
}
