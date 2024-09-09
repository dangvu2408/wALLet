package com.example.walletapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.Adapter.GridItemsProfileAdapter;
import com.example.walletapp.Adapter.UserInfoItemAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.Model.UserInfoModel;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView back_btn, home;
    private String username, full_name, dateofbirth, gender;
    private TextView user_full_name, userid;
    private ListView full_acc_info;
    private LinearLayout on_click_user_display;
    private ImageView more_user_info_icon;
    private boolean isShown;
    private GridView gridview_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        home = findViewById(R.id.home);
        back_btn = findViewById(R.id.back_btn);
        user_full_name = findViewById(R.id.user_full_name);
        userid = findViewById(R.id.userid);
        full_acc_info = findViewById(R.id.full_acc_info);
        on_click_user_display = findViewById(R.id.on_click_user_display);
        more_user_info_icon = findViewById(R.id.more_user_info_icon);
        gridview_profile = findViewById(R.id.gridview_profile);


        username = getIntent().getStringExtra("key_username_a");
        full_name = getIntent().getStringExtra("key_fullname_a");
        dateofbirth = getIntent().getStringExtra("key_dateofbirth_a");
        gender = getIntent().getStringExtra("key_gender_a");

        List<UserInfoModel> model = new ArrayList<>();

        model.add(new UserInfoModel("Họ tên", full_name));
        model.add(new UserInfoModel("Số điện thoại", username));
        model.add(new UserInfoModel("Ngày sinh", dateofbirth));
        model.add(new UserInfoModel("Giới tính", gender));

        UserInfoItemAdapter adapter = new UserInfoItemAdapter(this, model);
        full_acc_info.setAdapter(adapter);
        HeightUtils.setListViewHeight(full_acc_info);

        List<GridItem> listSetting = new ArrayList<>();
        listSetting.add(new GridItem(R.drawable.eidt_3, "Chỉnh sửa thông tin cá nhân"));
        listSetting.add(new GridItem(R.drawable.repeat, "Thay đổi mật khẩu"));
        GridItemsProfileAdapter adapter1 = new GridItemsProfileAdapter(this, listSetting, full_name, dateofbirth, gender, username);
        gridview_profile.setAdapter(adapter1);
        HeightUtils.setGridViewHeight(gridview_profile, 1);


        on_click_user_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown) {
                    full_acc_info.setVisibility(View.VISIBLE);
                    more_user_info_icon.setRotation(180);
                    isShown = true;
                } else {
                    more_user_info_icon.setRotation(0);
                    full_acc_info.setVisibility(View.GONE);
                    isShown = false;
                }

            }
        });




        Log.d("USER PROFILE DEBUG", "String test 02: " + username + " " + full_name);

        user_full_name.setText(full_name);

        String userText = "User ID: ";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString blackText = new SpannableString(userText);
        blackText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString mainColorText = new SpannableString(username);
        mainColorText.setSpan(new ForegroundColorSpan(0xFF141ED4), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(blackText);
        builder.append(mainColorText);
        userid.setText(builder);

        Log.d("USER PROFILE DEBUG", "String test: " + username + " " + full_name);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                intent.putExtra("key_data", username);
                startActivity(new Intent(intent));
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
