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
                fg = new QueryFragment();
                fg.setArguments(this.bundle);
                return fg;
            case 2:
                fg = new AnalyzeFragment();
                fg.setArguments(this.bundle);
                return fg;
            case 3:
                fg = new ProfileFragment();
                fg.setArguments(this.bundle);
                return fg;
            default:
                fg = new HomeFragment();
                fg.setArguments(this.bundle);
                return fg;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
