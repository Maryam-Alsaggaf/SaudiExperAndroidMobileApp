package com.example.saudiexpertsecondversion.TourGuide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTgLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TG_Login extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourGuide = database.getReference("AllTourGuide");
    String isExist;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    ActivityTgLoginBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTgLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageButton.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.loginTGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = binding.emailTG.getText().toString();
                if (inputEmail.isEmpty()) {
                    binding.emailTG.setError("Email is required !");
                    return;
                }
                String inputPassword = binding.passwordloginTG.getText().toString();
                if (inputPassword.isEmpty()) {
                    binding.passwordloginTG.setError("Password is required !");
                    return;
                }

               tourGuidSignIn(inputEmail,inputPassword);

            }
        });

    }
    public void tourGuidSignIn(String email,String password){
        userAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId=task.getResult().getUser().getUid();
                    referenceAllTourGuide.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(userId).exists()){
                                referenceAllTourGuide.child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userType = snapshot.getValue(String.class);
                                        if (userType.contains("TourGuide")) {
                                            Intent homePageIntent = new Intent(TG_Login.this,TG_Home.class);
                                            startActivity(homePageIntent);
                                        } else if (!userType.contains("TourGuide")) {
                                            Toast.makeText(TG_Login.this, "You can't log in as Tour Guide", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(TG_Login.this, "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else if(!snapshot.child(userId).exists()){
                                Toast.makeText(TG_Login.this, "You can't log in as Tour Guide", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TG_Login.this,"Error "+e,Toast.LENGTH_LONG).show();
            }
        });
    }
}
