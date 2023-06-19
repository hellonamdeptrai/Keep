package com.nam.keep.ui.home.helper;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void inItemDismiss(int position);
    void moveDone();
}
