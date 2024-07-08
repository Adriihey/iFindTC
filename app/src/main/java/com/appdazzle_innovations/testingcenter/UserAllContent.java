package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAllContent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private boolean isBackPressedOnce = false;
    private static final int REQUEST_CODE = 101;
    Uri uri;
    ImageView backgroundphoto;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    AppCompatButton logout, switchbutton, canceledit, confirmedit;
    CircleImageView profile_image;
    TextView username, useremail, contact1, contact2, schoolnameaccount, schooladdressaccount;
    String profileUri, usernameholder, number1, number2, number3, sc, sa, useremailholder, usernameeditholder;
    TextInputEditText usernameedit, useremailedit, contact1edit, contact2edit, schoolnameaccountedit, schooladdressaccountedit;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_content);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("PROFILE");

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

        backgroundphoto = findViewById(R.id.backgroundphoto);
        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        schoolnameaccount = findViewById(R.id.schoolnameaccount);
        schooladdressaccount = findViewById(R.id.schooladdressaccount);
        profile_image = findViewById(R.id.profile_image);
        usernameedit = findViewById(R.id.usernameedit);
        contact1edit = findViewById(R.id.contact1edit);
        contact2edit = findViewById(R.id.contact2edit);
        schoolnameaccountedit = findViewById(R.id.schoolnameaccountedit);
        schooladdressaccountedit = findViewById(R.id.schooladdressaccountedit);

        canceledit = findViewById(R.id.cancelmedit);
        confirmedit = findViewById(R.id.confirmedit);
        switchbutton = findViewById(R.id.switcbutton);
        switchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAllContent.this, Admin.class);
                intent.putExtra("admin", "true");
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        centers = FirebaseDatabase.getInstance().getReference().child("Testing Centers");
        logout = findViewById(R.id.logout);

        String admin = getIntent().getStringExtra("admin");
        if (admin.equals("true")) {
            switchbutton.setVisibility(View.VISIBLE);
        } else {
            switchbutton.setVisibility(View.GONE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameedit.setVisibility(View.VISIBLE);
                contact1edit.setVisibility(View.VISIBLE);
                contact2edit.setVisibility(View.VISIBLE);
                schoolnameaccountedit.setVisibility(View.VISIBLE);
                schooladdressaccountedit.setVisibility(View.VISIBLE);
                username.setVisibility(View.GONE);
                contact1.setVisibility(View.GONE);
                contact2.setVisibility(View.GONE);
                schoolnameaccount.setVisibility(View.GONE);
                schooladdressaccount.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                canceledit.setVisibility(View.VISIBLE);
                confirmedit.setVisibility(View.VISIBLE);
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
            }
        });

        canceledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameedit.setVisibility(View.GONE);
                contact1edit.setVisibility(View.GONE);
                contact2edit.setVisibility(View.GONE);
                schoolnameaccountedit.setVisibility(View.GONE);
                schooladdressaccountedit.setVisibility(View.GONE);
                username.setVisibility(View.VISIBLE);
                contact1.setVisibility(View.VISIBLE);
                users.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String number2;
                        if (snapshot.exists()) {
                            number2 = snapshot.child("contactnumber2").getValue(String.class);
                            if (number2.isEmpty()) {
                                contact2.setVisibility(View.GONE);
                            } else {
                                contact2.setVisibility(View.VISIBLE);
                                contact2.setText(number2);
                                contact2edit.setText(number2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                schoolnameaccount.setVisibility(View.VISIBLE);
                schooladdressaccount.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                confirmedit.setVisibility(View.GONE);
                canceledit.setVisibility(View.GONE);
            }
        });

        confirmedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, address, school, number1, number2;
                name = usernameedit.getText().toString().trim();
                address = schooladdressaccountedit.getText().toString().trim();
                school = schoolnameaccountedit.getText().toString().trim();
                number1 = contact1edit.getText().toString().trim();
                if (contact2edit.getText() != null) {
                    number2 = contact2edit.getText().toString().trim();
                } else {
                    number2 = "";
                }

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(school) || TextUtils.isEmpty(number1)) {
                    Toast.makeText(UserAllContent.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                } else {
                    if (uri != null) {
                        // Show progress dialog
                        progressDialog.show();
                        LottieAnimationView animationView = new LottieAnimationView(UserAllContent.this);
                        animationView.setAnimation(R.raw.loading);
                        animationView.loop(true); // Set loop to true
                        animationView.playAnimation();
                        progressDialog.setContentView(animationView);
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(progressDialog.getWindow().getAttributes());
                        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.progress_dialog_width); // Define your desired width here
                        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.progress_dialog_height); // Define your desired height here
                        layoutParams.width = dialogWidth;
                        layoutParams.height = dialogHeight;
                        progressDialog.getWindow().setAttributes(layoutParams);

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        storageReference = FirebaseStorage.getInstance().getReference().child("UserProfilePictures");
                        storageReference.child(user.getUid()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            updateUserInfo(user, name, number1, number2, school, address, uri.toString());
                                            progressDialog.dismiss();
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UserAllContent.this, "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUserInfo(user, name, number1, number2, school, address, profileUri);
                    }
                }
            }
        });

    }

    private void updateUserInfo(FirebaseUser user, String name, String number1, String number2, String school, String address, String profileUri) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("contactnumber1", number1);
        hashMap.put("contactnumber2", number2);
        hashMap.put("schoolname", school);
        hashMap.put("schooladdress", address);
        hashMap.put("profilepicture", profileUri);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            usernameedit.setVisibility(View.GONE);
            contact1edit.setVisibility(View.GONE);
            contact2edit.setVisibility(View.GONE);
            schoolnameaccountedit.setVisibility(View.GONE);
            schooladdressaccountedit.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            contact1.setVisibility(View.VISIBLE);
            schoolnameaccount.setVisibility(View.VISIBLE);
            schooladdressaccount.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            canceledit.setVisibility(View.GONE);
            confirmedit.setVisibility(View.GONE);
            users.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String number2;
                    if (snapshot.exists()) {
                        number2 = snapshot.child("contactnumber2").getValue(String.class);
                        if (number2.isEmpty()) {
                            contact2.setVisibility(View.GONE);
                        } else {
                            contact2.setVisibility(View.VISIBLE);
                            contact2.setText(number2);
                            contact2edit.setText(number2);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null) {
            SendUserToLogin();
        } else {
            users.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        profileUri = snapshot.child("profilepicture").getValue(String.class);
                        usernameholder = snapshot.child("name").getValue(String.class);
                        useremailholder = snapshot.child("email").getValue(String.class);
                        sa = snapshot.child("schooladdress").getValue(String.class);
                        sc = snapshot.child("schoolname").getValue(String.class);
                        number1 = snapshot.child("contactnumber1").getValue(String.class);
                        Picasso.get().load(profileUri).into(profile_image);
                        Picasso.get().load(profileUri).into(backgroundphoto);
                        username.setText(usernameholder);
                        usernameedit.setText(usernameholder);
                        useremail.setText(useremailholder);
                        contact1.setText(number1);
                        contact1edit.setText(number1);
                        schooladdressaccount.setText(sa);
                        schooladdressaccountedit.setText(sa);
                        schoolnameaccount.setText(sc);
                        schoolnameaccountedit.setText(sc);

                        number2 = snapshot.child("contactnumber2").getValue(String.class);
                        if (number2.isEmpty()) {
                            contact2.setVisibility(View.GONE);
                        } else {
                            contact2.setVisibility(View.VISIBLE);
                            contact2.setText(number2);
                            contact2edit.setText(number2);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void SendUserToLogin() {
        Intent intent = new Intent(UserAllContent.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            profile_image.setImageURI(uri);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(UserAllContent.this, Login.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.search) {
            Intent intent = new Intent(UserAllContent.this, UserSearch.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Toast.makeText(UserAllContent.this, "You are already in Profile", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.history) {
            Intent intent = new Intent(UserAllContent.this, History.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(UserAllContent.this, About.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.contacts) {
            Intent intent = new Intent(UserAllContent.this, Contacts.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.logout) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(UserAllContent.this);
            builder.setMessage("Are you sure you want to logout?");

            // Add buttons
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Confirm button
                    firebaseAuth.signOut();
                    Intent intent = new Intent(UserAllContent.this, MainActivity.class);
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
