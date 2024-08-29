package com.example.walletapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.walletapp.Adapter.TabLayoutAdapter;
import com.example.walletapp.Fragment.HomeFragment;
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

    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        ImageView close = header.findViewById(R.id.close_nav);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }

        });

        String phone_user = getIntent().getStringExtra("key_data");
        Bundle bundle = new Bundle();
        bundle.putString("key_str_data", phone_user);


        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager(), bundle);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                refreshFragment(position);  // Gọi hàm refresh fragment tại vị trí tương ứng
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                refreshFragment(position);  // Gọi hàm refresh lại fragment khi tab được chọn lại
            }
        });





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

    private void refreshFragment(int position) {
        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, position);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

}