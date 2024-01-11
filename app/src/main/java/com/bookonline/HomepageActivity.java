package com.bookonline;


import static com.bookonline.R.id.openLoginBtn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Button openRegisterBtn = findViewById(R.id.openRegisterBtn);


        openRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(HomepageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        Button OpenLoginBtn = findViewById(openLoginBtn);

        OpenLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(HomepageActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });


    }
}




