package com.nam.keep.ui.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
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
        final User userItem = list.get(position);

        if (!userItem.getAvatar().isEmpty()) {
            Glide.with(holder.avatar.getContext())
                    .load("file://" + userItem.getAvatar())
                    .override(100,100)
                    .apply(new RequestOptions().transform(new CircleCrop()))
                    .into(holder.avatar);
        }
        holder.textView.setText(userItem.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelNoteViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RoundedImageView avatar;

        public LabelNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_user_name_note);
            avatar = itemView.findViewById(R.id.avatar_user_note);
        }
    }
}
