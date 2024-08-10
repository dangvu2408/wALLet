package com.example.walletapp.Fragment;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Adapter.GridItemAddingAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private boolean isOverlayVisible = false;
    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        CardView cardView = view.findViewById(R.id.layout_root);

//        cardView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(cardView.getDrawingCache());
//        cardView.setDrawingCacheEnabled(false);
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
//        Canvas canvas = new Canvas(output);
//        Paint paint = new Paint();
//        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));
//        canvas.drawBitmap(bitmap, 0, 0, paint);
//        cardView.setBackground(new BitmapDrawable(getResources(), output));
        ScrollView fullLayout = view.findViewById(R.id.scroll_full_view);
        LinearLayout hidden = view.findViewById(R.id.layout_hidden);
        fullLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0 && !isOverlayVisible) {
                    AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
                    fade.setDuration(200);
                    hidden.setAnimation(fade);
                    hidden.setVisibility(View.VISIBLE);
                    isOverlayVisible = true;
                } else if (scrollY == 0 && isOverlayVisible) {
                    AlphaAnimation fade = new AlphaAnimation(1.0f, 0.0f);
                    fade.setDuration(200);
                    hidden.setAnimation(fade);
                    hidden.setVisibility(View.GONE);
                    isOverlayVisible = false;
                }
            }
        });

        GridView addingGrid = view.findViewById(R.id.gridview_adding);
        List<GridItem> listAdding = new ArrayList<>();
        listAdding.add(new GridItem(R.drawable.expense01, "Khoản chi"));
        listAdding.add(new GridItem(R.drawable.income01, "Khoản thu"));
        listAdding.add(new GridItem(R.drawable.percentage01, "Lãi suất"));
        listAdding.add(new GridItem(R.drawable.target01, "Khoản vay"));
        GridItemAddingAdapter adapter = new GridItemAddingAdapter(this.getContext(), listAdding);
        addingGrid.setAdapter(adapter);
        HeightUtils.setGridViewHeight(addingGrid, 4);
        return view;
    }
}
