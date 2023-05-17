package com.nam.keep.ui.note.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.ui.note.helper.IClickDeleteCheckBox;

import java.util.List;

public class RecyclerImagesAddNoteAdapter extends RecyclerView.Adapter<RecyclerImagesAddNoteAdapter.PhotoNoteViewHolder>{

    private final List<Uri> list;
    private final Context context;
    private final IClickDeleteCheckBox iClickDeleteCheckBox;

    public RecyclerImagesAddNoteAdapter(Context context, List<Uri> list, IClickDeleteCheckBox iClickDeleteCheckBox) {
        this.list = list;
        this.context = context;
        this.iClickDeleteCheckBox = iClickDeleteCheckBox;
    }

    @NonNull
    @Override
    public PhotoNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_note, parent, false);
        return new PhotoNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoNoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri imagePath = list.get(position);
        Glide.with(context)
                .load(imagePath)
                .override(200,300)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_dialog_image);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

                ImageView imageView = dialog.findViewById(R.id.image_view_detail);
                ImageButton closeImage = dialog.findViewById(R.id.button_close_image);
                ImageButton deleteImage = dialog.findViewById(R.id.button_delete_image);

                Glide.with(context)
                        .load(imagePath)
                        .into(imageView);
                closeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iClickDeleteCheckBox.onClickDeleteItem(position);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(layoutParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PhotoNoteViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        public PhotoNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_add_note);
        }
    }
}
