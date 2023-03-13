package com.example.saudiexpertsecondversion.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.databinding.ActivityTShowTourDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class T_ShowTourDetails extends AppCompatActivity {
    ActivityTShowTourDetailsBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referencesAllTourGuide=database.getReference("AllTourGuide");
    Tour tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTShowTourDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tour= (Tour) getIntent().getSerializableExtra("selectedTour");
        loadTourDetails();
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.bookTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(T_ShowTourDetails.this,T_MakeAppointment.class);
                intent.putExtra("selectedTour",tour);
                startActivity(intent);

            }
        });

    }
    public void loadTourDetails(){
        binding.title.setText(tour.getTourTitle());
        binding.tourItemCity.setText("City: "+tour.getTourCity());
        binding.tourItemPrice.setText("Price for hour: "+tour.getTourPrice());
        binding.tourItemDescription.setText(tour.getTourDescription());
        Picasso.get().load(tour.getTourImageUrl()).fit().into(binding.tourItemImage);
        referencesAllTourGuide.child(tour.getTourGuideKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TourGuide tourGuide=snapshot.getValue(TourGuide.class);
                String firstName=String.valueOf(tourGuide.getFirstName());
                String lastName=String.valueOf(tourGuide.getLastName());
                binding.tourGuideName.setText(firstName+""+lastName);
                binding.tourGuideDescription.setText(tourGuide.getBriefDescription());
                Picasso.get().load(tourGuide.getImageUrl()).fit().into(binding.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}