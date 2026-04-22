package com.osaid.scamguard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class CharCountRing extends View {

    private Paint trackPaint;
    private Paint fillPaint;
    private Paint textPaint;
    private RectF arcRect;

    private int maxChars = 500;
    private int currentChars = 0;
    private float animatedAngle = 0f;

    public CharCountRing(Context context) {
        super(context);
        init();
    }

    public CharCountRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Track ring (background circle)
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(4f);
        trackPaint.setColor(ContextCompat.getColor(getContext(), R.color.outline));
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        // Fill arc (progress)
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(4f);
        fillPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
        fillPaint.setStrokeCap(Paint.Cap.ROUND);

        // Centre text
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(24f);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_muted));

        arcRect = new RectF();
    }

    public void setCharCount(int count) {
        int previousChars = currentChars;
        currentChars = count;

        // Change colour when approaching limit
        if (count > maxChars * 0.9) {
            fillPaint.setColor(ContextCompat.getColor(getContext(), R.color.risk_high));
            textPaint.setColor(ContextCompat.getColor(getContext(), R.color.risk_high));
        } else if (count > maxChars * 0.7) {
            fillPaint.setColor(ContextCompat.getColor(getContext(), R.color.risk_medium));
            textPaint.setColor(ContextCompat.getColor(getContext(), R.color.risk_medium));
        } else if (count > 0) {
            fillPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
            textPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_secondary));
        } else {
            fillPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
            textPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_muted));
        }

        // Animate the arc smoothly from previous to current
        float fromAngle = (previousChars / (float) maxChars) * 360f;
        float toAngle = (currentChars / (float) maxChars) * 360f;

        android.animation.ValueAnimator animator =
                android.animation.ValueAnimator.ofFloat(fromAngle, Math.min(toAngle, 360f));
        animator.setDuration(400);
        animator.setInterpolator(new android.view.animation.DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animatedAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public void setMaxChars(int max) {
        maxChars = max;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float padding = 6f;
        arcRect.set(padding, padding, getWidth() - padding, getHeight() - padding);

        // Draw the track circle
        canvas.drawArc(arcRect, 0, 360, false, trackPaint);

        // Draw the fill arc based on character progress
        canvas.drawArc(arcRect, -90, animatedAngle, false, fillPaint);

        // Draw the count text in the centre
        String text = String.valueOf(currentChars);
        float textY = (getHeight() / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);
        canvas.drawText(text, getWidth() / 2f, textY, textPaint);
    }
}
