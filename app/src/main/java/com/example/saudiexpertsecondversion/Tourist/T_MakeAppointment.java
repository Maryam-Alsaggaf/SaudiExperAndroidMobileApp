package com.example.saudiexpertsecondversion.Tourist;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTMakeAppointmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class T_MakeAppointment extends AppCompatActivity {
    ActivityTMakeAppointmentBinding binding;
    ArrayList<String> numberOfHours = new ArrayList<>();
    ArrayList<String> timeToStart = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceAllAppointments = database.getReference("AllAppointments");
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    List<Address> addresses;



    FusedLocationProviderClient locationProviderClient;


    Tour tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTMakeAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tour = (Tour) getIntent().getSerializableExtra("selectedTour");
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String idGenerator = String.valueOf(System.currentTimeMillis());
        String appointmentId = idGenerator.substring(idGenerator.length() - 6);
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
        numberOfHours.add("1 hour");
        numberOfHours.add("2 hours");
        numberOfHours.add("3 hours");
        numberOfHours.add("4 hours");
        numberOfHours.add("5 hours");
        numberOfHours.add("6 hours");

        ArrayAdapter<String> numberOfHoursAdapter = new ArrayAdapter<String>(T_MakeAppointment.this,
                android.R.layout.simple_spinner_dropdown_item, numberOfHours);

        binding.filledExposedDropdownHours.setAdapter(numberOfHoursAdapter);
        timeToStart.add("1 PM");
        timeToStart.add("2 PM");
        timeToStart.add("3 PM");
        timeToStart.add("4 PM");
        timeToStart.add("5 PM");
        timeToStart.add("6 PM");
        timeToStart.add("7 PM");
        timeToStart.add("8 PM");
        timeToStart.add("9 PM");
        timeToStart.add("10 PM");
        timeToStart.add("7 AM");
        timeToStart.add("8 AM");
        timeToStart.add("9 AM");
        timeToStart.add("10 AM");
        timeToStart.add("11 AM");
        timeToStart.add("12 PM");

        ArrayAdapter<String> timeToStartAdapter = new ArrayAdapter<String>(T_MakeAppointment.this,
                android.R.layout.simple_spinner_dropdown_item, timeToStart);

        binding.filledExposedDropdownStartTime.setAdapter(timeToStartAdapter);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.meetingPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        binding.dialogDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        T_MakeAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        binding.tourDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }

        });
        binding.bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputNumberOfHours = binding.filledExposedDropdownHours.getText().toString();
                if (inputNumberOfHours.isEmpty()) {
                    binding.hoursLayout.setError("Required !");
                    return;
                }
                String inputTimeToStart = binding.filledExposedDropdownStartTime.getText().toString();
                if (inputTimeToStart.isEmpty()) {
                    binding.timeLayout.setError("Required !");
                    return;
                }
                String inputDate = binding.tourDate.getText().toString();
                if (inputDate.isEmpty()) {
                    binding.datelayout.setError("Required !");
                    return;
                }
                String inputMeetingPoint = binding.meetingPoint.getText().toString();
                if (inputMeetingPoint.isEmpty()) {
                    binding.locationlayout.setError("Required !");
                    return;
                }


                Appointment appointment = new Appointment();
                appointment.setTouristKey(user.getUid());
                appointment.setAppointmentID(appointmentId);
                appointment.setStartTime(inputTimeToStart);
                appointment.setNumberOfHours(inputNumberOfHours);
                appointment.setTourID(tour.getTourID());
                appointment.setStatus("Pending");
                appointment.setDate(inputDate);
                appointment.setMeetingLocation(inputMeetingPoint);
                appointment.setTourGuideKey(tour.getTourGuideKey());
                uploadAppointment(appointment);


            }
        });

    }

    public void uploadAppointment(Appointment appointment) {
        referenceAllAppointments.child(appointment.getAppointmentID()).setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
               dialog("Your reservation has been sent wait to be confirm");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(T_MakeAppointment.this, "Error" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            } else {
                getLocation();
            }
        }
    }



    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10
        );
    }


    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissions();

        }
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Geocoder geocoder=new Geocoder(T_MakeAppointment.this,Locale.getDefault());
                    List<Address> addresses= null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude()
                                ,location.getLongitude(),1);
                        binding.meetingPoint.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(T_MakeAppointment.this, "Error " + e, Toast.LENGTH_SHORT).show();

            }
        });
        }



    public void dialog(String message){
        Dialog dialog=new Dialog(T_MakeAppointment.this);
        dialog.setContentView(R.layout.search_pop_window);
        dialog.setCanceledOnTouchOutside(false);
        Button button=dialog.findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(T_MakeAppointment.this,T_Home.class);
                startActivity(intent);
            }
        });

        TextView title=dialog.findViewById(R.id.title);

        title.setText(message);

        dialog.show();
    }
}


