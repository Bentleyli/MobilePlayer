package com.ljh.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.activity.SystemVideoPlayer;
import com.ljh.mobileplayer.adapter.NetVideoPagerAdapter;
import com.ljh.mobileplayer.base.BasePager;
import com.ljh.mobileplayer.bean.MediaItem;
import com.ljh.mobileplayer.utils.Constants;
import com.ljh.mobileplayer.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bentley on 2017/3/10.
 */
public class NetVideoPager extends BasePager {

    @ViewInject(R.id.listview)
    private XListView  mListview;

    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    private NetVideoPagerAdapter netVideoPagerAdapter;

    private boolean isLoadMore;

    /**
     * 用来装数据的集合
     */
    private ArrayList<MediaItem> mediaItems;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {

        View view=View.inflate(context, R.layout.netvideo_pager,null);
        x.view().inject(NetVideoPager.this,view);//第一个参数是：NetVideoPager，第二个参数是：布局

        mListview.setOnItemClickListener(new MyOnItemClickListener());
        mListview.setPullLoadEnable(true);
        mListview.setXListViewListener(new MyIXListViewListener());
        return view;

    }

    class MyIXListViewListener implements XListView.IXListViewListener{

        @Override
        public void onRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            getMoreDataFromNet();
        }
    }

    private void getMoreDataFromNet() {
        //联网
        //视频内容
        RequestParams params = new RequestParams(Constants.NET_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("TAG","联网成功=="+result);
                isLoadMore=true;
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("TAG","联网失败=="+ex.getMessage());
                isLoadMore=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("TAG","onCancelled=="+cex.getMessage());
                isLoadMore=false;
            }

            @Override
            public void onFinished() {
                Log.d("TAG","onFinished==");
                isLoadMore=false;
            }
        });
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            //3.传递播放列表--（如果是传递对象，需要序列化）
            Intent intent = new Intent(context,SystemVideoPlayer.class);

            Bundle bundle=new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position-1);
            context.startActivity(intent);
        }
    }


    @Override
    public void initData() {
        super.initData();

        Log.d("TAG","网络视频页面被初始化");
        getDataFromNet();

    }

    private void getDataFromNet() {
        //联网
        //视频内容
        RequestParams params = new RequestParams(Constants.NET_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("TAG","联网成功=="+result);
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("TAG","联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("TAG","onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.d("TAG","onFinished==");
            }
        });
    }

    private void processData(String json) {

        if (!isLoadMore){
            mediaItems=parseJson(json);

            //设置适配器
            if (mediaItems!=null&&mediaItems.size()>0){
                //有数据
                //设置适配器
                netVideoPagerAdapter=new NetVideoPagerAdapter(context,mediaItems);
                mListview.setAdapter(netVideoPagerAdapter);
                onLoad();
                //把文本隐藏
                mTv_nonet.setVisibility(View.GONE);
            }else {
                //没有数据
                //文本显示
                mTv_nonet.setVisibility(View.VISIBLE);
            }
            //ProgressBar隐藏
            mProgressBar.setVisibility(View.GONE);
        }else{
            //加载更多
            //要把得到的更多的数据添加到原来的集合中
            isLoadMore=false;
            mediaItems.addAll(parseJson(json));

            //刷新适配器
            netVideoPagerAdapter.notifyDataSetChanged();
            onLoad();
        }

    }

    private void onLoad() {
        mListview.stopRefresh();
        mListview.stopLoadMore();
        mListview.setRefreshTime("更新时间："+getSystemTime());
    }

    public  String getSystemTime() {
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 解析json数据两种方法：
     * 1、用系统接口解析json数据
     * 2、使用第三方解析工具（Gson,fastjson）
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(String json) {
        ArrayList<MediaItem> mediaItems=new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray!=null&&jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if (jsonObjectItem!=null){
                        MediaItem mediaItem=new MediaItem();
                        String movieName = jsonObjectItem.optString("movieName");
                        mediaItem.setName(movieName);
                        String videoTitle = jsonObjectItem.optString("videoTitle");
                        mediaItem.setDesc(videoTitle);
                        String imageUrl = jsonObjectItem.optString("coverImg");
                        mediaItem.setImageUrl(imageUrl);
                        String hightUrl = jsonObjectItem.optString("hightUrl");
                        mediaItem.setData(hightUrl);

                        //添加到集合中
                        mediaItems.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
}
