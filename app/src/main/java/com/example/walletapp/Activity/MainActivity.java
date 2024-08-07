package com.example.walletapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.walletapp.Adapter.TabLayoutAdapter;
import com.example.walletapp.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private int[] navIcons = {
            R.drawable.homenonactive,
            R.drawable.servernonactive,
            R.drawable.piechartnonactive,
            R.drawable.usernonactive
    };
    private String[] navLabels = {
            "Trang chủ",
            "Truy vấn",
            "Phân tích",
            "Tôi"
    };
    private int[] navIconsActive = {
            R.drawable.home,
            R.drawable.server,
            R.drawable.piechart,
            R.drawable.user
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_bar_item, null);

            TextView label = tab.findViewById(R.id.nav_label);
            ImageView icon = tab.findViewById(R.id.nav_icon);

            label.setText(navLabels[i]);

            if (i == 0) {
                label.setTextColor(0xFFFFFFFF);
                icon.setImageResource(navIconsActive[i]);
            } else {
                label.setTextColor(0xFFFF8D8D);
                icon.setImageResource(navIcons[i]);
            }
            tabLayout.getTabAt(i).setCustomView(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                View tabView = tab.getCustomView();
                TextView label = tabView.findViewById(R.id.nav_label);
                ImageView icon = tabView.findViewById(R.id.nav_icon);
                label.setTextColor(0XFFFFFFFF);
                icon.setImageResource(navIconsActive[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                View tabView = tab.getCustomView();
                TextView label = tabView.findViewById(R.id.nav_label);
                ImageView icon = tabView.findViewById(R.id.nav_icon);
                label.setTextColor(0xFFFF8D8D);
                icon.setImageResource(navIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }
}