package com.example.appmaternal.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressView extends View {

    private Paint paint;
    private int progress;

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 20;

        // Dibuja el círculo completo
        paint.setAlpha(50);
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Dibuja el arco de progreso
        paint.setAlpha(255);
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, -90, 360 * progress / 100, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
