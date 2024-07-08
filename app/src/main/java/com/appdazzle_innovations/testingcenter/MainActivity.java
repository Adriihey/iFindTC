package com.appdazzle_innovations.testingcenter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.appdazzle_innovations.testingcenter.R;
import com.appdazzle_innovations.testingcenter.Register;

public class MainActivity extends AppCompatActivity {
    private boolean isBackPressedOnce = false;
    AppCompatButton loginform, registerform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginform = findViewById(R.id.loginform);
        registerform = findViewById(R.id.registerform);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));
            getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_trans));
        }

        loginform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginform.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.purple700)));

                // Delay changing back to original color after 0.5 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Change background tint back to original color after 0.5 seconds
                        loginform.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.purple200)));
                    }
                }, 200); // 500 milliseconds = 0.5 seconds
                Intent intent = new Intent(MainActivity.this, Register.class);
                intent.putExtra("Visibility", "login");

                Pair<View, String> pair1 = Pair.create(findViewById(R.id.applogo), "sharedApplogo");
                Pair<View, String> pair2 = Pair.create(findViewById(R.id.reglog), "sharedReglog");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2);

                startActivity(intent, options.toBundle());
            }
        });

        registerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerform.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.purple700)));

                // Delay changing back to original color after 0.5 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Change background tint back to original color after 0.5 seconds
                        registerform.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.purple200)));
                    }
                }, 200); // 500 milliseconds = 0.5 seconds
                Intent intent = new Intent(MainActivity.this, Register.class);
                intent.putExtra("Visibility", "register");

                Pair<View, String> pair1 = Pair.create(findViewById(R.id.applogo), "sharedApplogo");
                Pair<View, String> pair2 = Pair.create(findViewById(R.id.reglog), "sharedReglog");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2);

                startActivity(intent, options.toBundle());
            }
        });
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
