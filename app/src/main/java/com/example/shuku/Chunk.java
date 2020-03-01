package com.example.shuku;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class Chunk extends EditText{
    public Chunk(Context context) {
        super(context);
    }

    public Chunk(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chunk(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Chunk(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width= MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, height);
    }
}
