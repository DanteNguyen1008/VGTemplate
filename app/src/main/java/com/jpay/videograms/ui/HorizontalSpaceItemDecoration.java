package com.jpay.videograms.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Add a right space to divide items in RecyclerView
 *
 * Created by anguyen on 12/16/2015.
 */
public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public HorizontalSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
    }
}
