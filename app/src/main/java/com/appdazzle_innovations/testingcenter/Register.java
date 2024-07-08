package com.appdazzle_innovations.testingcenter;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    LinearLayout loginlinear, registerlinear;
    TextView switchregister, switchlogin, errormessage, loginerrormessage;
    CardView reglog;
    AppCompatButton loginbtn, registerbtn;
    TextInputEditText registeremail, registerpassword1, registerpassword2, loginemail, loginpassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));
            getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        firebaseAuth = FirebaseAuth.getInstance();

        registeremail = findViewById(R.id.registeremail);
        registerpassword1 = findViewById(R.id.registerpassword1);
        registerpassword2 = findViewById(R.id.registerpassword2);
        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        loginerrormessage = findViewById(R.id.loginerrormessage);
        loginerrormessage.setVisibility(View.INVISIBLE);
        errormessage = findViewById(R.id.errormessage);
        errormessage.setVisibility(View.INVISIBLE);
        reglog = findViewById(R.id.reglog);
        reglog.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        loginlinear = findViewById(R.id.loginlinear);
        loginlinear.setVisibility(View.GONE);
        registerlinear = findViewById(R.id.registerlinear);
        registerlinear.setVisibility(View.GONE);

        switchregister = findViewById(R.id.switchregister);
        switchlogin = findViewById(R.id.switchlogin);

        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.regsiterbtn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerbtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Register.this, R.color.purple700)));

                // Delay changing back to original color after 0.5 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Change background tint back to original color after 0.5 seconds
                        registerbtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Register.this, R.color.purple200)));
                    }
                }, 200);

                String email = registeremail.getText().toString().trim();
                String password1 = registerpassword1.getText().toString().trim();
                String password2 = registerpassword2.getText().toString().trim();
                if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    errormessage.setText("All fields are required");
                    errormessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (password1.length() < 6) {
                    // Password length is less than 6 characters
                    errormessage.setText("Password must have at least 6 characters");
                    errormessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (!password1.equals(password2)) {
                    errormessage.setText("Passwords do not match");
                    errormessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Email address format is invalid
                    errormessage.setText("Invalid email address");
                    errormessage.setVisibility(View.VISIBLE);
                    return;
                }

                errormessage.setVisibility(View.INVISIBLE);
                progressDialog.show();
                LottieAnimationView animationView = new LottieAnimationView(Register.this);
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
                firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseUser = firebaseAuth.getCurrentUser();
                            String email = firebaseUser.getEmail();
                            String uid = firebaseUser.getUid();
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("admin", "false");
                            hashMap.put("password", password1);
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Register.this, SetupAccount.class);
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        errormessage.setText("Failed to save user data");
                                        errormessage.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            errormessage.setText("Registration failed");
                            errormessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Register.this, R.color.purple700)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginbtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Register.this, R.color.purple200)));
                    }
                }, 200);

                String email = loginemail.getText().toString().trim();
                String password = loginpassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    loginerrormessage.setText("All fields are required");
                    loginerrormessage.setVisibility(View.VISIBLE);
                    return;
                }
                loginerrormessage.setVisibility(View.INVISIBLE);
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loginemail.setError("Invalid Email");
                    loginemail.setFocusable(true);
                }
                else {
                    loginUser(email, password);
                }
            }
        });

        switchlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerlinear.setVisibility(View.GONE);
                loginlinear.setVisibility(View.VISIBLE);
                errormessage.setVisibility(View.INVISIBLE);
            }
        });

        switchregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerlinear.setVisibility(View.VISIBLE);
                loginlinear.setVisibility(View.GONE);
                loginerrormessage.setVisibility(View.INVISIBLE);
            }
        });

        String visibility = getIntent().getStringExtra("Visibility");

        if ("login".equals(visibility)) {
            loginlinear.setVisibility(View.VISIBLE);
            registerlinear.setVisibility(View.GONE);
        } else if ("register".equals(visibility)) {
            loginlinear.setVisibility(View.GONE);
            registerlinear.setVisibility(View.VISIBLE);
        } else {
            loginlinear.setVisibility(View.GONE);
            registerlinear.setVisibility(View.GONE);
        }
    }

    private void loginUser(String email, String passw) {
        if (email.isEmpty() || passw.isEmpty()) {
            // Email or password is empty, show an error message
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        LottieAnimationView animationView = new LottieAnimationView(Register.this);
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
        firebaseAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            checkUserCategory(uid);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Pair<View, String> pair1 = Pair.create(findViewById(R.id.applogo), "sharedApplogo");
        Pair<View, String> pair2 = Pair.create(findViewById(R.id.reglog), "sharedReglog");

    }

    private void checkUserCategory(String uid) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String admin = dataSnapshot.child("admin").getValue(String.class);
                    Intent intent;
                    if ("true".equals(admin)) {
                        progressDialog.dismiss();
                        intent = new Intent(Register.this, Admin.class);
                    } else {
                        progressDialog.dismiss();
                        intent = new Intent(Register.this, Login.class);
                        intent.putExtra("admin", "false");
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
