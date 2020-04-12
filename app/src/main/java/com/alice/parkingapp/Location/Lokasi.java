package com.alice.parkingapp.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.parkingapp.Class.Session;
import com.alice.parkingapp.Home;
import com.alice.parkingapp.Model.User;
import com.alice.parkingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Lokasi extends AppCompatActivity {

    Session session;
    HashMap<String, String> map;

    String lattitude, longitude, id;
    Intent intent;

    TextView jarak;

    Double distanceDouble;
    Float distanceFloat;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);

        session = new Session(this);
        map = session.getDetailLogin();
        intent = getIntent();
        lattitude = intent.getStringExtra("lattitude");
        longitude = intent.getStringExtra("longitude");
        id = intent.getStringExtra("id");

        jarak = (TextView)findViewById(R.id.jarak);

        Location locationCurrent = new Location("Sekarang");
        locationCurrent.setLatitude(Double.parseDouble(lattitude));
        locationCurrent.setLongitude(Double.parseDouble(longitude));

        Location locationTujuan = new Location("Tujuan");
        locationTujuan.setLatitude(Double.parseDouble(map.get(session.KEY_LOKASI_LATTITUDE)));
        locationTujuan.setLongitude(Double.parseDouble(map.get(session.KEY_LOKASI_LONGITUDE)));

        distanceFloat = locationCurrent.distanceTo(locationTujuan) / 1000;
        distanceDouble = (double)(Math.round(distanceFloat*100)) / 100;

        if (distanceDouble > 1.0){
            Toast.makeText(Lokasi.this, "Lokasi anda terlalu jauh, minimal 7m", Toast.LENGTH_LONG).show();
            finish();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Lokasi.this).create();
            alertDialog.setTitle("Jarak anda "+String.valueOf(distanceDouble)+" Km");
            alertDialog.setMessage("Anda memesan tempat parkir ini?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YA",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d HH:mm");
                            String Date = simpleDateFormat.format(calendar.getTime());

                            String languageToLoad  = "in";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;

                            FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            if (user.getPlat().equals("kosong")){
                                                Toast.makeText(Lokasi.this, "Isi data user", Toast.LENGTH_LONG).show();
                                                ((Home)getApplicationContext()).DialogForm();
                                            }else {
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Slot");
                                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            if (snapshot.child("id").getValue(String.class).equals(id)){
                                                                snapshot.getRef().child("status").setValue("terisi");
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                                HashMap hashMap = new HashMap<>();
                                                map.put("status", "terisi");
                                                map.put("waktu", Date);
                                                map.put("id", id);

                                                reference1.updateChildren(hashMap);

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pesanan");
                                                String key = reference.push().getKey();
                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("id", key);
                                                map.put("slot", id);
                                                map.put("plat", user.getPlat());
                                                map.put("pemesan", user.getId());
                                                map.put("status", "Unchecked");
                                                map.put("start", Date);
                                                map.put("finish", "finish");
                                                reference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(Lokasi.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(Lokasi.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        finish();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        jarak.setText(String.valueOf(distanceDouble));

    }
}
