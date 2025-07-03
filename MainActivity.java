package com.example.docverifier.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.docverifier.Adapters.docAdapter;
import com.example.docverifier.LoginRegister.LoginActivity;
import com.example.docverifier.Model.docModel;
import com.example.docverifier.R;
import com.example.docverifier.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ActionBarDrawerToggle toggle;
    private final List<docModel> docList = new ArrayList<>();
    String docId, userID, docName, document, verifier, verified, reason;
    docAdapter docAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("VerifyDocs");

        toggle=new ActionBarDrawerToggle(MainActivity.this,binding.drawer,binding.toolbar, R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                docList.clear();
                for (DataSnapshot doc : snapshot.getChildren()){
                    if (doc.child("userID").getValue(String.class).equals(user.getUid())){
                        docId = doc.child("docId").getValue(String.class);
                        userID = doc.child("userID").getValue(String.class);
                        docName = doc.child("docName").getValue(String.class);
                        document = doc.child("document").getValue(String.class);
                        verifier = doc.child("verifier").getValue(String.class);
                        verified = doc.child("verified").getValue(String.class);
                        reason = doc.child("reason").getValue(String.class);

                        docModel list = new docModel(docId, userID, docName, document, verifier, verified, reason);
                        docList.add(list);
                    }
                    docAdapter = new docAdapter(docList, getBaseContext());
                    binding.userRecyclerView.setAdapter(docAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.navMenu.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.verifyCertificate){
                Intent i = new Intent(MainActivity.this, VerifyCertificateActivity.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.requiredDocuments){
                Intent i = new Intent(MainActivity.this, RequiredDocumentActivity.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.requestNotary){
                Intent i = new Intent(MainActivity.this, CheckNotaryActivity.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.updateUser){
                Intent i = new Intent(MainActivity.this, UpdateProfileActivity.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.logoutUser){
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishAffinity();
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                binding.drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        });
    }
}