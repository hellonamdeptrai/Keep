package com.nam.keep.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.Note;
import com.nam.keep.notification.NotificationHelper;
import com.nam.keep.ui.home.adapter.MyRecyclerAdapter;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.note.EditNoteActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    // View
    private Button buttonBack;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private EditText searchTextSearch;

    // data
    MyRecyclerAdapter adapter;
    DatabaseHelper myDatabase;
    SharedPreferences sharedPreferences;
    long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.main_recycler_search);
        swipeRefreshLayout = findViewById(R.id.refresh_note_search);
        buttonBack = findViewById(R.id.button_back_search);
        searchTextSearch = findViewById(R.id.search_text);

        myDatabase = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);

        init();
        generateItem();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.setData(getListNotes(editable.toString().trim().toLowerCase()));
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2, StaggeredGridLayoutManager.VERTICAL));
    }

    public void generateItem() {
        adapter = new MyRecyclerAdapter(this, viewHolder -> {

        }, new IClickItemDetail() {
            @Override
            public void onClickItemNote(View view, TextView title, TextView content,
                                        ImageView imageView, RecyclerView mainImagesNoteHome,
                                        RecyclerView mainCheckboxNoteHome,
                                        RecyclerView mainCategoriesNoteHome,
                                        RoundedImageView colorBackgroundImagedHome,
                                        RoundedImageView imageBackgroundHome, Note note) {
                Intent intent = new Intent(view.getContext(), EditNoteActivity.class);
                intent.putExtra(EditNoteActivity.EXTRA_PARAM_ID, note.getId());
                startActivityForResult(intent,0);
            }
        });
        adapter.setData(getListNotes(searchTextSearch.getText().toString().trim().toLowerCase()));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Note> getListNotes(String searchString) {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor;
        if (idUser == 0) {
            cursor = myDatabase.getSearchNote(searchString);
        } else {
            cursor = myDatabase.getSearchNoteUser(searchString, idUser);
        }

        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setIndex(Integer.parseInt(cursor.getString(1)));
                note.setTitle(cursor.getString(2));
                note.setContent(cursor.getString(3));
                note.setIsCheckBoxOrContent(Integer.parseInt(cursor.getString(4)));
                note.setDeadline(cursor.getString(5));
                note.setColor(Integer.parseInt(cursor.getString(6)));
                note.setBackground(cursor.getBlob(7));
                note.setUpdatedAt(cursor.getString(9));
                note.setUserId(Long.parseLong(cursor.getString(8)));
                note.setIsSync(Integer.parseInt(cursor.getString(10)));
                notes.add(note);
            }
        }
        NotificationHelper.showNotification(this, notes);
        return notes;
    }

    private void loadData() {
        adapter.setData(getListNotes(searchTextSearch.getText().toString().trim().toLowerCase()));
        recyclerView.setAdapter(adapter);

        // Sau khi thêm item mới vào danh sách, cập nhật hiển thị của RecyclerView.
        adapter.notifyDataSetChanged();

        // Dừng hiển thị animation refresh của SwipeRefreshLayout.
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }
}