package com.example.saudiexpertsecondversion.Tourist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.Object.Tourist;
import com.example.saudiexpertsecondversion.databinding.ActivityTEditProfileBinding;
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

import java.util.Calendar;

public class T_EditProfile extends AppCompatActivity {
    ActivityTEditProfileBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourist=database.getReference("ALLTourist");
    Tourist currentTourist;

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        return true;
    }
    Calendar calendar=Calendar.getInstance();
    int year= calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH);
    int day=calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
        loadUserData();
        binding.birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(T_EditProfile.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        binding.birthDateT.setText(date);

                    }
                },year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputFirstName=binding.firstNameT.getText().toString();
                if(inputFirstName.isEmpty()){
                    binding.firstNameTLayout.setEndIconVisible(false);
                    binding.firstNameT.setError("First Name is required");
                    return;
                }
                String inputLastName=binding.lastNameT.getText().toString();
                if(inputLastName.isEmpty()){
                    binding.lastNameTLayout.setEndIconVisible(false);
                    binding.lastNameT.setError("Last Name is required");
                    return;
                }
                String inputPhoneNumber=binding.phoneNumberT.getText().toString();
                if(inputPhoneNumber.isEmpty()) {
                    binding.phoneNumberTLayout.setEndIconVisible(false);
                    binding.phoneNumberT.setError("Phone Number is required");
                    return;
                }else if(inputPhoneNumber.length()>9||inputPhoneNumber.length()<9){
                    binding.phoneNumberTLayout.setEndIconVisible(false);
                    binding.phoneNumberT.setError("Phone number contains only 9 numbers");
                    return;
                }
                String inputPassword=binding.passwordT.getText().toString();
                if(inputPassword.isEmpty()){
                    binding.passwordTLayout.setEndIconVisible(false);
                    binding.passwordT.setError("Password is required");
                    return;
                }else if(inputPassword.length()<9||inputPassword.length()>9){
                    binding.passwordTLayout.setEndIconVisible(false);
                    binding.passwordT.setError("Password is nine characters or numbers");
                    return;
                }
                String inputPasswordConfirmation=binding.passwordConfirmitionT.getText().toString();
                if(inputPasswordConfirmation.isEmpty()){
                    binding.passwordConfirmitionTLayout.setEndIconVisible(true);
                    binding.passwordConfirmitionTLayout.setEndIconVisible(false);
                    binding.passwordConfirmitionT.setError("Password Confirmation is required");
                    return;
                }else if(!inputPasswordConfirmation.matches(inputPassword)){
                    binding.passwordConfirmitionTLayout.setEndIconVisible(false);
                    binding.passwordConfirmitionT.setError("Enter a valid password");
                    return;
                }
                String gender="";
                if(!binding.female.isChecked() && !binding.male.isChecked()){
                    Toast.makeText(T_EditProfile.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(binding.female.isChecked()){
                    gender="Female";
                }else{
                    gender="Male";
                }
                String inputBirthDate=binding.birthDateT.getText().toString();
                if(inputBirthDate.isEmpty()){
                    binding.birthDateT.setError("Birth Date is required");
                    return;
                }
                referenceAllTourist.child(user.getUid()).child("firstName").setValue(inputFirstName);
                referenceAllTourist.child(user.getUid()).child("lastName").setValue(inputLastName);
                referenceAllTourist.child(user.getUid()).child("phone").setValue(inputPhoneNumber);
                referenceAllTourist.child(user.getUid()).child("gender").setValue(gender);
                referenceAllTourist.child(user.getUid()).child("birthDate").setValue(inputBirthDate);
                updatePassword(currentTourist,inputPassword);

            }
        });




    }
    public void  updatePassword(Tourist tourist,String newPassword){
        AuthCredential credential= EmailAuthProvider.getCredential(tourist.getEmail(),tourist.getPassword());
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceAllTourist.child(user.getUid()).child("password").setValue(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Toast.makeText(T_EditProfile.this,"Updated Successfully",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(T_EditProfile.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }
    public void loadUserData(){
        referenceAllTourist.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tourist tourist=snapshot.getValue(Tourist.class);
                if(tourist!=null){
                    binding.firstNameT.setText(String.valueOf(tourist.getFirstName()));
                    binding.lastNameT.setText(String.valueOf(tourist.getLastName()));
                    binding.phoneNumberT.setText(String.valueOf(tourist.getPhone()));
                    binding.emailT.setText(String.valueOf(tourist.getEmail()));
                    binding.birthDateT.setText(String.valueOf(tourist.getBirthDate()));
                    binding.passwordT.setText(String.valueOf(tourist.getPassword()));
                    binding.passwordConfirmitionT.setText(String.valueOf(tourist.getPassword()));
                    currentTourist=tourist;
                    String gender=String.valueOf(tourist.getGender());
                    if(gender.equals("Female")){
                        binding.female.setChecked(true);

                    }else{
                        binding.male.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}