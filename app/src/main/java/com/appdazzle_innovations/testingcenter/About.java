package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private boolean isBackPressedOnce = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String admin = getIntent().getStringExtra("admin");
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("ABOUT");

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(About.this, Login.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.search) {
            Intent intent = new Intent(About.this, UserSearch.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(About.this, UserAllContent.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.history) {
            Intent intent = new Intent(About.this, History.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.about) {
            Toast.makeText(About.this, "You are already in About Page", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.contacts) {
            Intent intent = new Intent(About.this, Contacts.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.logout) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
            builder.setMessage("Are you sure you want to logout?");

            // Add buttons
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Confirm button
                    firebaseAuth.signOut();
                    Intent intent = new Intent(About.this, MainActivity.class);
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