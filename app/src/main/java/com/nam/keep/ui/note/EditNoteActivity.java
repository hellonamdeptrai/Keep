/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nam.keep.ui.note;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Our secondary Activity which is launched from {@link com.nam.keep.MainActivity}. Has a simple detail UI
 * which has a large banner image, title and body text.
 */
public class EditNoteActivity extends AppCompatActivity {

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_ID = "edit:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_IMAGE = "edit:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_TITLE = "edit:title";
    public static final String VIEW_NAME_CONTENT = "edit:content";
    public static final String VIEW_NAME_LIST_CHECKBOX = "edit:list_checkbox";
    public static final String VIEW_NAME_IS_CHECKBOX = "edit:is_checkbox";
    public static final String VIEW_NAME_BACKGROUND = "edit:background";
    public static final String VIEW_NAME_EDIT_COLOR = "edit:color";
    public static final String VIEW_NAME_LABEL = "edit:label";

    private RecyclerView mImageView, mMainCheckboxNote, mMainLabel;
    private TextView mTitle, mContent;
    private RoundedImageView mEditColor;
    private CoordinatorLayout mMainContainerEditNote;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mToolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mImageView = findViewById(R.id.main_images_note);
        mTitle = findViewById(R.id.title_note);
        mContent = findViewById(R.id.content_note);
        mEditColor = findViewById(R.id.color_background_imaged);

        mTitle.setText(getIntent().getStringExtra(VIEW_NAME_TITLE));
        mContent.setText(getIntent().getStringExtra(VIEW_NAME_CONTENT));

        ViewCompat.setTransitionName(mImageView, VIEW_NAME_IMAGE);
        ViewCompat.setTransitionName(mTitle, VIEW_NAME_TITLE);
        ViewCompat.setTransitionName(mContent, VIEW_NAME_CONTENT);
        ViewCompat.setTransitionName(mEditColor, VIEW_NAME_EDIT_COLOR);

    }
}
