package com.nam.keep.ui.note.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.ui.note.helper.IClickRecorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class RecyclerRecorderNoteAdapter extends RecyclerView.Adapter<RecyclerRecorderNoteAdapter.PhotoNoteViewHolder>{

    Context context;
    private final ArrayList<String> list;
    private final IClickRecorder iClickRecorder;

    private MediaPlayer mediaPlayer;
    private Handler handler;

    public RecyclerRecorderNoteAdapter(Context context, ArrayList<String> list, IClickRecorder iClickRecorder) {
        this.context = context;
        this.list = list;
        this.iClickRecorder = iClickRecorder;
    }

    @NonNull
    @Override
    public PhotoNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recorder_note, parent, false);

        handler = new Handler();

        return new PhotoNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoNoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int totalTime = getAudioTotalTime(position);
        String totalTimeFormatted = formatTime(totalTime);
        holder.titleTime.setText(totalTimeFormatted);
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    holder.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    mediaPlayer.stop();
                } else  {
                    holder.btnPlay.setImageResource(R.drawable.ic_outline_stop_circle_24);
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(list.get(position)));
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            holder.seekBar.setMax(mediaPlayer.getDuration());
                            mediaPlayer.start();
                            updateSeekBar(holder.seekBar);
                        }
                    });
                }
            }
        });
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    holder.seekBar.setProgress(i);
                }
                if (i == seekBar.getMax()) {
                    holder.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                }
                if (!mediaPlayer.isPlaying()) {
                    holder.seekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() == seekBar.getMax()) {
                    holder.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickRecorder.onClickDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        int hours = milliseconds / (1000 * 60 * 60);

        String timeString;
        if (hours > 0) {
            timeString = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        return timeString;
    }

    private int getAudioTotalTime(int position) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.parse(list.get(position)));
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Integer.parseInt(duration);
    }

    private void updateSeekBar(SeekBar seekBar) {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar(seekBar);
                }
            };
            handler.postDelayed(runnable, 100);
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static class PhotoNoteViewHolder extends RecyclerView.ViewHolder {
        ImageView btnPlay, btnDelete;
        SeekBar seekBar;
        TextView titleTime;
        public PhotoNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlay = itemView.findViewById(R.id.play_recorder);
            btnDelete = itemView.findViewById(R.id.delete_recorder);
            seekBar = itemView.findViewById(R.id.seekBar);
            titleTime = itemView.findViewById(R.id.text_recorder_note);
        }
    }
}
