    package com.example.quangminh.myiotproject.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;
    import android.widget.RelativeLayout;
    import android.widget.SeekBar;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.quangminh.myiotproject.R;
    import com.example.quangminh.myiotproject.StringUtils;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class DeviceActivity extends AppCompatActivity {
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
        Button btnControlTV;
        LinearLayout myLayout;
        RelativeLayout relativeLayout;
        SeekBar seekLevel;
        boolean canChangeLv = true;
        int progressi;


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
            if (canChangeLv==true) {
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
    }
