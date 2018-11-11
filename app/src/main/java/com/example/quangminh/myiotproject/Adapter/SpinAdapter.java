package com.example.quangminh.myiotproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quangminh.myiotproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpinAdapter extends BaseAdapter {
    Activity context;
    int resource;
    ArrayList<String> data;

    public SpinAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<String> objects) {
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, null);
        CircleImageView circleImageView = convertView.findViewById(R.id.imgLoaithietbi);
        TextView tenLoai = convertView.findViewById(R.id.txtTenThietbi);
        tenLoai.setText(data.get(position));
        if (data.get(position) == "Quat") circleImageView.setImageResource(R.drawable.fan);
        if (data.get(position) == "Den") circleImageView.setImageResource(R.drawable.ic_light);


        return convertView;
    }
}
