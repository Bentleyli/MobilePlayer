package com.ljh.mobileplayer.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.activity.AudioPlayerActivity;
import com.ljh.mobileplayer.adapter.VideoPagerAdapter;
import com.ljh.mobileplayer.base.BasePager;
import com.ljh.mobileplayer.bean.MediaItem;

import java.util.ArrayList;

/**
 * Created by Bentley on 2017/3/10.
 */
public class AudioPager extends BasePager {

    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private ArrayList<MediaItem> mediaItems;
    private VideoPagerAdapter videoPagerAdapter;


    public AudioPager(Context context) {
        super(context);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems!=null&&mediaItems.size()>0){
                //有数据
                //设置适配器\
                videoPagerAdapter=new VideoPagerAdapter(context,mediaItems,false);
                listView.setAdapter(videoPagerAdapter);
                //把文本隐藏
                tv_nomedia.setVisibility(View.GONE);
            }else {
                //没有数据
                //文本显示
                tv_nomedia.setVisibility(View.VISIBLE);
                tv_nomedia.setText("没有发现音频...");
            }
            //ProgressBar隐藏
            pb_loading.setVisibility(View.GONE);
        }
    };

    @Override
    public View initView() {
        /*textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);*/
        View view=View.inflate(context, R.layout.video_pager,null);
        listView= (ListView) view.findViewById(R.id.listview);
        tv_nomedia= (TextView) view.findViewById(R.id.tv_nomedia);
        pb_loading= (ProgressBar) view.findViewById(R.id.pb_loading);

        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);


            //3.传递播放列表--（如果是传递对象，需要序列化）
            Intent intent = new Intent(context,AudioPlayerActivity.class);
            intent.putExtra("position",position);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        //textView.setText("本地视频页面");
        Log.d("TAG","本地音频页面被初始化");
        //获取本地视频数据
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     * //1.遍历sdcard,根据后缀名
     * //2.从内容提供者里面获取视频
     * //3.如果是6.0的系统，动态获取读取sdcard的权限
     */
    private void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mediaItems=new ArrayList<MediaItem>();
                ContentResolver resolver= context.getContentResolver();
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri,objs,null,null,null);
                if (cursor!=null){
                    while (cursor.moveToNext()){
                        MediaItem mediaItem=new MediaItem();

                        String name = cursor.getString(0);//视频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                        mediaItems.add(mediaItem);
                    }
                    cursor.close();
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }



    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

}
