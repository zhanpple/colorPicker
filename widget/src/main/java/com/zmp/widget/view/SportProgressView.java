package com.zmp.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.zmp.widget.R;

/**
 * @author zmp
 * Created by zmp on 2018/10/13
 * 自定义圆形进度条
 */
public class SportProgressView extends View {

        private Paint mPaint;

        private int baseline;

        private int measuredWidth;

        private int measuredHeight;

        private float cR;

        private Paint mTextPaint;

        private RectF rectF;

        private Paint mTextPaint2;

        private int baseline2;

        private float mMaxAngle;

        private int mOutBgColor;

        private boolean mShowPointer;


        public int getProgress() {
                return mProgress;
        }

        private int mProgress = 0;

        private String mProgressText = "m/s";

        private Path mPath;

        private Paint mPointPaint;

        private int mCenterBgColor;

        private int mSecondProgressColor;

        private int mPointColor;

        private int mProgressBgColor;

        private int mProgressColor;

        private int mTextColor;

        private RectF maxRect;

        private float strokeW;

        private RectF rectF2;

        public int getMax() {
                return mMax;
        }

        private int mMax;

        public SportProgressView(Context context) {
                this(context, null);
        }

        public SportProgressView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public SportProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttributeSet(context, attrs);
                init();
        }

        private void initAttributeSet(Context context, AttributeSet attrs) {
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SportProgressView);
                mProgressColor = attributes.getColor(R.styleable.SportProgressView_spv_progressColor, Color.YELLOW);
                mProgressBgColor = attributes.getColor(R.styleable.SportProgressView_spv_progressBgColor, Color.MAGENTA);
                mTextColor = attributes.getColor(R.styleable.SportProgressView_spv_textColor, Color.WHITE);
                mSecondProgressColor = attributes.getColor(R.styleable.SportProgressView_spv_secondProgressColor, Color.BLUE);
                mOutBgColor = attributes.getColor(R.styleable.SportProgressView_spv_outBgColor, Color.BLUE);
                mCenterBgColor = attributes.getColor(R.styleable.SportProgressView_spv_centerBgColor, Color.GRAY);
                mPointColor = attributes.getColor(R.styleable.SportProgressView_spv_pointBgColor, Color.CYAN);
                mProgress = attributes.getInteger(R.styleable.SportProgressView_spv_progress, 0);
                mMax = attributes.getInteger(R.styleable.SportProgressView_spv_max, 100);
                mStartAngle = attributes.getFloat(R.styleable.SportProgressView_spv_startAngle, -90F);
                mMaxAngle = attributes.getFloat(R.styleable.SportProgressView_spv_maxAngle, 360F);
                mShowPointer = attributes.getBoolean(R.styleable.SportProgressView_spv_showPointer, true);
                String p = attributes.getString(R.styleable.SportProgressView_spv_progressText);
                if (!TextUtils.isEmpty(p)) {
                        mProgressText = p;
                }
                attributes.recycle();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);
                mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mTextPaint2.setColor(mTextColor);
                mTextPaint.setColor(mSecondProgressColor);
                mPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                int maxR = Math.min(measuredWidth, measuredHeight) / 2;
                strokeW = maxR * 0.2F;
                cR = maxR - strokeW / 2;
                mPaint.setStrokeWidth(strokeW);
                mTextPaint.setTextSize(cR * 0.6F);
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
                baseline = (fontMetrics.bottom + fontMetrics.top) / 2;
                rectF = new RectF(measuredWidth / 2F - cR, measuredHeight / 2F - cR, measuredWidth / 2F + cR, measuredHeight / 2F + cR);
                rectF2 = new RectF(measuredWidth / 2F - cR + strokeW * 3 / 4, measuredHeight / 2F - cR + strokeW * 3 / 4, measuredWidth / 2F + cR - strokeW * 3 / 4,
                        measuredHeight / 2F + cR - strokeW * 3 / 4);
                maxRect = new RectF(measuredWidth / 2 - maxR, measuredHeight / 2 - maxR, measuredWidth / 2 + maxR, measuredHeight / 2 + maxR);
                mTextPaint2.setTextSize(cR * 0.3F);
                mTextPaint2.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics2 = mTextPaint2.getFontMetricsInt();
                baseline2 = (fontMetrics2.bottom + fontMetrics2.top) / 2;
        }

        public void setProgressColor(int progressColor) {
                this.mProgressColor = progressColor;
                invalidate();
        }

        public void setTextColor(int textColor) {
                this.mTextColor = textColor;
                mTextPaint2.setColor(mTextColor);
                invalidate();
        }

        public void setProgressBgColor(int progressBgColor) {
                this.mProgressBgColor = progressBgColor;
                invalidate();
        }

        public void setCenterBgColor(int centerBgColor) {
                this.mCenterBgColor = centerBgColor;
                invalidate();
        }


        public void setPointColor(int pointColor) {
                this.mPointColor = pointColor;
                invalidate();
        }

        public void setProgress(int progress) {
                this.mProgress = progress;
                invalidate();
        }

        public void setMax(int max) {
                this.mMax = max;
                invalidate();
        }

        public void setProgressText(String progressText) {
                this.mProgressText = progressText;
                invalidate();
        }

        public void setStartAngle(float mStartAngle) {
                this.mStartAngle = mStartAngle;
        }

        public void setSecondProgressColor(int mSecondProgressColor) {
                this.mSecondProgressColor = mSecondProgressColor;
                mTextPaint.setColor(mSecondProgressColor);
        }


        private float mStartAngle = -90F;

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                mPointPaint.setColor(mOutBgColor);
                canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, cR + strokeW / 2, mPointPaint);
                mPointPaint.setColor(mCenterBgColor);
                canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, cR - strokeW / 2, mPointPaint);

                float sin = (float) Math.sin(mStartAngle * Math.PI / 180);
                float cos = (float) Math.cos(mStartAngle * Math.PI / 180);
                float sin2 = (float) Math.sin((mStartAngle + mMaxAngle) * Math.PI / 180);
                float cos2 = (float) Math.cos((mStartAngle + mMaxAngle) * Math.PI / 180);
                float sin3 = (float) Math.sin((mStartAngle + mProgress * mMaxAngle / mMax) * Math.PI / 180);
                float cos3 = (float) Math.cos((mStartAngle + mProgress * mMaxAngle / mMax) * Math.PI / 180);
                mPaint.setStrokeWidth(strokeW);
                mPaint.setColor(mProgressBgColor);
                mPointPaint.setColor(mProgressBgColor);
                canvas.drawArc(rectF, mStartAngle, mMaxAngle, false, mPaint);
                canvas.drawCircle(measuredWidth / 2F + cR * cos2, measuredHeight / 2F + cR * sin2, strokeW / 2, mPointPaint);

                mPaint.setColor(mProgressColor);
                mPointPaint.setColor(mProgressColor);
                canvas.drawArc(rectF, mStartAngle, mProgress * mMaxAngle / mMax, false, mPaint);
                canvas.drawCircle(measuredWidth / 2F + cR * cos, measuredHeight / 2F + cR * sin, strokeW / 2, mPointPaint);

                mPaint.setColor(mSecondProgressColor);
                mPointPaint.setColor(mSecondProgressColor);
                mPaint.setStrokeWidth(strokeW / 2);
                canvas.drawCircle(measuredWidth / 2F + (cR - strokeW * 3 / 4) * cos, measuredHeight / 2F + (cR - strokeW * 3 / 4) * sin, strokeW / 4, mPointPaint);
                canvas.drawArc(rectF2, mStartAngle, mProgress * mMaxAngle / mMax, false, mPaint);
                if (!mShowPointer) {
                        mPointPaint.setColor(mProgressColor);
                        canvas.drawCircle(measuredWidth / 2F + cR * cos3, measuredHeight / 2F + cR * sin3, strokeW / 2, mPointPaint);
                        mPointPaint.setColor(mSecondProgressColor);
                        canvas.drawCircle(measuredWidth / 2F + (cR - strokeW * 3 / 4) * cos3, measuredHeight / 2F + (cR - strokeW * 3 / 4) * sin3, strokeW / 4, mPointPaint);
                } else {
                        canvas.save();
                        mPath.reset();
                        mPath.moveTo(measuredWidth / 2F + cR * 0.5F, measuredHeight / 2);
                        mPath.arcTo(maxRect, -2, 4F);
                        mPath.close();
                        mPointPaint.setColor(mPointColor);
                        canvas.rotate(mProgress * mMaxAngle / mMax + mStartAngle, measuredWidth / 2, measuredHeight / 2);
                        canvas.drawPath(mPath, mPointPaint);
                        canvas.restore();
                }
                canvas.drawText(String.valueOf(mProgress), measuredWidth / 2, measuredHeight / 2 - baseline, mTextPaint);
                canvas.drawText(mProgressText, measuredWidth / 2, measuredHeight / 2F - baseline2 + cR * 0.4F, mTextPaint2);
        }

}
