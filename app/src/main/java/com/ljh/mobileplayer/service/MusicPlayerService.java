package com.ljh.mobileplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Bentley on 2017/4/5.
 */
public class MusicPlayerService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 根据位置打开对应的音频文件
     * @param position
     */
    private void openAudio(int position){

    }

    /**
     * 播放音乐
     */
    private void start(){

    }

    /**
     * 暂停音乐
     */
    private void pause(){

    }

    /**
     * 停止
     */
    private void stop(){

    }

    /**
     * 得到当前的播放进度
     * @return
     */
    private int getCurrentPosition(){
        return 0;
    }

    /**
     * 得到当前音频的总时长
     * @return
     */
    private int getDuration(){
        return 0;
    }

    /**
     * 得到艺术家
     * @return
     */
    private String getAritist(){
        return "";
    }

    /**
     * 得到歌曲名字
     * @return
     */
    private String getName(){
        return "";
    }

    /**
     * 得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return "";
    }

    /**
     * 播放下一首
     */
    private void next(){

    }

    /**
     * 播放上一首
     */
    private void pre(){

    }


    /**
     * 设置播放模式
     * @param playmode
     */
    private void setPlayMode(int playmode){

    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){
        return 0;
    }
}
