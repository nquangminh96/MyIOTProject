package com.example.quangminh.myiotproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.Adapter.ListViewDeviceAdapter;
import com.example.quangminh.myiotproject.ConnectivityReceiver;
import com.example.quangminh.myiotproject.Model.ThietBi;
import com.example.quangminh.myiotproject.MyApplication;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.allKeyStringsInApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private SharedPreferences sharedPreferences;
    TextView txtNameRoom, txtNhietDo, txtDoAm;
    Button btnTurnOffAll;
    String IDHome;
    DatabaseReference myData = FirebaseDatabase.getInstance().getReference();
    ListView listDevice;
    ArrayList<ThietBi> dataListView = new ArrayList<>();
    ArrayList<ThietBi> dataInserver;
    String nameRoom;

    AlertDialog dialogInternet;
    AlertDialog.Builder builderDialogInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        getView();
        sharedPreferences = this.getSharedPreferences(allKeyStringsInApp.cache, Context.MODE_PRIVATE);
        IDHome = sharedPreferences.getString(allKeyStringsInApp.IDHOME, "No ID");
        Intent intent = getIntent();
        nameRoom = intent.getStringExtra(allKeyStringsInApp.KEYINTENT);
        txtNameRoom.setText(nameRoom);
        createDialogInternet();
        myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.INFO).child(allKeyStringsInApp.HUMIDITY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String humidity = dataSnapshot.getValue().toString();
                humidity = humidity + " %";
                txtDoAm.setText(humidity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.INFO).child(allKeyStringsInApp.TEMP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.getValue().toString();
                temp = temp + " " + (char)0x00B0 + "C";
                txtNhietDo.setText(temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ListViewDeviceAdapter listViewDeviceAdapter = new ListViewDeviceAdapter(this, R.layout.item_listview_devices_each_room, dataListView);
        listDevice.setAdapter(listViewDeviceAdapter);
        myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.DEVICE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataInserver = new ArrayList<>();
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    ThietBi thietBi = new ThietBi(postData.getKey(), Integer.parseInt(postData.getValue().toString()));
                    dataInserver.add(thietBi);
                }
                if (!dataListView.equals(dataInserver)) {
                    dataListView.clear();
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        ThietBi thietBi = new ThietBi(postData.getKey(), Integer.parseInt(postData.getValue().toString()));
                        dataListView.add(thietBi);
                        listViewDeviceAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnTurnOffAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0 ; i< dataListView.size() ; i++){
                    myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.DEVICE).child(dataListView.get(i).getName()).setValue(0);
                }
                Toast.makeText(RoomActivity.this , getString(R.string.turned_off_all_devices), Toast.LENGTH_SHORT).show();
            }
        });
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RoomActivity.this, DeviceActivity.class);
                i.putExtra("nameRoom" , nameRoom);
                i.putExtra("deviceName", dataListView.get(position).getName());

                i.putExtra(allKeyStringsInApp.IDHOME , IDHome);
               // Toast.makeText(RoomActivity.this,dataListView.get(position).getName() , Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }

    private void createDialogInternet(){
        builderDialogInternet = new AlertDialog.Builder(RoomActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewInternetWarning = inflater.inflate(R.layout.dialog_internet_warning, null);
        builderDialogInternet.setView(viewInternetWarning);
        dialogInternet = builderDialogInternet.create();
        dialogInternet.setCancelable(false);
        checkConnection();
    }

    private void getView() {
        txtNameRoom = findViewById(R.id.txtRoomName);
        txtNhietDo = findViewById(R.id.txtTemperature);
        txtDoAm = findViewById(R.id.txtHumidity);
        btnTurnOffAll = findViewById(R.id.btnTurnOffAllDevices);
        listDevice = findViewById(R.id.lvItemDevices);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            dialogInternet.show();
            Toast.makeText(RoomActivity.this, getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
            dialogInternet.cancel();
            //Toast.makeText(DangNhapActivity.this, getString(R.string.haveInternet)  , Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            dialogInternet.show();
            Toast.makeText(RoomActivity.this,  getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
            dialogInternet.cancel();
            Toast.makeText(RoomActivity.this, getString(R.string.haveInternet) , Toast.LENGTH_SHORT).show();
        }
    }
}
