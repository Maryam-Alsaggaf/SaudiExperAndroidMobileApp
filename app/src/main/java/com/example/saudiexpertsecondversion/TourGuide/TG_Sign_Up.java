package com.example.saudiexpertsecondversion.TourGuide;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.databinding.ActivityTgSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class TG_Sign_Up extends AppCompatActivity {


    @Override

    //hide keyboard when touch anywhere else in the screen
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    ActivityTgSignUpBinding binding;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("AllTourGuide");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseStorage storageCloud=FirebaseStorage.getInstance();
    StorageReference storageReference=storageCloud.getReference("TourGuideImage");
    String[] languages = {"Arabic", "English", "French", "German", "Spanish", "Hindi", "Chinese", "Japanese"};
    boolean[] selectLanguge;
    ArrayList<Integer> languageList = new ArrayList<>();
    Uri selectedImagePath=null;


    // any value only unique for each permission
    int PERMISSION_GALLERY_CODE = 101;
    String[] galleryOpenPermission = {Manifest.permission.READ_EXTERNAL_STORAGE};

    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTgSignUpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        selectLanguge = new boolean[languages.length];

        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.spinner.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TG_Sign_Up.this);
            builder.setTitle("Select your speaking language :");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(
                    languages, selectLanguge, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int index, boolean isCheck) {
                            if (isCheck) {
                                languageList.add(index);
                            } else {
                                languageList.remove(Integer.valueOf(index));
                            }
                        }
                    }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                public void onClick(DialogInterface dialogInterface, int index) {
                    dialogInterface.dismiss();
                }
            }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    for (int j = 0; j < selectLanguge.length; j++) {
                        selectLanguge[j] = false;
                        languageList.clear();
                        binding.speakingLanguageTG.setText("");
                    }
                }
            });
            builder.show();
        });
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TG_Sign_Up.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = 1 + month;
                                String date = day + "/" + month + "/" + year;
                                binding.birthDateTG.setText(date);

                            }
                        }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputFirstName = binding.firstNameTG.getText().toString();
                if (inputFirstName.isEmpty()) {
                    binding.firstNameTG.setError("First Name is required");
                    return;
                } else if (!inputFirstName.matches("[a-zA-Z ]+ ")) {

                    binding.firstNameTG.setError("Enter only letters");
                    return;
                }
                String inputLastName = binding.lastNameTG.getText().toString();
                if (inputLastName.isEmpty()) {
                    binding.lastNameTG.setError("Last Name is required");
                    return;
                } else if (!inputLastName.matches("[a-zA-Z ]+ ")) {
                    binding.lastNameTG.setError("Enter only letters");
                    return;
                }
                String inputEmail = binding.emailTG.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
                    binding.emailTG.setError("Enter a valid Email");
                    return;
                }else if (inputEmail.isEmpty()){
                    binding.emailTG.setError("Email is required");
                    return;
                }
                String inputPassword = binding.passwordTG.getText().toString();
                if (inputPassword.isEmpty()) {
                    binding.passwordLayoutTG.setEndIconVisible(false);
                    binding.passwordTG.setError("Password is required");
                    return;
                }
                binding.passwordLayoutTG.setEndIconVisible(true);
                 if (inputPassword.length() > 9 || inputPassword.length() < 9) {
                    binding.passwordLayoutTG.setEndIconVisible(false);
                    binding.passwordTG.setError("Password is only 9 numbers or characters");
                    return;
                }
                String inputPasswordConformition = binding.passwordConformationTG.getText().toString();
                 if(inputPasswordConformition.isEmpty()){
                     binding.passwordCondirmtionLayoutTG.setEndIconVisible(false);
                     binding.passwordConformationTG.setError("Password Confirmation is required");
                     return;
                 }
                binding.passwordCondirmtionLayoutTG.setEndIconVisible(true);
                if (!inputPasswordConformition.matches(inputPassword)) {
                    binding.passwordCondirmtionLayoutTG.setEndIconVisible(false);
                    binding.passwordConformationTG.setError("Enter a valid password conformation");
                    return;
                }
                binding.passwordLayoutTG.setEndIconVisible(true);
                String inputBriefDescription = binding.briefDescriptionTG.getText().toString();
                if (inputBriefDescription.isEmpty()) {
                    binding.briefDescriptionTG.setError("Brief Description is required");
                    return;
                }
                binding.passwordLayoutTG.setEndIconVisible(true);

                String inputBirthDate=binding.birthDateTG.getText().toString();
                if(inputBirthDate.isEmpty()){
                    binding.birthDateTG.setError("Birth date is required");
                    return;
                }
                String inputSpeakingLanguge=binding.speakingLanguageTG.getText().toString();


                TourGuide tourGuide=new TourGuide();
                tourGuide.setEmail(inputEmail);
                tourGuide.setBirthDate(inputBirthDate);
                tourGuide.setBriefDescription(inputBriefDescription);
                tourGuide.setFirstName(inputFirstName);
                tourGuide.setLastName(inputLastName);
                tourGuide.setPassword(inputPassword);
                tourGuide.setSpeakingLanguages(inputSpeakingLanguge);
                tourGuide.setImageUrl("");
                tourGuide.setUserType("TourGuide");


                tourGuideRegistration(tourGuide);
            }

        });

    }
    public void tourGuideRegistration(TourGuide tourGuide){
        auth.createUserWithEmailAndPassword(tourGuide.getEmail(),tourGuide.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){//if the new user(email,password) created without any issue
                            user=auth.getCurrentUser();//put the identifier at firebase user type
                            tourGuide.setKey(user.getUid());
                            ref.child(user.getUid()).setValue(tourGuide).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (selectedImagePath != null) {
                                        uploadStorageImage(selectedImagePath);
                                    } else {
                                        Toast.makeText(TG_Sign_Up.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(TG_Sign_Up.this,TG_Login.class));
                                    }
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TG_Sign_Up.this,"Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void uploadStorageImage(Uri filepath){
        if(filepath!=null){
             StorageReference storageRef=storageReference.child(user.getUid()).child("profile Image");
             //tourGuideimage//userkey//profileimage//file
            storageRef.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    ref.child(user.getUid()).child("imageUrl").setValue(uri.toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(TG_Sign_Up.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                    //startActivity();
                                }
                            });

                });

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TG_Sign_Up.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    (TG_Sign_Up.this, galleryOpenPermission, PERMISSION_GALLERY_CODE);//pop up wi
        }
    }
    public void openGallery() {
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
                }

            }
        }
    }
}

