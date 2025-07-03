package com.example.docverifier.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docverifier.Model.docModel;
import com.example.docverifier.R;
import com.example.docverifier.User.ReviewImageActivity;
import com.example.docverifier.Verifier.CheckDocuments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class docAdapter extends RecyclerView.Adapter<docAdapter.MyViewHolder> {

    private final List<docModel> docModelList;
    private List<docModel> backupList;
    private final Context context;
    FirebaseAuth auth;
    FirebaseUser user;

    public docAdapter(List<docModel> docModelList, Context context) {
        this.docModelList = docModelList;
        this.context = context;
        backupList = new ArrayList<>(docModelList);
    }

    @NonNull
    @Override
    public docAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_row, null));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull docAdapter.MyViewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        docModel docModel = docModelList.get(position);

        assert user != null;
        if (user.getUid().equals("EuzDLW25s0dm6YMq3QC14ekE6eC3")
                || user.getUid().equals("K9yrCmvYxwQYXr4R2EduTHVmdsq2")
                || user.getUid().equals("VFYz7DE3d8dXjcfsMTM9tGLiZHG3")
                || user.getUid().equals("WDBzyNMz3xNI7Fnpu8bvPzf8gAU2")
                || user.getUid().equals("c1tHNUqFmIhYGx1X7wFieAl5M733")
                || user.getUid().equals("dIlubq6BK1VLDtLjwRvLhDyjIXt1")){
            holder.txtDocId.setText(docModel.getDocId());
            holder.txtDocName.setText(docModel.getDocName());

            holder.itemView.setOnClickListener(view -> {
                holder.itemView.getContext().startActivity(new Intent(context, CheckDocuments.class)
                        .putExtra("docId", docModel.getDocId())
                        .putExtra("document", docModel.getDocument()));
            });
        } else {
            holder.txtDocId.setText(docModel.getDocName());
            if (docModel.getVerified().equals("N")){
                holder.txtDocName.setText("In Progress");
            } else if(docModel.getVerified().equals("Rejected")){
                holder.txtDocName.setText("Rejected");
                holder.txtDocName.setTextColor(R.color.red);
            } else if(docModel.getVerified().equals("Y")){
                holder.txtDocName.setText("Approved");
                holder.txtDocName.setTextColor(R.color.green);
            }
            holder.itemView.setOnClickListener(view -> {
                holder.itemView.getContext().startActivity(new Intent(context, ReviewImageActivity.class)
                        .putExtra("document", docModel.getDocument()));
            });
        }


    }

    @Override
    public int getItemCount() {
        return docModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDocId, txtDocName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocId = itemView.findViewById(R.id.txtDocId);
            txtDocName = itemView.findViewById(R.id.txtDocName);
        }
    }
}
