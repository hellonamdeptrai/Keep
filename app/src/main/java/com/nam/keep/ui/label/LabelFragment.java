package com.nam.keep.ui.label;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.databinding.FragmentLabelBinding;
import com.nam.keep.model.Label;
import com.nam.keep.model.Note;
import com.nam.keep.notification.NotificationHelper;
import com.nam.keep.ui.home.adapter.MyRecyclerAdapter;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.home.helper.MyItemTouchHelperCallback;
import com.nam.keep.ui.label.adapter.LabelAdapter;
import com.nam.keep.ui.label.helper.ILabelClick;
import com.nam.keep.ui.note.EditNoteActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LabelFragment extends Fragment {

    private FragmentLabelBinding binding;

    // view
    private RecyclerView recyclerView, recyclerReminder;
    private EditText editTextLabel;
    private ImageButton buttonAddLabel;
    ImageView backFilterLabel;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout labelLayout, noteLabelLayout;

    // data
    DatabaseHelper myDatabase;
    SharedPreferences sharedPreferences;
    long idUser;
    MyRecyclerAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    long idLabel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLabelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainLabel;
        recyclerView.setHasFixedSize(true);

        editTextLabel = binding.addTextLabel;
        buttonAddLabel = binding.imageButtonAddLabel;
        swipeRefreshLayout = binding.noteLabelRefresh;
        labelLayout = binding.labelLayout;
        recyclerReminder = binding.mainRecyclerLabelFilter;
        noteLabelLayout = binding.noteLabelLayout;
        backFilterLabel = binding.backFilterLabel;

        myDatabase = new DatabaseHelper(getActivity());

        sharedPreferences = getContext().getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);

        getListLabel();

        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextLabel.getText().toString().trim())) {
                    editTextLabel.setError("Tên nhãn không được để trống!");
                    return;
                }
                Label label = new Label();
                label.setTitle(editTextLabel.getText().toString());
                label.setUpdated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                label.setUserId(idUser);
                myDatabase.createLabel(label);
                getListLabel();
                editTextLabel.setText("");
            }
        });

        // note filter
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        backFilterLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackPressed();
            }
        });
        return root;
    }

    private void getListLabel() {
        List<Label> list = new ArrayList<>();
        Cursor cursor = myDatabase.getLabel(idUser);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                list.add(new Label(
                        Long.parseLong(cursor.getString(0))
                        ,cursor.getString(1)
                ));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LabelAdapter adapter = new LabelAdapter(list, new ILabelClick() {
            @Override
            public void OnClickFilterNote(int position) {
                labelLayout.setVisibility(View.GONE);
                noteLabelLayout.setVisibility(View.VISIBLE);
                idLabel = list.get(position).getId();
                init();
                generateItem();
            }

            @Override
            public void OnClickEditDone(int position, EditText editText) {
                Label label = new Label();
                label.setId(list.get(position).getId());
                label.setTitle(editText.getText().toString());
                label.setUpdated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                label.setIsSync(0);
                myDatabase.updateLabel(label);
                getListLabel();
            }

            @Override
            public void OnClickDelete(int position) {
                myDatabase.deleteLabel(new Label(
                        list.get(position).getId()
                ));
                getListLabel();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // note filter
    private void init() {
        recyclerReminder.setHasFixedSize(true);
        recyclerReminder.setLayoutManager(new StaggeredGridLayoutManager(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2, StaggeredGridLayoutManager.VERTICAL));
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
        recyclerReminder.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerReminder);
    }

    private ArrayList<Note> getListNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = myDatabase.getNoteLabel(idLabel);
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
        recyclerReminder.setAdapter(adapter);

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

    public void handleBackPressed() {
        labelLayout.setVisibility(View.VISIBLE);
        noteLabelLayout.setVisibility(View.GONE);
    }
}