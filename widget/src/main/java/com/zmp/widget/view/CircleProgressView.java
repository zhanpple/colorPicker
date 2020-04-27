package com.zmp.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zmp.widget.R;

/**
 * @author zmp
 * Created by zmp on 2018/10/13
 * 自定义圆形进度条
 */
public class CircleProgressView extends View {

        private Paint mPaint;

        private Paint mCirclePaint;

        private int baseline;

        private int measuredWidth;

        private int measuredHeight;

        private float cR;

        private Paint mTextPaint;

        private RectF rectF;

        private RectF baseRectF = new RectF();

        private Paint mTextPaint2;

        private int baseline2;


        private int mProgress = 0;

        private String mProgressText = "progress";

        private Path mPath;

        private Paint mPointPaint;

        private int maxR;

        private int mCenterBgColor;

        private int mOutBgColor;

        private int mPointColor;

        private int mProgressBgColor;

        private int mProgressColor;

        private int mTextColor;

        private int outMode;

        private float mStrokeWidth;

        private float mStartAngle = 0;

        private float mMaxAngle = 360;

        private float marginTopPercent = 0;

        public int getMax() {
                return mMax;
        }

        public void setMax(int mMax) {
                this.mMax = mMax;
        }

        private int mMax = 100;

        public CircleProgressView(Context context) {
                this(context, null);
        }

        public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttributeSet(context, attrs);
                init();
        }

        private void initAttributeSet(Context context, AttributeSet attrs) {
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
                mProgressColor = attributes.getColor(R.styleable.CircleProgressView_cpv_progressColor, Color.YELLOW);
                mProgressBgColor = attributes.getColor(R.styleable.CircleProgressView_cpv_progressBgColor, Color.MAGENTA);
                mTextColor = attributes.getColor(R.styleable.CircleProgressView_cpv_textColor, Color.WHITE);
                mOutBgColor = attributes.getColor(R.styleable.CircleProgressView_cpv_outBgColor, Color.BLUE);
                mCenterBgColor = attributes.getColor(R.styleable.CircleProgressView_cpv_centerBgColor, Color.GRAY);
                mPointColor = attributes.getColor(R.styleable.CircleProgressView_cpv_pointBgColor, Color.CYAN);
                outMode = attributes.getInt(R.styleable.CircleProgressView_cpv_endMode, 3);
                mProgress = attributes.getInteger(R.styleable.CircleProgressView_cpv_progress, 0);
                mMax = attributes.getInteger(R.styleable.CircleProgressView_cpv_max, 100);
                mStartAngle = attributes.getFloat(R.styleable.CircleProgressView_cpv_startAngle, 0F);
                mMaxAngle = attributes.getFloat(R.styleable.CircleProgressView_cpv_maxAngle, 360F);
                mProgressText = attributes.getString(R.styleable.CircleProgressView_cpv_text);
                marginTopPercent = attributes.getFloat(R.styleable.CircleProgressView_cpv_text_marginTopPercent, 0);
                attributes.recycle();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);

                mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mCirclePaint.setStyle(Paint.Style.FILL);

                mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mTextPaint2.setColor(mTextColor);
                mTextPaint.setColor(mTextColor);
                mPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                maxR = Math.min(measuredWidth, measuredHeight) / 2;
                cR = maxR * 0.8F;
                mStrokeWidth = cR * 0.25F;
                mPaint.setStrokeWidth(mStrokeWidth);
                mTextPaint.setTextSize(cR * 0.5F);
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
                baseline = (fontMetrics.bottom + fontMetrics.top) / 2;
                rectF = new RectF(measuredWidth / 2F - cR, measuredHeight / 2F - cR, measuredWidth / 2F + cR, measuredHeight / 2F + cR);
                mTextPaint2.setTextSize(mStrokeWidth);
                mTextPaint2.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics2 = mTextPaint2.getFontMetricsInt();
                baseline2 = (fontMetrics2.bottom + fontMetrics2.top) / 2;
        }

        public void setColor(int progressColor) {
                this.mProgressColor = progressColor;
                invalidate();
        }

        public void setTextColor(int textColor) {
                this.mTextColor = textColor;
                mTextPaint2.setColor(mTextColor);
                mTextPaint.setColor(mTextColor);
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

        public void setOutBgColor(int outBgColor) {
                this.mOutBgColor = outBgColor;
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

        public void setProgressText(String progressText) {
                this.mProgressText = progressText;
                invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                mCirclePaint.setColor(mOutBgColor);
                canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, cR + mStrokeWidth / 2, mCirclePaint);

                mPointPaint.setColor(mCenterBgColor);
                canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, cR - mStrokeWidth / 2, mPointPaint);

                mPaint.setColor(mProgressBgColor);
                baseRectF.set(measuredWidth / 2F - cR, measuredHeight / 2F - cR, measuredWidth / 2F + cR, measuredHeight / 2F + cR);
                canvas.drawArc(baseRectF, mStartAngle, mMaxAngle, false, mPaint);
                float sin = (float) Math.sin(mStartAngle * Math.PI / 180);
                float cos = (float) Math.cos(mStartAngle * Math.PI / 180);
                float sin2 = (float) Math.sin((mStartAngle + mMaxAngle) * Math.PI / 180);
                float cos2 = (float) Math.cos((mStartAngle + mMaxAngle) * Math.PI / 180);
                float sin3 = (float) Math.sin((mStartAngle + mProgress * mMaxAngle / mMax) * Math.PI / 180);
                float cos3 = (float) Math.cos((mStartAngle + mProgress * mMaxAngle / mMax) * Math.PI / 180);
                mCirclePaint.setColor(mProgressBgColor);
                canvas.drawCircle(measuredWidth / 2F + cR * cos2, measuredHeight / 2F + cR * sin2, mStrokeWidth / 2, mCirclePaint);

                mPaint.setColor(mProgressColor);
                mCirclePaint.setColor(mProgressColor);
                canvas.drawArc(rectF, mStartAngle, mProgress * mMaxAngle / mMax, false, mPaint);
                canvas.drawCircle(measuredWidth / 2F + cR * cos, measuredHeight / 2F + cR * sin, mStrokeWidth / 2, mCirclePaint);

                if (outMode == 3) {
                        canvas.drawCircle(measuredWidth / 2F + cR * cos3, measuredHeight / 2F + cR * sin3, mStrokeWidth / 2, mCirclePaint);
                } else {
                        canvas.save();
                        mPath.reset();
                        if (outMode == 1) {
                                mPath.moveTo(measuredWidth / 2 + maxR, measuredHeight / 2);
                        } else if (outMode == 2) {
                                mPath.moveTo(measuredWidth / 2F + cR + mStrokeWidth / 2, measuredHeight / 2);
                        }
                        mPath.lineTo(measuredWidth / 2F + cR * 0.8F, measuredHeight / 2F + cR * 0.1F);
                        mPath.lineTo(measuredWidth / 2F + cR * 0.8F, measuredHeight / 2F - cR * 0.1F);
                        mPath.close();
                        mPointPaint.setColor(mPointColor);
                        canvas.rotate(mProgress * mMaxAngle / mMax + mStartAngle, measuredWidth / 2, measuredHeight / 2);
                        canvas.drawPath(mPath, mPointPaint);
                        canvas.restore();
                }

                canvas.drawText(String.valueOf(mProgress), measuredWidth / 2, measuredHeight / 2F - baseline - cR * 0.2F + cR * marginTopPercent, mTextPaint);
                if (!TextUtils.isEmpty(mProgressText)) {
                        canvas.drawText(mProgressText, measuredWidth / 2, measuredHeight / 2F - baseline2 + cR * 0.2F + cR * marginTopPercent, mTextPaint2);
                }
        }

}
