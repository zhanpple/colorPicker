package com.zmp.widget.view;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zmp on 2018/4/16.
 */

public class VerticalProgressView extends View {

        private LinearGradient linearGradient;

        private int measuredHeight;

        private int[] oc2;

        private Paint paint;

        private float y1;

        private GestureDetector mGestureDetector;

        private float mMax;

        public int getProgress() {
                return progress;
        }

        private int progress = 100;

        private Paint bgPaint;

        private int paddingLeft;

        private int strokeWidth;

        private int mStartY;

        private int mEndY;

        public VerticalProgressView(Context context) {
                this(context, null);
        }

        public VerticalProgressView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public VerticalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                int measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                paddingLeft = getPaddingLeft();
                int paddingRight = getPaddingRight();
                int paddingTop = getPaddingTop();
                int paddingBottom = getPaddingBottom();
                strokeWidth = measuredWidth - paddingLeft - paddingRight;
                paint.setStrokeWidth(strokeWidth);
                bgPaint.setStrokeWidth(strokeWidth);
                mMax = measuredHeight - paddingTop - paddingBottom;
                mStartY = measuredHeight - paddingBottom;
                mEndY = getPaddingTop();
                y1 =  getPaddingTop();
                linearGradient = new LinearGradient(0, measuredHeight, 0, y1,
                                                    oc2, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                setProgress(progress);
        }

        private void init() {
                mGestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());
                oc2 = new int[]{0xFF634E79, 0xFFF7F4FE};
                paint = new Paint();
                bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                bgPaint.setColor(Color.RED);
        }

        public void setProgress(MotionEvent event) {
                y1 = event.getY();
                if (y1 < mEndY) {
                        y1 = mEndY;
                }
                if (y1 > mStartY) {
                        y1 = mStartY;
                }
                linearGradient = new LinearGradient(0, mStartY, 0, y1,
                                                    oc2, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                invalidate();
        }

        public void setProgress(int progress) {
                if (progress > 100) {
                        progress = 100;
                }
                if (progress < 0) {
                        progress = 0;
                }
                this.progress = progress;
                if (measuredHeight == 0) {
                        return;
                }
                y1 = (100 - progress) / 100F * mMax + mEndY;
                linearGradient = new LinearGradient(0, mStartY, 0, y1,
                                                    oc2, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.drawLine(paddingLeft + strokeWidth / 2, mStartY, paddingLeft + strokeWidth / 2, mEndY, bgPaint);
                canvas.drawLine(paddingLeft + strokeWidth / 2, mStartY, paddingLeft + strokeWidth / 2, y1, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                                if (voiceProgress != null) {
                                        voiceProgress.onChoose(100 - (int) (y1 * 100 / measuredHeight));
                                }
                                performClick();
                                break;
                }
                return true;
        }

        @Override
        public boolean performClick() {
                return super.performClick();
        }

        public void setIVoiceProgress(VerticalProgressView.IVoiceProgress voiceProgress) {
                this.voiceProgress = voiceProgress;
        }

        private IVoiceProgress voiceProgress;

        public interface IVoiceProgress {

                void onChoose(int progress);
        }

        class MyOnGestureListener implements GestureDetector.OnGestureListener {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                        Log.i(getClass().getName(), "onLongPress-----");
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        Log.i(getClass().getName(),
                              "onScroll-----");
                        setProgress(e2);
                        if (voiceProgress != null) {
                                voiceProgress.onChoose(100 - (int) ((y1 - mEndY) * 100 / mMax));
                        }
                        return false;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Log.i(getClass().getName(),
                              "onFling-----");
                        return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                        Log.i(getClass().getName(), "onShowPress-----");
                }

                @Override
                public boolean onDown(MotionEvent e) {
                        Log.i(getClass().getName(), "onDown-----");
                        setProgress(e);
                        if (voiceProgress != null) {
                                voiceProgress.onChoose(100 - (int) ((y1 - mEndY) * 100 / mMax));
                        }
                        return false;
                }
        }
}
