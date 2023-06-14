package com.nam.keep.ui.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.model.User;

import java.util.List;

public class RecyclerUserNoteAdapter extends RecyclerView.Adapter<RecyclerUserNoteAdapter.LabelNoteViewHolder> {

    private List<User> list;

    public RecyclerUserNoteAdapter(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LabelNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabelNoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelNoteViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelNoteViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public LabelNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_user_name_note);
        }
    }
}
