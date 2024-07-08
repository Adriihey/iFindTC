package com.appdazzle_innovations.testingcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdazzle_innovations.testingcenter.Utills.Centers;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomCentersAdapter extends RecyclerView.Adapter<CustomCentersAdapter.MyHolder>{
    Context context;
    List<Centers> centersList;

    public CustomCentersAdapter(Context context, List<Centers> centersList) {
        this.context = context;
        this.centersList = centersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.centerlist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String image = centersList.get(position).getCenter_image();
        String name = centersList.get(position).getCenter_name();
        String location = centersList.get(position).getCenter_address();
        String tag = centersList.get(position).getTags();

        holder.centernameholder.setText(name);
        holder.centerlocationholder.setText(location);
        holder.centertag.setText(tag);
        Picasso.get().load(image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView centernameholder, centerlocationholder, centertag;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            centernameholder = itemView.findViewById(R.id.centernameholder);
            centerlocationholder = itemView.findViewById(R.id.centerlocationholder);
            centertag = itemView.findViewById(R.id.centertag);
        }
    }
}
