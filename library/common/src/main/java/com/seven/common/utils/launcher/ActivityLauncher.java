package com.seven.common.utils.launcher;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


/**
 * Created by SeVen on 2019-11-30 14:54
 */
public class ActivityLauncher {
    private static final String TAG = "ActivityLauncher";
    private Context mContext;
    /**
     * 标准SDK下的Fragment
     */
    private RouterFragment mRouterFragment;

    public static ActivityLauncher init(Fragment fragment) {
        return init(fragment.getActivity());
    }

    public static ActivityLauncher init(FragmentActivity activity) {
        return new ActivityLauncher(activity);
    }

    private ActivityLauncher(FragmentActivity activity) {
        mContext = activity;
        mRouterFragment = getRouterFragment(activity);
    }

    private RouterFragment getRouterFragment(FragmentActivity activity) {
        RouterFragment routerFragment = findRouterFragment(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragment findRouterFragment(FragmentActivity activity) {
        return (RouterFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        if (mRouterFragment != null) {
            mRouterFragment.startActivityForResult(intent, callback);
        } else {
            throw new RuntimeException("please do init first!");
        }
    }

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }
}
