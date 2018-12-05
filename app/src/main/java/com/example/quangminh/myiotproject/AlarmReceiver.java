package com.example.quangminh.myiotproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    DatabaseReference myData = FirebaseDatabase.getInstance().getReference();
    @Override
    public void onReceive(Context context, Intent intent) {
        String nameRoom = intent.getStringExtra("nameRoom");
        String deviceName = intent.getStringExtra("deviceName");
        String IDHome = intent.getStringExtra("IDHome");
        int mode = intent.getIntExtra("mode",1);
        if (mode == 1) myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.DEVICE).child(deviceName).setValue(1);
        if (mode == 0) myData.child(allKeyStringsInApp.LISTID).child(IDHome).child(nameRoom).child(allKeyStringsInApp.DEVICE).child(deviceName).setValue(0);
        String  i = nameRoom + " " + deviceName + " " + IDHome + " " + mode ;
        Log.e("ABC",i);
    }
}
