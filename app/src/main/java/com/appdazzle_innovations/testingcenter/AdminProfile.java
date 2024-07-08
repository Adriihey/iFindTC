package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    ImageView home, addcenter, profile, backgroundphoto;
    AppCompatButton switchbutton, logout;
    CircleImageView profile_image;
    TextView username, useremail, contact1, contact2, schoolnameaccount, schooladdressaccount;
    String profileUri, usernameholder ,number1, number2, sc, sa, useremailholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        backgroundphoto = findViewById(R.id.backgroundphoto);
        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        schoolnameaccount = findViewById(R.id.schoolnameaccount);
        schooladdressaccount = findViewById(R.id.schooladdressaccount);
        profile_image = findViewById(R.id.profile_image);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        centers = FirebaseDatabase.getInstance().getReference().child("Testing Centers");

        home = findViewById(R.id.home);
        addcenter = findViewById(R.id.addcenter);
        profile = findViewById(R.id.profile);

        logout = findViewById(R.id.logout);
        switchbutton = findViewById(R.id.switcbutton);
        switchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, Login.class);
                intent.putExtra("admin", "true");
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(AdminProfile.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();
            }
        });

        addcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, AdminAddCenter.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, Admin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseUser==null){
            SendUserToLogin();
        } else {
            users.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        profileUri = snapshot.child("profilepicture").getValue().toString();
                        usernameholder = snapshot.child("name").getValue().toString();
                        useremailholder = snapshot.child("email").getValue().toString();
                        sa = snapshot.child("schooladdress").getValue().toString();
                        sc = snapshot.child("schoolname").getValue().toString();
                        number1 = snapshot.child("contactnumber1").getValue().toString();
                        Picasso.get().load(profileUri).into(profile_image);
                        Picasso.get().load(profileUri).into(backgroundphoto);
                        username.setText(usernameholder);
                        useremail.setText(useremailholder);
                        contact1.setText(number1);
                        schooladdressaccount.setText(sa);
                        schoolnameaccount.setText(sc);

                        number2 = snapshot.child("contactnumber2").getValue().toString();
                        if (number2.isEmpty()) {
                            contact2.setVisibility(View.GONE);
                        } else {
                            contact2.setVisibility(View.VISIBLE);
                            contact2.setText(number2);
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
        Intent intent = new Intent(AdminProfile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed(){}
}