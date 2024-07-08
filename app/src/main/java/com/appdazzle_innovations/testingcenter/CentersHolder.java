package com.appdazzle_innovations.testingcenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdazzle_innovations.testingcenter.Utills.Centers;

import java.util.List;

public class CentersHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView centernameholder, centerlocationholder, centertag;
    public CentersHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        centernameholder = itemView.findViewById(R.id.centernameholder);
        centerlocationholder = itemView.findViewById(R.id.centerlocationholder);
        centertag = itemView.findViewById(R.id.centertag);
    }
}
