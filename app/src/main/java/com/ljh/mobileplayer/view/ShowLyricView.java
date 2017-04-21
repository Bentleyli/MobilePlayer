package com.ljh.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ljh.mobileplayer.bean.Lyric;

import java.util.ArrayList;

/**
 * Created by Bentley on 2017/4/20.
 */
public class ShowLyricView extends TextView{

    private ArrayList<Lyric> lyrics;

    public ShowLyricView(Context context) {
        this(context,null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }
}
