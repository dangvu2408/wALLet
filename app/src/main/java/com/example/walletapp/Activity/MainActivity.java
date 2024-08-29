package com.example.walletapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.AsyncListUtil;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.walletapp.Adapter.TabLayoutAdapter;
import com.example.walletapp.Constants;
import com.example.walletapp.Fragment.HomeFragment;
import com.example.walletapp.Model.UserModel;
import com.example.walletapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<String> userDataLogin;
    private String jsonUserValue = "";
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
        Bundle bundle = new Bundle();
        userDataLogin = new ArrayList<>();
        String phone_user = getIntent().getStringExtra("key_data");
        fetchData(Constants.BASE_URL_FETCH_USERDATA, phone_user, new DataCallback() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                userDataLogin = data;
                String username = userDataLogin.get(0);
                String password = userDataLogin.get(1);
                String fullname = userDataLogin.get(2);
                String dob = userDataLogin.get(3);
                String gender = userDataLogin.get(4);

                bundle.putString("key_username_data", username);
                bundle.putString("key_password_data", password);
                bundle.putString("key_fullname_data", fullname);
                bundle.putString("key_dob_data", dob);
                bundle.putString("key_gender_data", gender);
            }
        });

        Log.d("USER DATA LOGIN", userDataLogin.toString());
        Log.d("USER DATA LOGIN", "Size: " + userDataLogin.size());




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

    private void fetchData(String url, String key, DataCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonArr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Error")) {
                            Toast.makeText(MainActivity.this, "LỖI LẤY THÔNG TIN", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    userDataLogin.add(obj.getString("username"));
                                    userDataLogin.add(obj.getString("password"));
                                    userDataLogin.add(obj.getString("fullname"));
                                    userDataLogin.add(obj.getString("dob"));
                                    userDataLogin.add(obj.getString("gender"));
                                }
                                callback.onDataLoaded(userDataLogin);
                                Log.d("DEBUG USER DATA LOGIN", userDataLogin.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG JSON RESPONSE", error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usernameKey", key);
                return params;
            }
        };
        queue.add(jsonArr);
    }

    public interface DataCallback {
        void onDataLoaded(ArrayList<String> data);
    }
}