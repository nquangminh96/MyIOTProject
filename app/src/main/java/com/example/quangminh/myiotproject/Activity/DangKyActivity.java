package com.example.quangminh.myiotproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.quangminh.myiotproject.Model.User;
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

public class DangKyActivity extends AppCompatActivity {
    Button xacNhan;
    TextInputEditText edtuser, edtPass, edtCofirmpass, edtID;
    FirebaseAuth mAuth;
    DatabaseReference myData;
    String user, password, cofirmPass, idHome;
    boolean check;
    AlertDialog.Builder mBuilderWait;
    AlertDialog mDialogWait;
    String idHomeinFirebase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        getView();

        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangKy();
            }
        });
    }

    private void getView() {
        xacNhan = findViewById(R.id.btnXacNhan);
        edtuser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtCofirmpass = findViewById(R.id.edtConfirmPass);
        edtID = findViewById(R.id.edtIDHome);
        mAuth = FirebaseAuth.getInstance();
        myData = FirebaseDatabase.getInstance().getReference();
        mBuilderWait = new AlertDialog.Builder(DangKyActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewPleaseWait = inflater.inflate(R.layout.dialog_pleasewait, null);
        mBuilderWait.setView(viewPleaseWait);
        mDialogWait = mBuilderWait.create();
    }


    private void DangKy() {
        check = false;
        user = edtuser.getText().toString();
        password = edtPass.getText().toString();
        cofirmPass = edtCofirmpass.getText().toString();
        idHome = edtID.getText().toString();
        mDialogWait.show();
        if (user.length() == 0 || password.length() == 0 || cofirmPass.length() == 0 || idHome.length() == 0) {
            mDialogWait.dismiss();
            Toast.makeText(this,
                    getString(R.string.please_fill_in_all_required_filed),
                    Toast.LENGTH_SHORT).show();
        } else if (user.length() <5) {
            mDialogWait.dismiss();
            Toast.makeText(this,
                    getString(R.string.userLengthError),
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() <5) {
            mDialogWait.dismiss();
            Toast.makeText(this,
                    getString(R.string.passLengthError),
                    Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cofirmPass)) {
            mDialogWait.dismiss();
            Toast.makeText(this, getString(R.string.password_do_not_match), Toast.LENGTH_SHORT).show();
        } else {
            myData.child(allKeyStringsInApp.LISTID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        idHomeinFirebase = postData.getKey();
                        if (idHome.equals(postData.getKey())) {
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        mDialogWait.dismiss();
                        Toast.makeText(DangKyActivity.this, getString(R.string.id_home_not_exist), Toast.LENGTH_LONG).show();
                    } else {
                        myData.child(allKeyStringsInApp.LISTUSER).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(user).exists()) {
                                    mDialogWait.dismiss();
                                    Toast.makeText(DangKyActivity.this, getString(R.string.user_already_register), Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialogWait.dismiss();
                                    User myuser = new User(user, password, idHome, null);
                                    myData.child(allKeyStringsInApp.LISTUSER).child(user).setValue(myuser);
                                    Toast.makeText(DangKyActivity.this, getString(R.string.sign_up_successfully), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                mDialogWait.dismiss();
                                Toast.makeText(DangKyActivity.this, getString(R.string.connect_wifi), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mDialogWait.dismiss();
                    Toast.makeText(DangKyActivity.this, getString(R.string.connect_wifi), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
