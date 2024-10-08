package com.nam.keep.ui.label;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.Label;
import com.nam.keep.ui.label.adapter.LabelAdapter;
import com.nam.keep.ui.label.adapter.LabelAddNoteAdapter;
import com.nam.keep.ui.label.helper.ILabelAddNoteClick;
import com.nam.keep.ui.label.helper.ILabelClick;
import com.nam.keep.ui.note.AddNoteActivity;

import java.util.ArrayList;
import java.util.List;

public class LabelActivity extends AppCompatActivity {
    // View
    private Button buttonBack;
    private RecyclerView recyclerView;
    private EditText searchTextLabel;

    // data
    DatabaseHelper myDatabase;
    ArrayList<Label> list = new ArrayList<>();
    ArrayList<Label> labelList = new ArrayList<>();
    ArrayList<Label> searchResults = new ArrayList<>();
    LabelAddNoteAdapter adapter;
    SharedPreferences sharedPreferences;
    long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        if (getIntent().getParcelableArrayListExtra("labelListEditIntent") != null) {
            labelList.addAll(getIntent().getParcelableArrayListExtra("labelListEditIntent"));
        }

        buttonBack = findViewById(R.id.button_back_label);
        searchTextLabel = findViewById(R.id.search_text_label);

        sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labelList.clear();
                for (Label label : list) {
                    if (label.getIsChecked() == 1) {
                        labelList.add(label);
                    }
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("labelListIntent", labelList);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_label);
        recyclerView.setHasFixedSize(true);

        myDatabase = new DatabaseHelper(LabelActivity.this);

        searchTextLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                searchResults.clear();

                // Lọc các phần tử từ ArrayList ban đầu và thêm vào ArrayList tìm kiếm
                for (Label label : list) {
                    if (label.getTitle().toLowerCase().contains(searchText)) {
                        searchResults.add(label);
                    }
                }

                // Cập nhật dữ liệu của Adapter với ArrayList tìm kiếm
                adapter.setData(searchResults);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getListLabel();
    }

    private void getListLabel() {
        Cursor cursor = myDatabase.getLabel(idUser);
        if(cursor.getCount() != 0){
            for (int i = 0; cursor.moveToNext(); i++) {
                list.add(new Label(
                        Long.parseLong(cursor.getString(0))
                        ,cursor.getString(1),
                        0
                ));
                for (int j = 0; j < labelList.size(); j++) {
                    if (list.get(i).getId() == labelList.get(j).getId()) {
                        list.get(i).setIsChecked(1);
                    }
                }
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(LabelActivity.this));
        adapter = new LabelAddNoteAdapter(list, new ILabelAddNoteClick() {
            @Override
            public void OnClickCheckBox(Label label) {
                if (label.getIsChecked() == 0) {
                    label.setIsChecked(1);
                } else {
                    label.setIsChecked(0);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}