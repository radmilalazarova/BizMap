package com.radmila.businessdirectory.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.radmila.businessdirectory.fragment.CategoryFragment;

public class CategoryPagerAdapter extends FragmentStateAdapter {


    private static final String[] CATEGORIES = {
            "services",   // таб 0
            "fun",        // таб 1
            "industry",   // таб 2
            "education"   // таб 3
    };

    public CategoryPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }


    @Override
    public int getItemCount() {
        return CATEGORIES.length;
    }


    @Override
    public Fragment createFragment(int pos) {

        return CategoryFragment.newInstance(CATEGORIES[pos]);
    }

    public static String getCategoryAt(int pos) {
        return CATEGORIES[pos];
    }
}