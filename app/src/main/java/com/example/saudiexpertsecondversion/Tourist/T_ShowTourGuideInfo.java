package com.example.saudiexpertsecondversion.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.databinding.ActivityTShowTourGuideInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class T_ShowTourGuideInfo extends AppCompatActivity {
    ActivityTShowTourGuideInfoBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourGuide =database.getReference("AllTourGuide");
    String tourGuideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTShowTourGuideInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton3.setOnClickListener(view -> {onBackPressed();});
        tourGuideId=getIntent().getStringExtra("TourGuideId");
        loadTourGuideInfo(tourGuideId);

    }
    public void loadTourGuideInfo(String tourGuideId){
        referenceAllTourGuide.child(tourGuideId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TourGuide tourGuide=snapshot.getValue(TourGuide.class);
                Picasso.get().load(tourGuide.getImageUrl()).fit().into(binding.profileImage);
                binding.firstNameT5.setText(tourGuide.getFirstName());
                binding.lastNameT3.setText(tourGuide.getLastName());
                binding.birthDate.setText(tourGuide.getBirthDate());
                binding.speakingLanguage.setText(tourGuide.getSpeakingLanguages());
                binding.tourGuideDescription.setText(tourGuide.getBriefDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}