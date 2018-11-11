package com.example.quangminh.myiotproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quangminh.myiotproject.Model.ThietBi;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.StringUtils;

import java.util.ArrayList;

public class ListViewDeviceAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<ThietBi> dataListView;
    ViewHolder viewHolder;

    public ListViewDeviceAdapter(Context context, int layout, ArrayList<ThietBi> dataListView) {
        this.context = context;
        this.layout = layout;
        this.dataListView = dataListView;
    }

    @Override
    public int getCount() {
        return dataListView.size();
    }

    @Override
    public Object getItem(int position) {
        return dataListView.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listview_devices_each_room, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iconDevice = view.findViewById(R.id.imgDevices);
            viewHolder.txtDeviceName = view.findViewById(R.id.txtDeviceName);
            viewHolder.state = view.findViewById(R.id.txtDeviceState);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ThietBi thietBi = dataListView.get(position);
        String nameDevice = thietBi.getName();
        int levelDevice = thietBi.getLevel();
        String stateDevice = "";
        if (levelDevice == 0) {
            stateDevice = context.getString(R.string.off);
        } else {
            stateDevice = context.getString(R.string.inLevel) + " " +levelDevice;
        }
        viewHolder.txtDeviceName.setText(nameDevice);
        viewHolder.state.setText(stateDevice);
        if (StringUtils.removeAccent(nameDevice.trim()).toLowerCase().contains("đen"))
            viewHolder.iconDevice.setImageResource(R.drawable.light2);
        if (StringUtils.removeAccent(nameDevice.trim()).toLowerCase().contains("quat"))
            viewHolder.iconDevice.setImageResource(R.drawable.fan2);
        if (StringUtils.removeAccent(nameDevice.trim()).toLowerCase().contains("bep"))
            viewHolder.iconDevice.setImageResource(R.drawable.gas2);
//        if (nameDevice.trim().contains("Rem"))
//            viewHolder.iconDevice.setImageResource(R.drawable.bathroom2);
        if (StringUtils.removeAccent(nameDevice.trim()).toLowerCase().contains("tv"))
            viewHolder.iconDevice.setImageResource(R.drawable.tv22);
        return view;
    }

    public class ViewHolder {
        ImageView iconDevice;
        TextView txtDeviceName;
        TextView state;
    }
}
