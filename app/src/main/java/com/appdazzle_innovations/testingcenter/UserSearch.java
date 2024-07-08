package com.appdazzle_innovations.testingcenter;

import static androidx.core.app.PendingIntentCompat.getActivity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdazzle_innovations.testingcenter.CustomCentersAdapter;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.appdazzle_innovations.testingcenter.Utills.Centers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    androidx.appcompat.widget.SearchView searchView;
    CheckboxManager checkboxManager;
    private boolean isBackPressedOnce = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference users, centers;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    CheckBox checkbox_AggregateCrushingValueTest,
            checkbox_AirContentTest,
            checkbox_AsphaltDensityTest,
            checkbox_AtterbergLimitsTest,
            checkbox_BendTest,
            checkbox_BendingStrengthTest,
            checkbox_BitumenContentTest,
            checkbox_CaliforniaBearingRatioCBRTest,
            checkbox_ChemicalCompositionTest,
            checkbox_CompressionStrengthTest,
            checkbox_CompressiveStrengthTest,
            checkbox_ConsolidationTest,
            checkbox_DimensionalToleranceTest,
            checkbox_DuctilityTest,
            checkbox_DurabilityTest,
            checkbox_EfflorescenceTest,
            checkbox_FireResistanceTest,
            checkbox_FlakinessAndElongationIndexTest,
            checkbox_FlexuralStrengthTest,
            checkbox_HardnessTest,
            checkbox_ImpactTest,
            checkbox_MarshallStabilityTest,
            checkbox_MoistureContentTest,
            checkbox_PenetrationTest,
            checkbox_ProctorCompactionTest,
            checkbox_RapidChloridePenetrationTestRCPT,
            checkbox_ShearStrengthTest,
            checkbox_ShrinkageTest,
            checkbox_SieveAnalysisTest,
            checkbox_SlumpTest,
            checkbox_SoilClassificationTest,
            checkbox_SoilPermeabilityTest,
            checkbox_SoundInsulationTest,
            checkbox_SpecificGravityTest,
            checkbox_SplitTensileStrengthTest,
            checkbox_TensileStrengthTest,
            checkbox_ThermalConductivityTest,
            checkbox_TriaxialShearTest,
            checkbox_ViscosityTest,
            checkbox_WaterAbsorptionTest,
            checkbox_WaterPermeabilityTest;
    RecyclerView centerlist;
    FirebaseRecyclerAdapter<Centers, CentersHolder> adapter;
    FirebaseRecyclerOptions<Centers>options;
    CustomCentersAdapter customCentersAdapter;
    List<Centers> centersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        centersList = new ArrayList<>();
        searchView = findViewById(R.id.searchView);

        checkbox_AggregateCrushingValueTest = findViewById(R.id.checkbox_AggregateCrushingValueTest);
        checkbox_AirContentTest = findViewById(R.id.checkbox_AirContentTest);
        checkbox_AsphaltDensityTest = findViewById(R.id.checkbox_AsphaltDensityTest);
        checkbox_AtterbergLimitsTest = findViewById(R.id.checkbox_AtterbergLimitsTest);
        checkbox_BendTest = findViewById(R.id.checkbox_BendTest);
        checkbox_BendingStrengthTest = findViewById(R.id.checkbox_BendingStrengthTest);
        checkbox_BitumenContentTest = findViewById(R.id.checkbox_BitumenContentTest);
        checkbox_CaliforniaBearingRatioCBRTest = findViewById(R.id.checkbox_CaliforniaBearingRatioCBRTest);
        checkbox_ChemicalCompositionTest = findViewById(R.id.checkbox_ChemicalCompositionTest);
        checkbox_CompressionStrengthTest = findViewById(R.id.checkbox_CompressionStrengthTest);
        checkbox_CompressiveStrengthTest = findViewById(R.id.checkbox_CompressiveStrengthTest);
        checkbox_ConsolidationTest = findViewById(R.id.checkbox_ConsolidationTest);
        checkbox_DimensionalToleranceTest = findViewById(R.id.checkbox_DimensionalToleranceTest);
        checkbox_DuctilityTest = findViewById(R.id.checkbox_DuctilityTest);
        checkbox_DurabilityTest = findViewById(R.id.checkbox_DurabilityTest);
        checkbox_EfflorescenceTest = findViewById(R.id.checkbox_EfflorescenceTest);
        checkbox_FireResistanceTest = findViewById(R.id.checkbox_FireResistanceTest);
        checkbox_FlakinessAndElongationIndexTest = findViewById(R.id.checkbox_FlakinessAndElongationIndexTest);
        checkbox_FlexuralStrengthTest = findViewById(R.id.checkbox_FlexuralStrengthTest);
        checkbox_HardnessTest = findViewById(R.id.checkbox_HardnessTest);
        checkbox_ImpactTest = findViewById(R.id.checkbox_ImpactTest);
        checkbox_MarshallStabilityTest = findViewById(R.id.checkbox_MarshallStabilityTest);
        checkbox_MoistureContentTest = findViewById(R.id.checkbox_MoistureContentTest);
        checkbox_PenetrationTest = findViewById(R.id.checkbox_PenetrationTest);
        checkbox_ProctorCompactionTest = findViewById(R.id.checkbox_ProctorCompactionTest);
        checkbox_RapidChloridePenetrationTestRCPT = findViewById(R.id.checkbox_RapidChloridePenetrationTestRCPT);
        checkbox_ShearStrengthTest = findViewById(R.id.checkbox_ShearStrengthTest);
        checkbox_ShrinkageTest = findViewById(R.id.checkbox_ShrinkageTest);
        checkbox_SieveAnalysisTest = findViewById(R.id.checkbox_SieveAnalysisTest);
        checkbox_SlumpTest = findViewById(R.id.checkbox_SlumpTest);
        checkbox_SoilClassificationTest = findViewById(R.id.checkbox_SoilClassificationTest);
        checkbox_SoilPermeabilityTest = findViewById(R.id.checkbox_SoilPermeabilityTest);
        checkbox_SoundInsulationTest = findViewById(R.id.checkbox_SoundInsulationTest);
        checkbox_SpecificGravityTest = findViewById(R.id.checkbox_SpecificGravityTest);
        checkbox_SplitTensileStrengthTest = findViewById(R.id.checkbox_SplitTensileStrengthTest);
        checkbox_TensileStrengthTest = findViewById(R.id.checkbox_TensileStrengthTest);
        checkbox_ThermalConductivityTest = findViewById(R.id.checkbox_ThermalConductivityTest);
        checkbox_TriaxialShearTest = findViewById(R.id.checkbox_TriaxialShearTest);
        checkbox_ViscosityTest = findViewById(R.id.checkbox_ViscosityTest);
        checkbox_WaterAbsorptionTest = findViewById(R.id.checkbox_WaterAbsorptionTest);
        checkbox_WaterPermeabilityTest = findViewById(R.id.checkbox_WaterPermeabilityTest);

        checkboxManager = new CheckboxManager(UserSearch.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        centers = FirebaseDatabase.getInstance().getReference().child("Testing Centers");
        centerlist = findViewById(R.id.centerlist);
        centerlist.setLayoutManager(new LinearLayoutManager(this));
        customCentersAdapter = new CustomCentersAdapter(UserSearch.this, centersList);
        // Pass 'this' instead of 'getParent()'
        centerlist.setAdapter(customCentersAdapter);
        LoadCenters();

        String admin = getIntent().getStringExtra("admin");
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("SEARCH TESTING CENTERS");

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

        checkbox_AggregateCrushingValueTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_AirContentTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_AsphaltDensityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_AtterbergLimitsTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_BendTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_BendingStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_BitumenContentTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_CaliforniaBearingRatioCBRTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ChemicalCompositionTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_CompressionStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_CompressiveStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ConsolidationTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_DimensionalToleranceTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_DuctilityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_DurabilityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_EfflorescenceTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_FireResistanceTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_FlakinessAndElongationIndexTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_FlexuralStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_HardnessTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ImpactTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_MarshallStabilityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_MoistureContentTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_PenetrationTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ProctorCompactionTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_RapidChloridePenetrationTestRCPT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ShearStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ShrinkageTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SieveAnalysisTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SlumpTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SoilClassificationTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SoilPermeabilityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SoundInsulationTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SpecificGravityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_SplitTensileStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_TensileStrengthTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ThermalConductivityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_TriaxialShearTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_ViscosityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_WaterAbsorptionTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

        checkbox_WaterPermeabilityTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxManager.onCheckboxStateChanged(); // Call the method to handle checkbox state changes
            }
        });

    }

    private void SearchCenters(String s) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        centers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                centersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Centers centers1 = ds.getValue(Centers.class);
                    if (!centers1.getCenter_uid().equals(firebaseUser.getUid())){
                        String[] concatenatedTagsArray = s.split("tag");
                        String[] getTagsArray = centers1.getTags().split("tag");
                        boolean allTagsPresent = true;
                        for (String tag : concatenatedTagsArray) {
                            boolean tagFound = false;
                            for (String getTag : getTagsArray) {
                                if (tag.equalsIgnoreCase(getTag)) {
                                    tagFound = true;
                                    break;
                                }
                            }
                            if (!tagFound) {
                                allTagsPresent = false;
                                break;
                            }
                        }
                        if (allTagsPresent) {
                            centersList.add(centers1);
                        }
                    }
                }
                customCentersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoadCenters", "Error loading centers: " + error.getMessage());
            }
        });
    }


    private void LoadCenters() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        centers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                centersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Centers centers1 = ds.getValue(Centers.class);
                    if (!centers1.getCenter_uid().equals(firebaseUser.getUid())){
                        centersList.add(centers1);
                    }
                }
                customCentersAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoadCenters", "Error loading centers: " + error.getMessage());
            }
        });
    }

    // CustomCentersAdapter class
    public class CustomCentersAdapter extends RecyclerView.Adapter<CustomCentersAdapter.MyHolder>{
        Context context;
        List<Centers> centersList;

        public CustomCentersAdapter(Context context, List<Centers> centersList) {
            this.context = context;
            this.centersList = centersList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.centerlist, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            String image = centersList.get(position).getCenter_image();
            String name = centersList.get(position).getCenter_name();
            String location = centersList.get(position).getCenter_address();
            String tag = centersList.get(position).getTags();
            String id = centersList.get(position).getCenter_uid();


            holder.centernameholder.setText(name);
            holder.centerlocationholder.setText(location);
            holder.centertag.setText(tag);
            Picasso.get().load(image).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String admin = getIntent().getStringExtra("admin");
                    Intent intent = new Intent(UserSearch.this, ViewCenter.class);
                    intent.putExtra("uid", id);
                    intent.putExtra("from", "userview");
                    intent.putExtra("admin", admin.toString().trim());
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                }
            });
        }

        @Override
        public int getItemCount() {
            return centersList.size(); // Return size of centersList
        }

        class MyHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView centernameholder, centerlocationholder, centertag;

            public MyHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.imageView);
                centernameholder = itemView.findViewById(R.id.centernameholder);
                centerlocationholder = itemView.findViewById(R.id.centerlocationholder);
                centertag = itemView.findViewById(R.id.centertag);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(UserSearch.this, Login.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.search) {
            Toast.makeText(UserSearch.this, "You are already in Search Page", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(UserSearch.this, UserAllContent.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.history) {
            Intent intent = new Intent(UserSearch.this, History.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(UserSearch.this, About.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.contacts) {
            Intent intent = new Intent(UserSearch.this, Contacts.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.logout) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(UserSearch.this);
            builder.setMessage("Are you sure you want to logout?");

            // Add buttons
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Confirm button
                    firebaseAuth.signOut();
                    Intent intent = new Intent(UserSearch.this, MainActivity.class);
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

    public class CheckboxManager {
        private ArrayList<CheckBox> checkboxes = new ArrayList<>();
        private Context context;

        // Constructor
        public CheckboxManager(Context context) {
            this.context = context;
            // Add all checkboxes to the list
            checkboxes.add(checkbox_AggregateCrushingValueTest);
            checkboxes.add(checkbox_AirContentTest);
            checkboxes.add(checkbox_AsphaltDensityTest);
            checkboxes.add(checkbox_AtterbergLimitsTest);
            checkboxes.add(checkbox_BendTest);
            checkboxes.add(checkbox_BendingStrengthTest);
            checkboxes.add(checkbox_BitumenContentTest);
            checkboxes.add(checkbox_CaliforniaBearingRatioCBRTest);
            checkboxes.add(checkbox_ChemicalCompositionTest);
            checkboxes.add(checkbox_CompressionStrengthTest);
            checkboxes.add(checkbox_CompressiveStrengthTest);
            checkboxes.add(checkbox_ConsolidationTest);
            checkboxes.add(checkbox_DimensionalToleranceTest);
            checkboxes.add(checkbox_DuctilityTest);
            checkboxes.add(checkbox_DurabilityTest);
            checkboxes.add(checkbox_EfflorescenceTest);
            checkboxes.add(checkbox_FireResistanceTest);
            checkboxes.add(checkbox_FlakinessAndElongationIndexTest);
            checkboxes.add(checkbox_FlexuralStrengthTest);
            checkboxes.add(checkbox_HardnessTest);
            checkboxes.add(checkbox_ImpactTest);
            checkboxes.add(checkbox_MarshallStabilityTest);
            checkboxes.add(checkbox_MoistureContentTest);
            checkboxes.add(checkbox_PenetrationTest);
            checkboxes.add(checkbox_ProctorCompactionTest);
            checkboxes.add(checkbox_RapidChloridePenetrationTestRCPT);
            checkboxes.add(checkbox_ShearStrengthTest);
            checkboxes.add(checkbox_ShrinkageTest);
            checkboxes.add(checkbox_SieveAnalysisTest);
            checkboxes.add(checkbox_SlumpTest);
            checkboxes.add(checkbox_SoilClassificationTest);
            checkboxes.add(checkbox_SoilPermeabilityTest);
            checkboxes.add(checkbox_SoundInsulationTest);
            checkboxes.add(checkbox_SpecificGravityTest);
            checkboxes.add(checkbox_SplitTensileStrengthTest);
            checkboxes.add(checkbox_TensileStrengthTest);
            checkboxes.add(checkbox_ThermalConductivityTest);
            checkboxes.add(checkbox_TriaxialShearTest);
            checkboxes.add(checkbox_ViscosityTest);
            checkboxes.add(checkbox_WaterAbsorptionTest);
            checkboxes.add(checkbox_WaterPermeabilityTest);
        }

        // Method to generate concatenated tags based on selected checkboxes
        public String generateConcatenatedTags() {
            StringBuilder concatenatedString = new StringBuilder();
            ArrayList<String> selectedTags = new ArrayList<>();

            // Collect selected tags
            for (CheckBox checkBox : checkboxes) {
                if (checkBox.isChecked()) {
                    // Extract tag name from checkbox id
                    String tagName = context.getResources().getResourceEntryName(checkBox.getId());
                    // Replace "checkbox_" with "tag"
                    tagName = tagName.replace("checkbox_", "tag");
                    selectedTags.add(tagName);
                }
            }

            // Iterate over all selected tags
            for (String tag : selectedTags) {
                // Check if the tag or its reverse exists in concatenatedString
                if (concatenatedString.indexOf(tag) == -1 && concatenatedString.indexOf(reverse(tag)) == -1) {
                    concatenatedString.append(tag);
                }
            }

            return concatenatedString.toString();
        }

        // Method to reverse a string
        private String reverse(String str) {
            return new StringBuilder(str).reverse().toString();
        }

        public void onCheckboxStateChanged() {
            String concatenatedTags = generateConcatenatedTags(); // Generate concatenated tags
            if (!TextUtils.isEmpty(concatenatedTags.trim())) {
                ((UserSearch) context).SearchCenters(concatenatedTags); // Use concatenated tags for searching
            } else {
                ((UserSearch) context).LoadCenters();
            }
        }
    }

}