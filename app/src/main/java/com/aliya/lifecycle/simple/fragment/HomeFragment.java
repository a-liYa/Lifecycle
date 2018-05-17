package com.aliya.lifecycle.simple.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.lifecycle.simple.R;

/**
 * Home Fragment
 */
public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

    }

    static class PagerAdapter extends FragmentStatePagerAdapter {

        private String[] items = new String[]{"头条", "读报", "观点", "时局", "政情", "经济",
                "人文", "生态", "生活", "社会", "图片", "教育", "健康", "法制"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PageFragment fragment = new PageFragment();
            Bundle args = new Bundle();
            args.putString("title", items[position]);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items[position];
        }

        @Override
        public int getCount() {
            return items.length;
        }

    }

}
