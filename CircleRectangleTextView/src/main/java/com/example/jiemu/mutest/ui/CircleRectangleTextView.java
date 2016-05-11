package com.example.jiemu.mutest.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.jiemu.mutest.R;

import java.util.Random;

/**
 * 自定义左右圆角矩形textview
 *
 * @author: jie.mu
 */
public class CircleRectangleTextView extends TextView implements View.OnClickListener {

    /**
     * 边框颜色
     **/
    private int strokeColor;
    /**
     * 填充颜色
     **/
    private int solidColor;
    /**
     * 整体宽度（加上画笔宽度）
     **/
    private int width;
    /**
     * 整体高度（加上画笔宽度）
     **/
    private int height;
    /**
     * 画笔
     **/
    private Paint paint;
    /**
     * 画笔宽度
     **/
    private int strokeWidth = 0;
    private boolean isFirst = true;
    private String randomContent;

    public CircleRectangleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleRectangleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public CircleRectangleTextView(Context context) {
        super(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        this.setOnClickListener(this);
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleRectangleTextView);
        strokeColor = array.getColor(R.styleable.CircleRectangleTextView_crt_strokeColor, Color.BLACK);
        solidColor = array.getColor(R.styleable.CircleRectangleTextView_crt_solidColor, 0x00ffffff);
        strokeWidth = array.getInt(R.styleable.CircleRectangleTextView_crt_strokeWidth, (int) paint.getStrokeWidth());
        if (!TextUtils.isEmpty(randomContent)) {
            this.setText(randomContent);
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst) {
            isFirst = false;
            this.setWidth(getMeasuredWidth() + getMeasuredHeight() + strokeWidth * 2);
            this.setHeight(getMeasuredHeight() + strokeWidth * 2);
        }
    }

    protected void onDraw(Canvas canvas) {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        paint.setColor(0xff000000);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        // 填充内部颜色
        paint.setColor(solidColor);
        paint.setStyle(Paint.Style.FILL);
        // 画矩形
        canvas.drawRect((height - strokeWidth) / 2, strokeWidth / 2, width - (height - strokeWidth) / 2, height
                - strokeWidth / 2, paint);
        // 画左半圆
        RectF leftRectF = new RectF(strokeWidth / 2, strokeWidth / 2, height - strokeWidth / 2, height - strokeWidth
                / 2);
        canvas.drawArc(leftRectF, 90, 180, false, paint);
        // 画右半圆
        leftRectF.set(width - height - strokeWidth / 2, strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth
                / 2);
        canvas.drawArc(leftRectF, -90, 180, false, paint);

        // 画边框
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        // 画上直线
        canvas.drawLine(height / 2 - strokeWidth, strokeWidth / 2, width - height / 2 + strokeWidth, strokeWidth / 2,
                paint);
        // 画下直线
        canvas.drawLine(height / 2 - strokeWidth, height - strokeWidth * 3 / 4, width - height / 2 + strokeWidth,
                height - strokeWidth * 3 / 4, paint);
        // 画左半圆
        RectF leftRectF2 = new RectF(strokeWidth / 2, strokeWidth / 2, height - strokeWidth * 3 / 4, height
                - strokeWidth * 3 / 4);
        canvas.drawArc(leftRectF2, 90, 180, false, paint);
        // 画右半圆
        leftRectF2.set(width - height + strokeWidth * 3 / 4, strokeWidth / 2, width - strokeWidth * 3 / 4, height
                - strokeWidth * 3 / 4);
        canvas.drawArc(leftRectF2, -90, 180, false, paint);
        if (!TextUtils.isEmpty(randomContent)) {
            this.setText(randomContent);
        }
        super.onDraw(canvas);
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getSolidColor() {
        return solidColor;
    }

    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
    }

    @Override
    public void onClick(View v) {
        onRandom();
    }

    /**
     * 对外部暴漏的随机方法
     */
    public void onRandom() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int[] randomArray = new int[4];
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        randomContent = sb.toString();
        postInvalidate();
    }

    /**
     * 随机背景颜色
     */
    public void onRandomBgColor() {
        solidColor = Color.parseColor("#" + Integer.toHexString(-getRandomColor()));
        postInvalidate();
    }

    /**
     * 随机数 min~max 不包含最大值，包含最小值
     *
     * @param max 最大值
     * @param min 最小值
     * @return
     */
    public int onRandomNumber(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     * 获取随机颜色
     *
     * @return
     */
    private int getRandomColor() {
        //白色16进制的字符串
        String whiteHexStr = "ffffffff";
        //黑色16进制的字符串
        String blackHexStr = "ff000000";

        //把16进制数转成long型
        int whiteHexInt = Math.abs((int) Long.parseLong(whiteHexStr, 16));
        int blackHexInt = Math.abs((int) Long.parseLong(blackHexStr, 16));
        int maxInt, minInt;

        if (whiteHexInt >= blackHexInt) {
            maxInt = whiteHexInt;
            minInt = blackHexInt;
        } else {
            maxInt = blackHexInt;
            minInt = whiteHexInt;
        }

        //获取随机数：minInt~maxInt
        return onRandomNumber(maxInt, minInt);
    }
}
