package com.aliya.lifecycle;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 自定义携带Key{@link Object#hashCode()}的弱引用
 *
 * @author a_liYa
 * @date 2017/6/27 22:47.
 */
final class KeyedWeakReference<T> extends WeakReference<T> {

    static final int NO_KEY = 0;

    final int key;

    public KeyedWeakReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
        key = referent != null ? referent.hashCode() : NO_KEY;
    }

}
