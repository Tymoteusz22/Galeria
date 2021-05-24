package com.example.galeria.Functions_and_Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeListener implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private final GestureDetector gestureDetector;

    public OnSwipeListener(Context context){
        gestureDetector = new GestureDetector(context,this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //used
    @Override
    public void onLongPress(MotionEvent e) {
        onLongClick();
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX)>1000){
            if (e1.getX()>e2.getX()){
                onSwipeRightLeft();
            } else {
                onSwipeLeftRight();
            }
        }
        return false;
    }


    //not used
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }



    public void onSwipeLeftRight(){}
    public void onSwipeRightLeft(){}
    public void onLongClick(){}
}
