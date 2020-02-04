package com.example.exercise2.view.dialog;

import android.widget.TextView;

public class DialogSort extends BaseDialog {

    public static DialogSort newInstance() {
        DialogSort dialogSort = new DialogSort();

        return dialogSort;
    }

    @Override
    public void setTittle(TextView tvTittle) {
        tvTittle.setText("Sort");
    }
}
