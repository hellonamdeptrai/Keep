package com.nam.keep.ui.home.adapter;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.model.Label;
import com.nam.keep.model.User;
import com.nam.keep.ui.note.adapter.RecyclerCheckBoxNoteHomeAdapter;
import com.nam.keep.ui.note.adapter.RecyclerImagesNoteAdapter;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.CheckBoxContentNote;
import com.nam.keep.model.Note;
import com.nam.keep.ui.home.helper.IClickItemDetail;
import com.nam.keep.ui.home.helper.ItemTouchHelperAdapter;
import com.nam.keep.ui.home.helper.OnStartDangListener;
import com.nam.keep.ui.note.adapter.RecyclerLabelNoteAdapter;
import com.nam.keep.ui.note.adapter.RecyclerUserNoteHomeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    Context context;
    OnStartDangListener listener;
    private ArrayList<Note> notesList;
    DatabaseHelper dataSource;

    private IClickItemDetail iClickItemDetail;

    public MyRecyclerAdapter(Context context, OnStartDangListener listener, IClickItemDetail iClickItemDetail) {
        this.context = context;
        this.listener = listener;
        this.iClickItemDetail = iClickItemDetail;

        dataSource = new DatabaseHelper(context);
    }

    public void setData(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(itemView);

        viewHolder.mainImagesNoteHome.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        viewHolder.mainCategoriesNoteHome.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        viewHolder.mainUserNoteHome.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Note noteItem = notesList.get(position);

        RecyclerImagesNoteAdapter adapter = new RecyclerImagesNoteAdapter(getListImages(noteItem.getId()));
        holder.mainImagesNoteHome.setAdapter(adapter);

        RecyclerLabelNoteAdapter adapter1 = new RecyclerLabelNoteAdapter(getListLabel(noteItem.getId()));
        holder.mainCategoriesNoteHome.setAdapter(adapter1);

        RecyclerUserNoteHomeAdapter adapter2 = new RecyclerUserNoteHomeAdapter(getListUser(noteItem.getId()));
        holder.mainUserNoteHome.setAdapter(adapter2);

        if (getRecorder(noteItem.getId())) {
            holder.iconRecorderHome.setVisibility(View.VISIBLE);
        }

        if (noteItem.getBackground() != null){
            byte[] blob = noteItem.getBackground();
            Bitmap bitmap = null;
            if (blob != null) {
                bitmap = BitmapFactory.decodeByteArray(blob,0,blob.length);
            }
            BitmapDrawable ob = new BitmapDrawable(holder.itemView.getContext().getResources(), bitmap);
            holder.imageBackgroundHome.setBackground(ob);
        }else {
            holder.imageBackgroundHome.setVisibility(View.GONE);
        }

        holder.title.setText(noteItem.getTitle());
        holder.content.setText(noteItem.getContent());

        if (!noteItem.getDeadline().isEmpty()) {
            holder.layoutTextTimeHome.setVisibility(View.VISIBLE);
            try {
                Date dateTimePicker = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(noteItem.getDeadline());
                assert dateTimePicker != null;
                holder.textTimeHome.setText(new SimpleDateFormat("HH:mm dd/MM").format(dateTimePicker));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (noteItem.getColor() != Color.rgb(255,255,255)){
            holder.colorBackgroundImagedHome.setBackgroundColor(noteItem.getColor());
        }else {
            holder.colorBackgroundImagedHome.setVisibility(View.GONE);
        }

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onStartDrag(holder);
                return false;
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemDetail.onClickItemNote(
                        view,
                        holder.title,
                        holder.content,
                        holder.imageView,
                        holder.mainImagesNoteHome,
                        holder.mainCheckboxNoteHome,
                        holder.mainCategoriesNoteHome,
                        holder.colorBackgroundImagedHome,
                        holder.imageBackgroundHome,
                        noteItem);
            }
        });

        if (noteItem.getIsCheckBoxOrContent() == 1){
            holder.content.setVisibility(View.GONE);
            holder.mainCheckboxNoteHome.setLayoutManager(new LinearLayoutManager(context));
            RecyclerCheckBoxNoteHomeAdapter adapterCheckbox = new RecyclerCheckBoxNoteHomeAdapter(getListCheckBox(noteItem.getContent()));
            holder.mainCheckboxNoteHome.setAdapter(adapterCheckbox);
        }
    }

    @Override
    public int getItemCount() {
        if (notesList != null) {
            return notesList.size();
        }
        return 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Note movedItem = notesList.get(fromPosition);
        notesList.remove(fromPosition);
        notesList.add(toPosition, movedItem);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void inItemDismiss(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void moveDone() {
        int i = notesList.size();
        for (Note note : notesList) {
            Note note1 = new Note();
            note1.setId(note.getId());
            note1.setIndex(i);
            dataSource.updateNoteIndex(note1);
            i--;
        }
    }

    private List<Label> getListLabel(long idNote) {
        List<Label> listLabelNote = new ArrayList<>();
        Cursor cursor = dataSource.getLabelNote(idNote);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                listLabelNote.add(new Label(
                        Long.parseLong(cursor.getString(0))
                        ,cursor.getString(1)
                ));
            }
        }
        return listLabelNote;
    }

    private List<User> getListUser(long idNote) {
        List<User> listUserNote = new ArrayList<>();
        Cursor cursor = dataSource.getUserNote(idNote);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                User user = new User();
                user.setId(Long.parseLong(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(3));
                user.setAvatar(cursor.getString(2));
                listUserNote.add(user);
            }
        }
        return listUserNote;
    }

    private List<CheckBoxContentNote> getListCheckBox(String data) {
        List<CheckBoxContentNote> list = new ArrayList<>();
        String[] arr = data.split("\n");
        for (String s : arr) {
            boolean isId = false;
            String contentCheckBox = s;
            if (s.startsWith("!!$")) {
                isId = true;
                contentCheckBox = s.substring(3);
            }
            list.add(new CheckBoxContentNote(contentCheckBox, isId));
        }
        return list;
    }

    private ArrayList<String> getListImages(long idNote) {
        ArrayList<String> list = new ArrayList<>();
        if (idNote != 0){
            Cursor cursor = dataSource.readNoteImage(idNote);
            if(cursor.getCount() != 0 ){
                while (cursor.moveToNext()) {
                    list.add("file://" + cursor.getString(1));
                }
            }
        }

        return list;
    }

    private boolean getRecorder(long idNote) {
        boolean isRecorder = false;

        Cursor cursor = dataSource.getFileNote(idNote);
        if(cursor.getCount() != 0 ){
            isRecorder = true;
        }

        return isRecorder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, textTimeHome;
        ImageView imageView, iconRecorderHome;
        RecyclerView mainImagesNoteHome, mainCheckboxNoteHome, mainCategoriesNoteHome, mainUserNoteHome;
        RoundedImageView colorBackgroundImagedHome, imageBackgroundHome;
        LinearLayout layoutTextTimeHome;

        Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder = ButterKnife.bind(this, itemView);
            title = itemView.findViewById(R.id.title_note_home);
            content = itemView.findViewById(R.id.content_note_home);
            imageView = itemView.findViewById(R.id.layout_item);
            mainImagesNoteHome = itemView.findViewById(R.id.main_images_note_home);
            colorBackgroundImagedHome = itemView.findViewById(R.id.color_background_image_home);
            imageBackgroundHome = itemView.findViewById(R.id.image_background_home);
            mainCheckboxNoteHome = itemView.findViewById(R.id.main_checkbox_note_home);
            mainCategoriesNoteHome = itemView.findViewById(R.id.main_categories_note_home);
            iconRecorderHome = itemView.findViewById(R.id.icon_recorder_home);
            textTimeHome = itemView.findViewById(R.id.text_time_home);
            layoutTextTimeHome = itemView.findViewById(R.id.layout_text_time_home);
            mainUserNoteHome = itemView.findViewById(R.id.main_user_note_home);
        }
    }
}