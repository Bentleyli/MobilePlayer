package com.ljh.mobileplayer.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.ljh.mobileplayer.R;

/**
 * Created by Bentley on 2017/4/1.
 */
public class AudioPlayerActivity extends Activity {

    private ImageView iv_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audioplayer);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setBackgroundResource(R.drawable.animation_list);

        AnimationDrawable rocketAnimation = (AnimationDrawable) iv_icon.getBackground();
        rocketAnimation.start();
    }
}
