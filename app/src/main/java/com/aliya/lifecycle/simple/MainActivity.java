package com.aliya.lifecycle.simple;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.lifecycle.ActivityLifecycleCallbacks;
import com.aliya.lifecycle.LifecycleInjector;
import com.aliya.lifecycle.simple.widget.MainTabLayout;

public class MainActivity extends AppCompatActivity {

    private MainTabLayout mMainTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainTab = findViewById(R.id.main_tab);

        initTabs();
//        initLifecycle();

    }

    private void initTabs() {
        mMainTab.setupBind(getSupportFragmentManager(), R.id.frame_content);
        mMainTab.setAdapter(new MainTabAdapterImpl());
    }

    private void initLifecycle() {
        LifecycleInjector.get().addActivityLifecycleCallbacks(this, new
                ActivityLifecycleCallbacks() {

                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        Log.e("TAG", "onActivityCreated: " + hashCode());
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                        Log.e("TAG", "onActivityStarted: " + hashCode());
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        Log.e("TAG", "onActivityResumed: " + hashCode());
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                        Log.e("TAG", "onActivityPaused: " + hashCode());
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        Log.e("TAG", "onActivityStopped: " + hashCode());
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        Log.e("TAG", "onActivitySaveInstanceState: " + hashCode());
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        Log.e("TAG", "onActivityDestroyed: " + hashCode());
                    }

                });
    }

    static class MainTabAdapterImpl implements MainTabLayout.MainTabAdapter {

        MainTab[] tabs = MainTab.values();

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public MainTabLayout.TabInfo getTab(int position, ViewGroup parent) {
            MainTab mainTab = tabs[position];
            View inflate = LayoutInflater.from(parent.getContext()).inflate(mainTab
                    .getLayoutId(), parent, false);
            return new MainTabLayout.TabInfo(inflate, mainTab.getClazz(), null);
        }

    }

}
