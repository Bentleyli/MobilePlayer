package com.ljh.mobileplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.mobileplayer.base.BasePager;

/**
 * Created by Bentley on 2017/3/10.
 */
public class MyPagerFragment extends Fragment {

    private BasePager currPager;

    public MyPagerFragment(){

    }

    @SuppressLint("ValidFragment")
    public MyPagerFragment(BasePager pager) {
        this.currPager=pager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (currPager!=null){
            return currPager.rootview;
        }
        return null;
    }
}
