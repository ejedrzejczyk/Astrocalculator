package com.example.astrocalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astrocalculator.AstroCalculator;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private AstroCalculator astroCalculator;
    private int refreshTime;

    public ViewPagerAdapter(@NonNull FragmentManager fm, AstroCalculator astroCalculator, int refreshTime) {
        super(fm);
        this.astroCalculator = astroCalculator;
        this.refreshTime = refreshTime;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = SunFragment.newInstance(astroCalculator, refreshTime);
                break;
            case 1:
                fragment = MoonFragment.newInstance(astroCalculator, refreshTime);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "SUN";
                break;
            case 1:
                title = "MOON";
                break;
        }
        return title;
    }
}
