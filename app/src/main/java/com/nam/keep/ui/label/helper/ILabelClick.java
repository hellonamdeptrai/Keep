package com.nam.keep.ui.label.helper;

import android.view.View;
import android.widget.EditText;

public interface ILabelClick {
    void OnClickFilterNote(int position);
    void OnClickEditDone(int position, EditText editText);
    void OnClickDelete(int position);
}
