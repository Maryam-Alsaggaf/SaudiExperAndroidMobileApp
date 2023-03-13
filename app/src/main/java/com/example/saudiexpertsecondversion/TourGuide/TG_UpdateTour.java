package com.example.saudiexpertsecondversion.TourGuide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.databinding.ActivityTgUpdateTourBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TG_UpdateTour extends AppCompatActivity {
    ActivityTgUpdateTourBinding binding;
    Tour tour;
    ArrayList<String> hourPrice = new ArrayList<>();
    ArrayList<String> tourCity = new ArrayList<>();
    Uri selectedImageUrl=null;
    int PERMISSION_GALLERY_CODE = 101;
    String[] galleryOpenPermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    StorageReference storageReference=storage.getReference("ToursImages");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTgUpdateTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tour = (Tour) getIntent().getSerializableExtra("TourObject");
        loadingTour(tour);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome=new Intent(TG_UpdateTour.this,TG_Home.class);
                startActivity(intentHome);
            }
        });
        binding.tourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        hourPrice.add("10 RS");
        hourPrice.add("20 RS");
        hourPrice.add("30 RS");
        hourPrice.add("40 RS");
        hourPrice.add("50 RS");
        hourPrice.add("60 RS");
        hourPrice.add("70 RS");
        hourPrice.add("80 RS");
        hourPrice.add("90 RS");
        hourPrice.add("100 RS");
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(
                TG_UpdateTour.this, android.R.layout.simple_spinner_item, hourPrice);
        binding.filledExposedDropdownPrice.setAdapter(priceAdapter);
        tourCity.add("Abaha");
        tourCity.add("Al-Ahsa");
        tourCity.add("Al-Khobar");
        tourCity.add("Baha");
        tourCity.add("Dammam");
        tourCity.add("Dahran");
        tourCity.add("Riyadh");
        tourCity.add("Jeddah");
        tourCity.add("Jizan");
        tourCity.add("Jouf");
        tourCity.add("Jubail");
        tourCity.add("Madinh");
        tourCity.add("Makkah");
        tourCity.add("Najran");
        tourCity.add("Qassem");
        tourCity.add("Qatif");
        tourCity.add("Tabuk");
        tourCity.add("Taif");
        tourCity.add("Yanbu");
        ArrayAdapter<String> citesAdapter = new ArrayAdapter<String>(
                TG_UpdateTour.this, android.R.layout.simple_spinner_item, tourCity);
        binding.filledExposedDropdownCity.setAdapter(citesAdapter);
        binding.updateTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTitle = binding.tourTitle.getText().toString();
                if (inputTitle.isEmpty()) {
                    binding.tourTitle.setError("Tour Title is required !");
                    return;
                }
                String inputTourDescription = binding.tourDescription.getText().toString();
                if (inputTourDescription.isEmpty()) {
                    binding.tourDescription.setError("Tour Description is required !");
                    return;
                } else if (inputTourDescription.length() < 40) {
                    binding.tourDescription.setError("Tour Description more than 100 characters");
                    return;
                }
                String inputPrice = binding.filledExposedDropdownPrice.getText().toString();
                if (inputPrice.isEmpty()) {
                    binding.filledExposedDropdownPrice.setError("Tour hour price is required !");
                    return;
                }
                String inputTourCity = binding.filledExposedDropdownCity.getText().toString();
                if (inputTourCity.isEmpty()) {
                    binding.filledExposedDropdownCity.setError("Tour City is required !");
                    return;
                }


                referenceAllTour.child(tour.getTourID()).child("tourCity").setValue(inputTourCity);
                referenceAllTour.child(tour.getTourID()).child("tourTitle").setValue(inputTitle);
                referenceAllTour.child(tour.getTourID()).child("tourDescription").setValue(inputTourDescription);
                referenceAllTour.child(tour.getTourID()).child("tourPrice").setValue(inputPrice);
                if (selectedImageUrl != null) {
                    updateImage(selectedImageUrl);
                } else {
                    Toast.makeText(TG_UpdateTour.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(TG_UpdateTour.this,TG_Home.class);
                    startActivity(intent);
                }






            }
        });


    }
    public void updateImage(Uri selectedImageUrl){
       StorageReference tourReference=storageReference.child(tour.getTourID()).child("TourImage");
       tourReference.putFile(selectedImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               tourReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Toast.makeText(TG_UpdateTour.this,"Updated Successfully",Toast.LENGTH_LONG).show();
                       Intent intent=new Intent(TG_UpdateTour.this,TG_Home.class);
                       startActivity(intent);

                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(TG_UpdateTour.this,"Error"+e,Toast.LENGTH_LONG).show();
           }
       });
    }

    public void loadingTour(Tour tour) {
        Picasso.get().load(tour.getTourImageUrl()).fit().into(binding.tourImage);
        binding.tourTitle.setText(tour.getTourTitle());
        binding.tourDescription.setText(tour.getTourDescription());
        binding.filledExposedDropdownPrice.setText(tour.getTourPrice());
        binding.filledExposedDropdownCity.setText(tour.getTourCity());
    }






    public void pickImage() {
        //no permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            openGallery();

            //already there is a permission
            //PERMISSION_GALLERY_CODE == the request to open the gallery
            //galleryOpenPermission== ask to show this file to the user
        } else {
            ActivityCompat.requestPermissions(TG_UpdateTour.this,
                    galleryOpenPermission, PERMISSION_GALLERY_CODE);


        }
    }

    public void openGallery() {
        Intent gallery = new Intent("image/*");
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, ""), PERMISSION_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_GALLERY_CODE) {
                selectedImageUrl = data.getData();
                if (selectedImageUrl != null) {
                    binding.tourImage.setImageURI(selectedImageUrl);
                }


            }
        }
    }
}