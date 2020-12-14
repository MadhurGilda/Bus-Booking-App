package com.example.android.busbookings.Adapters;

import android.content.Context;

import com.example.android.busbookings.Fragments.BookingsFragment;
import com.example.android.busbookings.Fragments.SearchFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    Context context;
    int tabCount;

    public MyAdapter(FragmentManager fm, Context context, int tabCount) {
        super(fm);
        this.context = context;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new SearchFragment();

            case 1:
                return new BookingsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
