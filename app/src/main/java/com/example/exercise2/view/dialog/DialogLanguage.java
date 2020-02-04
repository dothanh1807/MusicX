package com.example.exercise2.view.dialog;

import android.widget.TextView;

public class DialogLanguage extends BaseDialog {

    public static DialogLanguage newInstance() {
        DialogLanguage dialogLanguage = new DialogLanguage();

        return dialogLanguage;
    }

    @Override
    public void setTittle(TextView tvTittle) {
        tvTittle.setText("Language");
    }

}
