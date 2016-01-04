package com.jpay.videograms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anguyen on 12/10/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    List<Fragment> fragments = null;
    List<String> titles = null;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        if(fragment != null) {
            fragments.add(fragment);
        }

        if(titles != null) {
            titles.add(title);
        }
    }

    @Override
    public int getCount() {
        if(fragments != null) {
            return fragments.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles != null && position < titles.size() && position >= 0) {
            return titles.get(position);
        }

        return "";
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments != null && position < fragments.size()) {
            return fragments.get(position);
        }

        return null;
    }
}
