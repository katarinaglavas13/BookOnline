package com.bookonline;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bookonline.model.User;

public class EditProfileActivity extends AppCompatActivity {
    ImageButton backBtn;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://bookonline-2021a-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_profile);

        FirebaseUser currentUser  = mAuth.getCurrentUser();
        EditText fullnameTxt = findViewById(R.id.fullnameTxt);
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText phoneTxt = findViewById(R.id.phoneTxt);
        Button submitBtn = findViewById(R.id.submitBtn);

        if(currentUser != null){
            DatabaseReference profileRef = mDatabase.getReference("Profile").child(currentUser.getUid());

            profileRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    User userProfile= task.getResult().getValue(User.class);
                    if(userProfile != null){
                        fullnameTxt.setText(userProfile.getFullname());
                        emailTxt.setText(userProfile.getEmail());
                        phoneTxt.setText(userProfile.getPhone());
                    }
                }
            });
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fullname = fullnameTxt.getText().toString();
                    String email = emailTxt.getText().toString();
                    String phone = phoneTxt.getText().toString();

                    User user = new User(fullname, email, phone);
                    profileRef.setValue(user);

                    Toast.makeText(getApplicationContext(), "Uspješno ste promijenili podatke", Toast.LENGTH_SHORT).show();
                }
            });
        }

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ne radi ispravno
                //onBackPressed();
                Intent i = new Intent(EditProfileActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }

}