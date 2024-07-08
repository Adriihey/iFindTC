package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewCenter extends AppCompatActivity {
    private boolean isUserView = false;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView imageView2, back;
    TextView centername, centerlocation, description, centeremail, priceAggregateCrushingValueTest,
            priceAirContentTest,
            priceAsphaltDensityTest,
            priceAtterbergLimitsTest,
            priceBendTest,
            priceBendingStrengthTest,
            priceBitumenContentTest,
            priceCaliforniaBearingRatioCBRTest,
            priceChemicalCompositionTest,
            priceCompressionStrengthTest,
            priceCompressiveStrengthTest,
            priceConsolidationTest,
            priceDimensionalToleranceTest,
            priceDuctilityTest,
            priceDurabilityTest,
            priceEfflorescenceTest,
            priceFireResistanceTest,
            priceFlakinessAndElongationIndexTest,
            priceFlexuralStrengthTest,
            priceHardnessTest,
            priceImpactTest,
            priceMarshallStabilityTest,
            priceMoistureContentTest,
            pricePenetrationTest,
            priceProctorCompactionTest,
            priceRapidChloridePenetrationTestRCPT,
            priceShearStrengthTest,
            priceShrinkageTest,
            priceSieveAnalysisTest,
            priceSlumpTest,
            priceSoilClassificationTest,
            priceSoilPermeabilityTest,
            priceSoundInsulationTest,
            priceSpecificGravityTest,
            priceSplitTensileStrengthTest,
            priceTensileStrengthTest,
            priceThermalConductivityTest,
            priceTriaxialShearTest,
            priceViscosityTest,
            priceWaterAbsorptionTest,
            priceWaterPermeabilityTest;
    LinearLayout layoutAggregateCrushingValueTest, layoutAirContentTest, layoutAsphaltDensityTest, layoutAtterbergLimitsTest, layoutBendTest, layoutBendingStrengthTest, layoutBitumenContentTest, layoutCaliforniaBearingRatioCBRTest, layoutChemicalCompositionTest, layoutCompressionStrengthTest, layoutCompressiveStrengthTest, layoutConsolidationTest, layoutDimensionalToleranceTest, layoutDuctilityTest, layoutDurabilityTest, layoutEfflorescenceTest, layoutFireResistanceTest, layoutFlakinessAndElongationIndexTest, layoutFlexuralStrengthTest, layoutHardnessTest, layoutImpactTest, layoutMarshallStabilityTest, layoutMoistureContentTest, layoutPenetrationTest, layoutProctorCompactionTest, layoutRapidChloridePenetrationTestRCPT, layoutShearStrengthTest, layoutShrinkageTest, layoutSieveAnalysisTest, layoutSlumpTest, layoutSoilClassificationTest, layoutSoilPermeabilityTest, layoutSoundInsulationTest, layoutSpecificGravityTest, layoutSplitTensileStrengthTest, layoutTensileStrengthTest, layoutThermalConductivityTest, layoutTriaxialShearTest, layoutViscosityTest, layoutWaterAbsorptionTest, layoutWaterPermeabilityTest;
    AppCompatButton visitpage, edit, sendrequest;
    private String centerID;
    private String destination;
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
    ScrollView scrollView, listView;
    private ArrayList<String> selectedTests = new ArrayList<>();
    private String formattedTestsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_center);

        scrollView = findViewById(R.id.scrollView2);
        listView = findViewById(R.id.listView);
        back = findViewById(R.id.back);
        sendrequest = findViewById(R.id.sendrequest);

        sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String centerID = getIntent().getStringExtra("uid");
                // Create an AlertDialog.Builder to build the confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Send a request to this testing center?")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Create an Intent to start the SendRequest activity
                                Intent intent = new Intent(v.getContext(), SenRequest.class);
                                intent.putExtra("centerID", centerID);
                                intent.putExtra("selectedTests", formattedTestsString);
                                v.getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Dismiss the dialog
                                dialog.cancel();
                            }
                        });

                // Create the AlertDialog and show it
                AlertDialog alert = builder.create();
                alert.show();
            }
        });



        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action){
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        visitpage = findViewById(R.id.visitpage);
        centeremail = findViewById(R.id.centeremail);
        centername = findViewById(R.id.centername);
        centerlocation = findViewById(R.id.centerlocation);
        description = findViewById(R.id.description);
        imageView2 = findViewById(R.id.imageView2);
        edit = findViewById(R.id.edit);

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

        setUpCheckBoxListeners();
        
        layoutAggregateCrushingValueTest = findViewById(R.id.layoutAggregateCrushingValueTest);
        layoutAirContentTest = findViewById(R.id.layoutAirContentTest);
        layoutAsphaltDensityTest = findViewById(R.id.layoutAsphaltDensityTest);
        layoutAtterbergLimitsTest = findViewById(R.id.layoutAtterbergLimitsTest);
        layoutBendTest = findViewById(R.id.layoutBendTest);
        layoutBendingStrengthTest = findViewById(R.id.layoutBendingStrengthTest);
        layoutBitumenContentTest = findViewById(R.id.layoutBitumenContentTest);
        layoutCaliforniaBearingRatioCBRTest = findViewById(R.id.layoutCaliforniaBearingRatioCBRTest);
        layoutChemicalCompositionTest = findViewById(R.id.layoutChemicalCompositionTest);
        layoutCompressionStrengthTest = findViewById(R.id.layoutCompressionStrengthTest);
        layoutCompressiveStrengthTest = findViewById(R.id.layoutCompressiveStrengthTest);
        layoutConsolidationTest = findViewById(R.id.layoutConsolidationTest);
        layoutDimensionalToleranceTest = findViewById(R.id.layoutDimensionalToleranceTest);
        layoutDuctilityTest = findViewById(R.id.layoutDuctilityTest);
        layoutDurabilityTest = findViewById(R.id.layoutDurabilityTest);
        layoutEfflorescenceTest = findViewById(R.id.layoutEfflorescenceTest);
        layoutFireResistanceTest = findViewById(R.id.layoutFireResistanceTest);
        layoutFlakinessAndElongationIndexTest = findViewById(R.id.layoutFlakinessAndElongationIndexTest);
        layoutFlexuralStrengthTest = findViewById(R.id.layoutFlexuralStrengthTest);
        layoutHardnessTest = findViewById(R.id.layoutHardnessTest);
        layoutImpactTest = findViewById(R.id.layoutImpactTest);
        layoutMarshallStabilityTest = findViewById(R.id.layoutMarshallStabilityTest);
        layoutMoistureContentTest = findViewById(R.id.layoutMoistureContentTest);
        layoutPenetrationTest = findViewById(R.id.layoutPenetrationTest);
        layoutProctorCompactionTest = findViewById(R.id.layoutProctorCompactionTest);
        layoutRapidChloridePenetrationTestRCPT = findViewById(R.id.layoutRapidChloridePenetrationTestRCPT);
        layoutShearStrengthTest = findViewById(R.id.layoutShearStrengthTest);
        layoutShrinkageTest = findViewById(R.id.layoutShrinkageTest);
        layoutSieveAnalysisTest = findViewById(R.id.layoutSieveAnalysisTest);
        layoutSlumpTest = findViewById(R.id.layoutSlumpTest);
        layoutSoilClassificationTest = findViewById(R.id.layoutSoilClassificationTest);
        layoutSoilPermeabilityTest = findViewById(R.id.layoutSoilPermeabilityTest);
        layoutSoundInsulationTest = findViewById(R.id.layoutSoundInsulationTest);
        layoutSpecificGravityTest = findViewById(R.id.layoutSpecificGravityTest);
        layoutSplitTensileStrengthTest = findViewById(R.id.layoutSplitTensileStrengthTest);
        layoutTensileStrengthTest = findViewById(R.id.layoutTensileStrengthTest);
        layoutThermalConductivityTest = findViewById(R.id.layoutThermalConductivityTest);
        layoutTriaxialShearTest = findViewById(R.id.layoutTriaxialShearTest);
        layoutViscosityTest = findViewById(R.id.layoutViscosityTest);
        layoutWaterAbsorptionTest = findViewById(R.id.layoutWaterAbsorptionTest);
        layoutWaterPermeabilityTest = findViewById(R.id.layoutWaterPermeabilityTest);

        priceAggregateCrushingValueTest = findViewById(R.id.priceAggregateCrushingValueTest);
        priceAirContentTest = findViewById(R.id.priceAirContentTest);
        priceAsphaltDensityTest = findViewById(R.id.priceAsphaltDensityTest);
        priceAtterbergLimitsTest = findViewById(R.id.priceAtterbergLimitsTest);
        priceBendTest = findViewById(R.id.priceBendTest);
        priceBendingStrengthTest = findViewById(R.id.priceBendingStrengthTest);
        priceBitumenContentTest = findViewById(R.id.priceBitumenContentTest);
        priceCaliforniaBearingRatioCBRTest = findViewById(R.id.priceCaliforniaBearingRatioCBRTest);
        priceChemicalCompositionTest = findViewById(R.id.priceChemicalCompositionTest);
        priceCompressionStrengthTest = findViewById(R.id.priceCompressionStrengthTest);
        priceCompressiveStrengthTest = findViewById(R.id.priceCompressiveStrengthTest);
        priceConsolidationTest = findViewById(R.id.priceConsolidationTest);
        priceDimensionalToleranceTest = findViewById(R.id.priceDimensionalToleranceTest);
        priceDuctilityTest = findViewById(R.id.priceDuctilityTest);
        priceDurabilityTest = findViewById(R.id.priceDurabilityTest);
        priceEfflorescenceTest = findViewById(R.id.priceEfflorescenceTest);
        priceFireResistanceTest = findViewById(R.id.priceFireResistanceTest);
        priceFlakinessAndElongationIndexTest = findViewById(R.id.priceFlakinessAndElongationIndexTest);
        priceFlexuralStrengthTest = findViewById(R.id.priceFlexuralStrengthTest);
        priceHardnessTest = findViewById(R.id.priceHardnessTest);
        priceImpactTest = findViewById(R.id.priceImpactTest);
        priceMarshallStabilityTest = findViewById(R.id.priceMarshallStabilityTest);
        priceMoistureContentTest = findViewById(R.id.priceMoistureContentTest);
        pricePenetrationTest = findViewById(R.id.pricePenetrationTest);
        priceProctorCompactionTest = findViewById(R.id.priceProctorCompactionTest);
        priceRapidChloridePenetrationTestRCPT = findViewById(R.id.priceRapidChloridePenetrationTestRCPT);
        priceShearStrengthTest = findViewById(R.id.priceShearStrengthTest);
        priceShrinkageTest = findViewById(R.id.priceShrinkageTest);
        priceSieveAnalysisTest = findViewById(R.id.priceSieveAnalysisTest);
        priceSlumpTest = findViewById(R.id.priceSlumpTest);
        priceSoilClassificationTest = findViewById(R.id.priceSoilClassificationTest);
        priceSoilPermeabilityTest = findViewById(R.id.priceSoilPermeabilityTest);
        priceSoundInsulationTest = findViewById(R.id.priceSoundInsulationTest);
        priceSpecificGravityTest = findViewById(R.id.priceSpecificGravityTest);
        priceSplitTensileStrengthTest = findViewById(R.id.priceSplitTensileStrengthTest);
        priceTensileStrengthTest = findViewById(R.id.priceTensileStrengthTest);
        priceThermalConductivityTest = findViewById(R.id.priceThermalConductivityTest);
        priceTriaxialShearTest = findViewById(R.id.priceTriaxialShearTest);
        priceViscosityTest = findViewById(R.id.priceViscosityTest);
        priceWaterAbsorptionTest = findViewById(R.id.priceWaterAbsorptionTest);
        priceWaterPermeabilityTest = findViewById(R.id.priceWaterPermeabilityTest);

        centerID = getIntent().getStringExtra("uid");
        destination = getIntent().getStringExtra("from");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Testing Centers").child(centerID);
        
        LoadCenter();

        if (destination.equals("admin")) {
            // If the destination is "admin", set visibility to VISIBLE
            edit.setVisibility(View.VISIBLE);
        } else if (destination.equals("user")) {
            // If the destination is "user", set visibility to GONE
            edit.setVisibility(View.GONE);
        } else if (destination.equals("userview")) {
            // If the destination is "user", set visibility to GONE
            edit.setVisibility(View.GONE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCenter.this, AdminAddCenter.class);
                intent.putExtra("centerID", centerID);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("admin".equals(destination)) {
                    onBackPressed();
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    finish();
                } else if ("user".equals(destination)) {
                    onBackPressed();
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    finish();
                } else if ("userview".equals(destination)) {
                    onBackPressed();
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                    finish();// Call the default behavior to go back to the previous activity
                }
            }
        });

        visitpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String link = snapshot.child("center_link").getValue(String.class);
                            if (link != null && !link.isEmpty()) {
                                Uri centerlink = Uri.parse(link);

                                // Check the link in the background
                                new CheckLinkTask().execute(centerlink.toString());
                            } else {
                                // Handle the case where the link is empty or null
                                Toast.makeText(getApplicationContext(), "The link is empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });
            }
        });


    }

    private void setUpCheckBoxListeners() {
        setCheckBoxListener(checkbox_AggregateCrushingValueTest, "Aggregate Crushing Value Test");
        setCheckBoxListener(checkbox_AirContentTest, "Air Content Test");
        setCheckBoxListener(checkbox_AsphaltDensityTest, "Asphalt Density Test");
        setCheckBoxListener(checkbox_AtterbergLimitsTest, "Atterberg Limits Test");
        setCheckBoxListener(checkbox_BendTest, "Bend Test");
        setCheckBoxListener(checkbox_BendingStrengthTest, "Bending Strength Test");
        setCheckBoxListener(checkbox_BitumenContentTest, "Bitumen Content Test");
        setCheckBoxListener(checkbox_CaliforniaBearingRatioCBRTest, "California Bearing Ratio (CBR) Test");
        setCheckBoxListener(checkbox_ChemicalCompositionTest, "Chemical Composition Test");
        setCheckBoxListener(checkbox_CompressionStrengthTest, "Compression Strength Test");
        setCheckBoxListener(checkbox_CompressiveStrengthTest, "Compressive Strength Test");
        setCheckBoxListener(checkbox_ConsolidationTest, "Consolidation Test");
        setCheckBoxListener(checkbox_DimensionalToleranceTest, "Dimensional Tolerance Test");
        setCheckBoxListener(checkbox_DuctilityTest, "Ductility Test");
        setCheckBoxListener(checkbox_DurabilityTest, "Durability Test");
        setCheckBoxListener(checkbox_EfflorescenceTest, "Efflorescence Test");
        setCheckBoxListener(checkbox_FireResistanceTest, "Fire Resistance Test");
        setCheckBoxListener(checkbox_FlakinessAndElongationIndexTest, "Flakiness and Elongation Index Test");
        setCheckBoxListener(checkbox_FlexuralStrengthTest, "Flexural Strength Test");
        setCheckBoxListener(checkbox_HardnessTest, "Hardness Test");
        setCheckBoxListener(checkbox_ImpactTest, "Impact Test");
        setCheckBoxListener(checkbox_MarshallStabilityTest, "Marshall Stability Test");
        setCheckBoxListener(checkbox_MoistureContentTest, "Moisture Content Test");
        setCheckBoxListener(checkbox_PenetrationTest, "Penetration Test");
        setCheckBoxListener(checkbox_ProctorCompactionTest, "Proctor Compaction Test");
        setCheckBoxListener(checkbox_RapidChloridePenetrationTestRCPT, "Rapid Chloride Penetration Test (RCPT)");
        setCheckBoxListener(checkbox_ShearStrengthTest, "Shear Strength Test");
        setCheckBoxListener(checkbox_ShrinkageTest, "Shrinkage Test");
        setCheckBoxListener(checkbox_SieveAnalysisTest, "Sieve Analysis Test");
        setCheckBoxListener(checkbox_SlumpTest, "Slump Test");
        setCheckBoxListener(checkbox_SoilClassificationTest, "Soil Classification Test");
        setCheckBoxListener(checkbox_SoilPermeabilityTest, "Soil Permeability Test");
        setCheckBoxListener(checkbox_SoundInsulationTest, "Sound Insulation Test");
        setCheckBoxListener(checkbox_SpecificGravityTest, "Specific Gravity Test");
        setCheckBoxListener(checkbox_SplitTensileStrengthTest, "Split Tensile Strength Test");
        setCheckBoxListener(checkbox_TensileStrengthTest, "Tensile Strength Test");
        setCheckBoxListener(checkbox_ThermalConductivityTest, "Thermal Conductivity Test");
        setCheckBoxListener(checkbox_TriaxialShearTest, "Triaxial Shear Test");
        setCheckBoxListener(checkbox_ViscosityTest, "Viscosity Test");
        setCheckBoxListener(checkbox_WaterAbsorptionTest, "Water Absorption Test");
        setCheckBoxListener(checkbox_WaterPermeabilityTest, "Water Permeability Test");
    }

    private void setCheckBoxListener(CheckBox checkBox, String testName) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTests.add(testName);
            } else {
                selectedTests.remove(testName);
            }
            updateSelectedTestsDisplay();
        });
    }

    private void updateSelectedTestsDisplay() {
        // Format the selected tests into the desired string format
        StringBuilder formattedTests = new StringBuilder();
        for (int i = 0; i < selectedTests.size(); i++) {
            formattedTests.append(selectedTests.get(i));
            if (i < selectedTests.size() - 2) {
                formattedTests.append(",\n");
            } else if (i == selectedTests.size() - 2) {
                formattedTests.append(", and\n");
            }
        }
        formattedTestsString = formattedTests.toString();
    }

    private class CheckLinkTask extends AsyncTask<String, Void, Boolean> {
        private String url;

        @Override
        protected Boolean doInBackground(String... urls) {
            url = urls[0];
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                urlConnection.setRequestMethod("HEAD");
                int responseCode = urlConnection.getResponseCode();
                return (responseCode == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // If the link is valid, open the link
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            } else {
                // If the link is broken, show a Toast
                Toast.makeText(getApplicationContext(), "The link is broken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LoadCenter() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image, cdescription, name, location, email, link, tagAggregateCrushingValueTest,
                            tagAirContentTest,
                            tagAsphaltDensityTest,
                            tagAtterbergLimitsTest,
                            tagBendTest,
                            tagBendingStrengthTest,
                            tagBitumenContentTest,
                            tagCaliforniaBearingRatioCBRTest,
                            tagChemicalCompositionTest,
                            tagCompressionStrengthTest,
                            tagCompressiveStrengthTest,
                            tagConsolidationTest,
                            tagDimensionalToleranceTest,
                            tagDuctilityTest,
                            tagDurabilityTest,
                            tagEfflorescenceTest,
                            tagFireResistanceTest,
                            tagFlakinessAndElongationIndexTest,
                            tagFlexuralStrengthTest,
                            tagHardnessTest,
                            tagImpactTest,
                            tagMarshallStabilityTest,
                            tagMoistureContentTest,
                            tagPenetrationTest,
                            tagProctorCompactionTest,
                            tagRapidChloridePenetrationTestRCPT,
                            tagShearStrengthTest,
                            tagShrinkageTest,
                            tagSieveAnalysisTest,
                            tagSlumpTest,
                            tagSoilClassificationTest,
                            tagSoilPermeabilityTest,
                            tagSoundInsulationTest,
                            tagSpecificGravityTest,
                            tagSplitTensileStrengthTest,
                            tagTensileStrengthTest,
                            tagThermalConductivityTest,
                            tagTriaxialShearTest,
                            tagViscosityTest,
                            tagWaterAbsorptionTest,
                            tagWaterPermeabilityTest;

                    image = snapshot.child("center_image").getValue().toString();
                    email = snapshot.child("center_email").getValue().toString();
                    cdescription = snapshot.child("center_description").getValue().toString();
                    name = snapshot.child("center_name").getValue().toString();
                    location = snapshot.child("center_address").getValue().toString();
                    tagAggregateCrushingValueTest = snapshot.child("tagAggregateCrushingValueTest").getValue().toString();
                    tagAirContentTest = snapshot.child("tagAirContentTest").getValue().toString();
                    tagAsphaltDensityTest = snapshot.child("tagAsphaltDensityTest").getValue().toString();
                    tagAtterbergLimitsTest = snapshot.child("tagAtterbergLimitsTest").getValue().toString();
                    tagBendTest = snapshot.child("tagBendTest").getValue().toString();
                    tagBendingStrengthTest = snapshot.child("tagBendingStrengthTest").getValue().toString();
                    tagBitumenContentTest = snapshot.child("tagBitumenContentTest").getValue().toString();
                    tagCaliforniaBearingRatioCBRTest = snapshot.child("tagCaliforniaBearingRatioCBRTest").getValue().toString();
                    tagChemicalCompositionTest = snapshot.child("tagChemicalCompositionTest").getValue().toString();
                    tagCompressionStrengthTest = snapshot.child("tagCompressionStrengthTest").getValue().toString();
                    tagCompressiveStrengthTest = snapshot.child("tagCompressiveStrengthTest").getValue().toString();
                    tagConsolidationTest = snapshot.child("tagConsolidationTest").getValue().toString();
                    tagDimensionalToleranceTest = snapshot.child("tagDimensionalToleranceTest").getValue().toString();
                    tagDuctilityTest = snapshot.child("tagDuctilityTest").getValue().toString();
                    tagDurabilityTest = snapshot.child("tagDurabilityTest").getValue().toString();
                    tagEfflorescenceTest = snapshot.child("tagEfflorescenceTest").getValue().toString();
                    tagFireResistanceTest = snapshot.child("tagFireResistanceTest").getValue().toString();
                    tagFlakinessAndElongationIndexTest = snapshot.child("tagFlakinessAndElongationIndexTest").getValue().toString();
                    tagFlexuralStrengthTest = snapshot.child("tagFlexuralStrengthTest").getValue().toString();
                    tagHardnessTest = snapshot.child("tagHardnessTest").getValue().toString();
                    tagImpactTest = snapshot.child("tagImpactTest").getValue().toString();
                    tagMarshallStabilityTest = snapshot.child("tagMarshallStabilityTest").getValue().toString();
                    tagMoistureContentTest = snapshot.child("tagMoistureContentTest").getValue().toString();
                    tagPenetrationTest = snapshot.child("tagPenetrationTest").getValue().toString();
                    tagProctorCompactionTest = snapshot.child("tagProctorCompactionTest").getValue().toString();
                    tagRapidChloridePenetrationTestRCPT = snapshot.child("tagRapidChloridePenetrationTestRCPT").getValue().toString();
                    tagShearStrengthTest = snapshot.child("tagShearStrengthTest").getValue().toString();
                    tagShrinkageTest = snapshot.child("tagShrinkageTest").getValue().toString();
                    tagSieveAnalysisTest = snapshot.child("tagSieveAnalysisTest").getValue().toString();
                    tagSlumpTest = snapshot.child("tagSlumpTest").getValue().toString();
                    tagSoilClassificationTest = snapshot.child("tagSoilClassificationTest").getValue().toString();
                    tagSoilPermeabilityTest = snapshot.child("tagSoilPermeabilityTest").getValue().toString();
                    tagSoundInsulationTest = snapshot.child("tagSoundInsulationTest").getValue().toString();
                    tagSpecificGravityTest = snapshot.child("tagSpecificGravityTest").getValue().toString();
                    tagSplitTensileStrengthTest = snapshot.child("tagSplitTensileStrengthTest").getValue().toString();
                    tagTensileStrengthTest = snapshot.child("tagTensileStrengthTest").getValue().toString();
                    tagThermalConductivityTest = snapshot.child("tagThermalConductivityTest").getValue().toString();
                    tagTriaxialShearTest = snapshot.child("tagTriaxialShearTest").getValue().toString();
                    tagViscosityTest = snapshot.child("tagViscosityTest").getValue().toString();
                    tagWaterAbsorptionTest = snapshot.child("tagWaterAbsorptionTest").getValue().toString();
                    tagWaterPermeabilityTest = snapshot.child("tagWaterPermeabilityTest").getValue().toString();

                    Picasso.get().load(image).into(imageView2);
                    description.setText(cdescription);
                    description.setText(cdescription);
                    centername.setText(name);
                    centerlocation.setText(location);
                    centeremail.setText(email);

                    // For tagAggregateCrushingValueTest
                    if (!tagAggregateCrushingValueTest.isEmpty()) {
                        layoutAggregateCrushingValueTest.setVisibility(View.VISIBLE);
                        checkbox_AggregateCrushingValueTest.setVisibility(View.VISIBLE);
                        priceAggregateCrushingValueTest.setText(tagAggregateCrushingValueTest);
                    } else {
                        layoutAggregateCrushingValueTest.setVisibility(View.GONE);
                        checkbox_AggregateCrushingValueTest.setVisibility(View.GONE);
                    }

// For tagAirContentTest
                    if (!tagAirContentTest.isEmpty()) {
                        layoutAirContentTest.setVisibility(View.VISIBLE);
                        checkbox_AirContentTest.setVisibility(View.VISIBLE);
                        priceAirContentTest.setText(tagAirContentTest);
                    } else {
                        layoutAirContentTest.setVisibility(View.GONE);
                        checkbox_AirContentTest.setVisibility(View.GONE);
                    }

// For tagAsphaltDensityTest
                    if (!tagAsphaltDensityTest.isEmpty()) {
                        layoutAsphaltDensityTest.setVisibility(View.VISIBLE);
                        checkbox_AsphaltDensityTest.setVisibility(View.VISIBLE);
                        priceAsphaltDensityTest.setText(tagAsphaltDensityTest);
                    } else {
                        layoutAsphaltDensityTest.setVisibility(View.GONE);
                        checkbox_AsphaltDensityTest.setVisibility(View.GONE);
                    }

// For tagAtterbergLimitsTest
                    if (!tagAtterbergLimitsTest.isEmpty()) {
                        layoutAtterbergLimitsTest.setVisibility(View.VISIBLE);
                        checkbox_AtterbergLimitsTest.setVisibility(View.VISIBLE);
                        priceAtterbergLimitsTest.setText(tagAtterbergLimitsTest);
                    } else {
                        layoutAtterbergLimitsTest.setVisibility(View.GONE);
                        checkbox_AtterbergLimitsTest.setVisibility(View.GONE);
                    }

// For tagBendTest
                    if (!tagBendTest.isEmpty()) {
                        layoutBendTest.setVisibility(View.VISIBLE);
                        checkbox_BendTest.setVisibility(View.VISIBLE);
                        priceBendTest.setText(tagBendTest);
                    } else {
                        layoutBendTest.setVisibility(View.GONE);
                        checkbox_BendTest.setVisibility(View.GONE);
                    }

// For tagBendingStrengthTest
                    if (!tagBendingStrengthTest.isEmpty()) {
                        layoutBendingStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_BendingStrengthTest.setVisibility(View.VISIBLE);
                        priceBendingStrengthTest.setText(tagBendingStrengthTest);
                    } else {
                        layoutBendingStrengthTest.setVisibility(View.GONE);
                        checkbox_BendingStrengthTest.setVisibility(View.GONE);
                    }

// For tagBitumenContentTest
                    if (!tagBitumenContentTest.isEmpty()) {
                        layoutBitumenContentTest.setVisibility(View.VISIBLE);
                        checkbox_BitumenContentTest.setVisibility(View.VISIBLE);
                        priceBitumenContentTest.setText(tagBitumenContentTest);
                    } else {
                        layoutBitumenContentTest.setVisibility(View.GONE);
                        checkbox_BitumenContentTest.setVisibility(View.GONE);
                    }

// For tagCaliforniaBearingRatioCBRTest
                    if (!tagCaliforniaBearingRatioCBRTest.isEmpty()) {
                        layoutCaliforniaBearingRatioCBRTest.setVisibility(View.VISIBLE);
                        checkbox_CaliforniaBearingRatioCBRTest.setVisibility(View.VISIBLE);
                        priceCaliforniaBearingRatioCBRTest.setText(tagCaliforniaBearingRatioCBRTest);
                    } else {
                        layoutCaliforniaBearingRatioCBRTest.setVisibility(View.GONE);
                        checkbox_CaliforniaBearingRatioCBRTest.setVisibility(View.GONE);
                    }

// For tagChemicalCompositionTest
                    if (!tagChemicalCompositionTest.isEmpty()) {
                        layoutChemicalCompositionTest.setVisibility(View.VISIBLE);
                        checkbox_ChemicalCompositionTest.setVisibility(View.VISIBLE);
                        priceChemicalCompositionTest.setText(tagChemicalCompositionTest);
                    } else {
                        layoutChemicalCompositionTest.setVisibility(View.GONE);
                        checkbox_ChemicalCompositionTest.setVisibility(View.GONE);
                    }

// For tagCompressionStrengthTest
                    if (!tagCompressionStrengthTest.isEmpty()) {
                        layoutCompressionStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_CompressionStrengthTest.setVisibility(View.VISIBLE);
                        priceCompressionStrengthTest.setText(tagCompressionStrengthTest);
                    } else {
                        layoutCompressionStrengthTest.setVisibility(View.GONE);
                        checkbox_CompressionStrengthTest.setVisibility(View.GONE);
                    }

// For tagCompressiveStrengthTest
                    if (!tagCompressiveStrengthTest.isEmpty()) {
                        layoutCompressiveStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_CompressiveStrengthTest.setVisibility(View.VISIBLE);
                        priceCompressiveStrengthTest.setText(tagCompressiveStrengthTest);
                    } else {
                        layoutCompressiveStrengthTest.setVisibility(View.GONE);
                        checkbox_CompressiveStrengthTest.setVisibility(View.GONE);
                    }

// For tagConsolidationTest
                    if (!tagConsolidationTest.isEmpty()) {
                        layoutConsolidationTest.setVisibility(View.VISIBLE);
                        checkbox_ConsolidationTest.setVisibility(View.VISIBLE);
                        priceConsolidationTest.setText(tagConsolidationTest);
                    } else {
                        layoutConsolidationTest.setVisibility(View.GONE);
                        checkbox_ConsolidationTest.setVisibility(View.GONE);
                    }

// For tagDimensionalToleranceTest
                    if (!tagDimensionalToleranceTest.isEmpty()) {
                        layoutDimensionalToleranceTest.setVisibility(View.VISIBLE);
                        checkbox_DimensionalToleranceTest.setVisibility(View.VISIBLE);
                        priceDimensionalToleranceTest.setText(tagDimensionalToleranceTest);
                    } else {
                        layoutDimensionalToleranceTest.setVisibility(View.GONE);
                        checkbox_DimensionalToleranceTest.setVisibility(View.GONE);
                    }

// For tagDuctilityTest
                    if (!tagDuctilityTest.isEmpty()) {
                        layoutDuctilityTest.setVisibility(View.VISIBLE);
                        checkbox_DuctilityTest.setVisibility(View.VISIBLE);
                        priceDuctilityTest.setText(tagDuctilityTest);
                    } else {
                        layoutDuctilityTest.setVisibility(View.GONE);
                        checkbox_DuctilityTest.setVisibility(View.GONE);
                    }

// For tagDurabilityTest
                    if (!tagDurabilityTest.isEmpty()) {
                        layoutDurabilityTest.setVisibility(View.VISIBLE);
                        checkbox_DurabilityTest.setVisibility(View.VISIBLE);
                        priceDurabilityTest.setText(tagDurabilityTest);
                    } else {
                        layoutDurabilityTest.setVisibility(View.GONE);
                        checkbox_DurabilityTest.setVisibility(View.GONE);
                    }

// For tagEfflorescenceTest
                    if (!tagEfflorescenceTest.isEmpty()) {
                        layoutEfflorescenceTest.setVisibility(View.VISIBLE);
                        checkbox_EfflorescenceTest.setVisibility(View.VISIBLE);
                        priceEfflorescenceTest.setText(tagEfflorescenceTest);
                    } else {
                        layoutEfflorescenceTest.setVisibility(View.GONE);
                        checkbox_EfflorescenceTest.setVisibility(View.GONE);
                    }

// For tagFireResistanceTest
                    if (!tagFireResistanceTest.isEmpty()) {
                        layoutFireResistanceTest.setVisibility(View.VISIBLE);
                        checkbox_FireResistanceTest.setVisibility(View.VISIBLE);
                        priceFireResistanceTest.setText(tagFireResistanceTest);
                    } else {
                        layoutFireResistanceTest.setVisibility(View.GONE);
                        checkbox_FireResistanceTest.setVisibility(View.GONE);
                    }

// For tagFlakinessAndElongationIndexTest
                    if (!tagFlakinessAndElongationIndexTest.isEmpty()) {
                        layoutFlakinessAndElongationIndexTest.setVisibility(View.VISIBLE);
                        checkbox_FlakinessAndElongationIndexTest.setVisibility(View.VISIBLE);
                        priceFlakinessAndElongationIndexTest.setText(tagFlakinessAndElongationIndexTest);
                    } else {
                        layoutFlakinessAndElongationIndexTest.setVisibility(View.GONE);
                        checkbox_FlakinessAndElongationIndexTest.setVisibility(View.GONE);
                    }

// For tagFlexuralStrengthTest
                    if (!tagFlexuralStrengthTest.isEmpty()) {
                        layoutFlexuralStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_FlexuralStrengthTest.setVisibility(View.VISIBLE);
                        priceFlexuralStrengthTest.setText(tagFlexuralStrengthTest);
                    } else {
                        layoutFlexuralStrengthTest.setVisibility(View.GONE);
                        checkbox_FlexuralStrengthTest.setVisibility(View.GONE);
                    }

// For tagHardnessTest
                    if (!tagHardnessTest.isEmpty()) {
                        layoutHardnessTest.setVisibility(View.VISIBLE);
                        checkbox_HardnessTest.setVisibility(View.VISIBLE);
                        priceHardnessTest.setText(tagHardnessTest);
                    } else {
                        layoutHardnessTest.setVisibility(View.GONE);
                        checkbox_HardnessTest.setVisibility(View.GONE);
                    }

// For tagImpactTest
                    if (!tagImpactTest.isEmpty()) {
                        layoutImpactTest.setVisibility(View.VISIBLE);
                        checkbox_ImpactTest.setVisibility(View.VISIBLE);
                        priceImpactTest.setText(tagImpactTest);
                    } else {
                        layoutImpactTest.setVisibility(View.GONE);
                        checkbox_ImpactTest.setVisibility(View.GONE);
                    }

// For tagMarshallStabilityTest
                    if (!tagMarshallStabilityTest.isEmpty()) {
                        layoutMarshallStabilityTest.setVisibility(View.VISIBLE);
                        checkbox_MarshallStabilityTest.setVisibility(View.VISIBLE);
                        priceMarshallStabilityTest.setText(tagMarshallStabilityTest);
                    } else {
                        layoutMarshallStabilityTest.setVisibility(View.GONE);
                        checkbox_MarshallStabilityTest.setVisibility(View.GONE);
                    }

// For tagMoistureContentTest
                    if (!tagMoistureContentTest.isEmpty()) {
                        layoutMoistureContentTest.setVisibility(View.VISIBLE);
                        checkbox_MoistureContentTest.setVisibility(View.VISIBLE);
                        priceMoistureContentTest.setText(tagMoistureContentTest);
                    } else {
                        layoutMoistureContentTest.setVisibility(View.GONE);
                        checkbox_MoistureContentTest.setVisibility(View.GONE);
                    }

// For tagPenetrationTest
                    if (!tagPenetrationTest.isEmpty()) {
                        layoutPenetrationTest.setVisibility(View.VISIBLE);
                        checkbox_PenetrationTest.setVisibility(View.VISIBLE);
                        pricePenetrationTest.setText(tagPenetrationTest);
                    } else {
                        layoutPenetrationTest.setVisibility(View.GONE);
                        checkbox_PenetrationTest.setVisibility(View.GONE);
                    }

// For tagProctorCompactionTest
                    if (!tagProctorCompactionTest.isEmpty()) {
                        layoutProctorCompactionTest.setVisibility(View.VISIBLE);
                        checkbox_ProctorCompactionTest.setVisibility(View.VISIBLE);
                        priceProctorCompactionTest.setText(tagProctorCompactionTest);
                    } else {
                        layoutProctorCompactionTest.setVisibility(View.GONE);
                        checkbox_ProctorCompactionTest.setVisibility(View.GONE);
                    }

// For tagRapidChloridePenetrationTestRCPT
                    if (!tagRapidChloridePenetrationTestRCPT.isEmpty()) {
                        layoutRapidChloridePenetrationTestRCPT.setVisibility(View.VISIBLE);
                        checkbox_RapidChloridePenetrationTestRCPT.setVisibility(View.VISIBLE);
                        priceRapidChloridePenetrationTestRCPT.setText(tagRapidChloridePenetrationTestRCPT);
                    } else {
                        layoutRapidChloridePenetrationTestRCPT.setVisibility(View.GONE);
                        checkbox_RapidChloridePenetrationTestRCPT.setVisibility(View.GONE);
                    }

// For tagShearStrengthTest
                    if (!tagShearStrengthTest.isEmpty()) {
                        layoutShearStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_ShearStrengthTest.setVisibility(View.VISIBLE);
                        priceShearStrengthTest.setText(tagShearStrengthTest);
                    } else {
                        layoutShearStrengthTest.setVisibility(View.GONE);
                        checkbox_ShearStrengthTest.setVisibility(View.GONE);
                    }

// For tagShrinkageTest
                    if (!tagShrinkageTest.isEmpty()) {
                        layoutShrinkageTest.setVisibility(View.VISIBLE);
                        checkbox_ShrinkageTest.setVisibility(View.VISIBLE);
                        priceShrinkageTest.setText(tagShrinkageTest);
                    } else {
                        layoutShrinkageTest.setVisibility(View.GONE);
                        checkbox_ShrinkageTest.setVisibility(View.GONE);
                    }

// For tagSieveAnalysisTest
                    if (!tagSieveAnalysisTest.isEmpty()) {
                        layoutSieveAnalysisTest.setVisibility(View.VISIBLE);
                        checkbox_SieveAnalysisTest.setVisibility(View.VISIBLE);
                        priceSieveAnalysisTest.setText(tagSieveAnalysisTest);
                    } else {
                        layoutSieveAnalysisTest.setVisibility(View.GONE);
                        checkbox_SieveAnalysisTest.setVisibility(View.GONE);
                    }

// For tagSlumpTest
                    if (!tagSlumpTest.isEmpty()) {
                        layoutSlumpTest.setVisibility(View.VISIBLE);
                        checkbox_SlumpTest.setVisibility(View.VISIBLE);
                        priceSlumpTest.setText(tagSlumpTest);
                    } else {
                        layoutSlumpTest.setVisibility(View.GONE);
                        checkbox_SlumpTest.setVisibility(View.GONE);
                    }

// For tagSoilClassificationTest
                    if (!tagSoilClassificationTest.isEmpty()) {
                        layoutSoilClassificationTest.setVisibility(View.VISIBLE);
                        checkbox_SoilClassificationTest.setVisibility(View.VISIBLE);
                        priceSoilClassificationTest.setText(tagSoilClassificationTest);
                    } else {
                        layoutSoilClassificationTest.setVisibility(View.GONE);
                        checkbox_SoilClassificationTest.setVisibility(View.GONE);
                    }

// For tagSoilPermeabilityTest
                    if (!tagSoilPermeabilityTest.isEmpty()) {
                        layoutSoilPermeabilityTest.setVisibility(View.VISIBLE);
                        checkbox_SoilPermeabilityTest.setVisibility(View.VISIBLE);
                        priceSoilPermeabilityTest.setText(tagSoilPermeabilityTest);
                    } else {
                        layoutSoilPermeabilityTest.setVisibility(View.GONE);
                        checkbox_SoilPermeabilityTest.setVisibility(View.GONE);
                    }

// For tagSoundInsulationTest
                    if (!tagSoundInsulationTest.isEmpty()) {
                        layoutSoundInsulationTest.setVisibility(View.VISIBLE);
                        checkbox_SoundInsulationTest.setVisibility(View.VISIBLE);
                        priceSoundInsulationTest.setText(tagSoundInsulationTest);
                    } else {
                        layoutSoundInsulationTest.setVisibility(View.GONE);
                        checkbox_SoundInsulationTest.setVisibility(View.GONE);
                    }

// For tagSpecificGravityTest
                    if (!tagSpecificGravityTest.isEmpty()) {
                        layoutSpecificGravityTest.setVisibility(View.VISIBLE);
                        checkbox_SpecificGravityTest.setVisibility(View.VISIBLE);
                        priceSpecificGravityTest.setText(tagSpecificGravityTest);
                    } else {
                        layoutSpecificGravityTest.setVisibility(View.GONE);
                        checkbox_SpecificGravityTest.setVisibility(View.GONE);
                    }

// For tagSplitTensileStrengthTest
                    if (!tagSplitTensileStrengthTest.isEmpty()) {
                        layoutSplitTensileStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_SplitTensileStrengthTest.setVisibility(View.VISIBLE);
                        priceSplitTensileStrengthTest.setText(tagSplitTensileStrengthTest);
                    } else {
                        layoutSplitTensileStrengthTest.setVisibility(View.GONE);
                        checkbox_SplitTensileStrengthTest.setVisibility(View.GONE);
                    }

// For tagTensileStrengthTest
                    if (!tagTensileStrengthTest.isEmpty()) {
                        layoutTensileStrengthTest.setVisibility(View.VISIBLE);
                        checkbox_TensileStrengthTest.setVisibility(View.VISIBLE);
                        priceTensileStrengthTest.setText(tagTensileStrengthTest);
                    } else {
                        layoutTensileStrengthTest.setVisibility(View.GONE);
                        checkbox_TensileStrengthTest.setVisibility(View.GONE);
                    }

// For tagThermalConductivityTest
                    if (!tagThermalConductivityTest.isEmpty()) {
                        layoutThermalConductivityTest.setVisibility(View.VISIBLE);
                        checkbox_ThermalConductivityTest.setVisibility(View.VISIBLE);
                        priceThermalConductivityTest.setText(tagThermalConductivityTest);
                    } else {
                        layoutThermalConductivityTest.setVisibility(View.GONE);
                        checkbox_ThermalConductivityTest.setVisibility(View.GONE);
                    }

// For tagTriaxialShearTest
                    if (!tagTriaxialShearTest.isEmpty()) {
                        layoutTriaxialShearTest.setVisibility(View.VISIBLE);
                        checkbox_TriaxialShearTest.setVisibility(View.VISIBLE);
                        priceTriaxialShearTest.setText(tagTriaxialShearTest);
                    } else {
                        layoutTriaxialShearTest.setVisibility(View.GONE);
                        checkbox_TriaxialShearTest.setVisibility(View.GONE);
                    }

// For tagViscosityTest
                    if (!tagViscosityTest.isEmpty()) {
                        layoutViscosityTest.setVisibility(View.VISIBLE);
                        checkbox_ViscosityTest.setVisibility(View.VISIBLE);
                        priceViscosityTest.setText(tagViscosityTest);
                    } else {
                        layoutViscosityTest.setVisibility(View.GONE);
                        checkbox_ViscosityTest.setVisibility(View.GONE);
                    }

// For tagWaterAbsorptionTest
                    if (!tagWaterAbsorptionTest.isEmpty()) {
                        layoutWaterAbsorptionTest.setVisibility(View.VISIBLE);
                        checkbox_WaterAbsorptionTest.setVisibility(View.VISIBLE);
                        priceWaterAbsorptionTest.setText(tagWaterAbsorptionTest);
                    } else {
                        layoutWaterAbsorptionTest.setVisibility(View.GONE);
                        checkbox_WaterAbsorptionTest.setVisibility(View.GONE);
                    }

// For tagWaterPermeabilityTest
                    if (!tagWaterPermeabilityTest.isEmpty()) {
                        layoutWaterPermeabilityTest.setVisibility(View.VISIBLE);
                        checkbox_WaterPermeabilityTest.setVisibility(View.VISIBLE);
                        priceWaterPermeabilityTest.setText(tagWaterPermeabilityTest);
                    } else {
                        layoutWaterPermeabilityTest.setVisibility(View.GONE);
                        checkbox_WaterPermeabilityTest.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if ("userview".equals(destination)) {
            isUserView = true;
        } else {
            isUserView = false;
        }

        if (isUserView) {
            super.onBackPressed(); // Call the default behavior
        } else {
            super.onBackPressed();
        }
    }

}