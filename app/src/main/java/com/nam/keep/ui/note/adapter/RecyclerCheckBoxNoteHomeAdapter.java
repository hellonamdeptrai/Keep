package com.nam.keep.ui.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.model.CheckBoxContentNote;

import java.util.List;

public class RecyclerCheckBoxNoteHomeAdapter extends RecyclerView.Adapter<RecyclerCheckBoxNoteHomeAdapter.CheckBoxNoteHomeViewHolder> {
    private List<CheckBoxContentNote> list;

    public RecyclerCheckBoxNoteHomeAdapter(List<CheckBoxContentNote> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CheckBoxNoteHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckBoxNoteHomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxNoteHomeViewHolder holder, int position) {
        final CheckBoxContentNote checkBoxItem = list.get(position);

        holder.checkBox.setChecked(checkBoxItem.isCheckBox());
        holder.textView.setText(checkBoxItem.getContent());
        holder.textView.setEnabled(!checkBoxItem.isCheckBox());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CheckBoxNoteHomeViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;

        public CheckBoxNoteHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box_home);
            textView = itemView.findViewById(R.id.view_text_check_box_home);
        }
    }
}
