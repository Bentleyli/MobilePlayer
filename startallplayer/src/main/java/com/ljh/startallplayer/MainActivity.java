package com.ljh.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAllPlayer(View view){
        Intent intent = new Intent();
//        intent.setDataAndType(Uri.parse("http://192.168.31.186:8080/thanksforyourself.mp4"),"video/*");
        //intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2017/03/10/mp4/170310094908405891.mp4"),"video/*");
//        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2017/03/10/mp4/1703100949084058911.mp4"),"video/*");
//        intent.setDataAndType(Uri.parse("http://cctv13.vtime.cntv.wscdns.com/live/no/23_/seg0/index.m3u8?uid=default&AUTH=6+sb7H/DDgZ9MYff0mJ1rpMUyksw8zC6nQhOykNIpXaTZdEDg6huYnsWRW7KsatosnIXEVhU2Yr6gZJ5V8xEUw=="),"video/*");


        intent.setDataAndType(Uri.parse("http://cctv13.live.cntv.dnion.com/live/no/23_/seg0/index.m3u8?uid=default&ptype=1&AUTH=eIWrQp0/KRWY3gcVKi+t/8mQCQzlEvu3J6r+pOY4S0iLc/As4fkVs/QOKBURE6sNUA6pOEmvW7pW16c2wKaqIQ=="),"video/*");startActivity(intent);
    }
}
