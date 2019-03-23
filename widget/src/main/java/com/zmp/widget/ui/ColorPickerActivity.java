package com.zmp.widget.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zmp.widget.R;
import com.zmp.widget.shader.ColorPickerView;
import com.zmp.widget.utils.ColorSPUtils;
import com.zmp.widget.view.VerticalProgressView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ColorPickerActivity extends AppCompatActivity implements View.OnClickListener {

        private static final String TAG = "ColorPickerActivity";

        public static final String COLOR_KEY = "COLOR_KEY";

        private int verticalSpacing;

        private ColorPickerView colorPickerView;

        private EditText etR;

        private EditText etG;

        private EditText etB;

        private EditText etRGB;

        private VerticalProgressView verticalProgressView1;

        private VerticalProgressView verticalProgressView2;

        private GridView gridView;

        private TextWatcher textWatcher;

        private ImageView ivPreview;

        private ArrayList<Integer> integers;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                setContentView(R.layout.activity_color_picker);
                initView();
                initRgbView();
                initColorView();
                initGridView();
        }

        private void initRgbView() {
                textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                Log.d(TAG, "beforeTextChanged: " + s);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Log.d(TAG, "onTextChanged: " + s);
                                getColor();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                                Log.d(TAG, "afterTextChanged: " + s);
                                etR.setSelection(etR.length());
                                etG.setSelection(etG.length());
                                etB.setSelection(etB.length());
                        }
                };

                TextWatcher rgbTextWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                Log.d(TAG, "beforeTextChanged: " + s);
                                etR.removeTextChangedListener(textWatcher);
                                etG.removeTextChangedListener(textWatcher);
                                etB.removeTextChangedListener(textWatcher);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Log.d(TAG, "onTextChanged: " + s);
                                if (!TextUtils.isEmpty(s)) {
                                        int color;
                                        try {
                                                color = Color.parseColor("#" + s.toString().trim());
                                        }
                                        catch (IllegalArgumentException e) {
                                                e.printStackTrace();
                                                return;
                                        }
                                        int r = Color.red(color);
                                        int g = Color.green(color);
                                        int b = Color.blue(color);
                                        etR.setText(String.valueOf(r));
                                        etG.setText(String.valueOf(g));
                                        etB.setText(String.valueOf(b));
                                        ivPreview.setImageDrawable(new ColorDrawable(color));
                                }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                                Log.d(TAG, "afterTextChanged: " + s);
                                etR.addTextChangedListener(textWatcher);
                                etG.addTextChangedListener(textWatcher);
                                etB.addTextChangedListener(textWatcher);
                        }
                };

                etR.addTextChangedListener(textWatcher);
                etG.addTextChangedListener(textWatcher);
                etB.addTextChangedListener(textWatcher);
                etRGB.addTextChangedListener(rgbTextWatcher);
        }

        private void getColor() {
                String r = etR.getText().toString().trim();
                String g = etG.getText().toString().trim();
                String b = etB.getText().toString().trim();
                int cR;
                int cG;
                int cB;
                if (TextUtils.isEmpty(r)) {
                        cR = 0;
                }
                else {
                        cR = Integer.valueOf(r);
                }
                if (TextUtils.isEmpty(g)) {
                        cG = 0;
                }
                else {
                        cG = Integer.valueOf(g);
                }
                if (TextUtils.isEmpty(b)) {
                        cB = 0;
                }
                else {
                        cB = Integer.valueOf(b);
                }
                String r1 = Integer.toHexString(cR);
                String g1 = Integer.toHexString(cG);
                String b1 = Integer.toHexString(cB);
                String a1 = Integer.toHexString((int) (colorPickerView.getAlpha() * 255));
                String text = String.format("%1$s%2$s%3$s%4$s",
                                            a1.length() == 2 ? a1 : ("0" + a1),
                                            r1.length() == 2 ? r1 : ("0" + r1),
                                            g1.length() == 2 ? g1 : ("0" + g1),
                                            b1.length() == 2 ? b1 : ("0" + b1)
                                           ).toUpperCase();
                etRGB.setText(text);
        }

        private void initView() {
                colorPickerView = findViewById(R.id.colorPickerView);
                ivPreview = findViewById(R.id.iv_preview);
                etR = findViewById(R.id.et_r);
                etG = findViewById(R.id.et_g);
                etB = findViewById(R.id.et_b);
                etRGB = findViewById(R.id.et_rgb);
                verticalProgressView1 = findViewById(R.id.verticalPV1);
                verticalProgressView2 = findViewById(R.id.verticalPV2);
                gridView = findViewById(R.id.gridView);
                findViewById(R.id.bt_choose).setOnClickListener(this);
                findViewById(R.id.bt_cancel).setOnClickListener(this);
        }

        private void initColorView() {
                colorPickerView.setChoiceColorListener(new ColorPickerView.IChoiceColorListener() {
                        @Override
                        public void onChoiceColor(int color, String a1, int r, int g, int b, String text) {
                                Log.e("ColorPickerView", "onChoiceColor: " + text);
                                etRGB.setText(text);
                        }
                });
                verticalProgressView1.setIVoiceProgress(new VerticalProgressView.IVoiceProgress() {
                        @Override
                        public void onChoose(int progress) {
                                colorPickerView.setAlpha(progress / 100F);
                                getColor();
                        }
                });
                verticalProgressView2.setIVoiceProgress(new VerticalProgressView.IVoiceProgress() {
                        @Override
                        public void onChoose(int progress) {
                                colorPickerView.setColorPercent(progress / 100F);
                        }
                });
                colorPickerView.setAlpha(verticalProgressView1.getProgress()/ 100F);
                colorPickerView.setColorPercent(verticalProgressView2.getProgress()/ 100F);
                getColor();
        }

        private void initGridView() {
                String colors = ColorSPUtils.getString(this, ColorSPUtils.COLOR_KEY, "");
                if (TextUtils.isEmpty(colors)) {
                        integers = new ArrayList<>();
                }
                else {
                        Type type = new TypeToken<List<Integer>>() {
                        }.getType();
                        integers = new Gson().fromJson(colors, type);
                }
                if (integers.size() > 0) {
                        setPositionColor(integers.size() - 1);
                }

                gridView.post(new Runnable() {
                        @Override
                        public void run() {
                                verticalSpacing = gridView.getVerticalSpacing();
                                gridView.setAdapter(adapter);
                        }
                });

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (integers.size() > position) {
                                        setPositionColor(position);
                                }
                        }
                });
        }

        private BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                        return 16;
                }

                @Override
                public Object getItem(int position) {
                        return position;
                }

                @Override
                public long getItemId(int position) {
                        return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        MyHolder myHolder;
                        if (convertView == null) {
                                convertView = View.inflate(parent.getContext(), R.layout.color_gv_item, null);
                                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                                                     ViewGroup.LayoutParams.MATCH_PARENT);
                                layoutParams.height = parent.getHeight() / 2 - verticalSpacing;
                                convertView.setLayoutParams(layoutParams);
                                myHolder = new MyHolder();
                                convertView.setTag(myHolder);
                                myHolder.imageView = convertView.findViewById(R.id.gv_item_iv);
                        }
                        else {
                                myHolder = ((MyHolder) convertView.getTag());
                        }

                        if (integers.size() > position) {
                                myHolder.imageView.setImageDrawable(new ColorDrawable(integers.get(position)));
                        }
                        else {
                                myHolder.imageView.setImageDrawable(new ColorDrawable(Color.WHITE));
                        }
                        return convertView;
                }
        };

        private void setPositionColor(int position) {
                int color = integers.get(position);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int alpha = Color.alpha(color);
                colorPickerView.setAlpha(alpha / 255F);
                verticalProgressView1.setProgress((int) (alpha * 100 / 255F));
                etR.setText(String.valueOf(r));
                etG.setText(String.valueOf(g));
                etB.setText(String.valueOf(b));
        }

        @Override
        public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.bt_choose) {
                        try {
                                int color = ((ColorDrawable) ivPreview.getDrawable()).getColor();
                                integers.add(color);
                                if (integers.size() > 16) {
                                        integers.remove(0);
                                }
                                String toJson = new Gson().toJson(integers);
                                ColorSPUtils.saveString(this, ColorSPUtils.COLOR_KEY, toJson);
                                setResult(RESULT_OK, new Intent().putExtra(COLOR_KEY, color));
                                finish();
                        }
                        catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "onClick: ", e);
                        }
                }
                else if (i == R.id.bt_cancel) {
                        setResult(RESULT_CANCELED);
                        finish();
                }
        }

        @Override
        public void onBackPressed() {
                setResult(RESULT_CANCELED);
                super.onBackPressed();
        }

        private class MyHolder {

                private ImageView imageView;
        }

}
