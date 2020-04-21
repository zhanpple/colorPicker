package com.zmp.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleChoiceView extends View {

        private static final String TAG = "CircleProgressView";

        private int measuredWidth;

        private int measuredHeight;

        private Path mPath;

        private Paint mPaint;

        private int mMaxR;

        private float mInR;

        private RectF mRectF;

        private GestureDetector mGestureDetector;

        public CircleChoiceView(Context context) {
                this(context, null);
        }

        public CircleChoiceView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public CircleChoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                mGestureDetector = new GestureDetector(getContext(), onGestureListener);
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPath = new Path();
                mRectF = new RectF();

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                mMaxR = Math.min(measuredWidth, measuredHeight * 2) / 2;
                mInR = mMaxR * 0.8F;
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                drawBg(canvas);
                drawProgress(canvas);
                drawRule(canvas);
        }

        private void drawProgress(Canvas canvas) {
                mPath.reset();
                mPaint.setColor(Color.GREEN);
                float v = (float) (mMaxR * Math.sin(Math.PI * startAngele / 180));
                float v1 = (float) (mMaxR * Math.cos(Math.PI * startAngele / 180));
                mPath.moveTo(measuredWidth / 2 + v1, measuredHeight + v);
                mRectF.left = measuredWidth / 2 - mMaxR;
                mRectF.top = measuredHeight - mMaxR;
                mRectF.right = measuredWidth / 2 + mMaxR;
                mRectF.bottom = measuredHeight + mMaxR;
                mPath.arcTo(mRectF, startAngele, progress * countAngle);
                v = (float) (mInR * Math.sin(Math.PI * (startAngele + progress * countAngle) / 180));
                v1 = (float) (mInR * Math.cos(Math.PI * (startAngele + progress * countAngle) / 180));
                mPath.lineTo(measuredWidth / 2 + v1, measuredHeight + v);
                mRectF.left = measuredWidth / 2 - mInR;
                mRectF.top = measuredHeight - mInR;
                mRectF.right = measuredWidth / 2 + mInR;
                mRectF.bottom = measuredHeight + mInR;
                mPath.arcTo(mRectF, startAngele + progress * countAngle, -progress * countAngle);
                mPath.close();
                mPaint.setColor(Color.GREEN);
                canvas.drawPath(mPath, mPaint);
        }

        private void drawRule(Canvas canvas) {
                mPaint.setColor(Color.BLUE);
                for (int i = 1; i < count; i++) {
                        mPath.reset();
                        float drawAngle = startAngele + i * countAngle;
                        float v = (float) (mMaxR * Math.sin(Math.PI * (drawAngle - ruleAngle / 2) / 180));
                        float v1 = (float) (mMaxR * Math.cos(Math.PI * (drawAngle - ruleAngle / 2) / 180));
                        mPath.moveTo(measuredWidth / 2 + v1, measuredHeight + v);
                        mRectF.left = measuredWidth / 2 - mMaxR;
                        mRectF.top = measuredHeight - mMaxR;
                        mRectF.right = measuredWidth / 2 + mMaxR;
                        mRectF.bottom = measuredHeight + mMaxR;
                        mPath.arcTo(mRectF, drawAngle - ruleAngle / 2, ruleAngle);
                        v = (float) (mInR * Math.sin(Math.PI * ((drawAngle + ruleAngle / 2)) / 180));
                        v1 = (float) (mInR * Math.cos(Math.PI * ((drawAngle + ruleAngle / 2)) / 180));
                        mPath.lineTo(measuredWidth / 2 + v1, measuredHeight + v);
                        mRectF.left = measuredWidth / 2 - mInR;
                        mRectF.top = measuredHeight - mInR;
                        mRectF.right = measuredWidth / 2 + mInR;
                        mRectF.bottom = measuredHeight + mInR;
                        mPath.arcTo(mRectF, drawAngle + ruleAngle / 2, -ruleAngle);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);
                }
                mPath.reset();
                mPath.moveTo(measuredWidth / 2 + mMaxR, measuredHeight);
                mPath.lineTo(measuredWidth / 2 + mMaxR * 0.4F, measuredHeight + mMaxR * 0.1F);
                mPath.lineTo(measuredWidth / 2 + mMaxR * 0.4F, measuredHeight - mMaxR * 0.1F);
                mPath.close();
                mPaint.setColor(Color.CYAN);
                canvas.rotate(startAngele + progress * countAngle, measuredWidth / 2, measuredHeight);
                canvas.drawPath(mPath, mPaint);
        }

        private static final float sweepAngle = 150F;

        private static final int count = 8;

        private static final float countAngle = sweepAngle / count;

        private static final float startAngele = -90 - sweepAngle / 2;

        private static final float endAngle = -90 + sweepAngle / 2;

        private static int progress = 2;

        private static final float ruleAngle = 0.5F;

        private void drawBg(Canvas canvas) {
                mPath.reset();
                mPaint.setColor(Color.RED);
                float v = (float) (mMaxR * Math.sin(Math.PI * startAngele / 180));
                float v1 = (float) (mMaxR * Math.cos(Math.PI * startAngele / 180));
                mPath.moveTo(measuredWidth / 2 + v1, measuredHeight + v);
                mRectF.left = measuredWidth / 2 - mMaxR;
                mRectF.top = measuredHeight - mMaxR;
                mRectF.right = measuredWidth / 2 + mMaxR;
                mRectF.bottom = measuredHeight + mMaxR;
                mPath.arcTo(mRectF, startAngele, sweepAngle);
                v = (float) (mInR * Math.sin(Math.PI * endAngle / 180));
                v1 = (float) (mInR * Math.cos(Math.PI * endAngle / 180));
                mPath.lineTo(measuredWidth / 2 + v1, measuredHeight + v);
                mRectF.left = measuredWidth / 2 - mInR;
                mRectF.top = measuredHeight - mInR;
                mRectF.right = measuredWidth / 2 + mInR;
                mRectF.bottom = measuredHeight + mInR;
                mPath.arcTo(mRectF, endAngle, -sweepAngle);
                mPath.close();
                canvas.drawPath(mPath, mPaint);
        }

        private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                        float x = e.getX();
                        float y = e.getY();
                        check(x, y);
                        return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        float x = e2.getX();
                        float y = e2.getY();
                        check(x, y);
                        return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return false;
                }
        };

        private void check(float x, float y) {
                double angle = Math.atan2(y - measuredHeight, x - measuredWidth / 2);
                double v = angle * 180 / Math.PI;
                if (v < startAngele) {
                        v = startAngele;
                }
                if (v > endAngle) {
                        v = endAngle;
                }
                progress = (int) ((v - startAngele) / countAngle);
                if (progress < count) {
                        progress++;
                }
                invalidate();
                Log.e(TAG, "check: " + v);
                Log.e(TAG, "check: progress:" + progress);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
                        performClick();
                }
                mGestureDetector.onTouchEvent(event);
                return true;
        }

        @Override
        public boolean performClick() {
                return super.performClick();
        }
}
