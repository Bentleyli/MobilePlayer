package com.ljh.mobileplayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.base.BasePager;
import com.ljh.mobileplayer.fragment.MyPagerFragment;
import com.ljh.mobileplayer.pager.AudioPager;
import com.ljh.mobileplayer.pager.NetAudioPager;
import com.ljh.mobileplayer.pager.NetVideoPager;
import com.ljh.mobileplayer.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;

    private ArrayList<BasePager> basePagers;

    //radioGroup选中的位置
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);


        basePagers=new ArrayList<>();
        basePagers.add(new VideoPager(this));//添加本地视频页面
        basePagers.add(new AudioPager(this));//添加本地音频页面
        basePagers.add(new NetVideoPager(this));//添加网络视频页面
        basePagers.add(new NetAudioPager(this));//添加网络音频页面

        //设置radioGroup点击事件
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_bottom_tag.check(R.id.rb_video);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.rb_video:
                    position=0;
                    break;
                case R.id.rb_audio:
                    position=1;
                    break;
                case R.id.rb_net_video:
                    position=2;
                    break;
                case R.id.rb_net_audio:
                    position=3;
                    break;
            }
            setFragment();
        }
    }

    private void setFragment() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.replace(R.id.fl_main_content,new MyPagerFragment(getBasePager()));
        ft.commit();
    }

    /**
     * 根据位置得到相应的页面
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager=basePagers.get(position);
        if (basePager!=null&&!basePager.isInitData){
            basePager.initData();
            basePager.isInitData=true;
        }
        return basePager;
    }
}
