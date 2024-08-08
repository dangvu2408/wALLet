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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.walletapp.R;

public class HomeFragment extends Fragment {
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
        return view;
    }
}
