package com.wangxiao.videorecorddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView svRecord = findViewById(R.id.sv_record);
        ImageView ivClose = findViewById(R.id.iv_close);
        ImageView ivSwitchPhoto = findViewById(R.id.iv_switch_photo);
        TextView tvFinish = findViewById(R.id.tv_finish);
    }
}