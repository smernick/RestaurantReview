package com.nucleuslife.restaurantreview.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.R;

public class TopViewClass extends LinearLayout
{
    private ImageView leftImageView;
    private TextView textView;
    private ImageView rightImageView;

    //region CONSTRUCTOR
    public TopViewClass(Context context)
    {
        super(context);

        this.init();
    }

    public TopViewClass(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.init();
    }

    public TopViewClass(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.init();
    }
    //endregion

    private void init()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.top_view_layout, this, true);

        this.leftImageView = (ImageView) view.findViewById(R.id.start_image_view);
        this.textView = (TextView) view.findViewById(R.id.screen_title_text_view);
        this.rightImageView = (ImageView) view.findViewById(R.id.end_image_view);
    }

    public void setLeftImageView(Drawable leftImageView)
    {
        this.leftImageView.setBackground(leftImageView);
    }

    public void setTitleText(String textView)
    {
        this.textView.setText(textView);
    }

    public void setRightImageView(Drawable leftImageView)
    {
        this.rightImageView.setBackground(leftImageView);
    }

    public ImageView getRightImageView()
    {
        return rightImageView;
    }

    public ImageView getLeftImageView()
    {
        return leftImageView;
    }
}
