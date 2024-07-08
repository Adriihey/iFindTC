package com.appdazzle_innovations.testingcenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CentersHolderUser extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView centernameholder, centerlocationholder;
    public CentersHolderUser(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.newcenterimageholder);
        centernameholder = itemView.findViewById(R.id.newcenternameholder);
        centerlocationholder = itemView.findViewById(R.id.newcenterlocholder);
    }
}
