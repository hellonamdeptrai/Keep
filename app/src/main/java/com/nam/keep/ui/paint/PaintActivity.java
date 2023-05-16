package com.nam.keep.ui.paint;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import petrov.kristiyan.colorpicker.ColorPicker;

import com.nam.keep.R;

public class PaintActivity extends AppCompatActivity {

    private DrawView paint;

    private ImageButton save, color, stroke, undo;

    private RangeSlider rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        paint = (DrawView) findViewById(R.id.draw_view);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        undo = (ImageButton) findViewById(R.id.btn_undo);
        save = (ImageButton) findViewById(R.id.btn_save);
        color = (ImageButton) findViewById(R.id.btn_color);
        stroke = (ImageButton) findViewById(R.id.btn_stroke);


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.undo();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {

                Bitmap bmp = paint.save();

                OutputStream imageOutStream = null;

                ContentValues cv = new ContentValues();

                cv.put(MediaStore.Images.Media.DISPLAY_NAME, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + "drawing.png");

                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("keyUri", uri);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                try {
                    imageOutStream = getContentResolver().openOutputStream(uri);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                    imageOutStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(PaintActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                            @Override
                            public void setOnFastChooseColorListener(int position, int color) {
                                paint.setColor(color);
                            }
                            @Override
                            public void onCancel() {
                                colorPicker.dismissDialog();
                            }
                        })
                        .setTitle("Chọn một màu sắc")
                        .setColumns(5)
                        .setDefaultColorButton(Color.parseColor("#000000"))
                        .show();
            }
        });

        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangeSlider.getVisibility() == View.VISIBLE)
                    rangeSlider.setVisibility(View.GONE);
                else
                    rangeSlider.setVisibility(View.VISIBLE);
            }
        });

        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                paint.setStrokeWidth((int) value);
            }
        });

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });
    }
}