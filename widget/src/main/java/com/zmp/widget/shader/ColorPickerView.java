package com.zmp.widget.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zmp on 2018/6/20.
 */

public class ColorPickerView extends View {

        private int measuredWidth;

        private int measuredHeight;

        private Paint mPaint;

        private Bitmap mBitmap;

        private int radius;

        private float colorPercent = 1;

        private float mAlpha;

        public ColorPickerView(Context context) {
                this(context, null);
        }

        public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                radius = Math.min(measuredWidth, measuredHeight) / 2;
                setColorPercent(colorPercent);
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.drawBitmap(mBitmap, 0, 0, null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                getColor(event);
                                break;
                        case MotionEvent.ACTION_MOVE:
                                getColor(event);
                                break;
                        case MotionEvent.ACTION_UP:
                                performClick();
                                break;

                }
                return true;
        }

        @Override
        public boolean performClick() {
                return super.performClick();
        }

        public void setColorPercent(float scale) {
                if (scale > 1) {
                        scale = 1;
                }
                if (scale < 0) {
                        scale = 0;

                }
                this.colorPercent = scale;
                if (radius <= 0) {
                        return;
                }
                SweepGradient mSweepGradient = new SweepGradient(measuredWidth / 2, measuredHeight / 2,
                                                                 new int[]{
                                                                         getCurrentColor(scale, 0x00000000, 0xFF00FFFF, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFF0000FF, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFFFF00FF, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFFFF0000, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFFFFFF00, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFF00FF00, getAlpha()),
                                                                         getCurrentColor(scale, 0x00000000, 0xFF00FFFF, getAlpha()),
                                                                         },
                                                                 null);
                RadialGradient radialGradient = new RadialGradient(measuredWidth / 2, measuredHeight / 2,
                                                                   radius, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
                ComposeShader composeShader = new ComposeShader(mSweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER);
                mPaint.setShader(composeShader);
                mBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, radius, mPaint);
                invalidate();
        }

        @Override
        public void setAlpha(float alpha) {
                this.mAlpha =alpha;
                setColorPercent(colorPercent);
        }

        @Override
        public float getAlpha() {
                return this.mAlpha;
        }

        private void getColor(MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if (!checkXY(x, y)) {
                        return;
                }
                int color = mBitmap.getPixel((int) x, (int) y);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);

                String r1 = Integer.toHexString(r);
                String g1 = Integer.toHexString(g);
                String b1 = Integer.toHexString(b);
                String a1 = Integer.toHexString(a);
                String text = String.format("%1$s%2$s%3$s%4$s",
                                            a1.length() == 2 ? a1 : ("0" + a1),
                                            r1.length() == 2 ? r1 : ("0" + r1),
                                            g1.length() == 2 ? g1 : ("0" + g1),
                                            b1.length() == 2 ? b1 : ("0" + b1)
                                           ).toUpperCase();
                if (choiceColorListener != null) {
                        choiceColorListener.onChoiceColor(color, a1, r, g, b, text);
                }
        }

        public void setChoiceColorListener(IChoiceColorListener choiceColorListener) {
                this.choiceColorListener = choiceColorListener;
        }

        private IChoiceColorListener choiceColorListener;

        public interface IChoiceColorListener {

                void onChoiceColor(int color, String a1, int r, int g, int b, String text);
        }

        private boolean checkXY(float x, float y) {
                float dx = x - measuredWidth / 2;
                float dy = y - measuredHeight / 2;
                return dx * dx + dy * dy <= radius * radius;
        }

        private int getCurrentColor(float fraction, int startColor, int endColor, float alpha) {
                int redCurrent;
                int blueCurrent;
                int greenCurrent;
                int alphaCurrent;

                int redStart = Color.red(startColor);
                int blueStart = Color.blue(startColor);
                int greenStart = Color.green(startColor);
                int alphaStart = Color.alpha(startColor);

                int redEnd = Color.red(endColor);
                int blueEnd = Color.blue(endColor);
                int greenEnd = Color.green(endColor);
                int alphaEnd = Color.alpha(endColor);

                int redDifference = redEnd - redStart;
                int blueDifference = blueEnd - blueStart;
                int greenDifference = greenEnd - greenStart;
                int alphaDifference = alphaEnd - alphaStart;

                redCurrent = (int) (redStart + fraction * redDifference);
                blueCurrent = (int) (blueStart + fraction * blueDifference);
                greenCurrent = (int) (greenStart + fraction * greenDifference);
                alphaCurrent = (int) (alphaStart + alpha * alphaDifference);

                return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
        }
}
