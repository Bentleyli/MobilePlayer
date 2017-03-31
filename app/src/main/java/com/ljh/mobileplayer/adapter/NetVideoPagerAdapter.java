package com.ljh.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.bean.MediaItem;
import com.squareup.picasso.Picasso;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Bentley on 2017/3/14.
 */
public class NetVideoPagerAdapter extends BaseAdapter {


    private ArrayList<MediaItem> mediaItems;
    private Context context;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_netvideo_pager,null);
            holder=new ViewHolder();
            holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem=mediaItems.get(position);
        holder.tv_name.setText(mediaItem.getName());

        holder.tv_desc.setText(mediaItem.getDesc());

        //1、使用xUtils3请求图片
        x.image().bind(holder.iv_icon,mediaItem.getImageUrl());

        //2、使用Glide请求图片
//        Glide.with(context)
//                .load(mediaItem.getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.video_default)
//                .error(R.drawable.video_default)
//                .into(holder.iv_icon);

        //3、使用Picasso请求图片
        Picasso.with(context)
                .load(mediaItem.getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(holder.iv_icon);
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
