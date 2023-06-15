package com.nam.keep.ui.label;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.databinding.FragmentLabelBinding;
import com.nam.keep.model.Label;
import com.nam.keep.ui.label.adapter.LabelAdapter;
import com.nam.keep.ui.label.helper.ILabelClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LabelFragment extends Fragment {

    private FragmentLabelBinding binding;

    // view
    private RecyclerView recyclerView;
    private EditText editTextLabel;
    private ImageButton buttonAddLabel;

    // data
    DatabaseHelper myDatabase;
    SharedPreferences sharedPreferences;
    long idUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLabelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainLabel;
        recyclerView.setHasFixedSize(true);

        editTextLabel = binding.addTextLabel;
        buttonAddLabel = binding.imageButtonAddLabel;

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
                System.out.println("aaaaaaaaaa");
            }

            @Override
            public void OnClickEditDone(int position, EditText editText) {
                Label label = new Label();
                label.setId(list.get(position).getId());
                label.setTitle(editTextLabel.getText().toString());
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
}