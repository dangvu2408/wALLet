package com.example.walletapp.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.walletapp.Fragment.AnalyzeFragment;
import com.example.walletapp.Fragment.HomeFragment;
import com.example.walletapp.Fragment.ProfileFragment;
import com.example.walletapp.Fragment.QueryFragment;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {
    private Bundle bundle;
    public TabLayoutAdapter(@NonNull FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fg;
        switch (position) {
            case 1:
                return new QueryFragment();
            case 2:
                return new AnalyzeFragment();
            case 3:
                return new ProfileFragment();
            default:
                fg = new HomeFragment();
                fg.setArguments(bundle);
                return fg;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
