package com.zmp.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zmp.widget.R;

/**
 * @author zmp
 * Created by zmp on 2018/10/13
 * 自定义水平进度条
 */
public class HProgressView extends View {

        private Paint mPaint;

        private Paint mPaint2;

        private int measuredWidth;

        private int measuredHeight;

        private RectF rect;

        private LinearGradient linearGradient;

        private Path path;

        private int mBgColor = Color.BLUE;

        private RectF rect2;

        public HProgressView(Context context) {
                this(context, null);
        }

        public HProgressView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public HProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttributeSet(context, attrs);
                init();
        }

        private void initAttributeSet(Context context, AttributeSet attrs) {
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HProgressView);
                mBgColor = attributes.getColor(R.styleable.HProgressView_hp_progressBgColor, Color.BLUE);
                int startColor = attributes.getColor(R.styleable.HProgressView_hp_progressStartColor, Color.BLUE);
                int endColor = attributes.getColor(R.styleable.HProgressView_hp_progressEndColor, Color.BLUE);
                colors = new int[]{
                        startColor, endColor
                };
                progress = attributes.getInt(R.styleable.HProgressView_hp_progress, 0);
                max = attributes.getInt(R.styleable.HProgressView_hp_max, 100);
                attributes.recycle();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint2.setStyle(Paint.Style.FILL);
                mPaint2.setColor(mBgColor);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                rect = new RectF(0, 0, measuredWidth, measuredHeight);
                rect2 = new RectF(2, 0, measuredWidth - 2, measuredHeight);
                path = new Path();
                path.addRoundRect(rect, measuredHeight / 2, measuredHeight / 2, Path.Direction.CCW);
                setProgress(progress);

        }

        public float getProgress() {
                return progress;
        }

        public void setColor(int startColor, int endColor) {
                colors[0] = startColor;
                colors[1] = endColor;
                linearGradient = new LinearGradient(measuredWidth * (1 - progress * 1.0F / max), 0, measuredWidth + measuredWidth * (1 - progress * 1.0F / max), 0,
                                                    colors, null, Shader.TileMode.CLAMP);
                mPaint.setShader(linearGradient);
                invalidate();
        }


        public void setBgColor(int bgColor) {
                this.mBgColor = bgColor;
                mPaint2.setColor(bgColor);
                invalidate();
        }

        private int[] colors;

        public void setProgress(int progress) {
                if (progress > 100) {
                        progress = 100;
                }
                else if (progress < 0) {
                        progress = 0;
                }
                this.progress = progress;

                if (measuredWidth * measuredHeight == 0) {
                        return;
                }

                linearGradient = new LinearGradient(measuredWidth * (1 - progress * 1.0F / max), 0, measuredWidth + measuredWidth * (1 - progress * 1.0F / max), 0,
                                                    colors, null, Shader.TileMode.CLAMP);
                mPaint.setShader(linearGradient);
                invalidate();
        }


        private int progress = 0;

        public int getMax() {
                return max;
        }

        public void setMax(int max) {
                this.max = max;
        }

        private int max = 100;

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.drawRoundRect(rect2, measuredHeight / 2, measuredHeight / 2, mPaint2);
                canvas.clipPath(path, Region.Op.INTERSECT);
                canvas.translate(measuredWidth * (progress * 1.0F / max - 1), 0);
                canvas.drawRoundRect(rect, measuredHeight / 2, measuredHeight / 2, mPaint);
        }
}
