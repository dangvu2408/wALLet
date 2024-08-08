package com.example.walletapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Adapter.GridItemsSettingAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ShapeableImageView avatar = view.findViewById(R.id.profile_avatar);
        TextView user_fullname = view.findViewById(R.id.user_full_name);
        GridView grid_setting = view.findViewById(R.id.gridview_setting);
        GridView grid_others = view.findViewById(R.id.gridview_others);
        List<GridItem> listSetting = new ArrayList<>();
        List<GridItem> listOthers = new ArrayList<>();
        listSetting.add(new GridItem(R.drawable.shield, "Đăng nhập và bảo mật"));
        listSetting.add(new GridItem(R.drawable.bell, "Cài đặt thông báo"));
        listSetting.add(new GridItem(R.drawable.usercheck, "Chỉnh sửa thông tin"));
        listOthers.add(new GridItem(R.drawable.dollarsign, "Tỉ giá ngoại tệ"));
        listOthers.add(new GridItem(R.drawable.airplay, "Thông tin chung"));


        GridItemsSettingAdapter adapter01 = new GridItemsSettingAdapter(this.getContext(), listSetting);
        GridItemsSettingAdapter adapter02 = new GridItemsSettingAdapter(this.getContext(), listOthers);
        grid_setting.setAdapter(adapter01);
        grid_others.setAdapter(adapter02);
        HeightUtils.setGridViewHeight(grid_setting, 1);
        HeightUtils.setGridViewHeight(grid_others, 1);
        return view;
    }
}
