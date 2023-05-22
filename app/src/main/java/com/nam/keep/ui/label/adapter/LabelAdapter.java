package com.nam.keep.ui.label.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nam.keep.R;
import com.nam.keep.model.Label;
import com.nam.keep.ui.label.helper.ILabelClick;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelViewHolder> {

    private List<Label> list;
    private ILabelClick iLabelClick;

    public LabelAdapter(List<Label> list, ILabelClick iLabelClick) {
        this.list = list;
        this.iLabelClick = iLabelClick;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label, parent, false);
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Label labelItem = list.get(position);

        holder.textView.setText(labelItem.getTitle());
        holder.editText.setText(labelItem.getTitle());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLabelClick.OnClickFilterNote(position);
            }
        });
        holder.editLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.actionLabel.setVisibility(View.GONE);
                holder.editDoneLabel.setVisibility(View.VISIBLE);

                holder.textView.setVisibility(View.GONE);
                holder.editText.setVisibility(View.VISIBLE);
            }
        });
        holder.editDoneLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(holder.editText.getText().toString().trim())) {
                    holder.editText.setError("Tên nhãn không được để trống!");
                    return;
                }
                iLabelClick.OnClickEditDone(position, holder.editText);

                holder.actionLabel.setVisibility(View.VISIBLE);
                holder.editDoneLabel.setVisibility(View.GONE);
                holder.textView.setVisibility(View.VISIBLE);
                holder.editText.setVisibility(View.GONE);
            }
        });
        holder.deleteLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLabelClick.OnClickDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        TextView textView;
        LinearLayout actionLabel;
        ImageButton editLabel, editDoneLabel, deleteLabel;
        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_label);
            editText = itemView.findViewById(R.id.edit_text_label);
            editLabel = itemView.findViewById(R.id.image_button_edit_label);
            editDoneLabel = itemView.findViewById(R.id.image_button_edit_done_label);
            deleteLabel = itemView.findViewById(R.id.image_button_delete_label);
            actionLabel = itemView.findViewById(R.id.action_label);
        }
    }
}
