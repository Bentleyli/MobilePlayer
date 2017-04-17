// IMusicPlayerService.aidl
package com.ljh.mobileplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {

   /**
        * 根据位置打开对应的音频文件
        * @param position
        */
       void openAudio(int position);

       /**
        * 播放音乐
        */
       void start();

       /**
        * 暂停音乐
        */
       void pause();

       /**
        * 停止
        */
       void stop();

       /**
        * 得到当前的播放进度
        * @return
        */
       int getCurrentPosition();

       /**
        * 得到当前音频的总时长
        * @return
        */
       int getDuration();

       /**
        * 得到艺术家
        * @return
        */
       String getAritist();

       /**
        * 得到歌曲名字
        * @return
        */
       String getName();

       /**
        * 得到歌曲播放的路径
        * @return
        */
       String getAudioPath();

       /**
        * 播放下一首
        */
       void next();

       /**
        * 播放上一首
        */
       void pre();


       /**
        * 设置播放模式
        * @param playmode
        */
       void setPlayMode(int playmode);

       /**
        * 得到播放模式
        * @return
        */
       int getPlayMode();

       /**
        * 是否正在播放
        * @return
        */
       boolean isPlaying();

       /**
       * 拖动音频
       */
       void seekTo(int position);

}
