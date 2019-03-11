package com.aliya.lifecycle.sample;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

import com.aliya.lifecycle.sample.fragment.HomeFragment;
import com.aliya.lifecycle.sample.fragment.MineFragment;
import com.aliya.lifecycle.sample.fragment.VideoFragment;

/**
 * 主界面 Tab 枚举
 *
 * @author a_liYa
 * @date 2018/5/17 09:10.
 */
public enum MainTab {

    HOME(R.layout.main_tab_item_home, HomeFragment.class),
    VIDEO(R.layout.main_tab_item_video, VideoFragment.class),
    MINE(R.layout.main_tab_item_mine, MineFragment.class);

    private @LayoutRes int layoutId;
    private Class<? extends Fragment> clazz;

    MainTab(int layoutId, Class<? extends Fragment> clazz) {
        this.layoutId = layoutId;
        this.clazz = clazz;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public Class<? extends Fragment> getClazz() {
        return clazz;
    }
}
