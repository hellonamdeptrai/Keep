package com.nam.keep.api.helper;

import java.io.IOException;

public interface AsyncTaskCompleteListener<T> {
    void onDoInBackground() throws IOException;
    void onTaskComplete(T result);
}
