package com.example.docverifier.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docverifier.Admin.AdminDashboard;
import com.example.docverifier.User.MainActivity;
import com.example.docverifier.Verifier.VerifierDashboard;
import com.example.docverifier.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    String emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        binding.txtSignUpUser.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, UserRegistrationActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.btnLogin.setOnClickListener(v -> {

            String customerEmail = binding.edUserEmail.getText().toString();
            String customerPassword = binding.edUserPassword.getText().toString();

            if (!customerEmail.matches(emailPattern) || customerEmail.isEmpty()) {
                binding.edUserEmail.setError("Enter correct email");
            } else if (customerPassword.isEmpty() || customerPassword.length() < 8) {
                binding.edUserPassword.setError("Enter the proper password");
            } else {
                progressDialog.setTitle("Login...");
                progressDialog.setMessage("Please wait while your login is getting done.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(customerEmail, customerPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(auth.getUid()));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userType = snapshot.child("userType").getValue(String.class);

                                assert userType != null;
                                switch (userType) {
                                    case "User": {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "User Login Successful.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case "Verifier": {
                                        Intent intent = new Intent(LoginActivity.this, VerifierDashboard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Verifier Login Successful.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case "Admin": {
                                        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Admin Login Successful.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("TAG", "onFailure: " + e.getMessage());
                });
            }
        });

    }
}