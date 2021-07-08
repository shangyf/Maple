package com.seven.common.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FragmentManagerUtils {
    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private List<String> titles;
    private List<Fragment> fragments;
    private Fragment fragment;
    private int fragmentContainerResId;     //fragment container

    public FragmentManagerUtils(FragmentManager fragmentManager, List<Fragment> fragments, int fragmentContainerResId) {
        this.fragmentManager = fragmentManager;
        this.fragments = fragments;
        this.fragmentContainerResId = fragmentContainerResId;

        CheckUtils.checkNotNull(fragmentManager, "fragmentManager == null");
        CheckUtils.checkNotEmpty(fragments, "fragments is empty");
        CheckUtils.checkNotEqual(fragmentContainerResId, 0, "fragmentContainerResId == 0");

        createFragment();
    }

    public FragmentManagerUtils(FragmentManager fragmentManager, TabLayout tabLayout,
                                List<String> titles, List<Fragment> fragments, int fragmentContainerResId) {
        this.fragmentManager = fragmentManager;
        this.tabLayout = tabLayout;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentContainerResId = fragmentContainerResId;

        CheckUtils.checkNotNull(fragmentManager, "fragmentManager == null");
        CheckUtils.checkNotNull(tabLayout, "tabLayout == null");
        CheckUtils.checkNotEmpty(titles, "titles is empty");
        CheckUtils.checkNotEmpty(fragments, "fragments is empty");
        CheckUtils.checkNotEqual(fragmentContainerResId, 0, "fragmentContainerResId == 0");

        initTabTitles();
        createFragment();
    }

    public FragmentManagerUtils(FragmentManager fragmentManager, Fragment fragment, int fragmentContainerResId) {
        CheckUtils.checkNotNull(fragmentManager, "fragmentManager == null");
        CheckUtils.checkNotEqual(fragmentContainerResId, 0, "fragmentContainerResId == 0");
        this.fragment = fragment;
        this.fragmentManager = fragmentManager;
        this.fragmentContainerResId = fragmentContainerResId;
        fragmentManager.beginTransaction().add(fragmentContainerResId, fragment).commit();
    }

    public void initTabTitles() {
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }
    }

    public void createFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            fragmentTransaction.add(fragmentContainerResId, fragment);
        }
        fragmentTransaction.commit();
    }

    public void setTabLayoutIndex(int index) {
        if (tabLayout != null) {
            tabLayout.getTabAt(index).select();
        }
    }

    public void showFragment(int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (checkedId == i) {
                fragmentTransaction.show(fragments.get(i));
            } else {
                fragmentTransaction.hide(fragments.get(i));
            }
        }
        fragmentTransaction.commit();
    }

    public void showFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }
}
