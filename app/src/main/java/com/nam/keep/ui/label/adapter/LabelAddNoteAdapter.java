package com.nam.keep.ui.label.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.model.Label;
import com.nam.keep.ui.label.helper.ILabelAddNoteClick;

import java.util.ArrayList;
import java.util.List;

public class LabelAddNoteAdapter extends RecyclerView.Adapter<LabelAddNoteAdapter.LabelViewHolder> {

    private ArrayList<Label> list;
    private ILabelAddNoteClick iLabelAddNoteClick;

    public LabelAddNoteAdapter(ArrayList<Label> list, ILabelAddNoteClick iLabelAddNoteClick) {
        this.list = list;
        this.iLabelAddNoteClick = iLabelAddNoteClick;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label_checkbox, parent, false);
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Label labelItem = list.get(position);

        holder.textView.setText(labelItem.getTitle());
        if (labelItem.getIsChecked() == 1) {
            holder.labelCheckbox.setChecked(true);
        }
        holder.labelItemCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.labelCheckbox.setChecked(!holder.labelCheckbox.isChecked());
                iLabelAddNoteClick.OnClickCheckBox(labelItem);
            }
        });
        holder.labelCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLabelAddNoteClick.OnClickCheckBox(labelItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout labelItemCheckbox;
        CheckBox labelCheckbox;
        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_label_checkbox);
            labelItemCheckbox = itemView.findViewById(R.id.main_label_item_checkbox);
            labelCheckbox = itemView.findViewById(R.id.label_checkbox);
        }
    }
}
