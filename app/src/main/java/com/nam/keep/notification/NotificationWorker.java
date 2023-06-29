package com.nam.keep.notification;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.nam.keep.R;
import com.nam.keep.ui.note.AddNoteActivity;
import com.nam.keep.ui.note.EditNoteActivity;

public class NotificationWorker extends Worker {
    private static final String CHANNEL_ID = "my_channel_id";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        long idNote = getInputData().getLong("id", 0);
        String title = getInputData().getString("title");
        String message = getInputData().getString("message");
        int notificationId = getInputData().getInt("notificationId", 0); // Lấy notificationId từ inputData

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lightbulb_circle_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class); // Thay YourActivity bằng Activity của bạn
        intent.putExtra(EditNoteActivity.EXTRA_PARAM_ID, idNote);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Gắn Intent với thông báo
        builder.setContentIntent(pendingIntent);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền VIBRATE từ người dùng
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.VIBRATE}, NotificationHelper.PERMISSION_REQUEST_CODE);
        } else {
            // Đã có quyền VIBRATE, hiển thị thông báo
            notificationManager.notify(notificationId, builder.build());
        };

        return Result.success();
    }
}
