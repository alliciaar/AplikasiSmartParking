package com.alice.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alice.parkingapp.Class.Session;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {

    private final int REQUEST_LOGIN = 1000;

    CardView login2, login3;
    CardView login;

    Session session;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        } else {
            session.setLoggedIn(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);

        login = (CardView)findViewById(R.id.satu);
        login2 = (CardView)findViewById(R.id.dua);
        login3 = (CardView)findViewById(R.id.tiga);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> provider1 = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                );
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(provider1)
                                .build(),
                        REQUEST_LOGIN);
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> provider2 = Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()
                );
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(provider2)
                                .build(),
                        REQUEST_LOGIN);
            }
        });

        login3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> provider3 = Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                );
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(provider3)
                                .build(),
                        REQUEST_LOGIN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", user.getUid());
                    map.put("nama", "kosong");
                    map.put("plat", "kosong");
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(Login.this, Home.class));
                            finish();
                            return;
                        }
                    });
                } else {
                    if (response == null) {
                        Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, "Tidak ada Internet", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        Toast.makeText(this, "Error Tidak Diketahui", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                Toast.makeText(this, "Error Sign In Tidak Diketahui !!!", Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                //Toast.makeText(Login.this, "Gagal ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
