package com.example.docverifier.Verifier;

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
import com.example.docverifier.databinding.ActivityVerifierDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VerifierDashboard extends AppCompatActivity {

    ActivityVerifierDashboardBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ActionBarDrawerToggle toggle;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Users");
    private final List<docModel> docModelList = new ArrayList<>();
    String docId, userID, docName, document, verifier, verified, reason;
    String loggedInVerifier;
    docAdapter docAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifierDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        toggle=new ActionBarDrawerToggle(VerifierDashboard.this,binding.drawer,binding.toolbar, R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.verifierRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loggedInVerifier = snapshot.child("address").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                docModelList.clear();
                for (DataSnapshot doc : snapshot.child("VerifyDocs").getChildren()){
                    docId = doc.child("docId").getValue(String.class);
                    userID = doc.child("userID").getValue(String.class);
                    docName = doc.child("docName").getValue(String.class);
                    document = doc.child("document").getValue(String.class);
                    verifier = doc.child("verifier").getValue(String.class);
                    verified = doc.child("verified").getValue(String.class);
                    reason = doc.child("reason").getValue(String.class);

                    docModel list = new docModel(docId, userID, docName, document, verifier, verified, reason);
                    if (loggedInVerifier.equals(verifier) && verified.equals("N")){
                        docModelList.add(list);
                    }
                }
                docAdapter = new docAdapter(docModelList, getBaseContext());
                binding.verifierRecyclerView.setAdapter(docAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.navMenu.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
//            if(itemId == R.id.checkDocs){
//                Intent i = new Intent(SPDashboard.this, AddService.class);
//                startActivity(i);
//                binding.drawer.closeDrawer(GravityCompat.START);
//            } else
//            if(itemId == R.id.verifyNotary){
//                Intent i = new Intent(VerifierDashboard.this, VerifyNotaryActivity.class);
//                startActivity(i);
//                binding.drawer.closeDrawer(GravityCompat.START);
//            } else
            if(itemId == R.id.logoutVerifier){
                final AlertDialog.Builder builder = new AlertDialog.Builder(VerifierDashboard.this);
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(VerifierDashboard.this, LoginActivity.class));
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