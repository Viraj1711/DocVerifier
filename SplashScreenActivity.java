package com.example.docverifier.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docverifier.Admin.AdminDashboard;
import com.example.docverifier.User.MainActivity;
import com.example.docverifier.Verifier.VerifierDashboard;
import com.example.docverifier.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Thread thread = new Thread()
        {
            public void run(){
                try
                {
                    sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally
                {
                    if (auth.getCurrentUser() != null){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(auth.getUid()));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                type = snapshot.child("userType").getValue(String.class);

                                switch (Objects.requireNonNull(type)) {
                                    case "User": {
                                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        break;
                                    }
                                    case "Verifier": {
                                        Intent intent = new Intent(SplashScreenActivity.this, VerifierDashboard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        break;
                                    }
                                    case "Admin": {
                                        Intent intent = new Intent(SplashScreenActivity.this, AdminDashboard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this , StartActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            }
        };thread.start();

    }
}