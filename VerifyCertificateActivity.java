package com.example.docverifier.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.docverifier.R;
import com.example.docverifier.databinding.ActivityVerifyCertificateBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class VerifyCertificateActivity extends AppCompatActivity {

    ActivityVerifyCertificateBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private final int REQ = 1;
    private Uri docData;
    String pdfName = "", item;
    String [] dropDown = {"Select Verifier", "Pune", "Mumbai", "Nashik", "Solapur", "Nagpur"};
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyCertificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("VerifyDocs");

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.selectPDF.setOnClickListener(view -> {
            openGallery();
        });

        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_list, dropDown);
        binding.searchDropDownSPDashboard.setAdapter(adapterItems);
        binding.searchDropDownSPDashboard.setOnItemClickListener((adapterView, view, i, l) -> {
            item = adapterView.getItemAtPosition(i).toString();
        });
        
        binding.btnSend.setOnClickListener(view -> {
            if (docData == null){
                Toast.makeText(VerifyCertificateActivity.this, "Please select document image.", Toast.LENGTH_SHORT).show();
            } else if(binding.edCertName.getText().toString().isEmpty()){
                binding.edCertName.setError("Please provide a name of document");
            } else if (item.isEmpty() || item.equals("Select Verifier")){
                Toast.makeText(VerifyCertificateActivity.this, "Please select verifier.", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("We are uploading your document");
                progressDialog.show();

                String filename = timestamp;
                StorageReference reference = FirebaseStorage.getInstance().getReference().child("documents").child("VerifyCertificates").child(filename);
                reference.putFile(docData).addOnSuccessListener(taskSnapshot -> {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri uri = uriTask.getResult();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("docId", "" + timestamp);
                    hashMap.put("userID", "" + auth.getUid());
                    hashMap.put("docName", "" + binding.edCertName.getText().toString());
                    hashMap.put("document", "" + uri);
                    hashMap.put("verifier", "" + item);
                    hashMap.put("verified", "N");

                    ref1.child(timestamp).setValue(hashMap).addOnSuccessListener(unused -> {
                        Toast.makeText(VerifyCertificateActivity.this, "Document sent for validation.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(VerifyCertificateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyCertificateActivity.this, "Failed to sent document. Please contact to admin to register your complaint..", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(VerifyCertificateActivity.this, "Something went wrong Please try again." + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
        
    }

    private void    openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image file"), REQ);
    }

    @SuppressLint({"Range", "NewApi"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK){
            docData = data.getData();
            Toast.makeText(VerifyCertificateActivity.this, "" + docData, Toast.LENGTH_SHORT).show();
            if (docData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = VerifyCertificateActivity.this.getContentResolver().query(docData, null, null,null);
                    if (cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (docData.toString().startsWith("file://")){
                pdfName = new File(docData.toString()).getName();
            }

            binding.txtPDFView.setText(pdfName);
        }
    }
}