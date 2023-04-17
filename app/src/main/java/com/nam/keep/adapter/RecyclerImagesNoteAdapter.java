package com.nam.keep.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nam.keep.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.model.Note;
import com.nam.keep.ui.home.helper.IClickItemDetail;

import java.util.List;

public class RecyclerImagesNoteAdapter extends RecyclerView.Adapter<RecyclerImagesNoteAdapter.PhotoNoteViewHolder>{

    private List<String> mImagePaths;
    private IClickItemNoteDetail iClickItemDetail;
    public interface IClickItemNoteDetail {
        void onClickItemNote(View view);
    }

    public RecyclerImagesNoteAdapter(List<String> imagePaths, IClickItemNoteDetail iClickItemDetail) {
        mImagePaths = imagePaths;
        this.iClickItemDetail = iClickItemDetail;
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
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemDetail.onClickItemNote(view);
            }
        });
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
