package com.aliya.lifecycle.sample;

import android.app.Application;

import com.aliya.lifecycle.LifecycleInjector;

/**
 * Application
 *
 * @author a_liYa
 * @date 2018/5/15 10:43.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LifecycleInjector.init(this);
    }
}
