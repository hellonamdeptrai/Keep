package com.nam.keep.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nam.keep.R;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainRecycler;
        swipeRefreshLayout = root.findViewById(R.id.refresh_note_home);
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
                // Gọi hàm để làm mới các dữ liệu trong RecyclerView ở đây.
                // Sau khi hoàn thành, gọi swipeRefreshLayout.setRefreshing(false) để dừng
                // hiển thị animation refresh.
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
//        ButterKnife.bind(getActivity());
        recyclerView = binding.mainRecycler;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2, StaggeredGridLayoutManager.VERTICAL));
    }

    public void generateItem() {
        adapter = new MyRecyclerAdapter(getActivity(), viewHolder -> {
            itemTouchHelper.startDrag(viewHolder);
        }, new IClickItemDetail() {
            @Override
            public void onClickItemNote(View view, Note note) {
                System.out.println("aaaaaaaaaaaaaaaaaaa");
                Intent intentImage = new Intent(getContext(), AddNoteActivity.class);
                startActivity(intentImage);
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
        for (int i = 0; i < 2; i++) {
            notes.add(new Note(
                    i
                    ,"hehe"
                    ,"skudfsf sjfg"
                    , 0
                    ,Color.rgb(255,255,255)
                    ,null
                    ,1
            ));
        }

        return notes;
    }

    private void loadData() {
        // Giả định là mình có một danh sách các item, được lưu trong một ArrayList.
        // Mỗi lần làm mới dữ liệu, mình sẽ thêm một item mới vào đầu danh sách.
        getListNotes();

        // Sau khi thêm item mới vào danh sách, cập nhật hiển thị của RecyclerView.
        adapter.notifyDataSetChanged();

        // Dừng hiển thị animation refresh của SwipeRefreshLayout.
        swipeRefreshLayout.setRefreshing(false);
    }
}