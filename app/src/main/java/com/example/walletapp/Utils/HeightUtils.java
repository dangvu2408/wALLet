package com.example.walletapp.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

public class HeightUtils {
    public static void setGridViewHeight(GridView gridView, int numColumns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int total = gridView.getPaddingTop() + gridView.getPaddingBottom();
        int numRow = (int) Math.ceil(listAdapter.getCount() * 1.0 / numColumns);
        for (int i = 0; i < numRow; i++) {
            View listItem = listAdapter.getView(i * numColumns, null, gridView);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            total += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = total;
        gridView.setLayoutParams(params);
    }
}
