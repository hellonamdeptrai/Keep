package com.nam.keep.ui.archive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.databinding.FragmentArchiveBinding;
import com.nam.keep.databinding.FragmentReminderBinding;
import com.nam.keep.model.Note;
import com.nam.keep.notification.NotificationHelper;
import com.nam.keep.ui.home.adapter.MyRecyclerAdapter;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.note.EditNoteActivity;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {

    private FragmentArchiveBinding binding;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    MyRecyclerAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    DatabaseHelper myDatabase;
    SharedPreferences sharedPreferences;
    long idUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainRecyclerArchive;
        swipeRefreshLayout = binding.refreshNoteArchive;
        myDatabase = new DatabaseHelper(getActivity());

        sharedPreferences = getContext().getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);

        init();
        generateItem();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2, StaggeredGridLayoutManager.VERTICAL));
    }

    public void generateItem() {
        adapter = new MyRecyclerAdapter(getActivity(), viewHolder -> {
            itemTouchHelper.startDrag(viewHolder);
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
        adapter.setData(getListNotes());
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ArrayList<Note> getListNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor;
        if (idUser == 0) {
            cursor = myDatabase.getNoteArchive();
        } else {
            cursor = myDatabase.getNoteArchiveUser(idUser);
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
        NotificationHelper.showNotification(getActivity(), notes);
        return notes;
    }

    private void loadData() {
        adapter.setData(getListNotes());
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