package com.ljh.mobileplayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.base.BasePager;
import com.ljh.mobileplayer.fragment.MyPagerFragment;
import com.ljh.mobileplayer.pager.AudioPager;
import com.ljh.mobileplayer.pager.NetAudioPager;
import com.ljh.mobileplayer.pager.NetVideoPager;
import com.ljh.mobileplayer.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;
    private TextView tv_search;
    private RelativeLayout rl_game;
    private ImageView iv_history;

    private ArrayList<BasePager> basePagers;

    //radioGroup选中的位置
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);
        tv_search = (TextView) findViewById(R.id.tv_search);
        rl_game = (RelativeLayout) findViewById(R.id.rl_game);
        iv_history = (ImageView) findViewById(R.id.iv_history);
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_history.setOnClickListener(this);

        basePagers=new ArrayList<>();
        basePagers.add(new VideoPager(this));//添加本地视频页面
        basePagers.add(new AudioPager(this));//添加本地音频页面
        basePagers.add(new NetVideoPager(this));//添加网络视频页面
        basePagers.add(new NetAudioPager(this));//添加网络音频页面

        //设置radioGroup点击事件
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_bottom_tag.check(R.id.rb_video);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search:
                Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(MainActivity.this, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_history:
                Toast.makeText(MainActivity.this, "历史记录", Toast.LENGTH_SHORT).show();
                break;
        }
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
