package com.nam.keep.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.MainActivity;
import com.nam.keep.R;
import com.nam.keep.api.ApiClient;
import com.nam.keep.database.DataBaseContract;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.databinding.FragmentHomeBinding;
import com.nam.keep.model.Note;
import com.nam.keep.model.User;
import com.nam.keep.notification.NotificationHelper;
import com.nam.keep.ui.home.adapter.MyRecyclerAdapter;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.note.AddNoteActivity;
import com.nam.keep.ui.note.EditNoteActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    MyRecyclerAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    DatabaseHelper myDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainRecycler;
        swipeRefreshLayout = root.findViewById(R.id.refresh_note_home);
        myDatabase = new DatabaseHelper(getActivity());
        init();
        generateItem();

//        Button addImageButton = root.findViewById(R.id.add_note);
//        addImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCheckbox = new Intent(getContext(), AddNoteActivity.class);
//                startActivity(intentCheckbox);
//            }
//        });

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

                Pair isColorData, isContentOrCheckbox;
                if (note.getColor() != Color.rgb(255,255,255)) {
                    isColorData = new Pair<>(colorBackgroundImagedHome,
                            EditNoteActivity.VIEW_NAME_EDIT_COLOR);
                } else {
                    isColorData = new Pair<>(title,
                            EditNoteActivity.VIEW_NAME_EDIT_COLOR);
                }

                if (note.getIsCheckBoxOrContent() == 1) {
                    isContentOrCheckbox = new Pair<>(mainCheckboxNoteHome,
                            EditNoteActivity.VIEW_NAME_LIST_CHECKBOX);
                } else {
                    isContentOrCheckbox = new Pair<>(content,
                            EditNoteActivity.VIEW_NAME_CONTENT);
                }

                @SuppressWarnings("unchecked")
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        new Pair<>(imageView,
                                EditNoteActivity.VIEW_NAME_IMAGE),
                        new Pair<>(title,
                                EditNoteActivity.VIEW_NAME_TITLE),
                        isContentOrCheckbox,
                        isColorData,
                        new Pair<>(mainCategoriesNoteHome,
                                EditNoteActivity.VIEW_NAME_LABEL));
                startActivityForResult(intent,0);
//                ActivityCompat.startActivityForResult(requireActivity(), intent, 0, activityOptions.toBundle());
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
        Cursor cursor = myDatabase.getNote();
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