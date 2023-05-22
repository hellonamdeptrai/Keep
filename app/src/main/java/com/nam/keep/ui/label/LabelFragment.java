package com.nam.keep.ui.label;

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

import java.util.ArrayList;
import java.util.List;

public class LabelFragment extends Fragment {

    private FragmentLabelBinding binding;

    // view
    private RecyclerView recyclerView;
    private EditText editTextLabel;
    private ImageButton buttonAddLabel;

    // data
    DatabaseHelper myDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLabelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.mainLabel;
        recyclerView.setHasFixedSize(true);

        editTextLabel = binding.addTextLabel;
        buttonAddLabel = binding.imageButtonAddLabel;

        myDatabase = new DatabaseHelper(getActivity());

        getListLabel();

        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextLabel.getText().toString().trim())) {
                    editTextLabel.setError("Tên nhãn không được để trống!");
                    return;
                }
                myDatabase.createLabel(new Label(
                        editTextLabel.getText().toString()
                ));
                getListLabel();
                editTextLabel.setText("");
            }
        });
        return root;
    }

    private void getListLabel() {
        List<Label> list = new ArrayList<>();
        Cursor cursor = myDatabase.getLabel();
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
                myDatabase.updateLabel(new Label(
                        list.get(position).getId(),
                        editText.getText().toString()
                ));
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