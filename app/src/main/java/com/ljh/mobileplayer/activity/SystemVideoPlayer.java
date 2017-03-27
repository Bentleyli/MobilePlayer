package com.ljh.mobileplayer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.bean.MediaItem;
import com.ljh.mobileplayer.utils.Utils;
import com.ljh.mobileplayer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bentley on 2017/3/14.
 */
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private boolean isUseSystem=true;

    private static final int PROGRESS = 1 ;//视频进度的更新
    private static final int HIDE_MEDIACONTROLLER = 2;//隐藏控制面板
    private static final int SHOW_SPEED = 3;//显示网速
    private static final int FULL_SCREEN = 1;//设置全屏
    private static final int DEFAULT_SCREEN = 2;//设置为默认
    private VideoView videoView;
    private Uri uri;

    private RelativeLayout media_controller;
    private LinearLayout llTop;
    private TextView tvName;

    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private TextView tv_buffer_netspeed;
    private LinearLayout ll_buffer;
    private LinearLayout ll_loading;
    private TextView tv_loading_netspeed;

    private Utils utils;
    private MyReceiver receiver;
    private ArrayList<MediaItem> mediaItems;
    private int position;
    private GestureDetector detector;
    private boolean isShowMediaController=false;
    private boolean isFullScreen=false;

    private int screenWidth=0;
    private int screenHeight=0;

    //真实视频的宽和高
    private int videoWidth;
    private int videoHeight;

    /**
     * 调节音量
     */
    private AudioManager am;

    /**
     * 当前音量
     */
    private int currentVolume;

    /**
     * 0-15
     * 最大音量
     */
    private int maxVolume;

    /**
     * 是否是静音
     */
    private boolean isMute=false;
    /**
     * 是否是网络的uri
     */
    private boolean isNetUri;

    //上一次的播放进度
    private int preCurrentPosition;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-15 15:10:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        media_controller = (RelativeLayout) findViewById(R.id.media_controller);
        videoView = (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSwitchScreen = (Button)findViewById( R.id.btn_video_switch_screen );
        tv_buffer_netspeed = (TextView) findViewById(R.id.tv_buffer_netspeed);
        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
        tv_loading_netspeed = (TextView) findViewById(R.id.tv_loading_netspeed);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );

        //最大音量和seekbar关联
        seekbarVoice.setMax(maxVolume);
        //设置当前进度--当前音量
        seekbarVoice.setProgress(currentVolume);

        //开始更新网速
        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-15 15:10:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
            isMute=!isMute;
            updateVolume(currentVolume,isMute);
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
            showSwitchPlayerDialog();
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
            finish();
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            startAndPause();
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
            setFullScreenAndDefault();
        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
    }

    private void showSwitchPlayerDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("系统播放器提示您");
        builder.setMessage("当您播放视频，有声音没有画面的时候，请切换万能播放器播放");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void startAndPause() {
        if (videoView.isPlaying()){
            //视频正在播放--》设置为暂停
            videoView.pause();
            //按钮状态设置为播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);

        }else{
            //视频未播放--》设置为播放
            videoView.start();
            //按钮状态设置为暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private void playPreVideo() {
        if (mediaItems!=null&&mediaItems.size()>0){
            //播放下一个
            position--;
            if (position>=0){
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem=mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri=utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
            }
        }else if(uri!=null){
            //设置按钮状态--上一个和下一个按钮设置成灰色，并且不可点击
            setButtonState();
        }
    }

    private void playNextVideo() {
        if (mediaItems!=null&&mediaItems.size()>0){
            //播放下一个
            position++;
            if (position<mediaItems.size()){
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem=mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri=utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
            }
        }else if(uri!=null){
            //设置按钮状态--上一个和下一个按钮设置成灰色，并且不可点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if (mediaItems!=null&&mediaItems.size()>0){
            if (mediaItems.size()==1){
                setEnable(false);
            }else if(mediaItems.size()==2){
                if (position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if (position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }
            }else {
                if (position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                }else if (position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }else{
                    setEnable(true);
                }
            }
        }else if (uri!=null){
            setEnable(false);
        }
    }

    private void setEnable(boolean isEnable) {
        if (isEnable){
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
        }else{
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }

    }



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                
                case SHOW_SPEED://显示网速
                    String netSpeed=utils.getNetSpeed(SystemVideoPlayer.this);

                    //显示网络速度
                    tv_loading_netspeed.setText("视频加载中..."+netSpeed);
                    tv_buffer_netspeed.setText("缓冲中..."+netSpeed);

                    //每两秒更新一次
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED,2000);
                    break;
                case HIDE_MEDIACONTROLLER:
                    //隐藏控制面板
                    hideMediaController();
                    break;
                case PROGRESS:
                   //1.得到当前的视频播放进度
                    int currentPosition=videoView.getCurrentPosition();
                    //2.SeekBar.setProgress(当前进度);
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //设置系统时间
                    tvSystemTime.setText(getSystemTime());

                    //缓存进度的更新
                    if (isNetUri){
                        //只有网络资源才有缓冲
                        int buffer=videoView.getBufferPercentage();//0-100
                        int totalBuffer=buffer*seekbarVideo.getMax();
                        int secondaryProgress=totalBuffer/100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else{
                        //本地视频没有缓冲效果
                        seekbarVideo.setSecondaryProgress(0);
                    }
                    
                    
                    //监听卡
                    if (!isUseSystem){
                        if (videoView.isPlaying()){
                            int buffer=currentPosition-preCurrentPosition;
                            if (buffer<500){
                                //视频卡了
                                ll_buffer.setVisibility(View.VISIBLE);
                            }else {
                                //视频不卡了
                                ll_buffer.setVisibility(View.GONE);
                            }
                        }else {
                            ll_buffer.setVisibility(View.GONE);
                        }
                    }
                    preCurrentPosition=currentPosition;

                    //3.每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        initData();
        findViews();
        setListener();
        getData();

        setData();
        //设置控制面板
        //videoView.setMediaController(new MediaController(this));
    }

    private void setData() {
        if (mediaItems!=null&&mediaItems.size()>0){
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            isNetUri=utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        }else if (uri!=null){
            tvName.setText(uri.toString());
            isNetUri=utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);

        }else{
            Toast.makeText(SystemVideoPlayer.this, "没有可以播放的数据", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

    private void getData() {
        //得到播放地址
        uri=getIntent().getData();

        mediaItems= (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position=getIntent().getIntExtra("position",0);



    }

    private void initData() {
        utils=new Utils();
        //注册电量广播
        receiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);

        /**
         * 实例化手势识别器，
         */
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(SystemVideoPlayer.this, "长按", Toast.LENGTH_SHORT).show();
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayer.this, "双击", Toast.LENGTH_SHORT).show();

                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //Toast.makeText(SystemVideoPlayer.this, "单击", Toast.LENGTH_SHORT).show();
                if (isShowMediaController){
                    //隐藏
                    hideMediaController();
                    //把隐藏消息移除
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                }else{
                    //显示
                    showMediaController();
                    //发消息隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        //得到屏幕的宽和高
        //过时方法
        /*screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();*/

        //得到屏幕的宽和高最新方式
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;

        //得到音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void setFullScreenAndDefault() {
        if (isFullScreen){
            //设置为默认
            setVideoType(DEFAULT_SCREEN);
        }else{
            //设置为全屏
            setVideoType(FULL_SCREEN);
        }
    }

    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN:
                //设置全屏
                //1、设置视频画面的大小
                videoView.setVideoSize(screenWidth,screenHeight);
                //2、设置按钮的状态--默认
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_default_selector);
                isFullScreen=true;
                break;
            case DEFAULT_SCREEN:
                //默认
                //1、设置视频画面的大小
                //视频真正的高和宽
                int mVideoWidth=videoWidth;
                int mVideoHeight=videoHeight;

                //屏幕的高和宽
                int width=screenWidth;
                int height=screenHeight;
                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width,height);
                //2、设置按钮的状态--全屏
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_full_selector);
                isFullScreen=false;
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra("level",10);
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level<=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if (level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if (level<=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if (level<=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if (level<=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if (level<=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if (level<=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        //准备好了的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());

        //播放出错了的监听
        videoView.setOnErrorListener(new MyOnErrorListener());

        //播放完成的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //设置seekBar状态变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

        //设置音量seekbar状态变化的监听
        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());

        if (isUseSystem){
            //监听视频播放卡顿--使用系统api
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(new MyOnInfoListener());
            }
        }

    }

    private class MyOnInfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频卡顿，拖动卡顿
                    Toast.makeText(SystemVideoPlayer.this, "视频卡了", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://视频卡顿结束，拖动卡顿结束
                    Toast.makeText(SystemVideoPlayer.this, "不卡了", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoWidth=mediaPlayer.getVideoWidth();
            videoHeight=mediaPlayer.getVideoHeight();
            videoView.start();
            //mediaPlayer.getDuration();// 也可以
            //视频的总时长
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));
            hideMediaController();//默认隐藏控制面板
            handler.sendEmptyMessage(PROGRESS);
            setVideoType(DEFAULT_SCREEN);//屏幕的默认播放

            //把加载页面消失掉
            ll_loading.setVisibility(View.GONE);

            /*mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Toast.makeText(SystemVideoPlayer.this, "拖动完成", Toast.LENGTH_SHORT).show();
                }
            });*/
//            videoView.setVideoSize(mediaPlayer.getVideoWidth(),mediaPlayer.getVideoHeight());
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//            Toast.makeText(SystemVideoPlayer.this, "播放出错了", Toast.LENGTH_SHORT).show();
            //出错情况：1、播放的视频格式不支持--跳转到万能播放器继续播放
            startVitamioPlayer();
            //2、播放网络视频的时候，网络中断--1.如果网络确实断了，可以提示用户网络断开；2.网络断断续续，重新播放
            //3、播放的时候本地文件中间有空白--下载做完成
            return true;
        }
    }

    /**
     * 跳转到vitamio万能播放器
     * a.把数据按照原装方式传入vitamioVideoPlayer播放器
     * b.关闭系统播放器
     */
    private void startVitamioPlayer() {
        Log.d("TAG","跳转vitamio");
        if (videoView!=null){
            videoView.stopPlayback();
        }
        Intent intent = new Intent(this,VitamioVideoPlayer.class);
        if(mediaItems!=null&&mediaItems.size()>0){

            //intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//*");
            Bundle bundle=new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);

        }else if (uri!=null){
            intent.setData(uri);
        }
        startActivity(intent);

        finish();
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //Toast.makeText(SystemVideoPlayer.this, "播放完成了", Toast.LENGTH_SHORT).show();
            playNextVideo();
        }
    }


    private class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * 当手指滑动，引起seekbar的进度变化，回调该方法
         * @param seekBar
         * @param progress
         * @param fromUser 如果是用户引起的，为true
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                videoView.seekTo(progress);
            }
        }

        /**
         * 当手指触碰的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        /**
         * 当手指离开的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        }
    }

    private class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                if (progress>0){
                    isMute=false;
                }else {
                    isMute=true;
                }
                updateVolume(progress,isMute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        }
    }

    /**
     * 设置音量的大小
     * @param progress
     */
    private void updateVolume(int progress,boolean isMute) {
        if (isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);//第三个参数如果是1，则会调出系统的音量框
            seekbarVoice.setProgress(0);
        }else{
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);//第三个参数如果是1，则会调出系统的音量框
            seekbarVoice.setProgress(progress);
            currentVolume=progress;
        }

    }

    @Override
    protected void onDestroy() {
        if (receiver!=null){
            unregisterReceiver(receiver);
        }

        super.onDestroy();
    }

    private float startY;
    //屏幕的高
    private float touchRange;

    //当一按下的音量
    private int mVol;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下
                startY=event.getY();
                mVol=am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRange=Math.min(screenWidth,screenHeight);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动
                float endY=event.getY();
                float distanceY=startY-endY;
                float delta=(distanceY/touchRange)*maxVolume;
                int voice= (int) Math.min(Math.max(mVol+delta,0),maxVolume);
                if (delta!=0){
                    isMute=false;
                    updateVolume(voice,isMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指离开
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                break;
        }

        return super.onTouchEvent(event);
    }


    private void showMediaController(){
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController=true;
    }

    private void hideMediaController(){
        media_controller.setVisibility(View.GONE);
        isShowMediaController=false;
    }

    /**
     * 监听物理键，实现声音的调节大小
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVolume--;
            updateVolume(currentVolume,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            currentVolume++;
            updateVolume(currentVolume,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
