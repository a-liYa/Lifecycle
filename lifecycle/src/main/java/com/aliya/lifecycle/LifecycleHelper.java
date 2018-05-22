package com.aliya.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 工具类
 *
 * @author a_liYa
 * @date 2018/5/17 21:08.
 */
public class LifecycleHelper {

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        HideLog.e("当前 Context : " + context + " 不是继承自Activity");
        return null;
    }

    /**
     * 获取当前 view 所属Fragment
     *
     * @param view view
     * @return 当View不属于任何Fragment时，返回null
     */
    public static @Nullable Fragment getFragment(View view) {
        while (view != null) {
            Object tag = view.getTag(R.id.lifecycle_tag_id_fragment);
            if (tag instanceof Fragment) {
                return (Fragment) tag;
            }
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                view = null;
            }
        }
        return null;
    }

}
