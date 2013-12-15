package it.falcon.massivefilerenamer.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDetector implements View.OnTouchListener {

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final String logTag = "SwipeDetector";
    private static final int SWIPE_MIN_DISTANCE = 100;//pixel
	private static final int SWIPE_MAX_OFF_PATH = 300;//pixel
//	private static final int SWIPE_THRESHOLD_VELOCITY = 10;//pixels per second
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }
    
    public boolean leftToRightSwipeDetected() {
        return mSwipeDetected == Action.LR;
    }
    
    public boolean rightToLeftSwipeDetected() {
        return mSwipeDetected == Action.RL;
    }

    public Action getAction() {
        return mSwipeDetected;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                return false; // allow other events like Click to be processed
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float dX = downX - upX;
                float dY = downY - upY;

                // horizontal swipe detection
                if (Math.abs(dY) < SWIPE_MAX_OFF_PATH  
    					&& Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
                    // left or right
                    if (dX < 0) {
                        Log.i(logTag, "Swipe Left to Right");
                        mSwipeDetected = Action.LR;
                        return true;
                    }
                    if (dX > 0) {
                        Log.i(logTag, "Swipe Right to Left");
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                } else 

                // vertical swipe detection
                if (Math.abs(dX) < SWIPE_MAX_OFF_PATH 
    					&& Math.abs(dY) >= SWIPE_MIN_DISTANCE) {
                    // top or down
                    if (dY < 0) {
                        Log.i(logTag, "Swipe Top to Bottom");
                        mSwipeDetected = Action.TB;
                        return false;
                    }
                    if (dY > 0) {
                        Log.i(logTag, "Swipe Bottom to Top");
                        mSwipeDetected = Action.BT;
                        return false;
                    }
                } 
                return true;
            }
        }
        return false;
    }
}