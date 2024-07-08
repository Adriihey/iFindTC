package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.appdazzle_innovations.testingcenter.Utills.Centers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Admin extends AppCompatActivity {
    private boolean isBackPressedOnce = false;
    ImageView home, addcenter, profile;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    FirebaseRecyclerAdapter<Centers, CentersHolder> adapter;
    FirebaseRecyclerOptions<Centers>options;
    RecyclerView centerlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        centers = FirebaseDatabase.getInstance().getReference().child("Testing Centers");

        home = findViewById(R.id.home);
        addcenter = findViewById(R.id.addcenter);
        profile = findViewById(R.id.profile);
        centerlist = findViewById(R.id.centerlist);

        centerlist.setLayoutManager(new LinearLayoutManager(this));

        addcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminAddCenter.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

            }
        });
        LoadCenters();
    }

    private void LoadCenters() {
        options = new FirebaseRecyclerOptions.Builder<Centers>().setQuery(centers, Centers.class).build();
        adapter = new FirebaseRecyclerAdapter<Centers, CentersHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CentersHolder holder, int position, @NonNull Centers model) {
                holder.centernameholder.setText(model.getCenter_name());
                holder.centerlocationholder.setText(model.getCenter_address());
                Picasso.get().load(model.getCenter_image()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getCenter_uid();
                        Intent intent = new Intent(Admin.this, ViewCenter.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("from", "admin");
                        startActivity(intent);
                        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    }
                });
            }

            @NonNull
            @Override
            public CentersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centerlist, parent, false);
                return new CentersHolder(view);
            }
        };
        adapter.startListening();
        centerlist.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        if(isBackPressedOnce){
            finishAffinity();
            return;
        }
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        isBackPressedOnce = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedOnce = false;
            }
        }, 2000);
    }
}