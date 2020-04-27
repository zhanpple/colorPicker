package com.zmp.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zmp.widget.R;

/**
 * Created by zmp on 2020/4/22 9:54
 *
 * @author zmp
 */
public class SimpleCircleProgress extends View {

        private int mCenterWidth;

        private int mCenterHeight;

        private float mOutR;

        private Path mPath = new Path();

        private Paint mPaint;

        private float mInR;

        private RectF mRectF;

        private RectF mRectF2;

        private SweepGradient mSweepGradient;

        private int[] mProgressColors;

        private boolean isIntMode = false;

        public SimpleCircleProgress(Context context) {
                this(context, null);
        }

        public SimpleCircleProgress(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public SimpleCircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttributeSet(context, attrs);
                init();
        }

        private void initAttributeSet(Context context, AttributeSet attrs) {
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SimpleCircleProgress);
                mProgressColor = attributes.getColor(R.styleable.SimpleCircleProgress_scp_progressColor, Color.GREEN);
                mProgressBg = attributes.getColor(R.styleable.SimpleCircleProgress_scp_progressBgColor, Color.GRAY);
                mTextColor = attributes.getColor(R.styleable.SimpleCircleProgress_scp_textColor, Color.BLACK);
                mStartAngle = attributes.getFloat(R.styleable.SimpleCircleProgress_scp_startAngle, 0);
                mSweepAngle = attributes.getFloat(R.styleable.SimpleCircleProgress_scp_maxAngle, 360);
                mProgress = attributes.getFloat(R.styleable.SimpleCircleProgress_scp_progress, 0);
                mText = attributes.getString(R.styleable.SimpleCircleProgress_scp_progressText);
                mTextMarginTop = attributes.getFloat(R.styleable.SimpleCircleProgress_scp_textMarginTop, 0);
                mProgressWidthPercent = attributes.getFloat(R.styleable.SimpleCircleProgress_scp_progressWidthPercent, 1);
                isIntMode = attributes.getBoolean(R.styleable.SimpleCircleProgress_scp_progress_int, false);
                attributes.recycle();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setAntiAlias(true);
                mPaint.setColor(Color.GRAY);
                mPaint.setTextAlign(Paint.Align.CENTER);
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                mCenterWidth = getMeasuredWidth() / 2;
                mCenterHeight = getMeasuredHeight() / 2;
                mOutR = Math.min(mCenterWidth, mCenterHeight) * 0.95F;
                mInR = mOutR * 0.9F * mProgressWidthPercent;
                mRectF = new RectF(mCenterWidth - mOutR, mCenterHeight - mOutR, mCenterWidth + mOutR, mCenterHeight + mOutR);
                mRectF2 = new RectF(mCenterWidth - mInR, mCenterHeight - mInR, mCenterWidth + mInR, mCenterHeight + mInR);
                mPaint.setTextSize(mInR * 0.4F);
                setProgressColors(mProgressColors);
        }

        public void setProgress(float mProgress) {
                this.mProgress = mProgress;
                postInvalidate();
        }

        public void setProgressWidthPercent(float progressWidthPercent) {
                this.mProgressWidthPercent = progressWidthPercent;
                postInvalidate();
        }


        public void setStartAngle(float mStartAngle) {
                this.mStartAngle = mStartAngle;
                postInvalidate();
        }


        public void setSweepAngle(float mSweepAngle) {
                this.mSweepAngle = mSweepAngle;
                postInvalidate();
        }

        public void setTextMarginTop(float mTextMarginTop) {
                this.mTextMarginTop = mTextMarginTop;
                postInvalidate();
        }


        private float mStartAngle = 120;

        private float mSweepAngle = 300;

        private float mProgress = 0.8F;

        private float mTextMarginTop = 0F;

        private float mProgressWidthPercent = 1F;


        private int mProgressColor = Color.GREEN;

        private int mProgressBg = Color.GRAY;

        private int mTextColor = Color.BLACK;

        private String mText = "分数";

        public void setTextColor(int textColor) {
                this.mTextColor = textColor;
                postInvalidate();
        }

        public void setText(String text) {
                this.mText = text;
                postInvalidate();
        }

        public void setProgressColor(int progressColor) {
                this.mProgressColor = progressColor;
                postInvalidate();
        }

        public void setProgressBg(int progressBg) {
                this.mProgressBg = progressBg;
                postInvalidate();
        }

        public void setProgressColors(int... colors) {
                this.mProgressColors = colors;
                if (colors == null || colors.length == 0) {
                        mSweepGradient = null;
                } else {
                        mSweepGradient = new SweepGradient(mCenterWidth, mCenterHeight, colors, null);
                }
                postInvalidate();
        }


        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                calculationPath(mStartAngle, mSweepAngle);
                mPaint.setColor(mProgressBg);
                canvas.drawPath(mPath, mPaint);

                calculationPath(mStartAngle, mSweepAngle * mProgress / 100F);
                if (mSweepGradient == null) {
                        mPaint.setColor(mProgressColor);
                        canvas.drawPath(mPath, mPaint);
                } else {
                        mPaint.setShader(mSweepGradient);
                        canvas.drawPath(mPath, mPaint);
                        mPaint.setShader(null);
                }

                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mInR * 0.5F);
                String text = isIntMode ? Math.round(mProgress) + "%" : mProgress + "%";
                canvas.drawText(text, mCenterWidth, mCenterHeight + (mInR * mTextMarginTop), mPaint);
                if (!TextUtils.isEmpty(mText)) {
                        mPaint.setTextSize(mInR * 0.4F);
                        canvas.drawText(mText, mCenterWidth, mCenterHeight + mInR * (0.4F + mTextMarginTop), mPaint);
                }
        }


        /**
         * 根据起始角度和旋转角度计算圆环path
         *
         * @param startAngle 起始角度
         * @param sweepAngle 旋转角度
         */
        private void calculationPath(float startAngle, float sweepAngle) {
                if (sweepAngle % 360F == 0) {
                        mPath.reset();
                        mPath.addCircle(mCenterWidth, mCenterHeight, mOutR, Path.Direction.CCW);
                        mPath.addCircle(mCenterWidth, mCenterHeight, mInR, Path.Direction.CW);
                        mPath.close();
                        return;
                }

                float sin = (float) Math.sin((startAngle * Math.PI) / 180);
                float cos = (float) Math.cos((startAngle * Math.PI) / 180);
                float sin2 = (float) Math.sin((startAngle + sweepAngle) * Math.PI / 180);
                float cos2 = (float) Math.cos((startAngle + sweepAngle) * Math.PI / 180);

                float startX = mCenterWidth + mOutR * cos;
                float startY = mCenterHeight + mOutR * sin;
                float startX2 = mCenterWidth + mInR * cos;
                float startY2 = mCenterHeight + mInR * sin;
                float endX = mCenterWidth + mOutR * cos2;
                float endY = mCenterHeight + mOutR * sin2;
                float endX2 = mCenterWidth + mInR * cos2;
                float endY2 = mCenterHeight + mInR * sin2;

                mPath.reset();
                mPath.lineTo(0, 0);
                mPath.moveTo(startX, startY);
                mPath.arcTo(mRectF, startAngle, sweepAngle, true);
                mPath.lineTo(mCenterWidth + mInR * cos2, mCenterHeight + mInR * sin2);
                mPath.arcTo(mRectF2, startAngle + sweepAngle, -sweepAngle, true);
                mPath.lineTo(startX, startY);
                mPath.addCircle((startX + startX2) / 2, (startY + startY2) / 2, (mOutR - mInR) / 2, Path.Direction.CW);
                mPath.addCircle((endX + endX2) / 2, (endY + endY2) / 2, (mOutR - mInR) / 2, Path.Direction.CW);
        }


}
