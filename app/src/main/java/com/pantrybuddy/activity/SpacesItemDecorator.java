package com.pantrybuddy.activity;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecorator extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing=10;
    private boolean includeEdge;

    public SpacesItemDecorator(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = 10;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = spacing;
        outRect.left = spacing;
        outRect.top = spacing;
        outRect.bottom = spacing;
    }
}