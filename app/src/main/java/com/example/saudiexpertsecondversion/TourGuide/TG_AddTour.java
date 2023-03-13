package com.example.saudiexpertsecondversion.TourGuide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.databinding.ActivityTgAddTourBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class TG_AddTour extends AppCompatActivity {
    ActivityTgAddTourBinding binding;
    Uri selectedImageUrl=null;
    int PERMISSION_GALLERY_CODE=101;
    String[] galleryOpenPermission={Manifest.permission.READ_EXTERNAL_STORAGE};
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference imageReference =storage.getReference("ToursImages");



    //tour price is drop down list
    //location is drop down
    //description not less than 100 charctrs

    //spinner items
    //every spinner needs two list -- 1- array list contains the options
    //every spinner so it can works it needs adapter and set the arraylist on the adapter
    ArrayList<String> hourPrice=new ArrayList<>();
    ArrayList<String> tourCity=new ArrayList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event){
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);//hide the current window foucs
        //false==0
        return true;

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTgAddTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        ArrayAdapter<String> priceAdapter=new ArrayAdapter<String>(
                TG_AddTour.this, android.R.layout.simple_spinner_item,hourPrice);
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
        ArrayAdapter<String> citesAdapter=new ArrayAdapter<String>(
                TG_AddTour.this, android.R.layout.simple_spinner_item,tourCity);
        binding.filledExposedDropdownCity.setAdapter(citesAdapter);

        String idGenerator=String.valueOf(System.currentTimeMillis());
        String tourId=idGenerator.substring(idGenerator.length()-8);



        //chose a photo
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.addTourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {pickImage();}
        });
        binding.addTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTitle=binding.tourTitle.getText().toString();
                if(inputTitle.isEmpty()){
                    binding.tourTitle.setError("Tour Title is required !");
                    return;
                }
                String inputTourDescription=binding.tourDescription.getText().toString();
                if(inputTourDescription.isEmpty()){
                    binding.tourDescription.setError("Tour Description is required !");
                    return;
                }else if(inputTourDescription.length()<40){
                    binding.tourDescription.setError("Tour Description more than 100 characters");
                    return;
                }
                String inputPrice=binding.filledExposedDropdownPrice.getText().toString();
                if(inputPrice.isEmpty()){
                    binding.filledExposedDropdownPrice.setError("Tour hour price is required !");
                    return;
                }
                String inputTourCity=binding.filledExposedDropdownCity.getText().toString();
                if(inputTourCity.isEmpty()){
                    binding.filledExposedDropdownCity.setError("Tour City is required !");
                    return;
                }
                if(selectedImageUrl==null){
                    Toast.makeText(TG_AddTour.this, "Tour Image is required !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Tour tour=new Tour();
                tour.setTourID(tourId);
                tour.setTourTitle(inputTitle);
                tour.setTourDescription(inputTourDescription);
                tour.setTourCity(inputTourCity);
                tour.setTourImageUrl("");
                tour.setTourPrice(inputPrice);
                tour.setTourGuideKey(user.getUid());
                uploadTour(tour,selectedImageUrl);

            }
        });

    }
    public void uploadTour(Tour tour,Uri imageUrl){
        referenceAllTour.child(tour.getTourID())
                .setValue(tour).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        StorageReference storageReferenceTour=imageReference
                                .child(tour.getTourID()).child("TourImage");
                        storageReferenceTour.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReferenceTour.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        referenceAllTour.child(tour.getTourID())
                                                .child("tourImageUrl").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(TG_AddTour.this, "Tour added successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TG_AddTour.this,"Image Error "+e,Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TG_AddTour.this, "Error !"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }




    public void pickImage(){
        //no permission
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_GRANTED){
            openGallery();

            //already there is a permission
            //PERMISSION_GALLERY_CODE == the request to open the gallery
            //galleryOpenPermission== ask to show this file to the user
        }else{
            ActivityCompat.requestPermissions(TG_AddTour.this,
                    galleryOpenPermission,PERMISSION_GALLERY_CODE);


        }
    }
    public void openGallery(){
        Intent gallery=new Intent("image/*");
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,""),PERMISSION_GALLERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==PERMISSION_GALLERY_CODE){
                selectedImageUrl=data.getData();
                if(selectedImageUrl!=null){
                    binding.addTourImage.setImageURI(selectedImageUrl);
                }

            }
        }
    }
}
