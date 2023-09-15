package com.example.iot_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GaugeView extends View {
    private float value;
    private Paint paint;

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK); // Color for the gauge
        paint.setStrokeWidth(12); // Adjust the width of the gauge line
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        // Draw the gauge arc
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                135, 270, false, paint);

        // Calculate the end point of the gauge based on the value
        float endX = (float) (centerX + radius * Math.cos(Math.toRadians(135 + value * 270)));
        float endY = (float) (centerY + radius * Math.sin(Math.toRadians(135 + value * 270)));

        // Draw a line indicating the value
        canvas.drawLine(centerX, centerY, endX, endY, paint);
    }
}

