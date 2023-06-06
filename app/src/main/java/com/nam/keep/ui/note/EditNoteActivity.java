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
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.CheckBoxContentNote;
import com.nam.keep.model.FileModel;
import com.nam.keep.model.Label;
import com.nam.keep.model.Note;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.home.helper.OnStartDangListener;
import com.nam.keep.ui.label.LabelActivity;
import com.nam.keep.ui.note.adapter.RecyclerCheckBoxNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerImagesAddNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerLabelNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerRecorderNoteAdapter;
import com.nam.keep.ui.note.helper.IClickChecked;
import com.nam.keep.ui.note.helper.IClickDeleteCheckBox;
import com.nam.keep.ui.note.helper.IClickRecorder;
import com.nam.keep.ui.note.helper.ITextWatcherCheckBox;
import com.nam.keep.ui.paint.PaintActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private boolean isSaveData = true;

    // View
    private Toolbar mToolbar;
    private EditText mTitle, mContent;
    private CoordinatorLayout mMainAddNote;
    private RoundedImageView mRoundedImageColor;
    private Button addCheckBox;
    private RecyclerView mainRecorderNote, mImageView, mMainCheckboxNote, mMainLabel;
    private LinearLayout layoutTextTimeNote;
    private TextView textTimeNote;
    private ImageView closeTextTimeNote;


    // data
    long idNote;
    private DatabaseHelper dataSource;
    Note noteData = new Note();
    ArrayList<Uri> listImageIntent = new ArrayList<>();
    List<CheckBoxContentNote> listCheckBox = new ArrayList<>();
    private int colorNote = Color.rgb(255,255,255);
    private Bitmap imageBackground;
    private int isCheckBoxOrContent = 0;
    ItemTouchHelper itemTouchHelper;
    MediaRecorder mediaRecorder;
    ArrayList<Label> listLabelNote = new ArrayList<>();
    ArrayList<String> listPathRecorder = new ArrayList<>();
    Date dateTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mToolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dataSource = new DatabaseHelper(this);

        mImageView = findViewById(R.id.main_images_note);
        mTitle = findViewById(R.id.title_note);
        mContent = findViewById(R.id.content_note);
        mRoundedImageColor = findViewById(R.id.color_background_imaged);
        mMainCheckboxNote = findViewById(R.id.main_checkbox_note);
        mMainAddNote = findViewById(R.id.main_container_add_note);
        mMainLabel = findViewById(R.id.main_categories_note);
        layoutTextTimeNote = findViewById(R.id.layout_text_time_note);
        textTimeNote = findViewById(R.id.text_time_note);
        closeTextTimeNote = findViewById(R.id.close_text_time_note);

        loadNote();

        ViewCompat.setTransitionName(mImageView, VIEW_NAME_IMAGE);
        ViewCompat.setTransitionName(mTitle, VIEW_NAME_TITLE);
        ViewCompat.setTransitionName(mContent, VIEW_NAME_CONTENT);
        ViewCompat.setTransitionName(mRoundedImageColor, VIEW_NAME_EDIT_COLOR);
        ViewCompat.setTransitionName(mMainCheckboxNote, VIEW_NAME_LIST_CHECKBOX);
        ViewCompat.setTransitionName(mMainAddNote, VIEW_NAME_BACKGROUND);
        ViewCompat.setTransitionName(mMainLabel, VIEW_NAME_LABEL);

        // recorder view
        mainRecorderNote = findViewById(R.id.main_recorder_note);
        mainRecorderNote.setLayoutManager(new LinearLayoutManager(EditNoteActivity.this));

        Button mSheetAddButton = findViewById(R.id.sheet_add_note_button);
        Button mSheetColorButton = findViewById(R.id.sheet_color_note_button);
        Button mSheetThreeDotsNoteButton = findViewById(R.id.sheet_three_dots_note_button);

        mSheetAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditNoteActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_add_note);
                bottomSheetDialog.show();
                bottomSheetDialog.findViewById(R.id.add_image_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 10);
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_brush_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intentLogin = new Intent(EditNoteActivity.this, PaintActivity.class);
                        startActivityForResult(intentLogin, 11);
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_mic_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        startSpeechRecognition();
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_recording_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        startRecording();
                        final Dialog dialog = new Dialog(EditNoteActivity.this);
                        dialog.setContentView(R.layout.layout_dialog_recording);
                        dialog.setCancelable(false);

                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(dialog.getWindow().getAttributes());
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        Button recordingFinish = dialog.findViewById(R.id.btn_recording_finish);
                        recordingFinish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stopRecording();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(layoutParams);
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_checkbox_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        changeTextToCheckbox();
                    }
                });
            }
        });
        mSheetColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditNoteActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_color_note);
                bottomSheetDialog.show();
                bottomActionColorImage(bottomSheetDialog);
            }
        });
        mSheetThreeDotsNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditNoteActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_three_dots_note);
                bottomSheetDialog.show();
                bottomSheetDialog.findViewById(R.id.add_label_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(EditNoteActivity.this, LabelActivity.class);
                        intent.putExtra("labelListEditIntent", listLabelNote);
                        startActivityForResult(intent, 14);
                    }
                });

                bottomSheetDialog.findViewById(R.id.add_delete_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                        builder.setTitle("Xóa ghi chú?");
                        builder.setMessage("Bạn có chắc chắn muốn xóa ghi chú này không?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isSaveData = false;
                                dataSource.deleteNote(idNote);
                                dataSource.deleteFileInNote(idNote);
                                dataSource.detachLabel(idNote);
                                dataSource.deleteImageInNote(idNote);
                                dataSource.close();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.nam_keep));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.nam_keep));
                    }
                });
            }
        });

        closeTextTimeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker = null;
                layoutTextTimeNote.setVisibility(View.GONE);
            }
        });
    }

    private void loadNote() {
        if(getIntent().hasExtra(EXTRA_PARAM_ID)){
            idNote = getIntent().getLongExtra(EXTRA_PARAM_ID,1);

            getListImages(idNote);
            addListImage();
            getListLabel();
            getListRecorder();
            listViewRecording();

            Cursor cursor = dataSource.getNoteDetail(idNote);
            if (cursor.moveToFirst()) {
                noteData.setId(Long.parseLong(cursor.getString(0)));
                noteData.setIndex(Integer.parseInt(cursor.getString(1)));
                noteData.setTitle(cursor.getString(2));
                noteData.setContent(cursor.getString(3));
                noteData.setIsCheckBoxOrContent(Integer.parseInt(cursor.getString(4)));
                noteData.setDeadline(cursor.getString(5));
                noteData.setColor(Integer.parseInt(cursor.getString(6)));
                noteData.setBackground(cursor.getBlob(7));
                noteData.setUpdatedAt(cursor.getString(8));
                noteData.setUserId(Long.parseLong(cursor.getString(9)));
            }

            mTitle.setText(noteData.getTitle());
            mContent.setText(noteData.getContent());

            colorNote = noteData.getColor();

            if (!noteData.getDeadline().isEmpty()) {
                try {
                    dateTimePicker = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(noteData.getDeadline());
                    layoutTextTimeNote.setVisibility(View.VISIBLE);
                    textTimeNote.setText(new SimpleDateFormat("HH:mm dd/MM/yyyy").format(dateTimePicker));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (noteData.getBackground() != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(noteData.getBackground(),0,noteData.getBackground().length);
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imageBackground = bitmap;
                mMainAddNote.setBackground(ob);
                mRoundedImageColor.setBackgroundColor(noteData.getColor());
                if (noteData.getColor() == Color.rgb(255,255,255)){
                    mRoundedImageColor.setBackground(null);
                }
            } else {
                mMainAddNote.setBackgroundColor(noteData.getColor());
                mRoundedImageColor.setBackground(null);
            }

            if (noteData.getIsCheckBoxOrContent() == 1){
                changeTextToCheckbox();
            }
        }else{
            Toast.makeText(this, "Không tìm thấy ghi chú", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_note, menu);
        MenuItem item = menu.findItem(R.id.notification_add);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDateTimePicker();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            listImageIntent.add(uri);
            addListImage();

        }
        if (requestCode == 11 && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra("keyUri");
            listImageIntent.add(uri);
            addListImage();

        }
        if (requestCode == 12 && resultCode == RESULT_OK) {
            String str = "";
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            str = mContent.getText().toString() + Objects.requireNonNull(result).get(0);
            mContent.setText(str);
        }
        if (requestCode == 14 && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("labelListIntent")) {
                listLabelNote = data.getParcelableArrayListExtra("labelListIntent");
                mMainLabel.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                RecyclerLabelNoteAdapter adapter1 = new RecyclerLabelNoteAdapter(listLabelNote);
                mMainLabel.setAdapter(adapter1);
            }
        }
    }

    private void showDateTimePicker() {
        final View dialogView = View.inflate(EditNoteActivity.this, R.layout.date_time_picker, null);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(EditNoteActivity.this).create();

        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        Button dateTimeSet = dialogView.findViewById(R.id.date_time_set);
        dateTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeSet.setText(R.string.button_set_picker);
                datePicker.setVisibility(View.GONE);
                if (timePicker.getVisibility() == View.VISIBLE) {
                    Calendar calendar = new GregorianCalendar(
                            datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());
                    dateTimePicker = calendar.getTime();

                    layoutTextTimeNote.setVisibility(View.VISIBLE);
                    textTimeNote.setText(new SimpleDateFormat("HH:mm dd/MM/yyyy").format(dateTimePicker));
                    layoutTextTimeNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDateTimePicker();
                        }
                    });

                    alertDialog.dismiss();
                }
                timePicker.setVisibility(View.VISIBLE);
                timePicker.setIs24HourView(true);
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void getListRecorder() {
        mainRecorderNote = findViewById(R.id.main_recorder_note);
        mainRecorderNote.setLayoutManager(new LinearLayoutManager(EditNoteActivity.this));

        Cursor cursor = dataSource.getFileNote(idNote);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                listPathRecorder.add(cursor.getString(1));
            }
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File myDir = new File(getExternalFilesDir(null), "audio");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, fileName);
        String filePath = file.getAbsolutePath();
        mediaRecorder.setOutputFile(filePath);

        listPathRecorder.add(filePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            listViewRecording();
        }
    }

    private void listViewRecording() {

        RecyclerRecorderNoteAdapter adapter = new RecyclerRecorderNoteAdapter(EditNoteActivity.this, listPathRecorder, new IClickRecorder() {
            @Override
            public void onClickDelete(int position) {
                File fileToDelete = new File(listPathRecorder.get(position));
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    listPathRecorder.remove(position);
                    listViewRecording();
                    Toast.makeText(EditNoteActivity.this, "Xóa bản ghi âm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Lỗi xóa bản ghi âm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainRecorderNote.setAdapter(adapter);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Thử nói gì đó");

        try {
            startActivityForResult(intent, 12);
        }
        catch (Exception e) {
            Toast.makeText(EditNoteActivity.this, " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getListLabel() {
        Cursor cursor = dataSource.getLabelNote(idNote);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                listLabelNote.add(new Label(
                        Long.parseLong(cursor.getString(0))
                        ,cursor.getString(1)
                ));
            }
        }
        mMainLabel.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        RecyclerLabelNoteAdapter adapter1 = new RecyclerLabelNoteAdapter(listLabelNote);
        mMainLabel.setAdapter(adapter1);
    }

    private Uri getUriFromPath(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    private void getListImages(long id) {
        Cursor cursor = dataSource.readNoteImage(id);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                listImageIntent.add(getUriFromPath(cursor.getString(1)));
            }
        }
    }

    private void addListImage() {
        mImageView = findViewById(R.id.main_images_note);
        mImageView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        RecyclerImagesAddNoteAdapter adapter = new RecyclerImagesAddNoteAdapter(this, listImageIntent, new IClickDeleteCheckBox() {
            @Override
            public void onClickDeleteItem(int position) {
                listImageIntent.remove(position);
                addListImage();
            }
        });
        mImageView.setAdapter(adapter);
    }

    @NonNull
    private String saveImageToExternalStorage(FileModel fileModel) {
        File myDir = new File(getExternalFilesDir(null), "image");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // Tạo tên tệp ảnh dựa trên thời gian hiện tại.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_" + fileModel.getName();

        File file = new File(myDir, fileName);
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse("file://"+ fileModel.getPath()));
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void checkBackgroundColorOrImage(int color) {
        if (imageBackground != null) {
            if(colorNote == Color.rgb(255,255,255)) {
                mRoundedImageColor.setBackground(null);
            } else {
                mRoundedImageColor.setBackgroundColor(color);
            }
        }else {
            mRoundedImageColor.setBackground(null);
            mMainAddNote.setBackgroundColor(color);
        }
    }

    private void bottomActionColorImage(BottomSheetDialog bottomSheetView) {
        bottomSheetView.findViewById(R.id.color_note_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(255,255,255);

                if (imageBackground == null) {
                    mRoundedImageColor.setBackground(null);
                    mMainAddNote.setBackground(null);
                } else {
                    mRoundedImageColor.setBackground(null);
                }
            }
        });
        bottomSheetView.findViewById(R.id.color_note_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(250,175,168);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.color_note_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(243,159,118);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.color_note_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(255,248,184);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.color_note_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(226,246,211);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.color_note_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(180,221,221);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.color_note_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNote = Color.rgb(212,228,237);
                checkBackgroundColorOrImage(colorNote);
            }
        });

//        image background
        bottomSheetView.findViewById(R.id.image_note_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBackgroundColorOrImage(colorNote);
                imageBackground = null;
                checkBackgroundColorOrImage(colorNote);

            }
        });
        bottomSheetView.findViewById(R.id.image_note_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(EditNoteActivity.this, R.drawable.gg1);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(EditNoteActivity.this, R.drawable.gg2);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(EditNoteActivity.this, R.drawable.gg3);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(EditNoteActivity.this, R.drawable.gg4);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(EditNoteActivity.this, R.drawable.gg5);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
    }

    private void changeTextToCheckbox() {
        String[] arr = mContent.getText().toString().split("\n");
        addCheckBox = findViewById(R.id.bottom_add_check_box);

        if (isCheckBoxOrContent == 0) {
            for (String s : arr) {
                boolean isId = false;
                String contentCheckBox = s;
                if (s.startsWith("!!$")) {
                    isId = true;
                    contentCheckBox = s.substring(3);
                }
                listCheckBox.add(new CheckBoxContentNote(contentCheckBox, isId));
            }
            mContent.setVisibility(View.GONE);
            addCheckBox.setVisibility(View.VISIBLE);
            isCheckBoxOrContent = 1;
        } else {
            endCheckBoxList();
        }
        addListCheckBox(listCheckBox);
        addCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDeleteIdCheckBox();
                listCheckBox.add(new CheckBoxContentNote());
                addListCheckBox(listCheckBox);
            }
        });
    }

    private void endCheckBoxList(){
        StringBuilder data = new StringBuilder();
        editTextDeleteIdCheckBox();
        for (int i = 0; i < listCheckBox.size(); i++) {
            String row = "";
            row = row + listCheckBox.get(i).getContent() + "\n";
            data.append(row);
        }
        mContent.setVisibility(View.VISIBLE);
        mContent.setText(data.toString());
        addCheckBox.setVisibility(View.GONE);
        listCheckBox = new ArrayList<>();
        isCheckBoxOrContent = 0;
    }

    private void editTextDeleteIdCheckBox() {
        for (int i = 0; i < listCheckBox.size(); i++) {
            if (listCheckBox.get(i).getContent().startsWith("!!$")) {
                listCheckBox.get(i).setContent(listCheckBox.get(i).getContent().substring(3));
                listCheckBox.get(i).setCheckBox(true);
            }
        }
    }

    private void editTextAddIdCheckBox() {
        editTextDeleteIdCheckBox();
        for (int i = 0; i < listCheckBox.size(); i++) {
            if (listCheckBox.get(i).isCheckBox()) {
                listCheckBox.get(i).setContent("!!$"+listCheckBox.get(i).getContent());
            }
        }
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < listCheckBox.size(); i++) {
            String row = "";
            row = row + listCheckBox.get(i).getContent() + "\n";
            data.append(row);
        }
        mContent.setText(data.toString());
    }

    private void addListCheckBox(List<CheckBoxContentNote> list) {
        mMainCheckboxNote = findViewById(R.id.main_checkbox_note);

        mMainCheckboxNote.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerCheckBoxNoteAdapter adapter = new RecyclerCheckBoxNoteAdapter( list, new IClickDeleteCheckBox() {
            @Override
            public void onClickDeleteItem(int position) {
                listCheckBox.remove(position);
                addListCheckBox(listCheckBox);
                if (listCheckBox.size() == 0) {
                    endCheckBoxList();
                }
            }
        }, new ITextWatcherCheckBox() {
            @Override
            public void TextWatcherItem(int position, String text) {
                listCheckBox.get(position).setContent(text);
            }
        }, new OnStartDangListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        }, new IClickChecked() {
            @Override
            public void ClickChecked(int position, EditText editText) {

                if (listCheckBox.get(position).getContent().startsWith("!!$")) {
                    listCheckBox.get(position).setContent(listCheckBox.get(position).getContent().substring(3));
                    listCheckBox.get(position).setCheckBox(false);
                    editText.setEnabled(true);
                } else {
                    listCheckBox.get(position).setContent("!!$"+editText.getText().toString());
                    listCheckBox.get(position).setCheckBox(true);
                    editText.setEnabled(false);
                }

            }
        });
        mMainCheckboxNote.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mMainCheckboxNote);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (isSaveData && isFinishing()) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            if (imageBackground != null) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                imageBackground.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }
            if (isCheckBoxOrContent == 1) {
                editTextAddIdCheckBox();
            }
            dataSource.updateNote(new Note(
                    idNote,
                    dataSource.getCountNote(),
                    mTitle.getText().toString(),
                    mContent.getText().toString(),
                    isCheckBoxOrContent,
                    dateTimePicker != null ? dateTimePicker.toString() : "",
                    colorNote,
                    imageBackground != null ? Objects.requireNonNull(byteArrayOutputStream).toByteArray() : null,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())
            ));

            dataSource.deleteFileInNote(idNote);
            for (String pathFile : listPathRecorder) {
                dataSource.createFile(new FileModel(
                        pathFile,
                        idNote
                ));
            }


            for (Uri uri : listImageIntent) {
                FileModel fileModel = new FileModel();
                String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    fileModel = new FileModel(name, path, idNote);
                    String newPath = saveImageToExternalStorage(fileModel);
                    fileModel.setPath(newPath);
                    cursor.close();
                }
                dataSource.createImage(fileModel);
            }
            dataSource.detachLabel(idNote);
            for (Label label: listLabelNote) {
                dataSource.attachLabel(idNote, label.getId());
            }
            dataSource.close();
            setResult(RESULT_OK);

        }
    }
}
