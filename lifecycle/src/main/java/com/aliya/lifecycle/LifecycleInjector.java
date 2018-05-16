package com.aliya.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;

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

    private SparseArray<SparseArray<ActivityLifecycleCallbacks>> mActivityArray;

    private LifecycleInjector() {
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl();
        mFragmentLifecycleCallbacks = new FragmentLifecycleCallbacksImpl();
        mActivityArray = new SparseArray();
    }

    public void addActivityLifecycleCallbacks(Activity activity,
                                              ActivityLifecycleCallbacks callbacks) {
        SparseArray<ActivityLifecycleCallbacks> keyArray = mActivityArray.get(activity.hashCode());
        if (keyArray == null) {
            mActivityArray.put(activity.hashCode(), keyArray = new SparseArray<>());
        }
        keyArray.put(callbacks.hashCode(), callbacks);
    }

    public void removeActivityLifecycleCallbacks(Activity activity,
                                                 ActivityLifecycleCallbacks callbacks) {
        SparseArray<ActivityLifecycleCallbacks> keyArray = mActivityArray.get(activity.hashCode());
        if (keyArray != null) {
            keyArray.remove(callbacks.hashCode());
        }
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
            onActivityLifecycle(activity, savedInstanceState, LifecycleCallbacksType.ON_CREATED);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            onActivityLifecycle(activity, null, LifecycleCallbacksType.ON_STARTED);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            onActivityLifecycle(activity, null, LifecycleCallbacksType.ON_RESUMED);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            onActivityLifecycle(activity, null, LifecycleCallbacksType.ON_PAUSED);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            onActivityLifecycle(activity, null, LifecycleCallbacksType.ON_STOPPED);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            onActivityLifecycle(activity, outState, LifecycleCallbacksType.ON_SAVE_INSTANCE_STATE);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            onActivityLifecycle(activity, null, LifecycleCallbacksType.ON_DESTROYED);
            get().mActivityArray.remove(activity.hashCode());
        }

    }

    private static void onActivityLifecycle(
            Activity activity, Bundle bundle, @LifecycleCallbacksType int type) {
        SparseArray<ActivityLifecycleCallbacks> keyArray = get().mActivityArray.get(activity
                .hashCode());
        if (keyArray != null) {
            for (int i = 0; i < keyArray.size(); i++) {
                ActivityLifecycleCallbacks callbacks = keyArray.get(keyArray.keyAt(i));
                if (callbacks != null) {
                    switch (type) {
                        case LifecycleCallbacksType.ON_CREATED:
                            callbacks.onActivityCreated(activity, bundle);
                            break;
                        case LifecycleCallbacksType.ON_STARTED:
                            callbacks.onActivityStarted(activity);
                            break;
                        case LifecycleCallbacksType.ON_RESUMED:
                            callbacks.onActivityResumed(activity);
                            break;
                        case LifecycleCallbacksType.ON_PAUSED:
                            callbacks.onActivityPaused(activity);
                            break;
                        case LifecycleCallbacksType.ON_STOPPED:
                            callbacks.onActivityStopped(activity);
                            break;
                        case LifecycleCallbacksType.ON_SAVE_INSTANCE_STATE:
                            callbacks.onActivitySaveInstanceState(activity, bundle);
                            break;
                        case LifecycleCallbacksType.ON_DESTROYED:
                            callbacks.onActivityDestroyed(activity);
                            break;
                    }
                }
            }
        }
    }

}
