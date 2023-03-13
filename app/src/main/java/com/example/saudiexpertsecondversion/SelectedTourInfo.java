package com.example.saudiexpertsecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.databinding.ActivitySelectedTourInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SelectedTourInfo extends AppCompatActivity {
    ActivitySelectedTourInfoBinding binding;
    String tourID;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour =database.getReference("AllTour");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectedTourInfoBinding.inflate(getLayoutInflater());
        binding.backButton5.setOnClickListener(view -> {onBackPressed();});
        tourID=getIntent().getStringExtra("tourInfo");
        uploadTourInfo(tourID);



        setContentView(binding.getRoot());
    }
    public void uploadTourInfo(String tourID){
        referenceAllTour.child(tourID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tour tour=snapshot.getValue(Tour.class);
                Picasso.get().load(tour.getTourImageUrl()).fit().into(binding.tourItemImage);
                binding.tourItemCity.setText("City: "+tour.getTourCity());
                binding.tourItemDescription.setText(tour.getTourDescription());
                binding.tourItemTitle.setText(tour.getTourTitle());
                binding.tourItemPrice.setText("Price: "+tour.getTourPrice());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}