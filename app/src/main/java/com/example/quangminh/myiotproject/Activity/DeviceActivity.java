package com.example.quangminh.myiotproject.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.Fragment.TimePicker;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeviceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String KEYINTENT = "roomName";
    public static final String LISTID = "List-ID";
    public static final String LISTUSER = "List-User";
    public static final String DEVICE = "Thiết Bị";
    public static final String INFO = "Thông Số";
    public static final String cache = "IDHomeCache";
    public static final String IDHOME = "IDHome";
    public static final String TEMP = "Nhiệt Độ";
    public static final String HUMIDITY = "Độ Ẩm";
    String IDHome;
    TextView txtRoomName, txtNhietDo, txtDeviceName;
    ImageView imgIconDevices;
    DatabaseReference myData = FirebaseDatabase.getInstance().getReference();
    Switch swOnOff;
    String deviceName, nameRoom;
    TextView textOn, textOff;
    Button btnControlTV, btnHengio , btnStopHengio;
    LinearLayout myLayout;
    RelativeLayout relativeLayout;
    SeekBar seekLevel;
    TextView txtTime, txtDate;
    boolean canChangeLv = true;
    int progressi;
    Button btnDialogOk, btnDialogOut;
    TextView txtTittle, txtTimerTittle,txtTimerTittle1, txtTimerTittle2;
    RadioGroup rgdb;
    int flagTimer = -1;
    String dateDialog = "", timeDialog = "";
    LinearLayout linearLayoutTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        getView();
        Intent intent = getIntent();
        nameRoom = intent.getStringExtra("nameRoom");
        deviceName = intent.getStringExtra("deviceName");
        IDHome = intent.getStringExtra(IDHOME);
        txtRoomName.setText(nameRoom);
        txtDeviceName.setText(deviceName);

        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("tv")) {
            imgIconDevices.setImageResource(R.drawable.tvwhite_bigicon);
            relativeLayout.setVisibility(View.VISIBLE);
            myLayout.setVisibility(View.INVISIBLE);
        }
        if (!StringUtils.removeAccent(deviceName).toLowerCase().contains("tv")) {
            relativeLayout.setVisibility(View.INVISIBLE);
            myLayout.setVisibility(View.VISIBLE);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("quat")) {
            imgIconDevices.setImageResource(R.drawable.fan_white_bigicon);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("đen")) {
            imgIconDevices.setImageResource(R.drawable.light_white_bigicon);
        }
        checkValue();

        myData.child(LISTID).child(IDHome).child(nameRoom).child(INFO).child(TEMP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.getValue().toString();
                temp = temp + " " + (char) 0x00B0 + "C";
                txtNhietDo.setText(temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    canChangeLv = false;
                    seekLevel.setEnabled(true);
                    myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int check = Integer.parseInt(dataSnapshot.getValue().toString());
                            if (check == 0) {
                                seekLevel.setProgress(0);
                                myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).setValue(1);
                            }
                            if (check > 0) {
                                seekLevel.setProgress(check);
                                //myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).setValue(check);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    canChangeLv = true;
                    seekLevel.setEnabled(false);
                    myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).setValue(0);
                    cannotTouch();
                    showLayoutOff();
                }
            }
        });
        textOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swOnOff.setChecked(false);
            }
        });
        textOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swOnOff.setChecked(true);

            }
        });
        btnControlTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swOnOff.isChecked()) {
                    Intent myIntent = new Intent(DeviceActivity.this, ActivityControlTV.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(DeviceActivity.this, getString(R.string.turn_on_tv_first), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (canChangeLv == true) {
            seekLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressi = progress;
                    //myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).setValue(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).setValue(progressi);
                }
            });
        }
        btnHengio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DeviceActivity.this);
                dialog.setTitle("Hẹn giờ");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_hengio);
                txtTime = dialog.findViewById(R.id.pickerTime);
                txtDate = dialog.findViewById(R.id.pickerDate);
                btnDialogOk = dialog.findViewById(R.id.btnHenGio);
                btnDialogOut = dialog.findViewById(R.id.btnThoat);
                txtTittle = dialog.findViewById(R.id.txtTittlee);
                txtTittle.setText("Hẹn giờ " + deviceName);
                rgdb = dialog.findViewById(R.id.rdgButton);
                rgdb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rbtnOff:
                                flagTimer = 0;
                                break;
                            case R.id.rbtnOn:
                                flagTimer = 1;
                                break;
                        }
                    }
                });

                txtTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment timePicker = new TimePicker();
                        timePicker.show(getSupportFragmentManager(), "Time");
                    }
                });
                txtDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment datePicker = new com.example.quangminh.myiotproject.Fragment.DatePicker();
                        datePicker.show(getSupportFragmentManager(), "Date");
                    }
                });
                btnDialogOk.setOnClickListener(new View.OnClickListener() {
                    String tittle0, tittle1, tittle2;

                    @Override
                    public void onClick(View v) {
                        if (flagTimer == -1) {
                            Toast.makeText(DeviceActivity.this, "Vui lòng chọn chế độ hẹn giờ", Toast.LENGTH_SHORT).show();
                        } else if (timeDialog == "") {
                            Toast.makeText(DeviceActivity.this, "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DeviceActivity.this, "Đã hẹn giờ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            if (flagTimer == 0) {
                                tittle0 = "Đã hẹn giờ tắt của " + deviceName;
                            }
                            if (flagTimer == 1) {
                                tittle0 = "Đã hẹn giờ bật của " + deviceName;
                            }

                            tittle1 = timeDialog;
                            tittle2 = dateDialog;
                            txtTimerTittle.setText(tittle0);
                            txtTimerTittle1.setText(tittle1);
                            txtTimerTittle2.setText(tittle2);
                            linearLayoutTimer.setVisibility(View.VISIBLE);
                            btnHengio.setVisibility(View.INVISIBLE);
                            flagTimer = -1;

                        }
                    }
                });
                btnDialogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btnStopHengio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutTimer.setVisibility(View.INVISIBLE);
                btnHengio.setVisibility(View.VISIBLE);
                Toast.makeText(DeviceActivity.this,"Đã dừng hẹn giờ" , Toast.LENGTH_SHORT).show();
            }
        });
        linearLayoutTimer.setVisibility(View.INVISIBLE);
        btnHengio.setVisibility(View.VISIBLE);

    }

    private void getView() {
        txtRoomName = findViewById(R.id.txtNameRoomOfDevice);
        txtNhietDo = findViewById(R.id.txtNhietDo);
        // txtDoAm = findViewById(R.id.txtDoAm);
        imgIconDevices = findViewById(R.id.imgIconDevice);
        txtDeviceName = findViewById(R.id.txtNameDevice);
        swOnOff = findViewById(R.id.onOff);
        textOff = findViewById(R.id.txtOff);
        textOn = findViewById(R.id.txtOn);
        btnControlTV = findViewById(R.id.btnControlTv);
        myLayout = findViewById(R.id.layout);
        relativeLayout = findViewById(R.id.myRelative);
        seekLevel = findViewById(R.id.seekbar);
        btnHengio = findViewById(R.id.btnTimer);
        linearLayoutTimer = findViewById(R.id.linearTimer);
        txtTimerTittle = findViewById(R.id.txtTimer0);
        txtTimerTittle1 = findViewById(R.id.txtTimer1);
        txtTimerTittle2 = findViewById(R.id.txtTimer2);
        btnStopHengio = findViewById(R.id.btnStopTimer);


    }

    private void showLayoutOn() {
        textOff.setTextColor(getResources().getColor(R.color.colorTextHint));
        textOn.setTextColor(getResources().getColor(R.color.colorWhite));

        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("quat")) {
            imgIconDevices.setImageResource(R.drawable.fan_white_bigicon);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("đen")) {
            imgIconDevices.setImageResource(R.drawable.light_white_bigicon);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("tv")) {
            imgIconDevices.setImageResource(R.drawable.tvwhite_bigicon);
        }
    }

    private void showLayoutOff() {
        textOn.setTextColor(getResources().getColor(R.color.colorTextHint));
        textOff.setTextColor(getResources().getColor(R.color.colorWhite));
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("quat")) {
            imgIconDevices.setImageResource(R.drawable.fan_bigicon_off);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("đen")) {
            imgIconDevices.setImageResource(R.drawable.light_bigicon_off);
        }
        if (StringUtils.removeAccent(deviceName).toLowerCase().contains("tv")) {
            imgIconDevices.setImageResource(R.drawable.tv_bigicon_off);
        }
    }

    private void cannotTouch() {
        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeviceActivity.this, getString(R.string.turn_on_device_first), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValue() {
        myData.child(LISTID).child(IDHome).child(nameRoom).child(DEVICE).child(deviceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int check = Integer.parseInt(dataSnapshot.getValue().toString());
                if (check == 0) {
                    swOnOff.setChecked(false);
                    showLayoutOff();
                    cannotTouch();
                    seekLevel.setEnabled(false);
                }
                if (check > 0) {
                    //choseLevel.setEnabled(true);
                    seekLevel.setEnabled(true);
                    showLayoutOn();
                    swOnOff.setChecked(true);
                    seekLevel.setProgress(check);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        // Toast.makeText(this, "Hour" + hourOfDay + "Min" + minute, Toast.LENGTH_SHORT).show();
        timeDialog = hourOfDay + " Giờ " + minute + " Phút";
        txtTime.setText(timeDialog);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Toast.makeText(this, "Year" + year + "Month" + month + "Day" + dayOfMonth, Toast.LENGTH_SHORT).show();
        dateDialog = "Ngày " + dayOfMonth + " Tháng " + month + " Năm " + year;
        txtDate.setText(dateDialog);
    }
}
