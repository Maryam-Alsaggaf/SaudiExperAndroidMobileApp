package com.example.saudiexpertsecondversion.TourGuide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTgShowTourDeatilsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class TG_ShowTourDetails extends AppCompatActivity {
    ActivityTgShowTourDeatilsBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    DatabaseReference referenceAllAppointment=database.getReference("AllAppointments");
    FirebaseStorage storage=FirebaseStorage.getInstance();
    Dialog dialog;
    Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= (ActivityTgShowTourDeatilsBinding) ActivityTgShowTourDeatilsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tour=(Tour) getIntent().getSerializableExtra("selectedTour");
        tourDetails(tour);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.deleteTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(TG_ShowTourDetails.this);
                dialog.setContentView(R.layout.search_pop_window);
                TextView title=dialog.findViewById(R.id.title);
                title.setText("Are sure you want to delete this tour ?");
            }
        });

        binding.updateTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookedTourStatus(tour,"update");

            }

        });
        binding.deleteTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              bookedTourStatus(tour,"delete");



            }
        });

    }
    public void bookedTourStatus(Tour tour , String request){
        referenceAllAppointment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isBooked = "false";
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Appointment appointment=dataSnapshot.getValue(Appointment.class);

                    if(appointment.getTourID().contains(tour.getTourID())) {
                        isBooked="true";
                        break;
                    }
                }
                if(request.equals("update")){
                    if(isBooked.equals("false")){
                        passCurrentObject(tour);
                    }if(isBooked.equals("true")){
                        showTourDialog("You can't update booked tour !");}

                    }else if(request.equals("delete")) {
                           if(isBooked.equals("false")){
                        deleteTourDialog("Are you sure you want to delete this tour ?", tour);
                    }if(isBooked.equals("true")){
                               showTourDialog("You can't delete booked tour !");
                           }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}




    public void tourDetails(Tour tour){
        Picasso.get().load(tour.getTourImageUrl()).fit().into(binding.tourItemImage);
        binding.title.setText(tour.getTourTitle());
        binding.tourItemDescription.setText(tour.getTourDescription());
        binding.tourItemCity.setText("City: "+tour.getTourCity());
        binding.tourItemPrice.setText("Price for hour: "+tour.getTourPrice());
    }
    public void passCurrentObject(Tour tour){
        Intent intent=new Intent(TG_ShowTourDetails.this,TG_UpdateTour.class);
        intent.putExtra("TourObject",tour);
        startActivity(intent);

    }

    public void showTourDialog(String message){
        dialog=new Dialog(TG_ShowTourDetails.this);
        dialog.setContentView(R.layout.search_pop_window);
        dialog.setCanceledOnTouchOutside(false);
        TextView title=dialog.findViewById(R.id.title);
        Button doneButton=dialog.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title.setText(message);
        dialog.show();
    }

    public void deleteTour(Tour tour){
            storage.getReferenceFromUrl(tour.getTourImageUrl()).delete();
            referenceAllTour.child(tour.getTourID()).removeValue();
            onBackPressed();

    }
    public void deleteTourDialog(String message,Tour tour){
        dialog=new Dialog(TG_ShowTourDetails.this);
        dialog.setContentView(R.layout.logout_window);
        TextView title=dialog.findViewById(R.id.title);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);
        title.setText(message);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTour(tour);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}