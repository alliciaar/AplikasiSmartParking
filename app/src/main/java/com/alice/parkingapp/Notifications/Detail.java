package com.alice.parkingapp.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.parkingapp.Model.Pesanan;
import com.alice.parkingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Detail extends AppCompatActivity {

    Intent intent;
    String id, slot, start, finish, status, pemesan, plat;
    String TAG = "GenerateQrCode";
    ImageView imageView;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView = (TextView) findViewById(R.id.txt);
        imageView = (ImageView) findViewById(R.id.image);
        intent = getIntent();
        id = intent.getStringExtra("id");
        slot = intent.getStringExtra("slot");
        start = intent.getStringExtra("start");
        finish = intent.getStringExtra("finish");
        status = intent.getStringExtra("status");
        pemesan = intent.getStringExtra("pemesan");
        plat = intent.getStringExtra("plat");

        FirebaseDatabase.getInstance().getReference("Pesanan").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pesanan pesanan = dataSnapshot.getValue(Pesanan.class);
                Toast.makeText(Detail.this, pesanan.getStatus(), Toast.LENGTH_SHORT).show();
                if (pesanan.getStatus().equals("kosong")){
                finish();
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        if (id==null){
            textView.setText("null");
        }
        else {
            textView.setText(id);
        }

            WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int widht = point.x;
            int height = point.y;
            int smallerdimension = widht<height ? widht:height;
            smallerdimension = smallerdimension*3/4;
            qrgEncoder = new QRGEncoder(id, null, QRGContents.Type.TEXT,smallerdimension);
            try {
                bitmap=qrgEncoder.encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                Log.v(TAG,e.toString());
            }
}
    }


