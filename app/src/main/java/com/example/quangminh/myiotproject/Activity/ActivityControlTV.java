package com.example.quangminh.myiotproject.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.quangminh.myiotproject.R;

public class ActivityControlTV extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_tv);
        getView();
        toolbar.setTitle("Điều Khiển TV");

    }

    private void getView() {
        toolbar = findViewById(R.id.ToolbarControlTV);
    }
}
