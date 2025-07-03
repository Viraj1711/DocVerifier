package com.example.docverifier.Verifier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docverifier.User.ReviewImageActivity;
import com.example.docverifier.databinding.ActivityCheckDocumentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class CheckDocuments extends AppCompatActivity {

    ActivityCheckDocumentsBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    String docId, document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckDocumentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        docId = intent.getStringExtra("docId");
        document = intent.getStringExtra("document");

        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdminDocsDetails");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("VerifyDocs");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("documents").child("VerifyCertificates");

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        Log.d("TAG", "onCreate: Document URL: " + document);
        if (!document.isEmpty()){
            Picasso.get().load(document).into(binding.ivDocumentImage);
        } else {
            Toast.makeText(CheckDocuments.this, "Something went wrong to fetch the document image", Toast.LENGTH_SHORT).show();
        }

        binding.ivDocumentImage.setOnClickListener(view -> {
            Intent i = new Intent(this, ReviewImageActivity.class);
            i.putExtra("document", document);
            startActivity(i);
        });

        binding.btnFetch.setOnClickListener(view -> {
            String certNo = binding.cert.getText().toString();
            if (certNo.isEmpty()){
                binding.cert.setError("Please enter certificate number");
            } else {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot doc : snapshot.getChildren()){

                            if (doc.exists() && doc.child("certNo").getValue(String.class).equals(certNo)){
                                String certId = doc.child("certId").getValue(String.class);
                                String certName = doc.child("certName").getValue(String.class);
                                String certOwnerName = doc.child("certOwnerName").getValue(String.class);
                                String certNo = doc.child("certNo").getValue(String.class);
                                String issuer = doc.child("issuer").getValue(String.class);
                                String obtainedMarks = doc.child("obtainedMarks").getValue(String.class);
                                String totalMarks = doc.child("totalMarks").getValue(String.class);
                                String exprDate = doc.child("exprDate").getValue(String.class);
                                String sub1 = doc.child("sub1").getValue(String.class);
                                String sub2 = doc.child("sub2").getValue(String.class);
                                String sub3 = doc.child("sub3").getValue(String.class);
                                String sub4 = doc.child("sub4").getValue(String.class);
                                String sub5 = doc.child("sub5").getValue(String.class);
                                String sub6 = doc.child("sub6").getValue(String.class);
                                String sub7 = doc.child("sub7").getValue(String.class);
                                String sub8 = doc.child("sub8").getValue(String.class);
                                String sub9 = doc.child("sub9").getValue(String.class);
                                String sub10 = doc.child("sub10").getValue(String.class);
                                String sub11 = doc.child("sub11").getValue(String.class);
                                String sub12 = doc.child("sub12").getValue(String.class);
                                String sub13 = doc.child("sub13").getValue(String.class);
                                String sub14 = doc.child("sub14").getValue(String.class);
                                String sub15 = doc.child("sub15").getValue(String.class);

                                binding.txtCertName.setText(certName);
                                binding.txtCertNo.setText(certNo);
                                binding.txtIssuer.setText(issuer);
                                binding.txtCertOwnerName.setText(certOwnerName);
                                binding.txtObtainedMarks.setText(obtainedMarks);
                                binding.txtTotalMarks.setText(totalMarks);
                                binding.txtExprDate.setText(exprDate);

                                if (!Objects.equals(sub1, "")){
                                    binding.lnSub1.setVisibility(View.VISIBLE);
                                    binding.txtSub1.setText(sub1);
                                }
                                if (!Objects.equals(sub2, "")){
                                    binding.lnSub2.setVisibility(View.VISIBLE);
                                    binding.txtSub2.setText(sub2);
                                }
                                if (!Objects.equals(sub3, "")){
                                    binding.lnSub3.setVisibility(View.VISIBLE);
                                    binding.txtSub3.setText(sub3);
                                }
                                if (!Objects.equals(sub4, "")){
                                    binding.lnSub4.setVisibility(View.VISIBLE);
                                    binding.txtSub4.setText(sub4);
                                }
                                if (!Objects.equals(sub5, "")){
                                    binding.lnSub5.setVisibility(View.VISIBLE);
                                    binding.txtSub5.setText(sub5);
                                }
                                if (!Objects.equals(sub6, "")){
                                    binding.lnSub6.setVisibility(View.VISIBLE);
                                    binding.txtSub6.setText(sub6);
                                }
                                if (!Objects.equals(sub7, "")){
                                    binding.lnSub7.setVisibility(View.VISIBLE);
                                    binding.txtSub7.setText(sub7);
                                }
                                if (!Objects.equals(sub8, "")){
                                    binding.lnSub8.setVisibility(View.VISIBLE);
                                    binding.txtSub8.setText(sub8);
                                }
                                if (!Objects.equals(sub9, "")){
                                    binding.lnSub9.setVisibility(View.VISIBLE);
                                    binding.txtSub9.setText(sub9);
                                }
                                if (!Objects.equals(sub10, "")){
                                    binding.lnSub10.setVisibility(View.VISIBLE);
                                    binding.txtSub10.setText(sub3);
                                }
                                if (!Objects.equals(sub11, "")){
                                    binding.lnSub11.setVisibility(View.VISIBLE);
                                    binding.txtSub11.setText(sub11);
                                }
                                if (!Objects.equals(sub12, "")){
                                    binding.lnSub12.setVisibility(View.VISIBLE);
                                    binding.txtSub12.setText(sub12);
                                }
                                if (!Objects.equals(sub13, "")){
                                    binding.lnSub13.setVisibility(View.VISIBLE);
                                    binding.txtSub13.setText(sub13);
                                }
                                if (!Objects.equals(sub14, "")){
                                    binding.lnSub14.setVisibility(View.VISIBLE);
                                    binding.txtSub14.setText(sub14);
                                }
                                if (!Objects.equals(sub15, "")){
                                    binding.lnSub15.setVisibility(View.VISIBLE);
                                    binding.txtSub15.setText(sub15);
                                }

                            } else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(CheckDocuments.this);
                                builder.setTitle("No Document");
                                builder.setMessage("Please check entered document number once or document details are not added by admin yet");
                                builder.setPositiveButton("Yes", (dialog, which) -> {
                                    dialog.cancel();
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.btnApprove.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(CheckDocuments.this);
            builder.setTitle("Approve Document");
            builder.setMessage("Please ensure all the details are correct in the document");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                ref1.child(docId).child("verified").setValue("Y").addOnCompleteListener(task1 -> {
                    Intent in = new Intent(CheckDocuments.this, VerifierDashboard.class);
                    startActivity(in);
                    finishAffinity();
                    Toast.makeText(CheckDocuments.this, "Document Validated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.d("TAG", "onFailure: "+e.getMessage());
                    Toast.makeText(CheckDocuments.this, "Failed to validate document. Please try again later", Toast.LENGTH_SHORT).show();
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        binding.btnReject.setOnClickListener(view -> {

            if (binding.edReason.getText().toString().isEmpty()){
                binding.edReason.setError("Please enter reason to reject.");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CheckDocuments.this);
                builder.setTitle("Reject Document");
                builder.setMessage("Are you sure, you want to reject the document");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("verified", "Rejected");
                    hashMap.put("reason", "" + binding.edReason.getText().toString());

                    ref1.child(docId).updateChildren(hashMap).addOnCompleteListener(task -> {
                        Intent in = new Intent(CheckDocuments.this, VerifierDashboard.class);
                        startActivity(in);
                        finishAffinity();
                        Toast.makeText(CheckDocuments.this, "Document Rejected", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(CheckDocuments.this, "Failed to reject document. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}