package com.nam.keep.api.helper;

import android.os.AsyncTask;

import java.io.IOException;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {

    private AsyncTaskCompleteListener<String> listener;

    public MyAsyncTask(AsyncTaskCompleteListener<String> listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Thực hiện các tác vụ bất đồng bộ ở đây
        // Ví dụ: Tải dữ liệu từ internet
        String result = "";

        // Simulate long-running task
        try {
            listener.onDoInBackground();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result = "Data downloaded successfully!";

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Gửi kết quả tới class gọi AsyncTask
        if (listener != null) {
            listener.onTaskComplete(result);
        }
    }
}
