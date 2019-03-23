package com.zmp.widget.shader;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zmp.widget.R;

/**
 * @author zmp
 * 图片加载 各种形状
 */
public class ShaderImageView extends View {


        private int measuredWidth;

        private int measuredHeight;

        private int shapeMode;

        private float padding;

        private float radius;

        private static final String TAG = "ShaderImageView";

        private float shaderR;

        private Drawable drawable;

        private Paint mPaint;

        private float centerX;

        private float centerY;

        private Paint mColorPaint;

        private ColorStateList strokeColor;

        private float strokeSize;

        private int polygon;

        private Path mPath;

        private Bitmap setBitmap;

        private Matrix shaderMx;

        private Bitmap drawBitmap;

        private Shader.TileMode shaderMode;

        private boolean isFitXX;

        private RectF rectF;

        public ShaderImageView(Context context) {
                this(context, null);
        }

        public ShaderImageView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public ShaderImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttributeSet(context, attrs);
                init();
        }

        private void initAttributeSet(Context context, AttributeSet attrs) {
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView);
                shapeMode = attributes.getInt(R.styleable.ShaderImageView_shape, 0);
                int tilMode = attributes.getInt(R.styleable.ShaderImageView_tilMode, 0);
                switch (tilMode) {
                        case 0:
                                shaderMode = Shader.TileMode.CLAMP;
                                break;

                        case 1:
                                shaderMode = Shader.TileMode.REPEAT;
                                break;
                        case 2:
                                shaderMode = Shader.TileMode.MIRROR;
                                break;
                        case 3:
                                shaderMode = Shader.TileMode.CLAMP;
                                isFitXX = true;
                                break;
                        default:
                                shaderMode = Shader.TileMode.CLAMP;
                                break;
                }
                polygon = attributes.getInt(R.styleable.ShaderImageView_polygon_num, 0);
                if (shapeMode == 3 && polygon < 3) {
                        polygon = 3;
                }
                if (shapeMode == 2 && polygon < 5) {
                        polygon = 5;
                }
                drawable = attributes.getDrawable(R.styleable.ShaderImageView_src);
                padding = attributes.getDimension(R.styleable.ShaderImageView_padding, 0);
                radius = attributes.getDimension(R.styleable.ShaderImageView_radius, 0);
                radius = attributes.getDimension(R.styleable.ShaderImageView_radius, 0);
                strokeColor = attributes.getColorStateList(R.styleable.ShaderImageView_stroke_color);
                if (strokeColor == null) {
                        strokeColor = ColorStateList.valueOf(Color.TRANSPARENT);
                }
                strokeSize = attributes.getDimension(R.styleable.ShaderImageView_stroke_size, 0);
                attributes.recycle();
        }

        private void init() {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setAntiAlias(true);
                mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mColorPaint.setAntiAlias(true);
                mColorPaint.setStyle(Paint.Style.STROKE);
                mColorPaint.setColor(strokeColor.getColorForState(getDrawableState(), R.color.colorTransparent));
                mColorPaint.setStrokeWidth(strokeSize);
                mPath = new Path();
                shaderMx = new Matrix();
                rectF = new RectF();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
                super.onSizeChanged(w, h, oldW, oldH);
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight();
                centerX = getMeasuredWidth() / 2;
                centerY = getMeasuredHeight() / 2;
                int baseR = Math.min(measuredWidth, measuredHeight) / 2;
                shaderR = baseR - padding - strokeSize;
                setShader();
        }

        private void setShader() {
                shaderMx.reset();
                if (measuredWidth * measuredHeight == 0) {
                        return;
                }
                drawBitmap = getBitmapFromDrawable(drawable);
                int width = drawBitmap.getWidth();
                int height = drawBitmap.getHeight();
                float scale = isFitXX ? Math.max((measuredWidth - (padding + strokeSize) * 2.0F) / drawBitmap.getWidth(),
                        (measuredHeight - (padding + strokeSize) * 2.0F) / drawBitmap.getHeight()) : Math.min(
                        (measuredWidth - (padding + strokeSize) * 2.0F) / drawBitmap.getWidth(),
                        (measuredHeight - (padding + strokeSize) * 2.0F) / drawBitmap.getHeight());
                if (scale < 1) {
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(drawBitmap, (int) (scale * width), (int) (scale * height), false);
                        scale = 1F;
                        drawBitmap = scaledBitmap;
                }
                shaderMx.setScale(scale, scale);
                shaderMx.postTranslate((int) centerX - drawBitmap.getWidth() * scale / 2,
                        (int) centerY - drawBitmap.getHeight() * scale / 2);
                BitmapShader bitmapShader = new BitmapShader(drawBitmap, shaderMode, shaderMode);
                bitmapShader.setLocalMatrix(shaderMx);
                mPaint.setShader(bitmapShader);
                mColorPaint.setColor(strokeColor.getColorForState(getDrawableState(), R.color.colorTransparent));
                postInvalidate();
        }

        public void setBitmap(Bitmap bitmap) {
                this.setBitmap = bitmap;
                setShader();
        }

        public void setDrawable(Drawable drawable) {
                this.drawable = drawable;
                setShader();
                Log.e(TAG, "setDrawable");
        }


        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (drawBitmap == null) {
                        return;
                }
                switch (shapeMode) {
                        case 0://矩形
                                rectF.left = padding + strokeSize;
                                rectF.right = measuredWidth - padding - strokeSize;
                                rectF.top = padding + strokeSize;
                                rectF.bottom = measuredHeight - padding - strokeSize;
                                canvas.drawRoundRect(rectF, radius, radius, mPaint);
                                canvas.drawRoundRect(rectF, radius, radius, mColorPaint);
                                break;
                        case 1://圆
                                canvas.drawCircle(centerX, centerY, shaderR, mPaint);
                                canvas.drawCircle(centerX, centerY, shaderR + strokeSize / 2, mColorPaint);
                                break;
                        case 2://正角星
                                drawMultiStar(canvas);
                                break;
                        case 3://正多边形
                                drawPolygon(canvas);
                                break;
                        default:
                                break;
                }
        }

        private void drawMultiStar(Canvas canvas) {
                setMultiPath(shaderR + strokeSize / 2, true);
                canvas.drawPath(mPath, mColorPaint);
                setMultiPath(shaderR, true);
                canvas.drawPath(mPath, mPaint);
        }

        private void setMultiPath(float shaderR, boolean isStar) {
                mPath.reset();
                if (isStar) {
                        if (polygon % 2 == 0) {
                                double v = Math.PI * 2 / polygon;
                                float vX0 = (float) (shaderR * Math.sin(v / 2));
                                float vY0 = (float) (shaderR * Math.cos(v / 2));
                                float vX1 = (float) (shaderR * Math.sin(v / 2 + v));
                                float vY1 = (float) (shaderR * Math.cos(v / 2 + v));
                                mPath.moveTo(centerX + vX0, centerY - vY0);
                                Path path = new Path();
                                path.moveTo(centerX + vX1, centerY - vY1);
                                for (int i = 1; i < polygon / 2; i++) {
                                        float vX = (float) (shaderR * Math.sin(v * (i * 2 + 0.5)));
                                        float vY = (float) (shaderR * Math.cos(v * (i * 2 + 0.5)));
                                        mPath.lineTo(centerX + vX, centerY - vY);
                                }
                                for (int i = 1; i < polygon / 2; i++) {
                                        float vX = (float) (shaderR * Math.sin(v * (i * 2 + 1.5)));
                                        float vY = (float) (shaderR * Math.cos(v * (i * 2 + 1.5)));
                                        path.lineTo(centerX + vX, centerY - vY);
                                }
                                path.close();
                                mPath.close();
                                mPath.addPath(path);
                        } else {

                                double v0 = Math.PI * 2 / polygon;
                                int i10 = polygon / 2;
                                float v10 = (float) (shaderR * Math.cos(v0 * i10));
                                float polygonCenterY0 = centerY + (shaderR + v10) / 2;
                                mPath.moveTo(centerX, polygonCenterY0 - shaderR);
                                for (int i = 1; i < polygon; i++) {
                                        float vX = (float) (shaderR * Math.sin(v0 * i * 2));
                                        float vY = (float) (shaderR * Math.cos(v0 * i * 2));
                                        mPath.lineTo(centerX + vX, polygonCenterY0 - vY);
                                }
                                mPath.close();
                        }
                        return;
                }

                if (polygon % 2 == 0) {
                        double v = Math.PI * 2 / polygon;
                        float vX0 = (float) (shaderR * Math.sin(v / 2));
                        float vY0 = (float) (shaderR * Math.cos(v / 2));
                        mPath.moveTo(centerX + vX0, centerY - vY0);
                        for (int i = 1; i < polygon; i++) {
                                float vX = (float) (shaderR * Math.sin(v * (i + 0.5)));
                                float vY = (float) (shaderR * Math.cos(v * (i + 0.5)));
                                mPath.lineTo(centerX + vX, centerY - vY);
                        }
                        mPath.close();
                } else {

                        double v = Math.PI * 2 / polygon;
                        int i1 = polygon / 2;
                        float v1 = (float) (shaderR * Math.cos(v * i1));
                        float polygonCenterY = centerY + (shaderR + v1) / 2;
                        mPath.moveTo(centerX, polygonCenterY - shaderR);
                        for (int i = 1; i < polygon; i++) {
                                float vX = (float) (shaderR * Math.sin(v * i));
                                float vY = (float) (shaderR * Math.cos(v * i));
                                mPath.lineTo(centerX + vX, polygonCenterY - vY);
                        }
                        mPath.close();
                }


        }

        private void drawPolygon(Canvas canvas) {
                setMultiPath(shaderR + strokeSize / 2, false);
                canvas.drawPath(mPath, mColorPaint);
                setMultiPath(shaderR, false);
                canvas.drawPath(mPath, mPaint);

        }

        private Bitmap getBitmapFromDrawable(Drawable drawable) {
                if (setBitmap != null) {
                        return setBitmap;
                }
                if (drawable == null) {
                        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                }

                if (drawable instanceof BitmapDrawable) {
                        return ((BitmapDrawable) drawable).getBitmap();
                }

                if (drawable instanceof StateListDrawable) {
                        StateListDrawable listDrawable = (StateListDrawable) drawable;
                        boolean b = listDrawable.setState(getDrawableState());
                        return getBitmapFromDrawable(listDrawable.getCurrent());
                }

                try {
                        Bitmap bitmap;

                        if (drawable instanceof ColorDrawable) {
                                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                        } else {
                                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        }

                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        return bitmap;
                } catch (Exception e) {
                        e.printStackTrace();
                        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                if (isClickable()) {
                                        setPressed(true);
                                }
                                break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                                setPressed(false);
                                performClick();
                                break;
                }
                return super.onTouchEvent(event);
        }


        @Override
        public boolean performClick() {
                return super.performClick();
        }

        @Override
        protected void drawableStateChanged() {
                super.drawableStateChanged();
                setShader();
        }
}
