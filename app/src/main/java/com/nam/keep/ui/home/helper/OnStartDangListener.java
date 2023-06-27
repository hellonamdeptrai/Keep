package com.nam.keep.ui.home.helper;

import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDangListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void onItemMoveListener(int fromPosition, int toPosition);
    void inItemDismissListener(int position);
    void moveDoneListener();
}
