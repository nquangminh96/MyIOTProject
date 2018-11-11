package com.example.quangminh.myiotproject.Adapter;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.Activity.DangNhapActivity;
import com.example.quangminh.myiotproject.Activity.RoomActivity;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.StringUtils;

import java.util.ArrayList;

public class GridViewRoomAdapter extends BaseAdapter {
    public static final String KEYINTENT = "roomName";
    Context context;
    int layout;
    ArrayList<String> dataGridView;
    ViewHolder viewHolder;

    public GridViewRoomAdapter(Context context, int layout, ArrayList<String> dataGridView) {
        this.context = context;
        this.layout = layout;
        this.dataGridView = dataGridView;
    }

    @Override
    public int getCount() {
        return dataGridView.size();
    }

    @Override
    public Object getItem(int position) {
        return dataGridView.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_gridview_room, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iconRoom = view.findViewById(R.id.imgView);
            viewHolder.txtRoomName = view.findViewById(R.id.txt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtRoomName.setText(dataGridView.get(position));
        if (StringUtils.removeAccent(dataGridView.get(position)).toLowerCase().contains("phong khach"))
            viewHolder.iconRoom.setImageResource(R.drawable.livingroom2);
        if (StringUtils.removeAccent(dataGridView.get(position)).toLowerCase().contains("phong ngu"))
            viewHolder.iconRoom.setImageResource(R.drawable.bedroom2);
        if (StringUtils.removeAccent(dataGridView.get(position)).toLowerCase().contains("phong an"))
            viewHolder.iconRoom.setImageResource(R.drawable.kitchen2);
        if (StringUtils.removeAccent(dataGridView.get(position)).toLowerCase().contains("phong tam"))
            viewHolder.iconRoom.setImageResource(R.drawable.bathroom2);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomActivity.class);
                intent.putExtra(KEYINTENT, dataGridView.get(position));
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        ImageView iconRoom;
        TextView txtRoomName;
    }

}
