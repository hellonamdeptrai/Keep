package com.nam.keep.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.User;
import com.nam.keep.ui.label.LabelActivity;
import com.nam.keep.ui.label.adapter.LabelAddNoteAdapter;
import com.nam.keep.ui.label.helper.ILabelAddNoteClick;
import com.nam.keep.ui.user.adapter.UserAddNoteAdapter;
import com.nam.keep.ui.user.helper.UserAddNoteClick;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    // View
    private Button buttonBack;
    private RecyclerView recyclerView;

    // data
    DatabaseHelper myDatabase;
    ArrayList<User> list = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (getIntent().getParcelableArrayListExtra("userListEditIntent") != null) {
            userList.addAll(getIntent().getParcelableArrayListExtra("userListEditIntent"));
        }

        buttonBack = findViewById(R.id.button_back_user);

        sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userList.clear();
                for (User user : list) {
                    if (user.getIsChecked() == 1) {
                        userList.add(user);
                    }
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("userListIntent", userList);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_user);
        recyclerView.setHasFixedSize(true);

        myDatabase = new DatabaseHelper(UserActivity.this);

        getListUser();
    }

    private void getListUser() {
        Cursor cursor = myDatabase.getUserNotMe(idUser);
        if(cursor.getCount() != 0){
            for (int i = 0; cursor.moveToNext(); i++) {
                User user = new User();
                user.setId(Long.parseLong(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(3));
                user.setAvatar(cursor.getString(2));
                user.setIsChecked(0);
                list.add(user);
                for (int j = 0; j < userList.size(); j++) {
                    if (list.get(i).getId() == userList.get(j).getId()) {
                        list.get(i).setIsChecked(1);
                    }
                }
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));
        UserAddNoteAdapter adapter = new UserAddNoteAdapter(list, new UserAddNoteClick() {
            @Override
            public void OnClickCheckBox(User user) {
                if (user.getIsChecked() == 0) {
                    user.setIsChecked(1);
                } else {
                    user.setIsChecked(0);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}