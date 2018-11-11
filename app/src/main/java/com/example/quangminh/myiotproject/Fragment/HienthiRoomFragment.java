package com.example.quangminh.myiotproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quangminh.myiotproject.Activity.DangKyActivity;
import com.example.quangminh.myiotproject.Activity.TrangChuActivity;
import com.example.quangminh.myiotproject.Adapter.GridViewRoomAdapter;
import com.example.quangminh.myiotproject.Model.User;
import com.example.quangminh.myiotproject.R;
import com.example.quangminh.myiotproject.Activity.ThemThietBiActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HienthiRoomFragment extends android.support.v4.app.Fragment {
    public static final String LISTID = "List-ID";
    public static final String LISTUSER = "List-User";
    public static final String cache = "IDHomeCache";
    public static final String IDHOME = "IDHome";
    public static final String keyIntent = "user";
    public static int REQUEST_CODE = 111;
    GridView gridViewRoom;
    ArrayList<String> data = new ArrayList<>();
    DatabaseReference myData;
    private SharedPreferences sharedPreferences;
    String IDHome;
    GridViewRoomAdapter gridViewRoomAdapter;
    ArrayList<String> dataInServer ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hienthiroom, container, false);
        setHasOptionsMenu(true);
        myData = FirebaseDatabase.getInstance().getReference();
        gridViewRoom = view.findViewById(R.id.grdRoom);
        sharedPreferences = this.getActivity().getSharedPreferences(cache, Context.MODE_PRIVATE);
        IDHome = sharedPreferences.getString(IDHOME, "No ID");
        gridViewRoomAdapter = new GridViewRoomAdapter(getContext(), R.layout.item_gridview_room, data);
        gridViewRoom.setAdapter(gridViewRoomAdapter);

        myData.child(LISTID).child(IDHome).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataInServer = new ArrayList<>();
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    dataInServer.add(postData.getKey());
                    //gridViewRoomAdapter.notifyDataSetChanged();
                }
                if (!data.equals(dataInServer)){
                    data.clear();
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        data.add(postData.getKey());
                        gridViewRoomAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemThemthietbi = menu.add(1, R.id.itemThemthietbi, 1, getString(R.string.add_device));
        itemThemthietbi.setIcon(R.drawable.ic_add);
        itemThemthietbi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemThemthietbi:
                Intent intentThemThietbi = new Intent(getActivity(), ThemThietBiActivity.class);
                startActivityForResult(intentThemThietbi, REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
