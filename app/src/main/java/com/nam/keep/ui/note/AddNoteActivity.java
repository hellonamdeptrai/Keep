package com.nam.keep.ui.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.MainActivity;
import com.nam.keep.R;
import com.nam.keep.model.Label;
import com.nam.keep.model.User;
import com.nam.keep.ui.label.LabelActivity;
import com.nam.keep.ui.login.LoginActivity;
import com.nam.keep.ui.note.adapter.RecyclerLabelNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerRecorderNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerUserNoteAdapter;
import com.nam.keep.ui.note.helper.IClickChecked;
import com.nam.keep.ui.note.helper.IClickDeleteCheckBox;
import com.nam.keep.ui.note.helper.IClickRecorder;
import com.nam.keep.ui.note.helper.ITextWatcherCheckBox;
import com.nam.keep.ui.note.adapter.RecyclerCheckBoxNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerImagesAddNoteAdapter;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.CheckBoxContentNote;
import com.nam.keep.model.FileModel;
import com.nam.keep.model.Note;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.home.helper.OnStartDangListener;
import com.nam.keep.ui.paint.PaintActivity;
import com.nam.keep.ui.user.UserActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

public class AddNoteActivity extends AppCompatActivity {

    private boolean isSaveData = true;

    // View
    private Toolbar mToolbar;
    private EditText mTitle, mContent;
    private CoordinatorLayout mMainAddNote;
    private RoundedImageView mRoundedImageColor;
    private Button addCheckBox;
    private RecyclerView mainRecorderNote;
    private LinearLayout layoutTextTimeNote;
    private TextView textTimeNote;
    private ImageView closeTextTimeNote;

    // Data
    private DatabaseHelper dataSource;
    ArrayList<Uri> listImageIntent = new ArrayList<>();
    List<CheckBoxContentNote> listCheckBox = new ArrayList<>();
    private int colorNote = Color.rgb(255,255,255);
    private Bitmap imageBackground;
    private int isCheckBoxOrContent = 0;
    ItemTouchHelper itemTouchHelper;
    MediaRecorder mediaRecorder;
    ArrayList<Label> listLabelNote = new ArrayList<>();
    ArrayList<User> listUserNote = new ArrayList<>();
    ArrayList<String> listPathRecorder = new ArrayList<>();
    Date dateTimePicker;
    private Stack<CharSequence> undoStack;
    private Stack<CharSequence> redoStack;
    private boolean isUndoOrRedo;
    SharedPreferences sharedPreferences;
    long idUser;

    // open
    public static boolean isOpenCheckBox = false;
    public static boolean isOpenAddImage = false;
    public static boolean isOpenAddBrush = false;
    public static boolean isOpenAddMic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mToolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dataSource = new DatabaseHelper(this);

        mTitle = findViewById(R.id.title_note);
        mContent = findViewById(R.id.content_note);
        mMainAddNote = findViewById(R.id.main_container_add_note);
        mRoundedImageColor = findViewById(R.id.color_background_imaged);
        layoutTextTimeNote = findViewById(R.id.layout_text_time_note);
        textTimeNote = findViewById(R.id.text_time_note);
        closeTextTimeNote = findViewById(R.id.close_text_time_note);

        // recorder view
        mainRecorderNote = findViewById(R.id.main_recorder_note);
        mainRecorderNote.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this));

        Button mSheetAddButton = findViewById(R.id.sheet_add_note_button);
        Button mSheetColorButton = findViewById(R.id.sheet_color_note_button);
        Button mSheetThreeDotsNoteButton = findViewById(R.id.sheet_three_dots_note_button);
        Button mUndoButton = findViewById(R.id.undo_note_button);
        Button mRedoButton = findViewById(R.id.redo_note_button);

        undoStack = new Stack<>();
        redoStack = new Stack<>();
        isUndoOrRedo = false;

        sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);

        mSheetAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddNoteActivity.this);
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
                        Intent intentLogin = new Intent(AddNoteActivity.this, PaintActivity.class);
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
                        final Dialog dialog = new Dialog(AddNoteActivity.this);
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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddNoteActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_color_note);
                bottomSheetDialog.show();
                bottomActionColorImage(bottomSheetDialog);
            }
        });
        mSheetThreeDotsNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddNoteActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_three_dots_note);
                bottomSheetDialog.show();
                bottomSheetDialog.findViewById(R.id.add_label_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(AddNoteActivity.this, LabelActivity.class);
                        intent.putExtra("labelListEditIntent", listLabelNote);
                        startActivityForResult(intent, 14);
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_share_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        // Tạo Intent để chia sẻ văn bản
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, mTitle.getText() +"\n\n" + mContent.getText());

                        // Kiểm tra và chọn ứng dụng chia sẻ
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, 0);
                        if (activities.size() > 0) {
                            // Mở hộp thoại chọn ứng dụng chia sẻ
                            Intent chooserIntent = Intent.createChooser(shareIntent, "Chia sẻ qua");
                            startActivity(chooserIntent);
                        }
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_delete_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddNoteActivity.this);
                        builder.setTitle("Xóa ghi chú?");
                        builder.setMessage("Bạn có chắc chắn muốn xóa ghi chú này không?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isSaveData = false;
                                finish();
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.nam_keep));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.nam_keep));
                    }
                });
                bottomSheetDialog.findViewById(R.id.add_person_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(AddNoteActivity.this, UserActivity.class);
                        intent.putExtra("userListEditIntent", listUserNote);
                        startActivityForResult(intent, 15);
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

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!isUndoOrRedo) {
                    undoStack.push(s.toString());
                    redoStack.clear();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        mRedoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redo();
            }
        });

        // open
        if (isOpenCheckBox){
            changeTextToCheckbox();
            isOpenCheckBox = false;
        }
        if (isOpenAddImage){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 10);
            isOpenAddImage = false;
        }
        if (isOpenAddBrush){
            Intent intentLogin = new Intent(AddNoteActivity.this, PaintActivity.class);
            startActivityForResult(intentLogin, 11);
            isOpenAddBrush = false;
        }
        if (isOpenAddMic){
            startSpeechRecognition();
            isOpenAddMic = false;
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (!undoStack.isEmpty()) {
//            undoStack.pop();
//        }
//
//        if (!undoStack.isEmpty()) {
//            CharSequence previousText = undoStack.peek();
//            isUndoOrRedo = true;
//            mContent.setText(previousText);
//            mContent.setSelection(previousText.length());
//            isUndoOrRedo = false;
//        }
//    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            CharSequence currentText = mContent.getText();
            redoStack.push(currentText);

            CharSequence previousText = undoStack.pop();
            isUndoOrRedo = true;
            mContent.setText(previousText);
            mContent.setSelection(previousText.length());
            isUndoOrRedo = false;
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            CharSequence currentText = mContent.getText();
            undoStack.push(currentText);

            CharSequence nextText = redoStack.pop();
            isUndoOrRedo = true;
            mContent.setText(nextText);
            mContent.setSelection(nextText.length());
            isUndoOrRedo = false;
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
                RecyclerView recyclerViewLabelNote = findViewById(R.id.main_categories_note);
                recyclerViewLabelNote.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                RecyclerLabelNoteAdapter adapter1 = new RecyclerLabelNoteAdapter(listLabelNote);
                recyclerViewLabelNote.setAdapter(adapter1);
            }
        }
        if (requestCode == 15 && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("userListIntent")) {
                listUserNote = data.getParcelableArrayListExtra("userListIntent");
                RecyclerView recyclerViewLabelNote = findViewById(R.id.main_users_note);
                recyclerViewLabelNote.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                RecyclerUserNoteAdapter adapter1 = new RecyclerUserNoteAdapter(listUserNote);
                recyclerViewLabelNote.setAdapter(adapter1);
            }
        }
    }

    private void showDateTimePicker() {
        final View dialogView = View.inflate(AddNoteActivity.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(AddNoteActivity.this).create();

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

        RecyclerRecorderNoteAdapter adapter = new RecyclerRecorderNoteAdapter(AddNoteActivity.this, listPathRecorder, new IClickRecorder() {
            @Override
            public void onClickDelete(int position) {
                File fileToDelete = new File(listPathRecorder.get(position));
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    listPathRecorder.remove(position);
                    listViewRecording();
                    Toast.makeText(AddNoteActivity.this, "Xóa bản ghi âm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Lỗi xóa bản ghi âm", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AddNoteActivity.this, " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }



    private void addListImage() {
        RecyclerView mainImagesNote = findViewById(R.id.main_images_note);
        mainImagesNote.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        RecyclerImagesAddNoteAdapter adapter = new RecyclerImagesAddNoteAdapter(this, listImageIntent, new IClickDeleteCheckBox() {
            @Override
            public void onClickDeleteItem(int position) {
                listImageIntent.remove(position);
                addListImage();
            }
        });
        mainImagesNote.setAdapter(adapter);
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
                Drawable drawable = ContextCompat.getDrawable(AddNoteActivity.this, R.drawable.gg1);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(AddNoteActivity.this, R.drawable.gg2);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(AddNoteActivity.this, R.drawable.gg3);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(AddNoteActivity.this, R.drawable.gg4);
                imageBackground = ((BitmapDrawable)drawable).getBitmap();
                mMainAddNote.setBackground(drawable);
                checkBackgroundColorOrImage(colorNote);
            }
        });
        bottomSheetView.findViewById(R.id.image_note_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = ContextCompat.getDrawable(AddNoteActivity.this, R.drawable.gg5);
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
                listCheckBox.add(new CheckBoxContentNote(s, false));
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
        RecyclerView mainCheckBoxNote = findViewById(R.id.main_checkbox_note);

        mainCheckBoxNote.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
        mainCheckBoxNote.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mainCheckBoxNote);
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

            Note note = new Note();
            note.setIndex(idUser == 0 ? dataSource.getCountNote() + 1 : dataSource.getCountNoteUser(idUser) + 1);
            note.setTitle(mTitle.getText().toString());
            note.setContent(mContent.getText().toString());
            note.setIsCheckBoxOrContent(isCheckBoxOrContent);
            note.setDeadline(dateTimePicker != null ? dateTimePicker.toString() : "");
            note.setColor(colorNote);
            note.setBackground(imageBackground != null ? Objects.requireNonNull(byteArrayOutputStream).toByteArray() : null);
            note.setUpdatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            note.setUserId(idUser);
            note.setIsSync(0);
            dataSource.createNote(note);

            long idNewNote = dataSource.getNoteIdNew();

            // attach note user
            if (idUser != 0) {
                dataSource.attachUser(idNewNote, idUser);
            }

            for (String pathFile : listPathRecorder) {
                dataSource.createFile(new FileModel(
                        pathFile,
                        idNewNote
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
                    fileModel = new FileModel(name, path, idNewNote);
                    String newPath = saveImageToExternalStorage(fileModel);
                    fileModel.setPath(newPath);
                    cursor.close();
                }
                dataSource.createImage(fileModel);
            }
            for (Label label: listLabelNote) {
                dataSource.attachLabel(idNewNote, label.getId());
            }
            for (User user: listUserNote) {
                dataSource.attachUser(idNewNote, user.getId());
            }
            dataSource.close();
            setResult(RESULT_OK);

        }
    }
}