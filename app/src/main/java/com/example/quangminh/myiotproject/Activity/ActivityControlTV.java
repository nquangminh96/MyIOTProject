package com.example.quangminh.myiotproject.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.quangminh.myiotproject.CheckConnect.ConnectivityReceiver;
import com.example.quangminh.myiotproject.CheckConnect.MyApplication;
import com.example.quangminh.myiotproject.R;

public class ActivityControlTV extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    Toolbar toolbar;

    AlertDialog dialogInternet;
    AlertDialog.Builder builderDialogInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_tv);
        getView();
        toolbar.setTitle("Điều Khiển TV");

        createDialogInternet();
    }

    private void createDialogInternet(){
        builderDialogInternet = new AlertDialog.Builder(ActivityControlTV.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewInternetWarning = inflater.inflate(R.layout.dialog_internet_warning, null);
        builderDialogInternet.setView(viewInternetWarning);
        dialogInternet = builderDialogInternet.create();
        dialogInternet.setCancelable(false);
        checkConnection();
    }
    private void getView() {
        toolbar = findViewById(R.id.ToolbarControlTV);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            dialogInternet.show();
            Toast.makeText(ActivityControlTV.this, getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
            dialogInternet.cancel();
            //Toast.makeText(DangNhapActivity.this, getString(R.string.haveInternet)  , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            dialogInternet.show();
            Toast.makeText(ActivityControlTV.this,  getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
            dialogInternet.cancel();
            Toast.makeText(ActivityControlTV.this, getString(R.string.haveInternet) , Toast.LENGTH_SHORT).show();
        }
    }
}
