package com.example.quangminh.myiotproject.Activity;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.Fragment.ConfigEspFragment;
import com.example.quangminh.myiotproject.Fragment.HienthiRoomFragment;
import com.example.quangminh.myiotproject.Model.User;
import com.example.quangminh.myiotproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import com.example.quangminh.myiotproject.allKeyStringsInApp;
import com.google.firebase.database.ValueEventListener;

public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String keyIntent = "user";
    DatabaseReference myData = FirebaseDatabase.getInstance().getReference();
    TextView tenHienthi;
    Intent intent;
    DrawerLayout mydrawerLayout;
    NavigationView navigationView;
    Toolbar mytoolbar;
    android.support.v4.app.FragmentManager fragmentManager;
    Fragment fragment = null;
    String myEmail = "bkfet.iot@gmail.com";
    String IDHOME;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);
        getView();
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.list_roon));
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mydrawerLayout, mytoolbar, R.string.Mo, R.string.Dong) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mydrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        intent = getIntent();
        final String ten = intent.getStringExtra(keyIntent);
        myData.child(allKeyStringsInApp.LISTUSER).child(ten).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                IDHOME = user.getIdHome();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tenHienthi.setText(ten);
        fragmentManager = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction hienthithietbi = fragmentManager.beginTransaction();
        fragment = new HienthiRoomFragment();
        hienthithietbi.replace(R.id.content, fragment);
        hienthithietbi.commit();
    }

    private void getView() {
        mydrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationViewtrangchu);
        View view = navigationView.inflateHeaderView(R.layout.layout_header_navigation_trangchu);
        mytoolbar = findViewById(R.id.toolBar);
        tenHienthi = view.findViewById(R.id.txtTen);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemTrangchu:
                android.support.v4.app.FragmentTransaction hienthithietbi = fragmentManager.beginTransaction();
                fragment = new HienthiRoomFragment();
                hienthithietbi.replace(R.id.content, fragment);
                hienthithietbi.commit();
                item.setChecked(true);
                mydrawerLayout.closeDrawers();
                getSupportActionBar().setTitle(getString(R.string.list_roon));
                break;
            case R.id.itemConfig:
                android.support.v4.app.FragmentTransaction configesp = fragmentManager.beginTransaction();
                fragment = new ConfigEspFragment();
                configesp.replace(R.id.content, fragment);
                configesp.commit();
                item.setChecked(true);
                mydrawerLayout.closeDrawers();
                getSupportActionBar().setTitle(getString(R.string.config));
                break;
            case R.id.itemFeedBack:
                String[] recipient = new String[]{myEmail};
                String subject   = "Feedback from " + IDHOME;
                String message = "";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL , recipient);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT  ,message);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Chọn trình email"));
                break;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof HienthiRoomFragment){

            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setMessage(getString(R.string.want_exit));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setInverseBackgroundForced(true);
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        } else {
            backHomeScreen();
        }

    }

    private void backHomeScreen(){
        fragment = new HienthiRoomFragment();
        if (fragment!= null){
            android.support.v4.app.FragmentTransaction hienthithietbi = fragmentManager.beginTransaction();
            hienthithietbi.replace(R.id.content, fragment);
            hienthithietbi.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data!=null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String toast = result.get(0);
                    Toast.makeText(this, toast , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void ClickHere(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }
}
