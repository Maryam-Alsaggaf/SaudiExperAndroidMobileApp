package com.example.saudiexpertsecondversion.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.saudiexpertsecondversion.Object.Tourist;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.TourGuide.TG_Sign_Up;
import com.example.saudiexpertsecondversion.databinding.ActivityTSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class T_Sign_Up extends AppCompatActivity {

    ActivityTSignUpBinding binding;
    final FirebaseDatabase database=FirebaseDatabase.getInstance();
    final DatabaseReference refrenceAllTourist=database.getReference("ALLTourist");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user;


    @Override
    //hide keyboard when touch anywhere else in the screen
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        return true;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton2.setOnClickListener(view -> {
            onBackPressed();
        });
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        T_Sign_Up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        binding.birthDateT.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputFirstName=binding.firstNameT.getText().toString();
                if(inputFirstName.isEmpty()){
                    binding.firstNameT.setError("First Name is required");

                    return;
                }
                String inputLastName=binding.lastNameT.getText().toString();
                if(inputLastName.isEmpty()){
                    binding.lastNameT.setError("Last Name is required");
                    return;
                }
                String inputPhoneNumber=binding.phoneNumberT.getText().toString();
                if(inputPhoneNumber.isEmpty()) {
                    binding.phoneNumberT.setError("Phone Number is required");
                    return;
                }else if(inputPhoneNumber.length()>9||inputPhoneNumber.length()<9){
                    binding.phoneNumberT.setError("Phone number contains only 9 numbers");
                    return;
                }
                String inputEmail=binding.emailT.getText().toString();
                if(inputEmail.isEmpty()){
                    binding.emailT.setError("Email is required");
                    return;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                    binding.emailT.setError("Enter a Valid Email Address");
                    return;
                }
                String inputPassword=binding.passwordT.getText().toString();
                if(inputPassword.isEmpty()){
                    binding.passwordLayout.setEndIconVisible(false);
                    binding.passwordT.setError("Password is required");
                    return;
                }else if(inputPassword.length()<9||inputPassword.length()>9){
                    binding.passwordLayout.setEndIconVisible(false);
                    binding.passwordT.setError("Password is nine characters or numbers");
                    return;
                }
                String inputPasswordConfirmation=binding.passwordConfirmitionT.getText().toString();
                if(inputPasswordConfirmation.isEmpty()){
                    binding.passwordLayout.setEndIconVisible(true);
                    binding.passwordConfirmitiondLayout.setEndIconVisible(false);
                    binding.passwordConfirmitionT.setError("Password Confirmation is required");
                    return;
                }else if(!inputPasswordConfirmation.matches(inputPassword)){
                    binding.passwordConfirmitiondLayout.setEndIconVisible(false);
                    binding.passwordConfirmitionT.setError("Enter a valid password");
                    return;
                }
                String gender="";
                if(!binding.radioButton.isChecked() && !binding.radioButton2.isChecked()){
                    Toast.makeText(T_Sign_Up.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(binding.radioButton.isChecked()){
                    gender="Female";
                }else{
                    gender="Male";
                }
                String inputBirthDate=binding.birthDateT.getText().toString();
                if(inputBirthDate.isEmpty()){
                    binding.birthDateT.setError("Birth Date is required");
                    return;
                }
                Tourist tourist=new Tourist();
                tourist.setFirstName(inputFirstName);
                tourist.setLastName(inputLastName);
                tourist.setGender(gender);
                tourist.setBirthDate(inputBirthDate);
                tourist.setPassword(inputPassword);
                tourist.setPhone(inputPhoneNumber);
                tourist.setEmail(inputEmail);
                tourist.setUserType("Tourist");

                TouristRegistration(tourist);

            }
        });
    }

    public void TouristRegistration(Tourist tourist){
        auth.createUserWithEmailAndPassword(tourist.getEmail(),tourist.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user=auth.getCurrentUser();
                            tourist.setKey(user.getUid());//to this object (with ita all info) put the user id to it
                            refrenceAllTourist.child(user.getUid())
                                    .setValue(tourist).addOnSuccessListener(unused -> {
                                        Toast.makeText(T_Sign_Up.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(T_Sign_Up.this,T_Login.class));
                                    });
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    }


