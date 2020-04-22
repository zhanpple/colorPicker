package com.zmp.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zmp.widget.view.SimpleCircleProgress;

/**
 * Created by zmp on 2020/4/21 10:20
 *
 * @author zmp
 */
public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main_act2);
                SimpleCircleProgress circleProgress = (SimpleCircleProgress) findViewById(R.id.scp);
                circleProgress.setProgressColors(Color.RED, Color.GREEN, Color.YELLOW,Color.RED);
        }

}
