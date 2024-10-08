package com.nam.keep.ui.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nam.keep.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class RecyclerImagesNoteAdapter extends RecyclerView.Adapter<RecyclerImagesNoteAdapter.PhotoNoteViewHolder>{

    private List<String> mImagePaths;

    public RecyclerImagesNoteAdapter(List<String> imagePaths) {
        mImagePaths = imagePaths;
    }

    @NonNull
    @Override
    public PhotoNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_note, parent, false);
        return new PhotoNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoNoteViewHolder holder, int position) {
        String imagePath = mImagePaths.get(position);
        Glide.with(holder.imageView.getContext())
                .load(imagePath)
                .override(200,300)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImagePaths.size();
    }

    public class PhotoNoteViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        public PhotoNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_add_note);
        }
    }
}
