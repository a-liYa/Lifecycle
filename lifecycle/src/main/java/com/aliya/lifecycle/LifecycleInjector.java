package com.aliya.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * 生命周期注册器
 *
 * @author a_liYa
 * @date 2018/5/14 20:06.
 */
public class LifecycleInjector {

    private static volatile LifecycleInjector sInstance;

    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks;
    private FragmentLifecycleCallbacksImpl mFragmentLifecycleCallbacks;

    private LifecycleInjector() {
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl();
        mFragmentLifecycleCallbacks = new FragmentLifecycleCallbacksImpl();
    }

    public static LifecycleInjector get() {
        if (sInstance == null) {
            synchronized (LifecycleInjector.class) {
                if (sInstance == null) {
                    sInstance = new LifecycleInjector();
                }
            }
        }
        return sInstance;
    }

    public static void init(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.unregisterActivityLifecycleCallbacks(get().mActivityLifecycleCallbacks);
        application.registerActivityLifecycleCallbacks(get().mActivityLifecycleCallbacks);
    }

    private static void registerFragmentLifecycle(Activity activity) {
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            get().mFragmentLifecycleCallbacks, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getFragmentManager()
                    .registerFragmentLifecycleCallbacks(null, true);
        }
    }

    static class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            registerFragmentLifecycle(activity);
            Log.e("TAG", "onActivityCreated: " + activity.hashCode());
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.e("TAG", "onActivityDestroyed: " + activity.hashCode());
        }

    }

    /**
     * 注意：Fragment的生命周期并不一定成对出现；当调用{@link FragmentTransaction#detach(Fragment)},
     * {@link Fragment#onPause()}之后的生命周期不会调用。
     */
    static class FragmentLifecycleCallbacksImpl extends FragmentManager.FragmentLifecycleCallbacks {

        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            Log.e("TAG", "onFragmentCreated: " + f.hashCode());
        }

        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);
            Log.e("TAG", "onFragmentResumed: " + f.hashCode());
        }

        @Override
        public void onFragmentPaused(FragmentManager fm, Fragment f) {
            super.onFragmentPaused(fm, f);
            Log.e("TAG", "onFragmentPaused: " + f.hashCode());
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            Log.e("TAG", "onFragmentDestroyed: " + f.hashCode());
        }

        @Override
        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentAttached(fm, f, context);
            Log.e("TAG", "onFragmentAttached: " + f.hashCode());
        }

        @Override
        public void onFragmentDetached(FragmentManager fm, Fragment f) {
            super.onFragmentDetached(fm, f);
            Log.e("TAG", "onFragmentDetached: " + f.hashCode());
        }
    }


}
