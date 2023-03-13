package com.example.saudiexpertsecondversion.TourGuide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.Object.Tourist;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTgShowTouristCartInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TG_ShowTouristCartInfo extends AppCompatActivity {
    ActivityTgShowTouristCartInfoBinding binding;
    String touristId;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourist=database.getReference("ALLTourist");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTgShowTouristCartInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(view -> {onBackPressed();});
        touristId=getIntent().getStringExtra("touristInfo");

        uploadTouristInfo(touristId);

    }
    public void uploadTouristInfo(String touristId){
        referenceAllTourist.child(touristId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tourist tourist=snapshot.getValue(Tourist.class);
                binding.firstNameT.setText(tourist.getFirstName());
                binding.lastNameT.setText(tourist.getLastName());
                binding.email.setText(tourist.getEmail());
                binding.phoneNumberT.setText(tourist.getPhone());
                binding.gender.setText(tourist.getGender());
                binding.birthDateT.setText(tourist.getBirthDate());
                if(tourist.getGender().equals("Female")){
                    binding.gender.setTextColor(getResources().getColor(R.color.pink));
                }else{
                    binding.gender.setTextColor(getResources().getColor(R.color.deepBlue));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}