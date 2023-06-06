package com.nam.keep.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.nam.keep.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationHelper {
    public static final String CHANNEL_ID = "my_channel_id";
    public static final CharSequence CHANNEL_NAME = "My Channel";
    public static final String CHANNEL_DESCRIPTION = "My Channel Description";
    public static final String WORK_TAG = "notification_work";
    public static final int PERMISSION_REQUEST_CODE = 1001;

    public static void showNotification(Context context, ArrayList<Note> note) {
        createNotificationChannel(context);

        // Lặp qua từng LocalDateTime trong danh sách và lên lịch công việc
        for (Note noteItem : note) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            if (!noteItem.getDeadline().isEmpty()) {
                try {
                    Date date = sdf.parse(noteItem.getDeadline());
                    long delayMillis = date.getTime() - System.currentTimeMillis();

                    if (delayMillis > 0) {
                        Data inputData = new Data.Builder()
                                .putString("title", noteItem.getTitle())
                                .putString("message", noteItem.getContent())
                                .putInt("notificationId", generateNotificationId()) // Truyền notificationId từ công việc
                                .build();

                        OneTimeWorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                                .setInputData(inputData)
                                .build();
                        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int generateNotificationId() {
        return new Random().nextInt();
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
