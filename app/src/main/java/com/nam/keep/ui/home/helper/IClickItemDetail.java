package com.nam.keep.ui.home.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.model.Note;

public interface IClickItemDetail {
    void onClickItemNote(View view, TextView title, TextView content,
    ImageView imageView,
    RecyclerView mainImagesNoteHome, RecyclerView mainCheckboxNoteHome, RecyclerView mainCategoriesNoteHome,
    RoundedImageView colorBackgroundImagedHome, RoundedImageView imageBackgroundHome, Note note);
}
