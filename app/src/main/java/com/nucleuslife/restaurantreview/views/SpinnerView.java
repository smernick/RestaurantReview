package com.nucleuslife.restaurantreview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.nucleuslife.restaurantreview.R;

public class SpinnerView extends ImageView
{
    //region CONSTRUCTOR
    public SpinnerView(Context context)
    {
        super(context);

        this.init();
    }

    public SpinnerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.init();
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.init();
    }
    //endregion

    private void init()
    {
        this.setSpinningView(R.mipmap.ic_launcher);
    }

    /**
     * You can change the spinning image here
     */
    public void setSpinningView(int resId)
    {
        this.setImageResource(resId);
    }

    public void startAnimation()
    {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(800);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());

        this.setVisibility(VISIBLE);
        this.startAnimation(rotate);
    }

    public void stopAnimation()
    {
        this.clearAnimation();
    }
}