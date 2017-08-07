package com.nucleuslife.restaurantreview.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nucleuslife.restaurantreview.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class CircleImageView extends ImageView
{
    protected String imageUrl;
    protected int borderColor;
    protected int borderSize;


    public CircleImageView(Context context)
    {
        super(context);
        this.init(null);
    }

    public CircleImageView(Context context, String imageUrl)
    {
        super(context);
        this.init(null);
        this.setImageUrl(imageUrl);
    }

    public CircleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        this.borderColor = typedArray.getColor(R.styleable.CircleImageView_borderColor, Color.TRANSPARENT);
        this.borderSize = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_borderSize, 0);
        typedArray.recycle();
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
        Glide.with(getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(this);
    }

    public void setImageResource(int resourceId)
    {
        Glide.with(getContext()).load(resourceId).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(this);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.drawBorder(canvas);
    }

    private void drawBorder(Canvas canvas)
    {
        if (this.getVisibility() != VISIBLE || this.borderSize <= 0 || this.borderColor == Color.TRANSPARENT) {
            return;
        }
        Paint paintBorder = new Paint();
        paintBorder.setColor(this.borderColor);
        paintBorder.setAntiAlias(true);
        paintBorder.setFilterBitmap(true);
        paintBorder.setDither(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(this.borderSize);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, ((canvas.getWidth() - borderSize*.7f) / 2), paintBorder);
    }
}
