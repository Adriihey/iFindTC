package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.appdazzle_innovations.testingcenter.Utills.Centers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private boolean isBackPressedOnce = false;
    ImageView home, search, profile;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    FirebaseRecyclerAdapter<Centers, CentersHolderUser> adapter;
    FirebaseRecyclerOptions<Centers> options;
    RecyclerView centerlist;
    AppCompatButton usertoadmin;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        centers = FirebaseDatabase.getInstance().getReference().child("Testing Centers");

        centerlist = findViewById(R.id.centerlist);
        centerlist.setLayoutManager(new GridLayoutManager(this,2));

        String admin = getIntent().getStringExtra("admin");
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("HOME");

        // Set font size, family, and color directly in the TextView
        customTitle.setTextSize(20); // Font size in sp
        customTitle.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)); // Font family
        customTitle.setTextColor(ContextCompat.getColor(this, R.color.white)); // Text color

        // Set layout parameters for the custom title
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL
        );
        customTitle.setLayoutParams(layoutParams);

        // Set the custom title view
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(customTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));
        getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));

        LoadCenters();
    }

    private void LoadCenters() {
        options = new FirebaseRecyclerOptions.Builder<Centers>().setQuery(centers, Centers.class).build();
        adapter = new FirebaseRecyclerAdapter<Centers, CentersHolderUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CentersHolderUser holder, int position, @NonNull Centers model) {
                holder.centernameholder.setText(model.getCenter_name());
                holder.centerlocationholder.setText(model.getCenter_address());
                Picasso.get().load(model.getCenter_image()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getCenter_uid();
                        String admin = getIntent().getStringExtra("admin");
                        Intent intent = new Intent(Login.this, ViewCenter.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("from", "user");
                        intent.putExtra("admin", admin.toString().trim());
                        startActivity(intent);
                        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    }
                });
            }

            @NonNull
            @Override
            public CentersHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centerlistuser, parent, false);
                return new CentersHolderUser(view);
            }
        };
        adapter.startListening();
        centerlist.setAdapter(adapter);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Toast.makeText(Login.this, "You are already in Home Page", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.search) {
            Intent intent = new Intent(Login.this, UserSearch.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(Login.this, UserAllContent.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.history) {
            Intent intent = new Intent(Login.this, History.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(Login.this, About.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.contacts) {
            Intent intent = new Intent(Login.this, Contacts.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.logout) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage("Are you sure you want to logout?");

            // Add buttons
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Confirm button
                    firebaseAuth.signOut();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivityWithAnimation(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Cancel button
                    // Do nothing, just close the dialog
                }
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }


        return false;
    }
    private void startActivityWithAnimation(Intent intent) {
        String admin = getIntent().getStringExtra("admin");
        intent.putExtra("admin", admin.toString().trim());
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, navigationView, "nav_view").toBundle());
        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
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