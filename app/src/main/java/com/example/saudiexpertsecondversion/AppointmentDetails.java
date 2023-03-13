package com.example.saudiexpertsecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.databinding.ActivityAppointmentDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentDetails extends AppCompatActivity {
    ActivityAppointmentDetailsBinding binding;
    String appointmentID;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllAppointment =database.getReference("AllAppointments");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAppointmentDetailsBinding.inflate(getLayoutInflater());
        appointmentID=getIntent().getStringExtra("appointmentInfo");
        binding.backButton.setOnClickListener(view->{onBackPressed();});
        loadAppointmentDetails(appointmentID);
        setContentView(binding.getRoot());
    }
    public void loadAppointmentDetails(String appointmentID){
        referenceAllAppointment.child(appointmentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Appointment appointment=snapshot.getValue(Appointment.class);
                binding.date.setText(appointment.getDate());
                binding.startTime.setText(appointment.getStartTime());
                binding.numberOfHours.setText(appointment.getNumberOfHours());
                binding.meetingPoint.setText(appointment.getMeetingLocation());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}