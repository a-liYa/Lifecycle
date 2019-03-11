package com.aliya.lifecycle.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.lifecycle.FragmentLifecycleCallbacks;
import com.aliya.lifecycle.LifecycleHelper;
import com.aliya.lifecycle.LifecycleInjector;
import com.aliya.lifecycle.sample.R;

/**
 * Mine Fragment
 */
public class MineFragment extends Fragment {

    private RecyclerView mRecycler;

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler = view.findViewById(R.id.recycler);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new Adapter());
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return 25;
        }

    }

    /**
     * ViewHolder判断所在Fragment声明周期
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvName;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recycler, parent, false));
            mTvName = itemView.findViewById(R.id.tv_name);

            Fragment fragment = LifecycleHelper.getFragment(parent);
            LifecycleInjector.get().addFragmentLifecycleCallbacks(fragment,
                    new FragmentLifecycleCallbacks() {

                        @Override
                        public void onFragmentResumed(FragmentManager fm, Fragment f) {
                            super.onFragmentResumed(fm, f);
                            Log.e("TAG", "onFragmentResumed " + f);
                        }

                        @Override
                        public void onFragmentPaused(FragmentManager fm, Fragment f) {
                            super.onFragmentPaused(fm, f);
                            Log.e("TAG", "onFragmentPaused " + f);
                        }
                    });
        }

        public void bindData(int position) {
            mTvName.setText("" + position);
        }

    }

}
