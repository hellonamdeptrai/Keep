package com.nam.keep.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.model.CheckBoxContentNote;
import com.nam.keep.ui.home.helper.ItemTouchHelperAdapter;
import com.nam.keep.ui.home.helper.OnStartDangListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecyclerCheckBoxNoteAdapter extends RecyclerView.Adapter<RecyclerCheckBoxNoteAdapter.CheckBoxNoteViewHolder> implements ItemTouchHelperAdapter {

    private List<CheckBoxContentNote> list;
    private IClickDeleteCheckBox iClickDeleteCheckBox;
    private ITextWatcherCheckBox iTextWatcherCheckBox;
    private IClickChecked iClickChecked;
    OnStartDangListener listener;

    public RecyclerCheckBoxNoteAdapter(List<CheckBoxContentNote> list,
                                       IClickDeleteCheckBox iClickDeleteCheckBox,
                                       ITextWatcherCheckBox iTextWatcherCheckBox,
                                       OnStartDangListener listener,
                                       IClickChecked iClickChecked) {
        this.list = list;
        this.iClickDeleteCheckBox = iClickDeleteCheckBox;
        this.iTextWatcherCheckBox = iTextWatcherCheckBox;
        this.listener =listener;
        this.iClickChecked = iClickChecked;
    }

    @NonNull
    @Override
    public CheckBoxNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckBoxNoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxNoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CheckBoxContentNote checkBoxItem = list.get(position);

        holder.checkBox.setChecked(checkBoxItem.isCheckBox());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iClickChecked.ClickChecked(position, holder.editText);
            }
        });
        holder.editText.setText(checkBoxItem.getContent());
        holder.editText.setEnabled(!checkBoxItem.isCheckBox());
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                iTextWatcherCheckBox.TextWatcherItem(position, holder.editText.getText().toString());
            }
        });
        holder.mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickDeleteCheckBox.onClickDeleteItem(position);
            }
        });
        holder.mDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    listener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void inItemDismiss(int position) {
        notifyItemRemoved(position);
    }

    public class CheckBoxNoteViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        EditText editText;
        ImageButton mDeleteItem, mDragItem;
        EditText mEditTextCheckBox;
        LinearLayout linearLayout;
        Unbinder unbinder;

        public CheckBoxNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            checkBox = itemView.findViewById(R.id.check_box_note);
            editText = itemView.findViewById(R.id.edit_text_check_box);
            mDeleteItem = itemView.findViewById(R.id.image_button_delete_item);
            mEditTextCheckBox = itemView.findViewById(R.id.edit_text_check_box);
            mDragItem = itemView.findViewById(R.id.image_button_drag);
            linearLayout = itemView.findViewById(R.id.main_check_box_item);
        }
    }
}
