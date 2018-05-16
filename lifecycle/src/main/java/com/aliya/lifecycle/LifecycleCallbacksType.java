package com.aliya.lifecycle;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.aliya.lifecycle.LifecycleCallbacksType.ON_CREATED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_DESTROYED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_PAUSED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_RESUMED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_SAVE_INSTANCE_STATE;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_STARTED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_STOPPED;

/**
 * LifecycleCallbacks回调类型注解类
 *
 * @author a_liYa
 * @date 2018/5/16 12:12.
 */
@IntDef({ON_CREATED, ON_STARTED, ON_RESUMED, ON_PAUSED, ON_STOPPED, ON_SAVE_INSTANCE_STATE,
        ON_DESTROYED})
@Retention(RetentionPolicy.SOURCE)
public @interface LifecycleCallbacksType {

    int ON_CREATED = 0;
    int ON_STARTED = 1;
    int ON_RESUMED = 2;
    int ON_PAUSED = 3;
    int ON_STOPPED = 4;
    int ON_SAVE_INSTANCE_STATE = 5;
    int ON_DESTROYED = 6;

}
