package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private boolean isBackPressedOnce = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    private List<String> name;
    private List<String> number;
    private List<String> email;
    private List<String> role;
    private List<Integer> picture;
    private ContactsAdapter adapter;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recyclerview);
        name = new ArrayList<>();
        number = new ArrayList<>();
        email = new ArrayList<>();
        role = new ArrayList<>();
        picture = new ArrayList<>();

        picture.add(R.drawable.alegre);
        name.add("ALEGRE, JULIENE AAROLE B.");
        number.add("(+63) 916 9857 561");
        email.add("julieneaarole.alegre@cvsu.edu.ph");
        role.add(" ADMINISTRATOR");

        picture.add(R.drawable.creus);
        name.add("CREUS, ANDREI NICOLE G. ");
        number.add("(+63) 939 7735 947");
        email.add("andreinicole.creus@cvsu.edu.ph");
        role.add("ADMINISTRATOR");

        picture.add(R.drawable.galisanao);
        name.add("GALISANAO, JAZZY M. ");
        number.add("(+63) 965 7430 230");
        email.add("jazzy.galisanao@cvsu.edu.ph");
        role.add("ADMINISTRATOR");

        picture.add(R.drawable.hernandez);
        name.add("HERNANDEZ, JUANITO A.");
        number.add("(+63) 929 3587 195");
        email.add("juanito.hernandez@cvsu.edu.ph");
        role.add("ADMINISTRATOR");

        picture.add(R.drawable.jimenez);
        name.add("JIMENEZ, REIZYMER R.");
        number.add("(+63) 998 9288 066");
        email.add("reizymer.jimenez@cvsu.edu.ph");
        role.add("ADMINISTRATOR");

        picture.add(R.drawable.tendencia);
        name.add("TENDENCIA, BRYNT");
        number.add("(+63) 915 900 0604");
        email.add("brynt.tendencia@cvsu.edu.ph");
        role.add("ADMINISTRATOR");

        picture.add(R.drawable.remigio);
        name.add("REMIGIO, ANGEL R.");
        number.add("(+63) 966 6892 877");
        email.add("angel.remigio@cvsu.edu.ph");
        role.add("FRONT-END DEVELOPER");

        picture.add(R.drawable.vinuyapic);
        name.add("VINUYA, ADRIEL B.");
        number.add("(+63) 969 2168 567");
        email.add("adriel.vinuya@cvsu.edu.ph");
        role.add("BACK-END DEVELOPER");

        adapter = new ContactsAdapter(this, name, number, email, role, picture);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastPosition = adapter.getItemCount() - 1;

                // Check if it's the last item
                if (currentPosition == lastPosition) {
                    recyclerView.smoothScrollToPosition(0); // Scroll to the first item
                } else {
                    recyclerView.smoothScrollToPosition(currentPosition + 1);
                }
                handler.postDelayed(this, 3000); // Schedule the next scroll after 3 seconds
            }
        };

        String admin = getIntent().getStringExtra("admin");
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("CONTACTS");

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
            Intent intent = new Intent(Contacts.this, Login.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.search) {
            Intent intent = new Intent(Contacts.this, UserSearch.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(Contacts.this, UserAllContent.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.history) {
            Intent intent = new Intent(Contacts.this, History.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(Contacts.this, About.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.contacts) {
            Toast.makeText(Contacts.this, "You are already in Contacts Page", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.logout) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(Contacts.this);
            builder.setMessage("Are you sure you want to logout?");

            // Add buttons
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Confirm button
                    firebaseAuth.signOut();
                    Intent intent = new Intent(Contacts.this, MainActivity.class);
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
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 3000); // Start the auto scroll after 3 seconds delay
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable); // Stop the auto scroll when activity is stopped
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