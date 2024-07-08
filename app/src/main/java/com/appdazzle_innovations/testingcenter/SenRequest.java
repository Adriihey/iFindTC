package com.appdazzle_innovations.testingcenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class SenRequest extends AppCompatActivity {
    private static final int REQUEST_CODE_SEND_EMAIL = 1;
    LinearLayout layout1, layout2;
    EditText body, subject;
    CardView cardView;
    TextView selectedtests, expand;
    AppCompatButton generate, createrequest, submitrequest, cancelrequest;
    DatabaseReference databaseReference, userreference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    // Variables to store user and center details
    String userName, userSchoolName, userSchoolAddress, userEmail, userNumber, userNumber1, userNumber2;
    String centerName, centerEmail, centerAddress;
    boolean userLoaded = false;
    boolean centerLoaded = false;
    String selectedTestsstring;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sen_request);

        String centerID = getIntent().getStringExtra("centerID");
        selectedTestsstring = getIntent().getStringExtra("selectedTests");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Testing Centers").child(centerID);
        userreference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        layout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        expand = findViewById(R.id.expand);
        body = findViewById(R.id.body);
        subject = findViewById(R.id.subject);
        cardView = findViewById(R.id.cardView);
        selectedtests = findViewById(R.id.selectedtests);
        generate = findViewById(R.id.generate);
        createrequest = findViewById(R.id.createrequest);
        submitrequest = findViewById(R.id.submitrequest);
        cancelrequest = findViewById(R.id.cancelrequest);

        selectedtests.setText(selectedTestsstring);

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

        cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SenRequest.this)
                        .setTitle("Cancel Request")
                        .setMessage("Are you sure you want to cancel the request?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        submitrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if body or subject is empty
                if (body.getText().toString().trim().isEmpty() || subject.getText().toString().trim().isEmpty()) {
                    // Show a toast message if either field is empty
                    Toast.makeText(SenRequest.this, "Subject and Body cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Show confirmation dialog if both fields are non-empty
                    new AlertDialog.Builder(SenRequest.this)
                            .setTitle("Submit Request")
                            .setMessage("Are you sure you want to submit the request?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{centerEmail});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                                    intent.putExtra(Intent.EXTRA_TEXT, body.getText().toString());
                                    intent.setType("message/rfc822");

                                    // Set the intent to open Gmail directly
                                    intent.setPackage("com.google.android.gm");

                                    // Use startActivityForResult instead of startActivity
                                    try {
                                        startActivityForResult(intent, REQUEST_CODE_SEND_EMAIL);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(SenRequest.this, "Gmail is not installed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });

        createrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.setText("");
                body.setText("");
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.setText("");
                body.setText("");
                updateBody();
            }
        });

        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = (selectedtests.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(layout1, new AutoTransition());
                selectedtests.setVisibility(visibility);

                if (visibility == View.VISIBLE) {
                    expand.setText("HIDE");
                } else {
                    expand.setText("SHOW");
                }
            }
        });

        // Fetch user details from Firebase
        fetchUserDetails();

        // Fetch center details from Firebase
        fetchCenterDetails(centerID);
    }

    private void fetchUserDetails() {
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userNumber1 = dataSnapshot.child("contactnumber1").getValue(String.class);
                    userNumber2 = dataSnapshot.child("contactnumber2").getValue(String.class);

                    if (userNumber2 != null && !userNumber2.isEmpty()) {
                        userNumber = userNumber1 + "\n" + userNumber2;
                    } else {
                        userNumber = userNumber1;
                    }

                    userEmail = dataSnapshot.child("email").getValue(String.class);
                    userName = dataSnapshot.child("name").getValue(String.class);
                    userSchoolName = dataSnapshot.child("schoolname").getValue(String.class);
                    userSchoolAddress = dataSnapshot.child("schooladdress").getValue(String.class);
                    userLoaded = true;

                    if (userLoaded && centerLoaded) {
                        updateBody();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void fetchCenterDetails(String centerID) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    centerAddress = dataSnapshot.child("center_address").getValue(String.class);
                    centerName = dataSnapshot.child("center_name").getValue(String.class);
                    centerEmail = dataSnapshot.child("center_email").getValue(String.class);
                    centerLoaded = true;
                    if (userLoaded && centerLoaded) {
                        updateBody();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void updateBody() {
        String stringSubject = "Request for Testing Construction Materials";
        String stringBody = userName + "\n" +
                userSchoolName + "\n" +
                userSchoolAddress + "\n\n" +
                "[DATE]\n\n" +
                centerName + "\n" +
                centerAddress + "\n\n" +
                "To whom it may concern,\n\n" +
                "We hope this letter finds you well. We, a group of [YEAR LEVEL] [COURSE] students from " + userSchoolName + ", are currently engaged in a project that involves the analysis and application of various construction materials. Our project aims to gain practical insights and understanding of the properties and performance of these materials under different conditions.\n\n" +
                "To achieve the objectives of our project, we seek to utilize the advanced testing facilities at your esteemed center. Specifically, we are interested in testing the following materials:\n\n" +
                selectedTestsstring + "\n\n" +
                "We intend to conduct a series of tests including, but not limited to the aforementioned tests and any other relevant tests that can provide comprehensive data on the performance characteristics of these materials.\n\n" +
                "Given the reputation and state-of-the-art facilities of " + centerName + ", we believe that your center is the ideal place to conduct these tests. We would be grateful if you could provide us with the necessary access and support to carry out our testing. Our preferred timeline for conducting these tests is from [PROPOSED START DATE] to [PROPOSED END DATE], but we are open to adjustments based on your availability.\n\n" +
                "In return, we are more than willing to share our findings and any data collected during the testing process with your center. Additionally, we are open to any terms and conditions you may have regarding the use of your facilities.\n\n" +
                "Thank you for considering our request. We look forward to the possibility of collaborating with your esteemed center and benefiting from your expertise in material testing.\n\n" +
                "Yours sincerely,\n\n" +
                userName + "\n" +
                "[Designation, e.g. Student Group Representative]\n" +
                userSchoolName + "\n" +
                userEmail + "\n" +
                userNumber;

        subject.setText(stringSubject);
        body.setText(stringBody);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SEND_EMAIL) {
            // Check if the email app was opened
            PackageManager packageManager = getPackageManager();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{centerEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
            emailIntent.putExtra(Intent.EXTRA_TEXT, body.getText().toString());
            List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;

            if (isIntentSafe) {
                // Email app was opened, so we assume the email was sent successfully
                // Create a HashMap to store request details
                HashMap<String, String> requestDetails = new HashMap<>();
                requestDetails.put("centerName", centerName);
                requestDetails.put("centerEmail", centerEmail);
                requestDetails.put("body", body.getText().toString());
                requestDetails.put("subject", subject.getText().toString());

                // Store the HashMap in Firebase under the user's Requests node
                userreference.child("Requests").push().setValue(requestDetails);
            } else {
                // Email app was not opened, so the email was likely not sent
                Toast.makeText(SenRequest.this, "Email was not sent.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
