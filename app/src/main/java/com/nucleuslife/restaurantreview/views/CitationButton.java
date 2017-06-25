package com.nucleuslife.restaurantreview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.nucleuslife.restaurantreview.R;

public class CitationButton extends android.support.v7.widget.AppCompatButton
{

    public CitationButton(Context context)
    {
        super(context);

        this.init(null);
    }

    public CitationButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(attrs);

    }

    public CitationButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs)
    {

        this.setOnTouchListener(new CitationTouchListener());
        this.setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
