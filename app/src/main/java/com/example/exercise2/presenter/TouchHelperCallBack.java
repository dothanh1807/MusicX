package com.example.exercise2.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.exercise2.R;
import com.example.exercise2.listner.TouchItemListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class TouchHelperCallBack extends ItemTouchHelper.SimpleCallback  {

    // Method khởi tạo các button, đc implement ở Activity/Fragment
    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> listUnderlayButtons);

    private final int BUTTON_WIDTH = 150;

    private GestureDetector.SimpleOnGestureListener mOnGestureListener;

    private View.OnTouchListener mOnTouchListener;

    private TouchItemListener mTouchItemListener;

    private RecyclerView mRecyclerView;

    private List<UnderlayButton> mListButtons;

    private GestureDetector mGestureDetector;

    private int mSwipedPosition = -1;

    private float swipeThreshold = 0.5f;

    private Map<Integer, List<UnderlayButton>> mButtonsBuffer;

    private Queue<Integer> mRecoverQueue;

    private boolean mIsMove;

    public TouchHelperCallBack(Context context, RecyclerView recyclerView) {
        /**
         * ItemTouchHelper.SimpleCallback là wrapper của ItemTouchHelper.CallBack .
         * Method super(...) này là thay thế cho getMovementFlags(...) của ItemTouchHelper.CallBack
         * --> cần truyền vào các Flags --> k phải Override getMovementFlags(...) nữa.
         */
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
        instantiateListener();
        mListButtons = new ArrayList<>();
        mButtonsBuffer = new HashMap<>();
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
        mRecoverQueue = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
            }
        };
        mRecyclerView = recyclerView;
        mTouchItemListener = (TouchItemListener) mRecyclerView.getAdapter();
        mRecyclerView.setOnTouchListener(mOnTouchListener);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mTouchItemListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return mIsMove = true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        if (mSwipedPosition != position){
            mRecoverQueue.add(mSwipedPosition);
        }

        mSwipedPosition = position;

        if (mButtonsBuffer.containsKey(mSwipedPosition)){
            mListButtons = mButtonsBuffer.get(mSwipedPosition);
        }
        else{
            mListButtons.clear();
        }


        mButtonsBuffer.clear();
        swipeThreshold = 0.5f * mListButtons.size() * BUTTON_WIDTH;
        recoverSwipedItem();
    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType,
                                     float animateDx, float animateDy) {
        if( mIsMove ){
            mTouchItemListener.onMoveCompleted();
            mIsMove = false;
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int position = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (position < 0){
            mSwipedPosition = position;
            return;
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            /**
             * dX > 0 là điều kiện swiped sang phải, dX < 0 tương ứng.
             */
            if(dX > 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!mButtonsBuffer.containsKey(position)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    mButtonsBuffer.put(position, buffer);
                }
                else {
                    buffer = mButtonsBuffer.get(position);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
                drawButtons(c, itemView, buffer, position, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem(){
        while ( ! mRecoverQueue.isEmpty()){
            int position = mRecoverQueue.poll();
            if (position > -1) {
                mRecyclerView.getAdapter().notifyItemChanged(position);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int position, float dX){
        for (UnderlayButton button : buffer) {
            /**
             * Ở đây sẽ khởi tạo khoảng trống để vẽ khi swiped sẽ hiện ra, sau đó vẽ icon bitmap trên nền này.
             * Khoảng này cũng sẽ được lấy làm khoảng click button luôn.
             * Khoảng này phải phụ thuộc với itemView để lấy ra tọa độ vẽ đúng cho từng item khi swiped.
             */
            RectF rect = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + BUTTON_WIDTH, itemView.getBottom());
            button.onDraw(c, rect , position);
        }
    }

    private void instantiateListener(){
        mOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                for (UnderlayButton button : mListButtons){
                    if(button.onClick(e.getX(), e.getY()))
                        break;
                }
                return true;
            }
        };
        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if (mSwipedPosition < 0){
                    return false;
                }
                Point point = new Point((int) e.getRawX(), (int) e.getRawY());

                RecyclerView.ViewHolder swipedViewHolder = mRecyclerView.findViewHolderForAdapterPosition(mSwipedPosition);
                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);

                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y)
                        mGestureDetector.onTouchEvent(e);
                    else {
                        mRecoverQueue.add(mSwipedPosition);
                        mSwipedPosition = -1;
                        recoverSwipedItem();
                    }
                }
                return false;
            }
        };
    }

    public static class UnderlayButton {

        private final int SIZE_ICON = 80;

        private Context mContext;

        private int mImage;

        private int mPosition;

        private RectF mClickRegion;

        private UnderlayButtonClickListener mUnderlayButtonClickListener;

        public UnderlayButton(int image, UnderlayButtonClickListener underlayButtonClickListener, Context context) {
            mImage = image;
            mUnderlayButtonClickListener = underlayButtonClickListener;
            mContext = context;
        }

        public boolean onClick(float x, float y){
            if (mClickRegion != null && mClickRegion.contains(x, y)){
                mUnderlayButtonClickListener.onClickUnderlayButton(mPosition);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int position){
            Paint paint = new Paint();

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mImage);
            bitmap = Bitmap.createScaledBitmap(bitmap, SIZE_ICON, SIZE_ICON, true);
            paint.setColor(Color.WHITE);

            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;

            paint.setColorFilter(new PorterDuffColorFilter(mContext.getResources().getColor(R.color.hightLight), PorterDuff.Mode.SRC_ATOP));
            c.drawBitmap(bitmap, rect.left + x - 30, rect.top + y - 40, paint);
            /**
             * Gán khoảng hình = rect có thể click, ở đây chỉ có 1 button thì gán thế này, nếu có nhiều
             * button phải tính để chia, k bằng cả hình rect này đc.
             */
            mClickRegion = rect;
            mPosition = position;
        }
    }

    public interface UnderlayButtonClickListener {
        void onClickUnderlayButton(int position);
    }

}
