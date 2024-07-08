package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAccount extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    AppCompatButton cancelprofile, createprofile, addnumber, removenumber;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    TextInputEditText contactnumber2, contactnumber1, fullname, schoolname, schooladdress;
    ScrollView scrollView;
    View view;
    LinearLayout layout;
    CircleImageView profile_image;
    Uri uri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);

        scrollView = findViewById(R.id.scrollview); // Initialize scrollView
        view = findViewById(R.id.view);
        view.setVisibility(View.GONE);

        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                boolean isKeyboardShowing = keypadHeight > screenHeight * 0.15;

                if (isKeyboardShowing) {
                    // Keyboard is shown, perform your actions here
                    view.setVisibility(View.VISIBLE);
                } else {
                    // Keyboard is hidden, perform your actions here
                    view.setVisibility(View.GONE);
                }
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        cancelprofile = findViewById(R.id.cancelprofile);
        createprofile = findViewById(R.id.createprofile);

        contactnumber1 = findViewById(R.id.contactnumber1);
        contactnumber2 = findViewById(R.id.contactnumber2);
        contactnumber2.setVisibility(View.GONE);
        addnumber = findViewById(R.id.addnumber);
        removenumber = findViewById(R.id.removenumber);
        removenumber.setVisibility(View.GONE);
        fullname = findViewById(R.id.fullname);
        schoolname = findViewById(R.id.schoolname);
        schooladdress = findViewById(R.id.schooladdress);

        layout = findViewById(R.id.layoutdetails);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        profile_image = findViewById(R.id.profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        removenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactnumber2.setText(""); // Clear the text field
                removenumber.setVisibility(View.GONE);
                contactnumber2.setVisibility(View.GONE);
                addnumber.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            }
        });

        addnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactnumber2.setVisibility(View.VISIBLE);
                addnumber.setVisibility(View.GONE);
                removenumber.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            }
        });

        cancelprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelprofile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SetupAccount.this, R.color.purple200)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancelprofile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SetupAccount.this, R.color.purple700)));
                    }
                }, 200);

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    // Remove user data from Firebase Realtime Database
                    databaseReference.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Delete user account from Firebase Authentication
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // User data removed from database and account deleted successfully
                                            Toast.makeText(SetupAccount.this, "Account canceled successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SetupAccount.this, MainActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                                            finish();
                                        } else {
                                            // Failed to delete user account
                                            Toast.makeText(SetupAccount.this, "Failed to cancel account. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // Failed to remove user data from database
                                Toast.makeText(SetupAccount.this, "Failed to cancel account. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SetupAccount.this, "No user is currently signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createprofile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SetupAccount.this, R.color.purple200)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createprofile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SetupAccount.this, R.color.purple700)));
                    }
                }, 200);

                String name, address, school, number1, number2;
                name = fullname.getText().toString().trim();
                address = schooladdress.getText().toString().trim();
                school = schoolname.getText().toString().trim();
                number1 = contactnumber1.getText().toString().trim();
                if (contactnumber2 != null) {
                    number2 = contactnumber2.getText().toString().trim();
                } else {
                    number2 = "";
                }
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(school)|| TextUtils.isEmpty(number1)) {
                    Toast.makeText(SetupAccount.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                } else {
                    if (uri==null){
                        Toast.makeText(SetupAccount.this, "Please add a profile picture", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        LottieAnimationView animationView = new LottieAnimationView(SetupAccount.this);
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
                                if (task.isSuccessful()){
                                    storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("name", name);
                                            hashMap.put("contactnumber1", number1);
                                            hashMap.put("contactnumber2", number2);
                                            hashMap.put("schoolname", school);
                                            hashMap.put("schooladdress", address);
                                            hashMap.put("profilepicture", uri.toString());
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference;
                                            reference = database.getReference("Users");
                                            reference.child(user.getUid()).updateChildren(hashMap);
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SetupAccount.this, Login.class);
                                            intent.putExtra("admin", "false");
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            uri = data.getData();
            profile_image.setImageURI(uri);
        }
    }
}
