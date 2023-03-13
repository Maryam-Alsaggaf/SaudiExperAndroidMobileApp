package com.example.saudiexpertsecondversion.Guest;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.Main_Registertion;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityGShowTourDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class G_ShowTourDetails extends AppCompatActivity {
    ActivityGShowTourDetailsBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referencesAllTourGuide=database.getReference("AllTourGuide");
    Dialog dialog;
    Tour tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGShowTourDetailsBinding.inflate(getLayoutInflater());
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
                showTourDialog("You can't book this tour unless you have an account");

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
    public void showTourDialog(String message){
        dialog=new Dialog(G_ShowTourDetails.this);
        dialog.setContentView(R.layout.geust_pop_window);
        TextView title=dialog.findViewById(R.id.title);
        Button doneButton=dialog.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(G_ShowTourDetails.this, Main_Registertion.class);
                startActivity(intent);

            }
        });

        title.setText(message);
        dialog.show();
    }

    }
