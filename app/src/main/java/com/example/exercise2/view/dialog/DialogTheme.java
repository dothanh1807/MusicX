package com.example.exercise2.view.dialog;

import android.widget.TextView;

public class DialogTheme extends BaseDialog {

    public static DialogTheme newInstance() {
        DialogTheme dialogTheme = new DialogTheme();

        return dialogTheme;
    }

    @Override
    public void setTittle(TextView tvTittle) {
        tvTittle.setText("Theme");
    }

}
