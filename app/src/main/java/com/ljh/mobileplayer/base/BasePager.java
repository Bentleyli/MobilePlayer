package com.ljh.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Bentley on 2017/3/10.
 */
public abstract class BasePager {

    public final Context context;

    public View rootview;
    public boolean isInitData;

    public BasePager(Context context) {
        this.context = context;
        rootview=initView();
    }

    public abstract View initView() ;

    public void initData(){

    }
}
