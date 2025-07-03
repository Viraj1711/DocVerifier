package com.example.docverifier.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docverifier.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {

    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnNext.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }
}