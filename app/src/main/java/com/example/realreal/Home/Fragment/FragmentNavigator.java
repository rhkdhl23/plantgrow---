package com.example.realreal.Home.Fragment;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.realreal.Home.FragmentNavigatorAdapter;

public class FragmentNavigator {

    private static final String EXTRA_CURRENT_POSITION = "extra_current_position";

    private FragmentManager mFragmentManager;

    private FragmentNavigatorAdapter mAdapter;

    @IdRes
    private int mContainerViewId;

    private int mCurrentPosition = -1;

    private int mDefaultPosition;

    public FragmentNavigator(FragmentManager fragmentManager, FragmentNavigatorAdapter adapter, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mAdapter = adapter;
        this.mContainerViewId = containerViewId;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION, mDefaultPosition);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    public void showFragment(int position) {
        showFragment(position, false);
    }

    public void showFragment(int position, boolean reset) {
        showFragment(position, reset, false);
    }

    public void showFragment(int position, boolean reset, boolean allowingStateLoss) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                if (reset) {
                    remove(position, transaction);
                    add(position, transaction);
                } else {
                    show(i, transaction);
                }
            } else {
                hide(i, transaction);
            }
        }
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public void resetFragments() {
        resetFragments(mCurrentPosition);
    }

    public void resetFragments(int position) {
        resetFragments(position, false);
    }

    public void resetFragments(int position, boolean allowingStateLoss) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        add(position, transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public void removeAllFragment() {
        removeAllFragment(false);
    }

    public void removeAllFragment(boolean allowingStateLoss) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public Fragment getCurrentFragment() {
        return getFragment(mCurrentPosition);
    }

    public Fragment getFragment(int position) {
        String tag = mAdapter.getTag(position);
        return mFragmentManager.findFragmentByTag(tag);
    }

    private void show(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            add(position, transaction);
        } else {
            transaction.show(fragment);
        }
    }

    private void hide(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    private void add(int position, FragmentTransaction transaction) {
        Fragment fragment = mAdapter.onCreateFragment(position);
        String tag = mAdapter.getTag(position);
        transaction.add(mContainerViewId, fragment, tag);
    }

    private void removeAll(FragmentTransaction transaction) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            remove(i, transaction);
        }
    }

    private void remove(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    public void setDefaultPosition(int defaultPosition) {
        this.mDefaultPosition = defaultPosition;
        if (mCurrentPosition == -1) {
            this.mCurrentPosition = defaultPosition;
        }
    }
}
