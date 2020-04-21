package com.zmp.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zmp.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zmp
 * @date 2017/10/27
 * IndexView.
 */

public class IndexView extends View {

        private Paint paint;

        private int measuredHeight;

        private int measuredWidth;

        private int textSize;

        public void setStrings(List<String> strings) {
                this.strings = strings;
                setTextPaint();
                invalidate();
        }

        private List<String> strings;

        private int padding;

        private GestureDetector mGestureDetector;

        private RectF rectF;

        private PopupWindow popupWindow;

        private TextView textView;

        private int top;

        private int[] outLocation;

        private int popHeight;

        private int popWidth;

        private View inflate;

        private int index = 0;


        public IndexView(Context context) {
                this(context, null);
        }

        public IndexView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                mGestureDetector = new GestureDetector(getContext(), onGestureListener);
                if (strings != null) {return;}
                initList();
        }

        private void initList() {
                strings = new ArrayList<>();
                {
                        strings.add("$");
                        strings.add("A");
                        strings.add("B");
                        strings.add("C");
                        strings.add("D");
                        strings.add("E");
                        strings.add("F");
                        strings.add("G");
                        strings.add("H");
                        strings.add("I");
                        strings.add("J");
                        strings.add("K");
                        strings.add("L");
                        strings.add("M");
                        strings.add("N");
                        strings.add("O");
                        strings.add("P");
                        strings.add("Q");
                        strings.add("R");
                        strings.add("S");
                        strings.add("T");
                        strings.add("U");
                        strings.add("V");
                        strings.add("W");
                        strings.add("X");
                        strings.add("Y");
                        strings.add("Z");
                        strings.add("#");
                }
        }

        @SuppressLint("InflateParams")
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                measuredHeight = getMeasuredHeight();
                measuredWidth = getMeasuredWidth();
                setTextPaint();
                rectF = new RectF();
                inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_index_view, null, false);
                inflate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                inflate.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                textView = inflate.findViewById(R.id.pop_tv);
                popHeight = inflate.getMeasuredHeight();
                popWidth = inflate.getMeasuredWidth();
        }

        private void setTextPaint() {
                if (measuredHeight <= 0) {
                        return;
                }
                textSize = measuredHeight / (strings.size() + 2);
                padding = (measuredHeight - textSize * strings.size()) / 2;
                paint.setTextSize(textSize - 4);
                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                // 转载请注明出处：http://blog.csdn.net/hursing
                // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
                top = (fontMetrics.bottom - fontMetrics.top) / 2 + fontMetrics.top;
                paint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                for (int i = 0; i < strings.size(); i++) {
                        if (index == i) {
                                paint.setColor(Color.GREEN);
                                canvas.drawCircle(measuredWidth / 2, padding + textSize / 2 + textSize * i, textSize / 2, paint);
                        }
                        paint.setColor(Color.BLACK);
                        canvas.drawText(strings.get(i), measuredWidth / 2, padding + textSize / 2 - top + textSize * i, paint);

                }

        }


        private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                        float x = e.getX();
                        float y = e.getY();
                        for (int i = 0; i < strings.size(); i++) {
                                rectF.set(0, padding + textSize * i, measuredWidth, padding + textSize * (i + 1));
                                if (rectF.contains(x, y)) {
                                        setIndex(i);
                                        break;
                                }
                        }
                        removeCallbacks(cancelAction);
                        removeCallbacks(action);
                        postDelayed(action, 100);
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
                        for (int i = 0; i < strings.size(); i++) {
                                rectF.set(0, padding + textSize * i, measuredWidth, padding + textSize * (i + 1));
                                if (rectF.contains(x, y)) {
                                        setIndex(i);
                                        break;
                                }
                        }
                        if (popupWindow != null) {
                                popupWindow.dismiss();
                        }
                        removeCallbacks(action);
                        postDelayed(action, 200);
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

        Runnable action = new Runnable() {
                @Override
                public void run() {
                        showOnclick(index);
                }
        };

        public void setIndex(int index) {
                this.index = index;
                invalidate();
        }

        private void showOnclick(final int i) {
                Log.e("OnClick", "showOnclick:" + i);
                if (listener != null) {
                        listener.onChoice(index);
                }
                if (popupWindow == null) {
                        outLocation = new int[2];
                        getLocationInWindow(outLocation);
                        popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //设置 popupWindow外面可点击
                        popupWindow.setOutsideTouchable(true);
                        //给popupWindow设置一个背景
                        popupWindow.setBackgroundDrawable(new ColorDrawable());
                }
                textView.setText(strings.get(i));
                Log.e("OnClick", "outLocation:" + outLocation[0]);
                popupWindow.showAtLocation(this, Gravity.TOP | Gravity.START, outLocation[0] - popWidth,
                        outLocation[1] + padding + textSize / 2 + textSize * i - popHeight / 2);
                popupWindow.update();

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                        eventCancel();
                }
                mGestureDetector.onTouchEvent(event);
                return true;
        }

        Runnable cancelAction = new Runnable() {
                @Override
                public void run() {
                        if (popupWindow != null) {
                                removeCallbacks(action);
                                popupWindow.dismiss();
                        }
                }
        };

        private void eventCancel() {
                if (listener != null) {
                        listener.onUp(index, strings.get(index));
                }
                removeCallbacks(cancelAction);
                postDelayed(cancelAction, 800);
        }

        public void setListener(IOnChoiceListener listener) {
                this.listener = listener;
        }

        private IOnChoiceListener listener;

        public interface IOnChoiceListener {

                void onChoice(int index);

                void onUp(int index, String s);

        }

}
