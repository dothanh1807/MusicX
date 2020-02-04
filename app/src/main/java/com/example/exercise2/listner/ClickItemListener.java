package com.example.exercise2.listner;

import android.view.View;

public interface ClickItemListener {

    void onClick(View view, int positon);

    void onLongClick(View view, int positon);
}
