package com.example.walletapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.walletapp.Adapter.TabLayoutAdapter;
import com.example.walletapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private int[] navIcons = {
            R.drawable.home1,
            R.drawable.server1,
            R.drawable.piechart1,
            R.drawable.user1
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        ImageView close = header.findViewById(R.id.close_nav);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });

        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        float width = display.widthPixels / display.density;
        Log.d("SYSTEM OUTPUT", "Device's width = " + String.valueOf(width));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_bar_item, null);

            TextView label = tab.findViewById(R.id.nav_label);
            ImageView icon = tab.findViewById(R.id.nav_icon);

            label.setText(navLabels[i]);

            if (i == 0) {
                label.setTextColor(0xFFFFFFFF);
                label.setTextSize(15);
                icon.setImageResource(navIconsActive[i]);
            } else {
                label.setTextColor(0xff59ECFF);
                label.setTextSize(15);
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
                label.setTextSize(15);
                icon.setImageResource(navIconsActive[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                View tabView = tab.getCustomView();
                TextView label = tabView.findViewById(R.id.nav_label);
                ImageView icon = tabView.findViewById(R.id.nav_icon);
                label.setTextColor(0xff59ECFF);
                label.setTextSize(15);
                icon.setImageResource(navIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }
}