package com.aliya.lifecycle.simple.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.lifecycle.FragmentLifecycleCallbacks;
import com.aliya.lifecycle.LifecycleInjector;
import com.aliya.lifecycle.simple.R;

/**
 * Page Fragment
 */
public class PageFragment extends Fragment {

    private TextView mTextView;
    private String mTitle;

    public PageFragment() {
        LifecycleInjector.get().addFragmentLifecycleCallbacks(this, new
                FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentAttached(FragmentManager fm, Fragment f, Context
                            context) {
                        super.onFragmentAttached(fm, f, context);
                        Log.e("TAG", mTitle + " - onFragmentAttached: ");
                    }

                    @Override
                    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle
                            savedInstanceState) {
                        super.onFragmentCreated(fm, f, savedInstanceState);
                        Log.e("TAG", mTitle + " - onFragmentCreated: ");
                    }

                    @Override
                    public void onFragmentResumed(FragmentManager fm, Fragment f) {
                        super.onFragmentResumed(fm, f);
                        Log.e("TAG", mTitle + " - onFragmentResumed: ");
                    }

                    @Override
                    public void onFragmentPaused(FragmentManager fm, Fragment f) {
                        super.onFragmentPaused(fm, f);
                        Log.e("TAG", mTitle + " - onFragmentPaused: ");
                    }

                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        Log.e("TAG", mTitle + " - onFragmentViewDestroyed: ");
                    }

                    @Override
                    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentDestroyed(fm, f);
                        Log.e("TAG", mTitle + " - onFragmentDestroyed: ");
                    }
                });
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mTitle = args.getString("title", "无数据");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTextView = view.findViewById(R.id.text_view);
        mTextView.setText(mTitle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
