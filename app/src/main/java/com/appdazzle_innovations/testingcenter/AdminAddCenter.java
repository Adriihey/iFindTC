package com.appdazzle_innovations.testingcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddCenter extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private Uri currentImageUri;
    AppCompatButton confirmaddcenter;
    SearchView searchView;
    ImageView home, addcenter, profile, tcdisplaypic, checkicon, down;
    NestedScrollView scrollView;
    ListView listView;
    ArrayAdapter<String> adapter;
    Uri uri;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String[] tagsTests = {
            "Aggregate Crushing Value Test", "Air Content Test", "Asphalt Density Test", "Atterberg Limits Test", "Bend Test",
            "Bending Strength Test", "Bitumen Content Test", "California Bearing Ratio (CBR) Test", "Chemical Composition Test",
            "Compression Strength Test", "Compressive Strength Test", "Consolidation Test", "Dimensional Tolerance Test",
            "Ductility Test", "Durability Test", "Efflorescence Test", "Fire Resistance Test", "Flakiness And Elongation Index Test",
            "Flexural Strength Test", "Hardness Test", "Impact Test", "Marshall Stability Test", "Moisture Content Test", "Penetration Test",
            "Proctor Compaction Test", "Rapid Chloride Penetration Test (RCPT)", "Shear Strength Test", "Shrinkage Test", "Sieve Analysis Test",
            "Slump Test", "Soil Classification Test", "Soil Permeability Test", "Sound Insulation Test", "Specific Gravity Test",
            "Split Tensile Strength Test", "Tensile Strength Test", "Thermal Conductivity Test", "Triaxial Shear Test", "Viscosity Test",
            "Water Absorption Test", "Water Permeability Test"
    };
    LinearLayout searchandlist, mainlayout, layoutAggregateCrushingValueTest, layoutAirContentTest, layoutAsphaltDensityTest, layoutAtterbergLimitsTest, layoutBendTest, layoutBendingStrengthTest, layoutBitumenContentTest, layoutCaliforniaBearingRatioCBRTest, layoutChemicalCompositionTest, layoutCompressionStrengthTest, layoutCompressiveStrengthTest, layoutConsolidationTest, layoutDimensionalToleranceTest, layoutDuctilityTest, layoutDurabilityTest, layoutEfflorescenceTest, layoutFireResistanceTest, layoutFlakinessAndElongationIndexTest, layoutFlexuralStrengthTest, layoutHardnessTest, layoutImpactTest, layoutMarshallStabilityTest, layoutMoistureContentTest, layoutPenetrationTest, layoutPorosityTest, layoutProctorCompactionTest, layoutRapidChloridePenetrationTestRCPT, layoutShearStrengthTest, layoutShrinkageTest, layoutSieveAnalysisTest, layoutSlumpTest, layoutSoilClassificationTest, layoutSoilPermeabilityTest, layoutSoundInsulationTest, layoutSpecificGravityTest, layoutSplitTensileStrengthTest, layoutTensileStrengthTest, layoutThermalConductivityTest, layoutTriaxialShearTest, layoutViscosityTest, layoutWaterAbsorptionTest, layoutWaterPermeabilityTest;
    TextInputEditText tcemail, tcname, tcaddress, tclink, tcdescription, aggregateCrushingValueTest, airContentTest, asphaltDensityTest, atterbergLimitsTest, bendTest, bendingStrengthTest, bitumenContentTest, californiaBearingRatioCBRTest, chemicalCompositionTest, compressionStrengthTest, compressiveStrengthTest, consolidationTest, dimensionalToleranceTest, ductilityTest, durabilityTest, efflorescenceTest, fireResistanceTest, flakinessAndElongationIndexTest, flexuralStrengthTest, hardnessTest, impactTest, marshallStabilityTest, moistureContentTest, penetrationTest, porosityTest, proctorCompactionTest, rapidChloridePenetrationTestRCPT, shearStrengthTest, shrinkageTest, sieveAnalysisTest, slumpTest, soilClassificationTest, soilPermeabilityTest, soundInsulationTest, specificGravityTest, splitTensileStrengthTest, tensileStrengthTest, thermalConductivityTest, triaxialShearTest, viscosityTest, waterAbsorptionTest, waterPermeabilityTest;
    private String centerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_center);

        tcname = findViewById(R.id.tcname);
        tcemail = findViewById(R.id.tcemail);
        tcaddress = findViewById(R.id.tcaddress);
        tclink = findViewById(R.id.tclink);
        tcdescription = findViewById(R.id.tcadescription);

        confirmaddcenter = findViewById(R.id.confirmaddcenter);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tcdisplaypic = findViewById(R.id.tcdisplaypic);

        searchandlist = findViewById(R.id.searchandlist);
        mainlayout = findViewById(R.id.mainlayout);
        mainlayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        checkicon = findViewById(R.id.checkicon);
        checkicon.setVisibility(View.GONE);
        searchView = findViewById(R.id.searchView);
        scrollView = findViewById(R.id.scrollview);

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, tagsTests);
        listView.setAdapter(adapter);

        layoutAggregateCrushingValueTest = findViewById(R.id.layoutAggregateCrushingValueTest);
        layoutAggregateCrushingValueTest.setVisibility(View.GONE);
        aggregateCrushingValueTest = findViewById(R.id.aggregateCrushingValueTest);

        layoutAirContentTest = findViewById(R.id.layoutAirContentTest);
        layoutAirContentTest.setVisibility(View.GONE);
        airContentTest = findViewById(R.id.airContentTest);

        layoutAsphaltDensityTest = findViewById(R.id.layoutAsphaltDensityTest);
        layoutAsphaltDensityTest.setVisibility(View.GONE);
        asphaltDensityTest = findViewById(R.id.asphaltDensityTest);

        layoutAtterbergLimitsTest = findViewById(R.id.layoutAtterbergLimitsTest);
        layoutAtterbergLimitsTest.setVisibility(View.GONE);
        atterbergLimitsTest = findViewById(R.id.atterbergLimitsTest);

        layoutBendTest = findViewById(R.id.layoutBendTest);
        layoutBendTest.setVisibility(View.GONE);
        bendTest = findViewById(R.id.bendTest);

        layoutBendingStrengthTest = findViewById(R.id.layoutBendingStrengthTest);
        layoutBendingStrengthTest.setVisibility(View.GONE);
        bendingStrengthTest = findViewById(R.id.bendingStrengthTest);

        layoutBitumenContentTest = findViewById(R.id.layoutBitumenContentTest);
        layoutBitumenContentTest.setVisibility(View.GONE);
        bitumenContentTest = findViewById(R.id.bitumenContentTest);

        layoutCaliforniaBearingRatioCBRTest = findViewById(R.id.layoutCBR);
        layoutCaliforniaBearingRatioCBRTest.setVisibility(View.GONE);
        californiaBearingRatioCBRTest = findViewById(R.id.californiaBearingRatioCBRTest);

        layoutChemicalCompositionTest = findViewById(R.id.layoutChemicalCompositionTest);
        layoutChemicalCompositionTest.setVisibility(View.GONE);
        chemicalCompositionTest = findViewById(R.id.chemicalCompositionTest);

        layoutCompressionStrengthTest = findViewById(R.id.layoutCompressionStrengthTest);
        layoutCompressionStrengthTest.setVisibility(View.GONE);
        compressionStrengthTest = findViewById(R.id.compressionStrengthTest);

        layoutCompressiveStrengthTest = findViewById(R.id.layoutCompressiveStrengthTest);
        layoutCompressiveStrengthTest.setVisibility(View.GONE);
        compressiveStrengthTest = findViewById(R.id.compressiveStrengthTest);

        layoutConsolidationTest = findViewById(R.id.layoutConsolidationTest);
        layoutConsolidationTest.setVisibility(View.GONE);
        consolidationTest = findViewById(R.id.consolidationTest);

        layoutDimensionalToleranceTest = findViewById(R.id.layoutDimensionalToleranceTest);
        layoutDimensionalToleranceTest.setVisibility(View.GONE);
        dimensionalToleranceTest = findViewById(R.id.dimensionalToleranceTest);

        layoutDuctilityTest = findViewById(R.id.layoutDuctilityTest);
        layoutDuctilityTest.setVisibility(View.GONE);
        ductilityTest = findViewById(R.id.ductilityTest);

        layoutDurabilityTest = findViewById(R.id.layoutDurabilityTest);
        layoutDurabilityTest.setVisibility(View.GONE);
        durabilityTest = findViewById(R.id.durabilityTest);

        layoutEfflorescenceTest = findViewById(R.id.layoutEfflorescenceTest);
        layoutEfflorescenceTest.setVisibility(View.GONE);
        efflorescenceTest = findViewById(R.id.efflorescenceTest);

        layoutFireResistanceTest = findViewById(R.id.layoutFireResistanceTest);
        layoutFireResistanceTest.setVisibility(View.GONE);
        fireResistanceTest = findViewById(R.id.fireResistanceTest);

        layoutFlakinessAndElongationIndexTest = findViewById(R.id.layoutFlakinessAndElongationIndexTest);
        layoutFlakinessAndElongationIndexTest.setVisibility(View.GONE);
        flakinessAndElongationIndexTest = findViewById(R.id.flakinessAndElongationIndexTest);

        layoutFlexuralStrengthTest = findViewById(R.id.layoutFlexuralStrengthTest);
        layoutFlexuralStrengthTest.setVisibility(View.GONE);
        flexuralStrengthTest = findViewById(R.id.flexuralStrengthTest);

        layoutHardnessTest = findViewById(R.id.layoutHardnessTest);
        layoutHardnessTest.setVisibility(View.GONE);
        hardnessTest = findViewById(R.id.hardnessTest);

        layoutImpactTest = findViewById(R.id.layoutImpactTest);
        layoutImpactTest.setVisibility(View.GONE);
        impactTest = findViewById(R.id.impactTest);

        layoutMarshallStabilityTest = findViewById(R.id.layoutMarshallStabilityTest);
        layoutMarshallStabilityTest.setVisibility(View.GONE);
        marshallStabilityTest = findViewById(R.id.marshallStabilityTest);

        layoutMoistureContentTest = findViewById(R.id.layoutMoistureContentTest);
        layoutMoistureContentTest.setVisibility(View.GONE);
        moistureContentTest = findViewById(R.id.moistureContentTest);

        layoutPenetrationTest = findViewById(R.id.layoutPenetrationTest);
        layoutPenetrationTest.setVisibility(View.GONE);
        penetrationTest = findViewById(R.id.penetrationTest);

        layoutProctorCompactionTest = findViewById(R.id.layoutProctorCompactionTest);
        layoutProctorCompactionTest.setVisibility(View.GONE);
        proctorCompactionTest = findViewById(R.id.proctorCompactionTest);

        layoutRapidChloridePenetrationTestRCPT = findViewById(R.id.layoutRapidChloridePenetrationTestRCPT);
        layoutRapidChloridePenetrationTestRCPT.setVisibility(View.GONE);
        rapidChloridePenetrationTestRCPT = findViewById(R.id.rapidChloridePenetrationTestRCPT);

        layoutShearStrengthTest = findViewById(R.id.layoutShearStrengthTest);
        layoutShearStrengthTest.setVisibility(View.GONE);
        shearStrengthTest = findViewById(R.id.shearStrengthTest);

        layoutShrinkageTest = findViewById(R.id.layoutShrinkageTest);
        layoutShrinkageTest.setVisibility(View.GONE);
        shrinkageTest = findViewById(R.id.shrinkageTest);

        layoutSieveAnalysisTest = findViewById(R.id.layoutSieveAnalysisTest);
        layoutSieveAnalysisTest.setVisibility(View.GONE);
        sieveAnalysisTest = findViewById(R.id.sieveAnalysisTest);

        layoutSlumpTest = findViewById(R.id.layoutSlumpTest);
        layoutSlumpTest.setVisibility(View.GONE);
        slumpTest = findViewById(R.id.slumpTest);

        layoutSoilClassificationTest = findViewById(R.id.layoutSoilClassificationTest);
        layoutSoilClassificationTest.setVisibility(View.GONE);
        soilClassificationTest = findViewById(R.id.soilClassificationTest);

        layoutSoilPermeabilityTest = findViewById(R.id.layoutSoilPermeabilityTest);
        layoutSoilPermeabilityTest.setVisibility(View.GONE);
        soilPermeabilityTest = findViewById(R.id.soilPermeabilityTest);

        layoutSoundInsulationTest = findViewById(R.id.layoutSoundInsulationTest);
        layoutSoundInsulationTest.setVisibility(View.GONE);
        soundInsulationTest = findViewById(R.id.soundInsulationTest);

        layoutSpecificGravityTest = findViewById(R.id.layoutSpecificGravityTest);
        layoutSpecificGravityTest.setVisibility(View.GONE);
        specificGravityTest = findViewById(R.id.specificGravityTest);

        layoutSplitTensileStrengthTest = findViewById(R.id.layoutSplitTensileStrengthTest);
        layoutSplitTensileStrengthTest.setVisibility(View.GONE);
        splitTensileStrengthTest = findViewById(R.id.splitTensileStrengthTest);

        layoutTensileStrengthTest = findViewById(R.id.layoutTensileStrengthTest);
        layoutTensileStrengthTest.setVisibility(View.GONE);
        tensileStrengthTest = findViewById(R.id.tensileStrengthTest);

        layoutThermalConductivityTest = findViewById(R.id.layoutThermalConductivityTest);
        layoutThermalConductivityTest.setVisibility(View.GONE);
        thermalConductivityTest = findViewById(R.id.thermalConductivityTest);

        layoutTriaxialShearTest = findViewById(R.id.layoutTriaxialShearTest);
        layoutTriaxialShearTest.setVisibility(View.GONE);
        triaxialShearTest = findViewById(R.id.triaxialShearTest);

        layoutViscosityTest = findViewById(R.id.layoutViscosityTest);
        layoutViscosityTest.setVisibility(View.GONE);
        viscosityTest = findViewById(R.id.viscosityTest);

        layoutWaterAbsorptionTest = findViewById(R.id.layoutWaterAbsorptionTest);
        layoutWaterAbsorptionTest.setVisibility(View.GONE);
        waterAbsorptionTest = findViewById(R.id.waterAbsorptionTest);

        layoutWaterPermeabilityTest = findViewById(R.id.layoutWaterPermeabilityTest);
        layoutWaterPermeabilityTest.setVisibility(View.GONE);
        waterPermeabilityTest = findViewById(R.id.waterPermeabilityTest);

        down = findViewById(R.id.down);
        centerID = getIntent().getStringExtra("centerID");

        if (centerID != null) {
            // If centerID is not null, proceed with programming the details
            DatabaseReference centerRef = FirebaseDatabase.getInstance().getReference().child("Testing Centers").child(centerID);
            centerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String centername, centerlocation, centerdescription, centerlink, centeremail, centerimage,
                                tagAggregateCrushingValueTest,
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
                        // Retrieve details from snapshot
                        tagAggregateCrushingValueTest = snapshot.child("tagAggregateCrushingValueTest").getValue(String.class);
                        tagAirContentTest = snapshot.child("tagAirContentTest").getValue(String.class);
                        tagAsphaltDensityTest = snapshot.child("tagAsphaltDensityTest").getValue(String.class);
                        tagAtterbergLimitsTest = snapshot.child("tagAtterbergLimitsTest").getValue(String.class);
                        tagBendTest = snapshot.child("tagBendTest").getValue(String.class);
                        tagBendingStrengthTest = snapshot.child("tagBendingStrengthTest").getValue(String.class);
                        tagBitumenContentTest = snapshot.child("tagBitumenContentTest").getValue(String.class);
                        tagCaliforniaBearingRatioCBRTest = snapshot.child("tagCaliforniaBearingRatioCBRTest").getValue(String.class);
                        tagChemicalCompositionTest = snapshot.child("tagChemicalCompositionTest").getValue(String.class);
                        tagCompressionStrengthTest = snapshot.child("tagCompressionStrengthTest").getValue(String.class);
                        tagCompressiveStrengthTest = snapshot.child("tagCompressiveStrengthTest").getValue(String.class);
                        tagConsolidationTest = snapshot.child("tagConsolidationTest").getValue(String.class);
                        tagDimensionalToleranceTest = snapshot.child("tagDimensionalToleranceTest").getValue(String.class);
                        tagDuctilityTest = snapshot.child("tagDuctilityTest").getValue(String.class);
                        tagDurabilityTest = snapshot.child("tagDurabilityTest").getValue(String.class);
                        tagEfflorescenceTest = snapshot.child("tagEfflorescenceTest").getValue(String.class);
                        tagFireResistanceTest = snapshot.child("tagFireResistanceTest").getValue(String.class);
                        tagFlakinessAndElongationIndexTest = snapshot.child("tagFlakinessAndElongationIndexTest").getValue(String.class);
                        tagFlexuralStrengthTest = snapshot.child("tagFlexuralStrengthTest").getValue(String.class);
                        tagHardnessTest = snapshot.child("tagHardnessTest").getValue(String.class);
                        tagImpactTest = snapshot.child("tagImpactTest").getValue(String.class);
                        tagMarshallStabilityTest = snapshot.child("tagMarshallStabilityTest").getValue(String.class);
                        tagMoistureContentTest = snapshot.child("tagMoistureContentTest").getValue(String.class);
                        tagPenetrationTest = snapshot.child("tagPenetrationTest").getValue(String.class);
                        tagProctorCompactionTest = snapshot.child("tagProctorCompactionTest").getValue(String.class);
                        tagRapidChloridePenetrationTestRCPT = snapshot.child("tagRapidChloridePenetrationTestRCPT").getValue(String.class);
                        tagShearStrengthTest = snapshot.child("tagShearStrengthTest").getValue(String.class);
                        tagShrinkageTest = snapshot.child("tagShrinkageTest").getValue(String.class);
                        tagSieveAnalysisTest = snapshot.child("tagSieveAnalysisTest").getValue(String.class);
                        tagSlumpTest = snapshot.child("tagSlumpTest").getValue(String.class);
                        tagSoilClassificationTest = snapshot.child("tagSoilClassificationTest").getValue(String.class);
                        tagSoilPermeabilityTest = snapshot.child("tagSoilPermeabilityTest").getValue(String.class);
                        tagSoundInsulationTest = snapshot.child("tagSoundInsulationTest").getValue(String.class);
                        tagSpecificGravityTest = snapshot.child("tagSpecificGravityTest").getValue(String.class);
                        tagSplitTensileStrengthTest = snapshot.child("tagSplitTensileStrengthTest").getValue(String.class);
                        tagTensileStrengthTest = snapshot.child("tagTensileStrengthTest").getValue(String.class);
                        tagThermalConductivityTest = snapshot.child("tagThermalConductivityTest").getValue(String.class);
                        tagTriaxialShearTest = snapshot.child("tagTriaxialShearTest").getValue(String.class);
                        tagViscosityTest = snapshot.child("tagViscosityTest").getValue(String.class);
                        tagWaterAbsorptionTest = snapshot.child("tagWaterAbsorptionTest").getValue(String.class);
                        tagWaterPermeabilityTest = snapshot.child("tagWaterPermeabilityTest").getValue(String.class);

                        // Pattern for tagAggregateCrushingValueTest
                        if (tagAggregateCrushingValueTest == null || tagAggregateCrushingValueTest.equals("")) {
                            layoutAggregateCrushingValueTest.setVisibility(View.GONE);
                        } else {
                            layoutAggregateCrushingValueTest.setVisibility(View.VISIBLE);
                            aggregateCrushingValueTest.setText(tagAggregateCrushingValueTest);
                        }

// Pattern for tagAirContentTest
                        if (tagAirContentTest == null || tagAirContentTest.equals("")) {
                            layoutAirContentTest.setVisibility(View.GONE);
                        } else {
                            layoutAirContentTest.setVisibility(View.VISIBLE);
                            airContentTest.setText(tagAirContentTest);
                        }

// Pattern for tagAsphaltDensityTest
                        if (tagAsphaltDensityTest == null || tagAsphaltDensityTest.equals("")) {
                            layoutAsphaltDensityTest.setVisibility(View.GONE);
                        } else {
                            layoutAsphaltDensityTest.setVisibility(View.VISIBLE);
                            asphaltDensityTest.setText(tagAsphaltDensityTest);
                        }

                        // Pattern for tagAtterbergLimitsTest
                        if (tagAtterbergLimitsTest == null || tagAtterbergLimitsTest.equals("")) {
                            layoutAtterbergLimitsTest.setVisibility(View.GONE);
                        } else {
                            layoutAtterbergLimitsTest.setVisibility(View.VISIBLE);
                            atterbergLimitsTest.setText(tagAtterbergLimitsTest);
                        }

// Pattern for tagBendTest
                        if (tagBendTest == null || tagBendTest.equals("")) {
                            layoutBendTest.setVisibility(View.GONE);
                        } else {
                            layoutBendTest.setVisibility(View.VISIBLE);
                            bendTest.setText(tagBendTest);
                        }

// Pattern for tagBendingStrengthTest
                        if (tagBendingStrengthTest == null || tagBendingStrengthTest.equals("")) {
                            layoutBendingStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutBendingStrengthTest.setVisibility(View.VISIBLE);
                            bendingStrengthTest.setText(tagBendingStrengthTest);
                        }

// Pattern for tagBitumenContentTest
                        if (tagBitumenContentTest == null || tagBitumenContentTest.equals("")) {
                            layoutBitumenContentTest.setVisibility(View.GONE);
                        } else {
                            layoutBitumenContentTest.setVisibility(View.VISIBLE);
                            bitumenContentTest.setText(tagBitumenContentTest);
                        }

// Pattern for tagCaliforniaBearingRatioCBRTest
                        if (tagCaliforniaBearingRatioCBRTest == null || tagCaliforniaBearingRatioCBRTest.equals("")) {
                            layoutCaliforniaBearingRatioCBRTest.setVisibility(View.GONE);
                        } else {
                            layoutCaliforniaBearingRatioCBRTest.setVisibility(View.VISIBLE);
                            californiaBearingRatioCBRTest.setText(tagCaliforniaBearingRatioCBRTest);
                        }

// Pattern for tagChemicalCompositionTest
                        if (tagChemicalCompositionTest == null || tagChemicalCompositionTest.equals("")) {
                            layoutChemicalCompositionTest.setVisibility(View.GONE);
                        } else {
                            layoutChemicalCompositionTest.setVisibility(View.VISIBLE);
                            chemicalCompositionTest.setText(tagChemicalCompositionTest);
                        }

// Pattern for tagCompressionStrengthTest
                        if (tagCompressionStrengthTest == null || tagCompressionStrengthTest.equals("")) {
                            layoutCompressionStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutCompressionStrengthTest.setVisibility(View.VISIBLE);
                            compressionStrengthTest.setText(tagCompressionStrengthTest);
                        }

// Pattern for tagCompressiveStrengthTest
                        if (tagCompressiveStrengthTest == null || tagCompressiveStrengthTest.equals("")) {
                            layoutCompressiveStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutCompressiveStrengthTest.setVisibility(View.VISIBLE);
                            compressiveStrengthTest.setText(tagCompressiveStrengthTest);
                        }

// Pattern for tagConsolidationTest
                        if (tagConsolidationTest == null || tagConsolidationTest.equals("")) {
                            layoutConsolidationTest.setVisibility(View.GONE);
                        } else {
                            layoutConsolidationTest.setVisibility(View.VISIBLE);
                            consolidationTest.setText(tagConsolidationTest);
                        }

// Pattern for tagDimensionalToleranceTest
                        if (tagDimensionalToleranceTest == null || tagDimensionalToleranceTest.equals("")) {
                            layoutDimensionalToleranceTest.setVisibility(View.GONE);
                        } else {
                            layoutDimensionalToleranceTest.setVisibility(View.VISIBLE);
                            dimensionalToleranceTest.setText(tagDimensionalToleranceTest);
                        }

// Pattern for tagDuctilityTest
                        if (tagDuctilityTest == null || tagDuctilityTest.equals("")) {
                            layoutDuctilityTest.setVisibility(View.GONE);
                        } else {
                            layoutDuctilityTest.setVisibility(View.VISIBLE);
                            ductilityTest.setText(tagDuctilityTest);
                        }

// Pattern for tagDurabilityTest
                        if (tagDurabilityTest == null || tagDurabilityTest.equals("")) {
                            layoutDurabilityTest.setVisibility(View.GONE);
                        } else {
                            layoutDurabilityTest.setVisibility(View.VISIBLE);
                            durabilityTest.setText(tagDurabilityTest);
                        }

// Pattern for tagEfflorescenceTest
                        if (tagEfflorescenceTest == null || tagEfflorescenceTest.equals("")) {
                            layoutEfflorescenceTest.setVisibility(View.GONE);
                        } else {
                            layoutEfflorescenceTest.setVisibility(View.VISIBLE);
                            efflorescenceTest.setText(tagEfflorescenceTest);
                        }

// Pattern for tagFireResistanceTest
                        if (tagFireResistanceTest == null || tagFireResistanceTest.equals("")) {
                            layoutFireResistanceTest.setVisibility(View.GONE);
                        } else {
                            layoutFireResistanceTest.setVisibility(View.VISIBLE);
                            fireResistanceTest.setText(tagFireResistanceTest);
                        }

// Pattern for tagFlakinessAndElongationIndexTest
                        if (tagFlakinessAndElongationIndexTest == null || tagFlakinessAndElongationIndexTest.equals("")) {
                            layoutFlakinessAndElongationIndexTest.setVisibility(View.GONE);
                        } else {
                            layoutFlakinessAndElongationIndexTest.setVisibility(View.VISIBLE);
                            flakinessAndElongationIndexTest.setText(tagFlakinessAndElongationIndexTest);
                        }

// Pattern for tagFlexuralStrengthTest
                        if (tagFlexuralStrengthTest == null || tagFlexuralStrengthTest.equals("")) {
                            layoutFlexuralStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutFlexuralStrengthTest.setVisibility(View.VISIBLE);
                            flexuralStrengthTest.setText(tagFlexuralStrengthTest);
                        }

// Pattern for tagHardnessTest
                        if (tagHardnessTest == null || tagHardnessTest.equals("")) {
                            layoutHardnessTest.setVisibility(View.GONE);
                        } else {
                            layoutHardnessTest.setVisibility(View.VISIBLE);
                            hardnessTest.setText(tagHardnessTest);
                        }

// Pattern for tagImpactTest
                        if (tagImpactTest == null || tagImpactTest.equals("")) {
                            layoutImpactTest.setVisibility(View.GONE);
                        } else {
                            layoutImpactTest.setVisibility(View.VISIBLE);
                            impactTest.setText(tagImpactTest);
                        }

// Pattern for tagMarshallStabilityTest
                        if (tagMarshallStabilityTest == null || tagMarshallStabilityTest.equals("")) {
                            layoutMarshallStabilityTest.setVisibility(View.GONE);
                        } else {
                            layoutMarshallStabilityTest.setVisibility(View.VISIBLE);
                            marshallStabilityTest.setText(tagMarshallStabilityTest);
                        }

// Pattern for tagMoistureContentTest
                        if (tagMoistureContentTest == null || tagMoistureContentTest.equals("")) {
                            layoutMoistureContentTest.setVisibility(View.GONE);
                        } else {
                            layoutMoistureContentTest.setVisibility(View.VISIBLE);
                            moistureContentTest.setText(tagMoistureContentTest);
                        }

// Pattern for tagPenetrationTest
                        if (tagPenetrationTest == null || tagPenetrationTest.equals("")) {
                            layoutPenetrationTest.setVisibility(View.GONE);
                        } else {
                            layoutPenetrationTest.setVisibility(View.VISIBLE);
                            penetrationTest.setText(tagPenetrationTest);
                        }

// Pattern for tagProctorCompactionTest
                        if (tagProctorCompactionTest == null || tagProctorCompactionTest.equals("")) {
                            layoutProctorCompactionTest.setVisibility(View.GONE);
                        } else {
                            layoutProctorCompactionTest.setVisibility(View.VISIBLE);
                            proctorCompactionTest.setText(tagProctorCompactionTest);
                        }

// Pattern for tagRapidChloridePenetrationTestRCPT
                        if (tagRapidChloridePenetrationTestRCPT == null || tagRapidChloridePenetrationTestRCPT.equals("")) {
                            layoutRapidChloridePenetrationTestRCPT.setVisibility(View.GONE);
                        } else {
                            layoutRapidChloridePenetrationTestRCPT.setVisibility(View.VISIBLE);
                            rapidChloridePenetrationTestRCPT.setText(tagRapidChloridePenetrationTestRCPT);
                        }

// Pattern for tagShearStrengthTest
                        if (tagShearStrengthTest == null || tagShearStrengthTest.equals("")) {
                            layoutShearStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutShearStrengthTest.setVisibility(View.VISIBLE);
                            shearStrengthTest.setText(tagShearStrengthTest);
                        }

// Pattern for tagShrinkageTest
                        if (tagShrinkageTest == null || tagShrinkageTest.equals("")) {
                            layoutShrinkageTest.setVisibility(View.GONE);
                        } else {
                            layoutShrinkageTest.setVisibility(View.VISIBLE);
                            shrinkageTest.setText(tagShrinkageTest);
                        }

// Pattern for tagSieveAnalysisTest
                        if (tagSieveAnalysisTest == null || tagSieveAnalysisTest.equals("")) {
                            layoutSieveAnalysisTest.setVisibility(View.GONE);
                        } else {
                            layoutSieveAnalysisTest.setVisibility(View.VISIBLE);
                            sieveAnalysisTest.setText(tagSieveAnalysisTest);
                        }

// Pattern for tagSlumpTest
                        if (tagSlumpTest == null || tagSlumpTest.equals("")) {
                            layoutSlumpTest.setVisibility(View.GONE);
                        } else {
                            layoutSlumpTest.setVisibility(View.VISIBLE);
                            slumpTest.setText(tagSlumpTest);
                        }

// Pattern for tagSoilClassificationTest
                        if (tagSoilClassificationTest == null || tagSoilClassificationTest.equals("")) {
                            layoutSoilClassificationTest.setVisibility(View.GONE);
                        } else {
                            layoutSoilClassificationTest.setVisibility(View.VISIBLE);
                            soilClassificationTest.setText(tagSoilClassificationTest);
                        }

// Pattern for tagSoilPermeabilityTest
                        if (tagSoilPermeabilityTest == null || tagSoilPermeabilityTest.equals("")) {
                            layoutSoilPermeabilityTest.setVisibility(View.GONE);
                        } else {
                            layoutSoilPermeabilityTest.setVisibility(View.VISIBLE);
                            soilPermeabilityTest.setText(tagSoilPermeabilityTest);
                        }

// Pattern for tagSoundInsulationTest
                        if (tagSoundInsulationTest == null || tagSoundInsulationTest.equals("")) {
                            layoutSoundInsulationTest.setVisibility(View.GONE);
                        } else {
                            layoutSoundInsulationTest.setVisibility(View.VISIBLE);
                            soundInsulationTest.setText(tagSoundInsulationTest);
                        }

// Pattern for tagSpecificGravityTest
                        if (tagSpecificGravityTest == null || tagSpecificGravityTest.equals("")) {
                            layoutSpecificGravityTest.setVisibility(View.GONE);
                        } else {
                            layoutSpecificGravityTest.setVisibility(View.VISIBLE);
                            specificGravityTest.setText(tagSpecificGravityTest);
                        }

// Pattern for tagSplitTensileStrengthTest
                        if (tagSplitTensileStrengthTest == null || tagSplitTensileStrengthTest.equals("")) {
                            layoutSplitTensileStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutSplitTensileStrengthTest.setVisibility(View.VISIBLE);
                            splitTensileStrengthTest.setText(tagSplitTensileStrengthTest);
                        }

// Pattern for tagTensileStrengthTest
                        if (tagTensileStrengthTest == null || tagTensileStrengthTest.equals("")) {
                            layoutTensileStrengthTest.setVisibility(View.GONE);
                        } else {
                            layoutTensileStrengthTest.setVisibility(View.VISIBLE);
                            tensileStrengthTest.setText(tagTensileStrengthTest);
                        }

// Pattern for tagThermalConductivityTest
                        if (tagThermalConductivityTest == null || tagThermalConductivityTest.equals("")) {
                            layoutThermalConductivityTest.setVisibility(View.GONE);
                        } else {
                            layoutThermalConductivityTest.setVisibility(View.VISIBLE);
                            thermalConductivityTest.setText(tagThermalConductivityTest);
                        }

// Pattern for tagTriaxialShearTest
                        if (tagTriaxialShearTest == null || tagTriaxialShearTest.equals("")) {
                            layoutTriaxialShearTest.setVisibility(View.GONE);
                        } else {
                            layoutTriaxialShearTest.setVisibility(View.VISIBLE);
                            triaxialShearTest.setText(tagTriaxialShearTest);
                        }

// Pattern for tagViscosityTest
                        if (tagViscosityTest == null || tagViscosityTest.equals("")) {
                            layoutViscosityTest.setVisibility(View.GONE);
                        } else {
                            layoutViscosityTest.setVisibility(View.VISIBLE);
                            viscosityTest.setText(tagViscosityTest);
                        }

// Pattern for tagWaterAbsorptionTest
                        if (tagWaterAbsorptionTest == null || tagWaterAbsorptionTest.equals("")) {
                            layoutWaterAbsorptionTest.setVisibility(View.GONE);
                        } else {
                            layoutWaterAbsorptionTest.setVisibility(View.VISIBLE);
                            waterAbsorptionTest.setText(tagWaterAbsorptionTest);
                        }

// Pattern for tagWaterPermeabilityTest
                        if (tagWaterPermeabilityTest == null || tagWaterPermeabilityTest.equals("")) {
                            layoutWaterPermeabilityTest.setVisibility(View.GONE);
                        } else {
                            layoutWaterPermeabilityTest.setVisibility(View.VISIBLE);
                            waterPermeabilityTest.setText(tagWaterPermeabilityTest);
                        }
                        centeremail = snapshot.child("center_email").getValue(String.class);
                        centername = snapshot.child("center_name").getValue(String.class);
                        centerlocation = snapshot.child("center_address").getValue(String.class);
                        centerdescription = snapshot.child("center_description").getValue(String.class);
                        centerlink = snapshot.child("center_link").getValue(String.class);
                        centerimage = snapshot.child("center_image").getValue(String.class);

                        // Set the retrieved details to the corresponding views
                        tcname.setText(centername);
                        tcemail.setText(centeremail);
                        tcaddress.setText(centerlocation);
                        tclink.setText(centerlink);
                        tcdescription.setText(centerdescription);
                        if (centerID != null) {
                            // Use the stored current image URI if available
                            if (currentImageUri != null) {
                                // Load the stored current image URI into tcdisplaypic using Picasso
                                Picasso.get().load(currentImageUri).into(tcdisplaypic);
                                // Set the URI for further processing if needed
                                uri = currentImageUri;
                            }
                        } else {
                            // If centerID is null
                            // Ensure that centerimage is not null before attempting to load
                            if (centerimage != null) {
                                // Load the image using Picasso and set the URI
                                Picasso.get().load(centerimage).into(tcdisplaypic);
                                // Parse centerimage into a URI and assign it to uri
                                uri = Uri.parse(centerimage);
                            }
                        }
                        // Continue setting other details as needed

                    } else {
                        // Handle the case when the snapshot does not exist
                        // This might occur if the centerID does not match any center in the database
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors that may occur during data retrieval
                }
            });
        } else {
            // If centerID is null, continue the activity as is
            // You can add any additional logic here if needed
        }



        confirmaddcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String centername, centerlocation, centerdescription, centerlink, centeremail,
                        tagAggregateCrushingValueTest,
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

                centeremail = tcemail.getText().toString().trim();
                centername = tcname.getText().toString().trim();
                centerlocation = tcaddress.getText().toString().trim();
                centerdescription = tcdescription.getText().toString().trim();
                centerlink = tclink.getText().toString().trim();
                tagAggregateCrushingValueTest = aggregateCrushingValueTest.getText().toString().trim();
                tagAirContentTest = airContentTest.getText().toString().trim();
                tagAsphaltDensityTest = asphaltDensityTest.getText().toString().trim();
                tagAtterbergLimitsTest = atterbergLimitsTest.getText().toString().trim();
                tagBendTest = bendTest.getText().toString().trim();
                tagBendingStrengthTest = bendingStrengthTest.getText().toString().trim();
                tagBitumenContentTest = bitumenContentTest.getText().toString().trim();
                tagCaliforniaBearingRatioCBRTest = californiaBearingRatioCBRTest.getText().toString().trim();
                tagChemicalCompositionTest = chemicalCompositionTest.getText().toString().trim();
                tagCompressionStrengthTest = compressionStrengthTest.getText().toString().trim();
                tagCompressiveStrengthTest = compressiveStrengthTest.getText().toString().trim();
                tagConsolidationTest = consolidationTest.getText().toString().trim();
                tagDimensionalToleranceTest = dimensionalToleranceTest.getText().toString().trim();
                tagDuctilityTest = ductilityTest.getText().toString().trim();
                tagDurabilityTest = durabilityTest.getText().toString().trim();
                tagEfflorescenceTest = efflorescenceTest.getText().toString().trim();
                tagFireResistanceTest = fireResistanceTest.getText().toString().trim();
                tagFlakinessAndElongationIndexTest = flakinessAndElongationIndexTest.getText().toString().trim();
                tagFlexuralStrengthTest = flexuralStrengthTest.getText().toString().trim();
                tagHardnessTest = hardnessTest.getText().toString().trim();
                tagImpactTest = impactTest.getText().toString().trim();
                tagMarshallStabilityTest = marshallStabilityTest.getText().toString().trim();
                tagMoistureContentTest = moistureContentTest.getText().toString().trim();
                tagPenetrationTest = penetrationTest.getText().toString().trim();
                tagProctorCompactionTest = proctorCompactionTest.getText().toString().trim();
                tagRapidChloridePenetrationTestRCPT = rapidChloridePenetrationTestRCPT.getText().toString().trim();
                tagShearStrengthTest = shearStrengthTest.getText().toString().trim();
                tagShrinkageTest = shrinkageTest.getText().toString().trim();
                tagSieveAnalysisTest = sieveAnalysisTest.getText().toString().trim();
                tagSlumpTest = slumpTest.getText().toString().trim();
                tagSoilClassificationTest = soilClassificationTest.getText().toString().trim();
                tagSoilPermeabilityTest = soilPermeabilityTest.getText().toString().trim();
                tagSoundInsulationTest = soundInsulationTest.getText().toString().trim();
                tagSpecificGravityTest = specificGravityTest.getText().toString().trim();
                tagSplitTensileStrengthTest = splitTensileStrengthTest.getText().toString().trim();
                tagTensileStrengthTest = tensileStrengthTest.getText().toString().trim();
                tagThermalConductivityTest = thermalConductivityTest.getText().toString().trim();
                tagTriaxialShearTest = triaxialShearTest.getText().toString().trim();
                tagViscosityTest = viscosityTest.getText().toString().trim();
                tagWaterAbsorptionTest = waterAbsorptionTest.getText().toString().trim();
                tagWaterPermeabilityTest = waterPermeabilityTest.getText().toString().trim();

                if (TextUtils.isEmpty(centername) || TextUtils.isEmpty(centerlocation) || TextUtils.isEmpty(centerdescription)|| TextUtils.isEmpty(centerlink)|| TextUtils.isEmpty(centeremail)){
                    Toast.makeText(AdminAddCenter.this, "Testing Center Details Missing", Toast.LENGTH_SHORT).show();
                } else if (uri==null) {
                    Toast.makeText(AdminAddCenter.this, "Testing Center Image Missing", Toast.LENGTH_SHORT).show();
                } else{
                    progressDialogExtra();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    storageReference = FirebaseStorage.getInstance().getReference().child("Testing Center Pictures");
                    storageReference.child(centername).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.child(centername).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ArrayList<String> tags = new ArrayList<>();
                                        String centerUID = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        HashMap<String, Object> v1 = new HashMap<>();
                                        v1.put("center_name", centername);
                                        v1.put("center_address", centerlocation);
                                        v1.put("center_description", centerdescription);
                                        v1.put("center_link", centerlink);
                                        v1.put("center_image", uri.toString());
                                        if (centerID != null && !centerID.isEmpty()) {
                                            v1.put("center_uid", centerID);
                                        } else {
                                            v1.put("center_uid", centerUID);
                                        }
                                        v1.put("center_email", centeremail);
                                        // Shortcut function for adding tags
                                        addTagIfNotEmpty(v1, tags, "tagAggregateCrushingValueTest", tagAggregateCrushingValueTest);
                                        addTagIfNotEmpty(v1, tags, "tagAirContentTest", tagAirContentTest);
                                        addTagIfNotEmpty(v1, tags, "tagAsphaltDensityTest", tagAsphaltDensityTest);
                                        addTagIfNotEmpty(v1, tags, "tagAtterbergLimitsTest", tagAtterbergLimitsTest);
                                        addTagIfNotEmpty(v1, tags, "tagBendTest", tagBendTest);
                                        addTagIfNotEmpty(v1, tags, "tagBendingStrengthTest", tagBendingStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagBitumenContentTest", tagBitumenContentTest);
                                        addTagIfNotEmpty(v1, tags, "tagCaliforniaBearingRatioCBRTest", tagCaliforniaBearingRatioCBRTest);
                                        addTagIfNotEmpty(v1, tags, "tagChemicalCompositionTest", tagChemicalCompositionTest);
                                        addTagIfNotEmpty(v1, tags, "tagCompressionStrengthTest", tagCompressionStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagCompressiveStrengthTest", tagCompressiveStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagConsolidationTest", tagConsolidationTest);
                                        addTagIfNotEmpty(v1, tags, "tagDimensionalToleranceTest", tagDimensionalToleranceTest);
                                        addTagIfNotEmpty(v1, tags, "tagDuctilityTest", tagDuctilityTest);
                                        addTagIfNotEmpty(v1, tags, "tagDurabilityTest", tagDurabilityTest);
                                        addTagIfNotEmpty(v1, tags, "tagEfflorescenceTest", tagEfflorescenceTest);
                                        addTagIfNotEmpty(v1, tags, "tagFireResistanceTest", tagFireResistanceTest);
                                        addTagIfNotEmpty(v1, tags, "tagFlakinessAndElongationIndexTest", tagFlakinessAndElongationIndexTest);
                                        addTagIfNotEmpty(v1, tags, "tagFlexuralStrengthTest", tagFlexuralStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagHardnessTest", tagHardnessTest);
                                        addTagIfNotEmpty(v1, tags, "tagImpactTest", tagImpactTest);
                                        addTagIfNotEmpty(v1, tags, "tagMarshallStabilityTest", tagMarshallStabilityTest);
                                        addTagIfNotEmpty(v1, tags, "tagMoistureContentTest", tagMoistureContentTest);
                                        addTagIfNotEmpty(v1, tags, "tagPenetrationTest", tagPenetrationTest);
                                        addTagIfNotEmpty(v1, tags, "tagProctorCompactionTest", tagProctorCompactionTest);
                                        addTagIfNotEmpty(v1, tags, "tagRapidChloridePenetrationTestRCPT", tagRapidChloridePenetrationTestRCPT);
                                        addTagIfNotEmpty(v1, tags, "tagShearStrengthTest", tagShearStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagShrinkageTest", tagShrinkageTest);
                                        addTagIfNotEmpty(v1, tags, "tagSieveAnalysisTest", tagSieveAnalysisTest);
                                        addTagIfNotEmpty(v1, tags, "tagSlumpTest", tagSlumpTest);
                                        addTagIfNotEmpty(v1, tags, "tagSoilClassificationTest", tagSoilClassificationTest);
                                        addTagIfNotEmpty(v1, tags, "tagSoilPermeabilityTest", tagSoilPermeabilityTest);
                                        addTagIfNotEmpty(v1, tags, "tagSoundInsulationTest", tagSoundInsulationTest);
                                        addTagIfNotEmpty(v1, tags, "tagSpecificGravityTest", tagSpecificGravityTest);
                                        addTagIfNotEmpty(v1, tags, "tagSplitTensileStrengthTest", tagSplitTensileStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagTensileStrengthTest", tagTensileStrengthTest);
                                        addTagIfNotEmpty(v1, tags, "tagThermalConductivityTest", tagThermalConductivityTest);
                                        addTagIfNotEmpty(v1, tags, "tagTriaxialShearTest", tagTriaxialShearTest);
                                        addTagIfNotEmpty(v1, tags, "tagViscosityTest", tagViscosityTest);
                                        addTagIfNotEmpty(v1, tags, "tagWaterAbsorptionTest", tagWaterAbsorptionTest);
                                        addTagIfNotEmpty(v1, tags, "tagWaterPermeabilityTest", tagWaterPermeabilityTest);

                                        String concatenatedTags = concatenateTags(tags);
                                        v1.put("tags", concatenatedTags);

                                        // Continue with the rest of your code
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference centerReference = database.getReference("Testing Centers");
                                        if (centerID != null) {
                                            centerReference.child(centerID).updateChildren(v1);
                                        } else {
                                            centerReference.child(centerUID).updateChildren(v1);
                                        }
                                        progressDialog.dismiss();
                                        startActivity(new Intent(AdminAddCenter.this, Admin.class));
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        tcdisplaypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            private boolean isDown = true; // Variable to track the state

            @Override
            public void onClick(View v) {
                int newVisibility;
                if (isDown) {
                    checkicon.setVisibility(View.VISIBLE);
                    down.setImageResource(R.drawable.up); // Change the image resource to up
                    // Expand the view
                    newVisibility = (searchandlist.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                } else {
                    checkicon.setVisibility(View.GONE);
                    down.setImageResource(R.drawable.down); // Change the image resource to down
                    // Collapse the view
                    newVisibility = View.GONE;
                }
                TransitionManager.beginDelayedTransition(mainlayout, new AutoTransition());
                //TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                searchandlist.setVisibility(newVisibility);
                isDown = !isDown; // Toggle the state
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AdminAddCenter.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AdminAddCenter.this.adapter.getFilter().filter(newText);
                return true;
            }
        });

        checkicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCheckIconClick();
            }
        });

        scrollView = findViewById(R.id.scrollview); // Initialize scrollView
        View view = findViewById(R.id.view);
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

        home = findViewById(R.id.home);
        addcenter = findViewById(R.id.addcenter);
        profile = findViewById(R.id.profile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddCenter.this, Admin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddCenter.this, AdminProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
                finish();

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
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

    private void progressDialogExtra() {
        progressDialog.show();
        LottieAnimationView animationView = new LottieAnimationView(AdminAddCenter.this);
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
    }

    private void handleCheckIconClick() {
        for (int i = 0; i < listView.getCount(); i++) {
            if (listView.isItemChecked(i)) {
                String selectedItem = (String) listView.getItemAtPosition(i);
                switch (selectedItem) {
                    case "Aggregate Crushing Value Test":
                        layoutAggregateCrushingValueTest.setVisibility(View.VISIBLE);
                        break;
                    case "Air Content Test":
                        layoutAirContentTest.setVisibility(View.VISIBLE);
                        break;
                    case "Asphalt Density Test":
                        layoutAsphaltDensityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Atterberg Limits Test":
                        layoutAtterbergLimitsTest.setVisibility(View.VISIBLE);
                        break;
                    case "Bend Test":
                        layoutBendTest.setVisibility(View.VISIBLE);
                        break;
                    case "Bending Strength Test":
                        layoutBendingStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Bitumen Content Test":
                        layoutBitumenContentTest.setVisibility(View.VISIBLE);
                        break;
                    case "California Bearing Ratio (CBR) Test":
                        layoutCaliforniaBearingRatioCBRTest.setVisibility(View.VISIBLE);
                        break;
                    case "Chemical Composition Test":
                        layoutChemicalCompositionTest.setVisibility(View.VISIBLE);
                        break;
                    case "Compression Strength Test":
                        layoutCompressionStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Compressive Strength Test":
                        layoutCompressiveStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Consolidation Test":
                        layoutConsolidationTest.setVisibility(View.VISIBLE);
                        break;
                    case "Dimensional Tolerance Test":
                        layoutDimensionalToleranceTest.setVisibility(View.VISIBLE);
                        break;
                    case "Ductility Test":
                        layoutDuctilityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Durability Test":
                        layoutDurabilityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Efflorescence Test":
                        layoutEfflorescenceTest.setVisibility(View.VISIBLE);
                        break;
                    case "Fire Resistance Test":
                        layoutFireResistanceTest.setVisibility(View.VISIBLE);
                        break;
                    case "Flakiness And Elongation Index Test":
                        layoutFlakinessAndElongationIndexTest.setVisibility(View.VISIBLE);
                        break;
                    case "Flexural Strength Test":
                        layoutFlexuralStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Hardness Test":
                        layoutHardnessTest.setVisibility(View.VISIBLE);
                        break;
                    case "Impact Test":
                        layoutImpactTest.setVisibility(View.VISIBLE);
                        break;
                    case "Marshall Stability Test":
                        layoutMarshallStabilityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Moisture Content Test":
                        layoutMoistureContentTest.setVisibility(View.VISIBLE);
                        break;
                    case "Penetration Test":
                        layoutPenetrationTest.setVisibility(View.VISIBLE);
                        break;
                    case "Porosity Test":
                        layoutPorosityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Proctor Compaction Test":
                        layoutProctorCompactionTest.setVisibility(View.VISIBLE);
                        break;
                    case "Rapid Chloride Penetration Test (RCPT)":
                        layoutRapidChloridePenetrationTestRCPT.setVisibility(View.VISIBLE);
                        break;
                    case "Shear Strength Test":
                        layoutShearStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Shrinkage Test":
                        layoutShrinkageTest.setVisibility(View.VISIBLE);
                        break;
                    case "Sieve Analysis Test":
                        layoutSieveAnalysisTest.setVisibility(View.VISIBLE);
                        break;
                    case "Slump Test":
                        layoutSlumpTest.setVisibility(View.VISIBLE);
                        break;
                    case "Soil Classification Test":
                        layoutSoilClassificationTest.setVisibility(View.VISIBLE);
                        break;
                    case "Soil Permeability Test":
                        layoutSoilPermeabilityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Sound Insulation Test":
                        layoutSoundInsulationTest.setVisibility(View.VISIBLE);
                        break;
                    case "Specific Gravity Test":
                        layoutSpecificGravityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Split Tensile Strength Test":
                        layoutSplitTensileStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Tensile Strength Test":
                        layoutTensileStrengthTest.setVisibility(View.VISIBLE);
                        break;
                    case "Thermal Conductivity Test":
                        layoutThermalConductivityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Triaxial Shear Test":
                        layoutTriaxialShearTest.setVisibility(View.VISIBLE);
                        break;
                    case "Viscosity Test":
                        layoutViscosityTest.setVisibility(View.VISIBLE);
                        break;
                    case "Water Absorption Test":
                        layoutWaterAbsorptionTest.setVisibility(View.VISIBLE);
                        break;
                    case "Water Permeability Test":
                        layoutWaterPermeabilityTest.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        listView.clearChoices();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Check if the user has selected a new image
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // User has selected a new image
                uri = selectedImageUri;
                tcdisplaypic.setImageURI(uri);
                // Update the currentImageUri to the newly selected image URI
                currentImageUri = uri;
            } else if (currentImageUri != null) {
                // User hasn't selected a new image, use the stored current image URI
                // Load the stored current image URI into tcdisplaypic using Picasso
                Picasso.get().load(currentImageUri).into(tcdisplaypic);
                // Set the URI to the stored current image URI
                uri = currentImageUri;
                tcdisplaypic.setImageURI(uri);
            }
        }
    }
    private void addTagIfNotEmpty(HashMap<String, Object> v1, ArrayList<String> tags, String tagName, String tagValue) {
        if (tagValue == null || tagValue.isEmpty()) {
            v1.put(tagName, "");
        } else {
            v1.put(tagName, tagValue);
            tags.add(tagName);
        }
    }

    public String concatenateTags(ArrayList<String> tags) {
        StringBuilder concatenatedTags = new StringBuilder();
        for (String tag : tags) {
            concatenatedTags.append(tag);
        }
        return concatenatedTags.toString();
    }

    @Override
    public void onBackPressed(){}
}