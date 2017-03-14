package com.ljh.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljh.mobileplayer.R;
import com.ljh.mobileplayer.bean.MediaItem;
import com.ljh.mobileplayer.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Bentley on 2017/3/14.
 */
public class VideoPagerAdapter extends BaseAdapter {


    private ArrayList<MediaItem> mediaItems;
    private Context context;
    private Utils utils;

    public VideoPagerAdapter(Context context,ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
        utils=new Utils();
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
            convertView=View.inflate(context, R.layout.item_video_pager,null);
            holder=new ViewHolder();
            holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem=mediaItems.get(position);
        holder.tv_name.setText(mediaItem.getName());
        holder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
        holder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));

        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
