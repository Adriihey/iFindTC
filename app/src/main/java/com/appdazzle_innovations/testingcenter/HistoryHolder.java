package com.appdazzle_innovations.testingcenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryHolder extends RecyclerView.ViewHolder {
    TextView centerName, centerEmail, subject, body;
    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        body = itemView.findViewById(R.id.body);
        centerEmail = itemView.findViewById(R.id.centerEmail);
        centerName = itemView.findViewById(R.id.centerName);
        subject = itemView.findViewById(R.id.subject);
    }
}
