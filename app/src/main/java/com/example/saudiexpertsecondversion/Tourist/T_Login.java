package com.example.saudiexpertsecondversion.Tourist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.databinding.ActivityTLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class T_Login extends AppCompatActivity {
    ActivityTLoginBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourist=database.getReference("ALLTourist");


    @Override
    //hide keyboard when touch anywhere else in the screen
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    FirebaseAuth userAuth=FirebaseAuth.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageButton.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.loginTButton.setOnClickListener(view -> {
            String inputEmail=binding.emailT.getText().toString();
            String inputPassword=binding.passwordloginT.getText().toString();
            if(inputEmail.isEmpty()){
                binding.passwordTLoginlayout.setEndIconVisible(false);
                binding.emailT.setError("Please Enter Email!");
                return;
            }else if(inputPassword.isEmpty()){
                binding.passwordTLoginlayout.setEndIconVisible(false);
                binding.passwordloginT.setError("Please Enter Password!");
                return;
            }
            touristLogin(inputEmail,inputPassword);
        });
        //Toast.makeText(T_Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(T_Login.this,T_Home.class));



    }
    public void touristLogin(String email,String password){
        userAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId=task.getResult().getUser().getUid();
                            referenceAllTourist.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child(userId).exists()){
                                        startActivity(new Intent(T_Login.this,T_Home.class));
                                      /*referenceAllTourist.child(userId).child("typeUser").addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              String userType = snapshot.getValue(String.class);
                                              if(userType.contains("Tourist")){
                                                  Toast.makeText(T_Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                                  startActivity(new Intent(T_Login.this,T_Home.class));
                                              }

                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });*/

                                    }else if(!snapshot.child(userId).exists()){
                                        Toast.makeText(T_Login.this,"You can't login as Tourist",Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(T_Login.this,"Error"+error,Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(e ->
                        {Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
    }
}