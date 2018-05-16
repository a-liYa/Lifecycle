package com.aliya.lifecycle.simple;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aliya.lifecycle.ActivityLifecycleCallbacks;
import com.aliya.lifecycle.LifecycleInjector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LifecycleInjector.get().addActivityLifecycleCallbacks(this, new ActivityLifecycleCallbacks() {

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

}
