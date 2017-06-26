package com.nucleuslife.restaurantreview.views;


import android.view.MotionEvent;
import android.view.View;

import com.nucleuslife.restaurantreview.utils.ViewUtil;

public class TouchListener implements View.OnTouchListener
{
    private TouchInterface touchInterface;

    public TouchListener()
    {
        this.touchInterface = new ScaleDownTouchListener();
    }

    public TouchListener(TouchInterface touchInterface)
    {
        if (touchInterface == null) {
            touchInterface = new ScaleDownTouchListener();
        }

        this.touchInterface = touchInterface;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        final int x = (int) motionEvent.getX();
        final int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.touchInterface.onTouchIn(view);
                break;

            case MotionEvent.ACTION_UP:
                this.touchInterface.onTouchOut(view);
                break;

            case MotionEvent.ACTION_MOVE:
                boolean isOutsideView = ViewUtil.isOutsideView(view, x, y);
                if (!isOutsideView) {
                    this.touchInterface.onTouchIn(view);
                } else {
                    this.touchInterface.onTouchOut(view);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                this.touchInterface.onTouchOut(view);
                break;
        }

        return false;
    }
}
