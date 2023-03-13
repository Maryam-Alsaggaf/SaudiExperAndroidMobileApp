package com.example.saudiexpertsecondversion.TourGuide;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTgEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class TG_EditProfile extends AppCompatActivity {
    ActivityTgEditProfileBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference referenceAllTourGuide = database.getReference("AllTourGuide");
    int PERMISSION_GALLERY_CODE = 101;
    String[] galleryOpenPermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
    Uri selectedImagePath=null;
    FirebaseStorage storageCloud=FirebaseStorage.getInstance();
    StorageReference storageReference=storageCloud.getReference("TourGuideImage");
    /////////////
    String[] languages={"Arabic", "English", "French", "German", "Spanish", "Hindi", "Chinese", "Japanese"};
    ArrayList<Integer> languageList=new ArrayList<>();
    boolean[] selectedLanguages;
    TourGuide currentTourGuide;


    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTgEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Calendar calendar=Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        loadUserData();

        binding.backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(TG_EditProfile.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        binding.birthDateTG.setText(date);

                    }
                },year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });

        binding.spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(TG_EditProfile.this);
                alertDialog.setTitle("Select your speaking language :");
                alertDialog.setCancelable(false);
                alertDialog.setMultiChoiceItems(languages, selectedLanguages, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int inedx, boolean isCheck) {
                        if(isCheck){
                            languageList.add(inedx);
                        }else {
                            languageList.remove(Integer.valueOf(inedx));
                        }

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < languageList.size(); i++) {
                            stringBuilder.append(languages[languageList.get(i)]);
                            stringBuilder.append(",");
                        }
                        binding.speakingLanguageTG.setText(stringBuilder.toString());

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedLanguages.length; j++) {
                            selectedLanguages[j] = false;
                            languageList.clear();
                            binding.speakingLanguageTG.setText("");
                        }

                    }
                });
                alertDialog.show();
            }
        });
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputFirstName = binding.firstNameTG.getText().toString();
                if (inputFirstName.isEmpty()) {
                    binding.firstNameTgLayout.setEndIconVisible(false);
                    binding.firstNameTG.setError("First Name is required");
                    return;
                }
                String inputLastName = binding.lastNameTG.getText().toString();
                if (inputLastName.isEmpty()) {
                    binding.lastNameTgLayout.setEndIconVisible(false);
                    binding.lastNameTG.setError("Last Name is required");
                    return;
                }
                String inputPassword = binding.passwordTG.getText().toString();
                if (inputPassword.isEmpty()) {
                    binding.passwordLayoutTG.setEndIconVisible(false);
                    binding.passwordTG.setError("Password is required");
                    return;
                } else if (inputPassword.length() < 9 || inputPassword.length() > 9) {
                    binding.passwordLayoutTG.setEndIconVisible(false);
                    binding.passwordTG.setError("Password is nine characters or numbers");
                    return;
                }
                String inputPasswordConfirmation = binding.passwordConformationTG.getText().toString();
                if (inputPasswordConfirmation.isEmpty()) {
                    binding.passwordCondirmtionLayoutTG.setEndIconVisible(true);
                    binding.passwordCondirmtionLayoutTG.setEndIconVisible(false);
                    binding.passwordConformationTG.setError("Password Confirmation is required");
                    return;
                } else if (!inputPasswordConfirmation.matches(inputPassword)) {
                    binding.passwordCondirmtionLayoutTG.setEndIconVisible(false);
                    binding.passwordConformationTG.setError("Enter a valid password");
                    return;
                }
                String inputBriefDescription = binding.briefDescriptionTG.getText().toString();
                if (inputBriefDescription.isEmpty()) {
                    binding.briefDescriptionTG.setError("Brief Description is required !");
                    return;
                }
                String inputBirthDate = binding.birthDateTG.getText().toString();
                if (inputBirthDate.isEmpty()) {
                    binding.birthDateTG.setError("Birth date is required");
                    return;
                }
                String inputSpeakingLanguage = binding.speakingLanguageTG.getText().toString();


                referenceAllTourGuide.child(user.getUid()).child("firstName").setValue(inputFirstName);
                referenceAllTourGuide.child(user.getUid()).child("lastName").setValue(inputLastName);
                referenceAllTourGuide.child(user.getUid()).child("briefDescription").setValue(inputBriefDescription);
                referenceAllTourGuide.child(user.getUid()).child("birthDate").setValue(inputBirthDate);
                referenceAllTourGuide.child(user.getUid()).child("speakingLanguages").setValue(inputSpeakingLanguage);
                //updatePassword(currentTourGuide, inputPassword);
                if(selectedImagePath!=null){
                    updateImage(selectedImagePath);
                }
                updatePassword(currentTourGuide,inputPassword);
                Toast.makeText(TG_EditProfile.this,"Profile Updated Successfully",Toast.LENGTH_LONG).show();




            }
        });

    }

    public void loadUserData() {
        referenceAllTourGuide.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TourGuide tourGuide = snapshot.getValue(TourGuide.class);
                if (tourGuide != null) {
                    binding.firstNameTG.setText(String.valueOf(tourGuide.getFirstName()));
                    binding.lastNameTG.setText(String.valueOf(tourGuide.getLastName()));
                    binding.briefDescriptionTG.setText(String.valueOf(tourGuide.getBriefDescription()));
                    binding.passwordConformationTG.setText(String.valueOf(tourGuide.getPassword()));
                    binding.passwordTG.setText(String.valueOf(tourGuide.getPassword()));
                    binding.emailTG.setText(String.valueOf(tourGuide.getEmail()));
                    binding.birthDateTG.setText(String.valueOf(tourGuide.getBirthDate()));
                    binding.speakingLanguageTG.setText(String.valueOf(tourGuide.getSpeakingLanguages()));
                    currentTourGuide=tourGuide;
                    String imageFilePath = String.valueOf(tourGuide.getImageUrl());
                    Picasso.get().load(imageFilePath).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updatePassword(TourGuide tourGuide, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(tourGuide.getEmail(), tourGuide.getPassword());
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            referenceAllTourGuide.child(user.getUid()).child("password").setValue(newPassword);
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           Toast.makeText(TG_EditProfile.this,"Eroor"+e,Toast.LENGTH_LONG).show();
            }
        });

    }
    public void updateImage(Uri filePath){//upload it ti firebase
        if(filePath!=null){
            //firebase path i want at the refence "TourGuideImage"
            StorageReference userRef=storageReference.child(user.getUid()).child("profile Image");
            userRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//the full uri of firebase
                        @Override
                        public void onSuccess(Uri uri) {
                            //upload at realtime
                            referenceAllTourGuide.child(user.getUid()).child("imageUrl").setValue(uri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //Toast.makeText(TG_EditProfile.this,"Update Profile Successfully",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TG_EditProfile.this,"error "+e,Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TG_EditProfile.this,"Error "+e,Toast.LENGTH_LONG);
                }
            });

        }

    }



    public void pickImage() {
        if (ContextCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
            //Permission is granted
        } else {
            ActivityCompat.requestPermissions
                    (TG_EditProfile.this, galleryOpenPermission, PERMISSION_GALLERY_CODE);//pop up wi
        }

    }
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), PERMISSION_GALLERY_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//clcik the widget to open the gallery
            if (requestCode == PERMISSION_GALLERY_CODE) {//the code needed to figure which result called
                selectedImagePath=data.getData();
                if(null!=selectedImagePath){
                    binding.profileImage.setImageURI(selectedImagePath);
                }else if (selectedImagePath==null){
                    binding.profileImage.setImageDrawable
                            (getResources().getDrawable(R.drawable.profile_image));
                }
            }
        }
    }
}