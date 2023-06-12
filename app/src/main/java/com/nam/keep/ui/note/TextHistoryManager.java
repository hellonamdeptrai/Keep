package com.nam.keep.ui.note;

import java.util.ArrayList;
import java.util.List;

public class TextHistoryManager {
    private List<String> history;
    private int currentIndex;

    public TextHistoryManager() {
        history = new ArrayList<>();
        currentIndex = -1;
    }

    public void addToHistory(String text) {
        // Xóa bỏ tất cả các phiên bản sau đó
        while (history.size() - 1 > currentIndex) {
            history.remove(history.size() - 1);
        }

        history.add(text);
        currentIndex++;
    }

    public String undo() {
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex);
        }
        return null;
    }

    public String redo() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        }
        return null;
    }
}
