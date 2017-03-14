package com.ljh.mobileplayer.pager;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.base.BasePager;

/**
 * Created by Bentley on 2017/3/10.
 */
public class VideoPager extends BasePager {

    private TextView textView;
    private Context context;

    public VideoPager(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public View initView() {
        /*textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);*/
        View view=View.inflate(context, R.layout.video_pager,null);
        return textView;

    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("本地视频页面");
        Log.d("TAG","本地视频页面被初始化");
    }
}
