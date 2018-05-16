package com.aliya.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;

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
    private SparseArray<SparseArray<FragmentLifecycleCallbacks>> mFragmentArray;

    private LifecycleInjector() {
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl();
        mFragmentLifecycleCallbacks = new FragmentLifecycleCallbacksImpl();
        mActivityArray = new SparseArray();
        mFragmentArray = new SparseArray();
    }

    public void addActivityLifecycleCallbacks(Activity activity,
                                              ActivityLifecycleCallbacks callbacks) {
        if (activity == null || callbacks == null) return;
        SparseArray<ActivityLifecycleCallbacks> keyArray = mActivityArray.get(activity.hashCode());
        if (keyArray == null) {
            mActivityArray.put(activity.hashCode(), keyArray = new SparseArray<>());
        }
        keyArray.put(callbacks.hashCode(), callbacks);
    }

    /**
     * 删除 activity 对应的生命周期回调 callbacks
     *
     * @param activity  .
     * @param callbacks 为null时，删除当前Activity所有生命周期回调，
     */
    public void removeActivityLifecycleCallbacks(Activity activity,
                                                 @Nullable ActivityLifecycleCallbacks callbacks) {
        if (activity == null) return;
        SparseArray keyArray = mActivityArray.get(activity.hashCode());
        if (keyArray != null) {
            if (callbacks == null) {
                keyArray.clear();
            } else {
                keyArray.remove(callbacks.hashCode());
            }
        }
    }

    public void addFragmentLifecycleCallbacks(Fragment fragment,
                                              FragmentLifecycleCallbacks callbacks) {
        if (fragment == null || callbacks == null) return;
        SparseArray<FragmentLifecycleCallbacks> keyArray = mFragmentArray.get(fragment.hashCode());
        if (keyArray == null) {
            mFragmentArray.put(fragment.hashCode(), keyArray = new SparseArray());
        }
        keyArray.put(callbacks.hashCode(), callbacks);
    }

    /**
     * 删除 fragment 对应的生命周期回调 callbacks
     *
     * @param fragment  .
     * @param callbacks 为null时，删除当前Fragment所有生命周期回调，
     */
    public void removeFragmentLifecycleCallbacks(Fragment fragment,
                                                 FragmentLifecycleCallbacks callbacks) {
        if (fragment == null) return;
        SparseArray keyArray = mFragmentArray.get(fragment.hashCode());
        if (keyArray != null) {
            if (callbacks == null) {
                keyArray.clear();
            } else {
                keyArray.remove(callbacks.hashCode());
            }
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

    /**
     * 注意：Fragment的生命周期并不一定成对出现；当调用{@link FragmentTransaction#detach(Fragment)},
     * {@link Fragment#onPause()}之后的生命周期不会调用。
     *
     * @author a_liYa
     * @date 2018/5/15 18:06.
     */
    static class FragmentLifecycleCallbacksImpl extends FragmentManager.FragmentLifecycleCallbacks {

        @Override
        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentPreAttached(fm, f, context);
            onFragmentLifecycle(fm, f, null, context, null, LifecycleCallbacksType.ON_PRE_ATTACHED);
        }

        @Override
        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentAttached(fm, f, context);
            onFragmentLifecycle(fm, f, null, context, null, LifecycleCallbacksType.ON_ATTACHED);
        }

        @Override
        public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle
                savedInstanceState) {
            super.onFragmentPreCreated(fm, f, savedInstanceState);
            onFragmentLifecycle(fm, f, savedInstanceState, null, null, LifecycleCallbacksType
                    .ON_PRE_CREATED);
        }

        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            onFragmentLifecycle(fm, f, savedInstanceState, null, null, LifecycleCallbacksType
                    .ON_CREATED);
        }

        @Override
        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle
                savedInstanceState) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState);
            onFragmentLifecycle(fm, f, savedInstanceState, null, null, LifecycleCallbacksType
                    .ON_ACTIVITY_CREATED);
        }

        @Override
        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle
                savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            onFragmentLifecycle(fm, f, savedInstanceState, null, v, LifecycleCallbacksType
                    .ON_VIEW_CREATED);
        }

        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            super.onFragmentStarted(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_STARTED);
        }

        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_RESUMED);
        }

        @Override
        public void onFragmentPaused(FragmentManager fm, Fragment f) {
            super.onFragmentPaused(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_PAUSED);
        }

        @Override
        public void onFragmentStopped(FragmentManager fm, Fragment f) {
            super.onFragmentStopped(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_STOPPED);
        }

        @Override
        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);
            onFragmentLifecycle(fm, f, outState, null, null, LifecycleCallbacksType
                    .ON_SAVE_INSTANCE_STATE);
        }

        @Override
        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_VIEW_DESTROYED);
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_DESTROYED);
        }

        @Override
        public void onFragmentDetached(FragmentManager fm, Fragment f) {
            super.onFragmentDetached(fm, f);
            onFragmentLifecycle(fm, f, null, null, null, LifecycleCallbacksType.ON_DETACHED);
        }
    }

    private static void onFragmentLifecycle(FragmentManager fm, Fragment f, Bundle bundle,
                                            Context context, View v,
                                            @LifecycleCallbacksType int type) {
        SparseArray<FragmentLifecycleCallbacks> keyArray = get().mFragmentArray.get(f.hashCode());
        if (keyArray != null) {
            for (int i = 0; i < keyArray.size(); i++) {
                FragmentLifecycleCallbacks callbacks = keyArray.get(keyArray.keyAt(i));
                if (callbacks != null) {
                    switch (type) {
                        case LifecycleCallbacksType.ON_CREATED:
                            callbacks.onFragmentActivityCreated(fm, f, bundle);
                            break;
                        case LifecycleCallbacksType.ON_STARTED:
                            callbacks.onFragmentStarted(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_RESUMED:
                            callbacks.onFragmentResumed(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_PAUSED:
                            callbacks.onFragmentPaused(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_STOPPED:
                            callbacks.onFragmentStopped(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_SAVE_INSTANCE_STATE:
                            callbacks.onFragmentSaveInstanceState(fm, f, bundle);
                            break;
                        case LifecycleCallbacksType.ON_DESTROYED:
                            callbacks.onFragmentDestroyed(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_PRE_ATTACHED:
                            callbacks.onFragmentPreAttached(fm, f, context);
                            break;
                        case LifecycleCallbacksType.ON_ATTACHED:
                            callbacks.onFragmentAttached(fm, f, context);
                            break;
                        case LifecycleCallbacksType.ON_PRE_CREATED:
                            callbacks.onFragmentPreCreated(fm, f, bundle);
                            break;
                        case LifecycleCallbacksType.ON_ACTIVITY_CREATED:
                            callbacks.onFragmentActivityCreated(fm, f, bundle);
                            break;
                        case LifecycleCallbacksType.ON_VIEW_CREATED:
                            callbacks.onFragmentViewCreated(fm, f, v, bundle);
                            break;
                        case LifecycleCallbacksType.ON_VIEW_DESTROYED:
                            callbacks.onFragmentViewDestroyed(fm, f);
                            break;
                        case LifecycleCallbacksType.ON_DETACHED:
                            callbacks.onFragmentDetached(fm, f);
                            break;
                    }
                }
            }
        }
    }

}
