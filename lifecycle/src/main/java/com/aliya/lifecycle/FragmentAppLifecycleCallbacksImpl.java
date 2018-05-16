package com.aliya.lifecycle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 注意：Fragment的生命周期并不一定成对出现；当调用{@link FragmentTransaction#detach(Fragment)},
 * {@link android.support.v4.app.Fragment#onPause()}之后的生命周期不会调用。
 *
 * @author a_liYa
 * @date 2018/5/15 17:32.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
class FragmentAppLifecycleCallbacksImpl extends FragmentManager.FragmentLifecycleCallbacks {

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
