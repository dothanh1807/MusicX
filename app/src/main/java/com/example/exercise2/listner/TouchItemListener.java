package com.example.exercise2.listner;

public interface TouchItemListener {

    void onMove(int oldPosition, int newPosition);

    void onSwipe(int position, int direction);

    void onMoveCompleted();

}
