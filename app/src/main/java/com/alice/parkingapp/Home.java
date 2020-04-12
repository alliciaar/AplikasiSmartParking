package com.alice.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alice.parkingapp.Class.BottomNavigationViewHelper;
import com.alice.parkingapp.Class.InternetConnection;
import com.alice.parkingapp.Class.Session;
import com.alice.parkingapp.Fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;
import java.util.Locale;

public class Home extends AppCompatActivity {
    public final static int ACTIVITY_NULL = 0;
    private static final String TAG = "Home";

    Toolbar toolbar;
    TextView text_toolbar;
    RelativeLayout inti;

    Button setData;
    EditText nama, plat;

    String name, platNomor;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    InternetConnection internetConnection;

    Session session;
    HashMap<String, String> map;

    @Override
    protected void onStart() {
        super.onStart();
        if (!session.loggedin()){
            DialogForm();
        }
        FirebaseDatabase.getInstance().getReference("Lokasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                session = new Session(Home.this);
                session.storeLogin(String.valueOf(dataSnapshot.child("latitude").getValue(Double.class)), String.valueOf(dataSnapshot.child("longitude").getValue(Double.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String languageToLoad  = "in";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        map = new HashMap<>();

        session = new Session(this);
        map = session.getDetailLogin();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        text_toolbar = (TextView)findViewById(R.id.text_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        text_toolbar.setText("Smart Park");


        setupBottomNavigationView();


        inti = (RelativeLayout)findViewById(R.id.inti);


//        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    // untuk menampilkan dialog
    public void DialogForm() {
        dialog = new AlertDialog.Builder(Home.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alert_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Data User");

        nama    = (EditText) dialogView.findViewById(R.id.nama);
        plat    = (EditText) dialogView.findViewById(R.id.plat);


        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("nama", nama.getText().toString());
                map.put("plat", plat.getText().toString());

                FirebaseDatabase.getInstance().getReference("User")
                        .child(firebaseUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        session.setLoggedIn(true);
                        startActivity(new Intent(Home.this, Home.class));
                        finish();
                    }
                });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                DialogForm();
//                startActivity(new Intent(Home.this, RetrieveMapsActivity2.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        HomeFragment homeFragment = new HomeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("lattitude", map.get(session.KEY_LOKASI_LATTITUDE));
        bundle.putString("longitude", map.get(session.KEY_LOKASI_LONGITUDE));

        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_place, homeFragment)
                .commit();

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(Home.this, bottomNavigationViewEx, map.get(session.KEY_LOKASI_LATTITUDE), map.get(session.KEY_LOKASI_LONGITUDE));

    }

//    private void updateToken(String token) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token1 = new Token(token);
//        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
//    }
}
