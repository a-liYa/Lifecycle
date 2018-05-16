package com.aliya.lifecycle;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.aliya.lifecycle.LifecycleCallbacksType.ON_ACTIVITY_CREATED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_ATTACHED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_CREATED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_DESTROYED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_DETACHED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_PAUSED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_PRE_ATTACHED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_PRE_CREATED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_RESUMED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_SAVE_INSTANCE_STATE;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_STARTED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_STOPPED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_VIEW_CREATED;
import static com.aliya.lifecycle.LifecycleCallbacksType.ON_VIEW_DESTROYED;

/**
 * LifecycleCallbacks回调类型注解类
 *
 * @author a_liYa
 * @date 2018/5/16 12:12.
 */
@IntDef({ON_CREATED, ON_STARTED, ON_RESUMED, ON_PAUSED, ON_STOPPED, ON_SAVE_INSTANCE_STATE,
        ON_DESTROYED, ON_PRE_ATTACHED, ON_ATTACHED, ON_PRE_CREATED, ON_ACTIVITY_CREATED,
        ON_VIEW_CREATED, ON_VIEW_DESTROYED, ON_DETACHED})
@Retention(RetentionPolicy.SOURCE)
@interface LifecycleCallbacksType {

    int ON_CREATED = 0;
    int ON_STARTED = 1;
    int ON_RESUMED = 2;
    int ON_PAUSED = 3;
    int ON_STOPPED = 4;
    int ON_SAVE_INSTANCE_STATE = 5;
    int ON_DESTROYED = 6;
    int ON_PRE_ATTACHED = 7;
    int ON_ATTACHED = 8;
    int ON_PRE_CREATED = 9;
    int ON_ACTIVITY_CREATED = 10;
    int ON_VIEW_CREATED = 11;
    int ON_VIEW_DESTROYED = 12;
    int ON_DETACHED = 13;

}
