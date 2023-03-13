package com.example.saudiexpertsecondversion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.Guest.G_Home;
import com.example.saudiexpertsecondversion.TourGuide.TG_Login;
import com.example.saudiexpertsecondversion.TourGuide.TG_Sign_Up;
import com.example.saudiexpertsecondversion.Tourist.T_Login;
import com.example.saudiexpertsecondversion.Tourist.T_Sign_Up;

public class Main_Registertion extends AppCompatActivity {
    Button signup, login;
    TextView explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registertion);

        signup = (Button) findViewById(R.id.button2);
        login = (Button) findViewById(R.id.button4);
        explore=(TextView)findViewById(R.id.textView);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog("signup");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog("login");
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main_Registertion.this,G_Home.class);
                startActivity(intent);
            }
        });


    }

    void registerDialog(String registerType) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.type_user_dialog);
        dialog.show();
        TextView tourGuide=dialog.findViewById(R.id.textView2);
        TextView tourist=dialog.findViewById(R.id.textView3);






        tourGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerType.equals("signup")){
                    startActivity(new Intent(Main_Registertion.this, TG_Sign_Up.class));
                }else if(registerType.equals("login")){
                    startActivity(new Intent(Main_Registertion.this, TG_Login.class));
                }
                }
        });
        tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerType.equals("signup")){
                    startActivity(new Intent(Main_Registertion.this, T_Sign_Up.class));
                }else if(registerType.equals("login")){
                    startActivity(new Intent(Main_Registertion.this, T_Login.class));
                }

            }
        });

    }
}
