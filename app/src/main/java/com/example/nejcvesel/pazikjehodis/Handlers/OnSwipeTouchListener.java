package com.example.nejcvesel.pazikjehodis.Handlers;

import android.animation.Animator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {
    float dX;
    private int positionX = 0;
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = v.getX() - event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                v.animate()
                        .x(event.getRawX() + dX)
                        .setDuration(0)
                        .start();
                float a = (v.getWidth() - Math.abs(v.getX())) / v.getWidth();
                v.setAlpha(a);
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(v.getX()) > (v.getWidth() / 2)) {
                    if (v.getX() > 0) {
                        v.animate().x(v.getWidth() + 30).setDuration(10).start();
                    } else {
                        v.animate().x(0 - v.getWidth() - 30).setDuration(10).start();
                    }
                } else {
                    v.animate().x(0).setDuration(100).alpha(1.0f).start();
                }
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
