package com.aliya.lifecycle.sample.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义实现TabLayout，方便底部多Tab组合
 *
 * @author a_liYa
 * @date 2018/5/15 下午2:26.
 */
public class MainTabLayout extends LinearLayout implements View.OnClickListener {

    private MainTabAdapter mAdapter;
    private OnSelectedListener mOnSelectedListener;
    private FragmentManager mFragmentManager;

    private int mContainerId = 0;       // fragment容器id
    private int mDefaultIndex = 0;      // 默认选中的索引
    private Integer selectedKey = -1;   // 被选中的item对应的Key
    private int mAttachType = ATTACH_TYPE_SHOW;
    /**
     * 子条目View集合
     */
    private SparseArray<TabInfo> mTabs = new SparseArray<>();

    /**
     * @see FragmentTransaction#show(Fragment)
     * @see FragmentTransaction#hide(Fragment)
     */
    public static final int ATTACH_TYPE_SHOW = 0;
    /**
     * @see FragmentTransaction#add(int, Fragment)
     * @see FragmentTransaction#remove(Fragment)
     */
    public static final int ATTACH_TYPE_ADD = 1;
    /**
     * @see FragmentTransaction#attach(Fragment)
     * @see FragmentTransaction#detach(Fragment)
     */
    public static final int ATTACH_TYPE_ATTACH = 2;

    public MainTabLayout(Context context) {
        this(context, null);
    }

    public MainTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

    }

    /**
     * 设置默认页面索引，注意：该方法应该在{@link #setupBind(FragmentManager, int)}之前调用
     *
     * @param index 索引
     */
    public void setDefaultIndex(int index) {
        if (index < 0) return;
        mDefaultIndex = index;
    }

    public void setAttachType(@AttachType int attachType) {
        mAttachType = attachType;
    }

    public OnSelectedListener getOnSelectedListener() {
        return mOnSelectedListener;
    }

    /**
     * 设置选中监听
     *
     * @param onSelectedListener .
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    /**
     * 设置适配器，注意：该方法应该在{@link #setupBind(FragmentManager, int)}之后调用
     *
     * @param adapter .
     */
    public void setAdapter(MainTabAdapter adapter) {
        this.mAdapter = adapter;
        initChild();
        setSelected(mDefaultIndex);
    }

    /**
     * 选择指定的页面
     *
     * @param index .
     */
    public void setSelected(int index) {
        if (mAdapter == null || mFragmentManager == null || mContainerId == 0
                || mAdapter.getCount() <= 0)
            return;

        index %= mAdapter.getCount();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        TabInfo tab = mTabs.get(index);
        String tag = String.valueOf(index);
        tab.fragment = mFragmentManager.findFragmentByTag(tag);
        // 显示选中Fragment
        if (tab.fragment == null) {
            ft.add(mContainerId, tab.fragment =
                    Fragment.instantiate(getContext(), tab.clazz.getName(), tab.args), tag);
        } else {
            switch (mAttachType) {
                case ATTACH_TYPE_SHOW:
                    ft.show(tab.fragment);
                    break;
                case ATTACH_TYPE_ATTACH:
                    ft.attach(tab.fragment);
                    break;
            }
        }

        // 隐藏上一个Fragment
        if (selectedKey != -1) {
            TabInfo lastTab = mTabs.get(selectedKey);
            if (lastTab.fragment != null) {
                switch (mAttachType) {
                    case ATTACH_TYPE_SHOW:
                        ft.hide(lastTab.fragment);
                        break;
                    case ATTACH_TYPE_ADD:
                        ft.remove(lastTab.fragment);
                        break;
                    case ATTACH_TYPE_ATTACH:
                        ft.detach(lastTab.fragment);
                        break;
                }
            }
        }
        ft.commitAllowingStateLoss();

        // 设置当前item为选中状态
        View selectedView = mTabs.get(index).view;
        if (selectedView != null) {
            selectedView.setSelected(true);
        }

        // 取消上个item的选中状态
        TabInfo tabInfo = mTabs.get(selectedKey);
        View lastSelectedView = tabInfo == null ? null : tabInfo.view;
        if (lastSelectedView != null) {
            lastSelectedView.setSelected(false);
        }

        selectedKey = index;
        if (mOnSelectedListener != null)
            mOnSelectedListener.onSelected(index);
    }

    /**
     * 初始化绑定对象, 若是页面恢复，赋值默认选中为恢复页面
     *
     * @param manager     .
     * @param containerId fragment容器id
     */
    public void setupBind(FragmentManager manager, @IdRes int containerId) {
        mFragmentManager = manager;
        mContainerId = containerId;
        Fragment fragment = manager.findFragmentById(containerId);
        if (fragment != null) {
            try { // 页面恢复时保存的索引
                mDefaultIndex = Math.max(Integer.parseInt(fragment.getTag()), 0);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void initChild() {
        if (mAdapter == null) return;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mTabs.get(i) == null) {
                TabInfo tab = mAdapter.getTab(i, this);
                mTabs.put(i, tab);
                LayoutParams lp = (LayoutParams) tab.view.getLayoutParams();
                if (lp == null) {
                    lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                }
                lp.weight = 1;
                tab.view.setOnClickListener(this);
                addView(tab.view, i, lp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (v == mTabs.get(mTabs.keyAt(i)).view) {
                if (i != selectedKey) {
                    setSelected(i);
                }
            }
        }
    }

    /**
     * 获取指定Tab
     *
     * @param index 下标
     * @return View .
     */
    public @Nullable
    View getIndexTabView(int index) {
        return mTabs.get(index) != null ? mTabs.get(index).view : null;
    }

    /**
     * 获取指定TabFragment
     *
     * @param index 下标
     * @return .
     */
    public Fragment getIndexTabFragment(int index) {
        return mTabs.get(index) != null ? mTabs.get(index).fragment : null;
    }

    /**
     * MainTabLayout适配器接口
     *
     * @author a_liYa
     * @date 2016-4-23 上午10:58:36
     */
    public interface MainTabAdapter {
        int getCount();

        TabInfo getTab(int position, ViewGroup parent);
    }

    /**
     * 选中监听接口
     *
     * @author a_liYa
     * @date 2016/4/30 11:15.
     */
    public interface OnSelectedListener {
        /**
         * @param index 被选中索引
         */
        void onSelected(int index);
    }

    public static final class TabInfo {
        private final View view;
        private final Class<?> clazz;
        private final Bundle args;
        private Fragment fragment;

        public TabInfo(View _view, Class<?> _class, Bundle _args) {
            view = _view;
            clazz = _class;
            args = _args;
        }
    }

    @IntDef({ATTACH_TYPE_SHOW, ATTACH_TYPE_ADD, ATTACH_TYPE_ATTACH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AttachType {
    }

}