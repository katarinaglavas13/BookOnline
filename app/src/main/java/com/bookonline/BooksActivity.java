package com.bookonline;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bookonline.adapter.BookAdapter;
import com.bookonline.model.Book;

public class BooksActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://bookonline-2021a-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "BOOKS_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        FloatingActionButton openAddBooksBtn = findViewById(R.id.openAddBooksBtn);


        this.recyclerView = findViewById(R.id.bookRv);
        this.recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        FirebaseRecyclerOptions<Book> options = new FirebaseRecyclerOptions.Builder<Book>().setQuery(this.mDatabase.getReference("Books"), Book.class).build();
        this.bookAdapter = new BookAdapter(options);
        this.recyclerView.setAdapter(this.bookAdapter);


        openAddBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser  = mAuth.getCurrentUser();
                if(isEmailValid(currentUser.getEmail())){
                    Intent addBookActivity = new Intent(BooksActivity.this, AddBookActivity.class);
                    startActivity(addBookActivity);
                }else{
                    Toast.makeText(getApplicationContext(), "Nemate mogućnost dodavavanja knjiga", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Navigacija
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_books);
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
                        return true;

                    case R.id.navigation_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.bookAdapter.startListening();
        Log.d(TAG, "starting: početak");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.bookAdapter.stopListening();
        Log.d(TAG, "ending: kraj");
    }

    //Validacija emaila
    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(gmail.com)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}