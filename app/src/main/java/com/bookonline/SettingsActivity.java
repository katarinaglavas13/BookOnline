package com.bookonline;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bookonline.model.User;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView imgLogoutView;
    TextView viewLl, fullnameTv, emailTv, phoneTv;
    Button editProfile, changePassword, logoutBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://bookonline-2021a-default-rtdb.europe-west1.firebasedatabase.app/");

        editProfile = findViewById(R.id.editProfile);
        changePassword = findViewById(R.id.changePassword);
        logoutBtn = findViewById(R.id.logoutBtn);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        viewLl = findViewById(R.id.viewLl);
        fullnameTv = findViewById(R.id.fullnameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            DatabaseReference profileRef = mDatabase.getReference("Profile").child(currentUser.getUid());
            profileRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    User profileUser = task.getResult().getValue(User.class);
                    if(profileUser != null){
                        viewLl.setText(profileUser.getFullname());
                        fullnameTv.setText(profileUser.getFullname());
                        emailTv.setText(profileUser.getEmail());
                        phoneTv.setText(profileUser.getPhone());
                    }
                }
            });
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileActivity = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(editProfileActivity);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordActivity = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(changePasswordActivity);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_books:
                        startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_settings:
                        return true;
                }
                return false;
            }
        });
    }


    //Provjeravanje korisnikove prijave
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =mAuth.getCurrentUser();
        if(user != null){

        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}