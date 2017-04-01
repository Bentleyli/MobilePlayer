package com.ljh.mobileplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bentley on 2017/4/1.
 * 缓存工具类
 */
public class CacheUtils {

    //缓存数据
    public static void putString(Context context,String key,String values){
        SharedPreferences sharedPreferences=context.getSharedPreferences("netcachetext",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();
    }

    //得到缓存数据
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences("netcachetext",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");

    }

}
