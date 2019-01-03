package com.example.quangminh.myiotproject.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.ConnectivityReceiver;
import com.example.quangminh.myiotproject.Model.User;
import com.example.quangminh.myiotproject.MyApplication;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.allKeyStringsInApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhapActivity extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static final String keyIntent = "user";
    TextInputEditText userName, passWord;
    CheckBox checkBox;
    Button login;
    TextView signUp, forgotPass;

    AlertDialog.Builder mBuilderWait;
    AlertDialog mDialogWait;
    DatabaseReference myData;
    boolean check, checkLogin;
    private SharedPreferences sharedPreferences;

    AlertDialog dialogInternet;
    AlertDialog.Builder builderDialogInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        getView();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangKy = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(dangKy);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });

        createDialogInternet();


    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            dialogInternet.show();
            Toast.makeText(DangNhapActivity.this, getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
             dialogInternet.cancel();
            //Toast.makeText(DangNhapActivity.this, getString(R.string.haveInternet)  , Toast.LENGTH_SHORT).show();
        }
    }


    private void createDialogInternet(){
        builderDialogInternet = new AlertDialog.Builder(DangNhapActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewInternetWarning = inflater.inflate(R.layout.dialog_internet_warning, null);
        builderDialogInternet.setView(viewInternetWarning);
        dialogInternet = builderDialogInternet.create();
        dialogInternet.setCancelable(false);
        checkConnection();
    }

    private void getView() {
        userName = findViewById(R.id.edtUserName);
        passWord = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.txtSignUpAccount);
        checkBox = findViewById(R.id.checkRemember);
        forgotPass = findViewById(R.id.txtForgotPass);

        mBuilderWait = new AlertDialog.Builder(DangNhapActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewPleaseWait = inflater.inflate(R.layout.dialog_pleasewait, null);
        mBuilderWait.setView(viewPleaseWait);
        mDialogWait = mBuilderWait.create();
        myData = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences(allKeyStringsInApp.cache, Context.MODE_PRIVATE);
    }

    private void DangNhap() {
        checkLogin = false;
        mDialogWait.show();
        final String username = userName.getText().toString();
        final String password = passWord.getText().toString();
        check = checkBox.isChecked();
        if (username.length() == 0 || password.length() == 0) {
            mDialogWait.dismiss();
            Toast.makeText(this,
                    getString(R.string.please_fill_in_all_required_filed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // final ProgressDialog progressDialog = new ProgressDialog(DangNhapActivity.this , R.style.AppTheme_DarkDialog);
            myData.child(allKeyStringsInApp.LISTUSER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        String userInSever = postData.getKey();
                        if (username.equals(userInSever)) {
                            checkLogin = true;
                            break;
                        }
                    }
                    if (checkLogin) {
                        User user = dataSnapshot.child(username).getValue(User.class);
                        assert user != null;
                        if (user.getPassword().equals(password)) {
                            mDialogWait.dismiss();
                            Toast.makeText(DangNhapActivity.this, getString(R.string.sign_in_successfully), Toast.LENGTH_SHORT).show();
                            if (check == false) {
                                passWord.setText("");
                                userName.setText("");
                                checkBox.setChecked(false);
                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(allKeyStringsInApp.IDHOME, user.getIdHome());
                            editor.apply();
                            Intent intent = new Intent(DangNhapActivity.this, TrangChuActivity.class);
                            intent.putExtra(keyIntent, username);
                            startActivity(intent);
                        } else {
                            mDialogWait.dismiss();
                            Toast.makeText(DangNhapActivity.this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        mDialogWait.dismiss();
                        Toast.makeText(DangNhapActivity.this, getString(R.string.account_not_exist), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mDialogWait.dismiss();
                    Toast.makeText(DangNhapActivity.this, getString(R.string.sign_in_fail), Toast.LENGTH_SHORT).show();
                }

            });
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
            Toast.makeText(DangNhapActivity.this,  getString(R.string.noInternet) , Toast.LENGTH_SHORT).show();
        }
        else{
            dialogInternet.cancel();
            Toast.makeText(DangNhapActivity.this, getString(R.string.haveInternet) , Toast.LENGTH_SHORT).show();
        }
    }


}
