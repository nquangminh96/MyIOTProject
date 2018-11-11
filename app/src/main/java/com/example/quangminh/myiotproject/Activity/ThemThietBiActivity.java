package com.example.quangminh.myiotproject.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.example.quangminh.myiotproject.Adapter.SpinAdapter;
import com.example.quangminh.myiotproject.R;

import java.util.ArrayList;

public class ThemThietBiActivity extends AppCompatActivity {
    Spinner spinLoaiThietbi;
    ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themthietbi);
        spinLoaiThietbi = findViewById(R.id.spinLoaithietbi);
        data.add("Quat");
        data.add("Den");
        SpinAdapter myAdapter = new SpinAdapter(ThemThietBiActivity.this, R.layout.item_spinner_loaithietbi, data);
        spinLoaiThietbi.setAdapter(myAdapter);
    }
}
