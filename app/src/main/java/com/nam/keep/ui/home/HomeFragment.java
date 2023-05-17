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

import com.nam.keep.MainActivity;
import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.databinding.FragmentHomeBinding;
import com.nam.keep.model.Note;
import com.nam.keep.ui.home.adapter.MyRecyclerAdapter;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.note.AddNoteActivity;

import java.util.ArrayList;

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
            public void onClickItemNote(View view, Note note) {
//                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
//                intent.putExtra(EditNoteActivity.EXTRA_PARAM_ID, note.getId());
//                intent.putExtra(EditNoteActivity.VIEW_NAME_TITLE, note.getTitle());
//                intent.putExtra(EditNoteActivity.VIEW_NAME_CONTENT, note.getContent());
//                intent.putExtra(EditNoteActivity.VIEW_NAME_IS_CHECKBOX, note.getIsCheckBoxOrContent());
//                intent.putExtra(EditNoteActivity.VIEW_NAME_BACKGROUND, note.getBackground());
//                intent.putExtra(EditNoteActivity.VIEW_NAME_EDIT_COLOR, note.getColor());
//
//                Pair isColorData, isContentOrCheckbox;
//                if (note.getColor() != Color.rgb(255,255,255)) {
//                    isColorData = new Pair<>(view.findViewById(R.id.color_background_image_home),
//                            EditNoteActivity.VIEW_NAME_EDIT_COLOR);
//                } else {
//                    isColorData = new Pair<>(view.findViewById(R.id.title_note_home),
//                            EditNoteActivity.VIEW_NAME_EDIT_COLOR);
//                }
//
//                if (note.getIsCheckBoxOrContent() == 1) {
//                    isContentOrCheckbox = new Pair<>(view.findViewById(R.id.main_checkbox_note_home),
//                            EditNoteActivity.VIEW_NAME_LIST_CHECKBOX);
//                } else {
//                    isContentOrCheckbox = new Pair<>(view.findViewById(R.id.content_note_home),
//                            EditNoteActivity.VIEW_NAME_CONTENT);
//                }
//
//                @SuppressWarnings("unchecked")
//                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        getActivity(),
//                        new Pair<>(view.findViewById(R.id.main_images_note_home),
//                                EditNoteActivity.VIEW_NAME_IMAGE),
//                        new Pair<>(view.findViewById(R.id.title_note_home),
//                                EditNoteActivity.VIEW_NAME_TITLE),
//                        isContentOrCheckbox,
//                        isColorData,
//                        new Pair<>(view.findViewById(R.id.main_categories_note_home),
//                                EditNoteActivity.VIEW_NAME_LABEL));
//                ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
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
                notes.add(new Note(Long.parseLong(cursor.getString(0))
                        ,Integer.parseInt(cursor.getString(1))
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,Integer.parseInt(cursor.getString(4))
                        ,cursor.getString(5)
                        ,Integer.parseInt(cursor.getString(6))
                        ,cursor.getBlob(7)
                        ,cursor.getString(8)
                        ,Long.parseLong(cursor.getString(9))
                ));
            }
        }

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