package com.nam.keep.ui.user.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.model.User;
import com.nam.keep.ui.user.helper.UserAddNoteClick;

import java.util.ArrayList;

public class UserAddNoteAdapter extends RecyclerView.Adapter<UserAddNoteAdapter.LabelViewHolder> {

    private ArrayList<User> list;
    private UserAddNoteClick iUserAddNoteClick;

    public UserAddNoteAdapter(ArrayList<User> list, UserAddNoteClick iUserAddNoteClick) {
        this.list = list;
        this.iUserAddNoteClick = iUserAddNoteClick;
    }

    public void setData(ArrayList<User> newData) {
        list = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_checkbox, parent, false);
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final User userItem = list.get(position);

        if (!userItem.getAvatar().isEmpty()) {
            Glide.with(holder.avatar.getContext())
                    .load("file://" + userItem.getAvatar())
                    .override(100,100)
                    .apply(new RequestOptions().transform(new CircleCrop()))
                    .into(holder.avatar);
        }

        holder.textViewName.setText(userItem.getName());
        holder.textViewEmail.setText(userItem.getEmail());
        if (userItem.getIsChecked() == 1) {
            holder.labelCheckbox.setChecked(true);
        }
        holder.labelItemCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.labelCheckbox.setChecked(!holder.labelCheckbox.isChecked());
                iUserAddNoteClick.OnClickCheckBox(userItem);
            }
        });
        holder.labelCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iUserAddNoteClick.OnClickCheckBox(userItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail;
        LinearLayout labelItemCheckbox;
        CheckBox labelCheckbox;
        RoundedImageView avatar;
        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_name_user);
            textViewEmail = itemView.findViewById(R.id.text_email_user);
            labelItemCheckbox = itemView.findViewById(R.id.main_user_item_checkbox);
            labelCheckbox = itemView.findViewById(R.id.user_checkbox);
            avatar = itemView.findViewById(R.id.avatar_checkbox_item);
        }
    }
}
