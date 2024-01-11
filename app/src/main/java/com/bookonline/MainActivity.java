package com.bookonline;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookonline.adapter.BooksAdapter;
import com.bookonline.model.BookAPI;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton imageButton;
    RecyclerView recyclerViewBooks;
    BooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.t1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.t2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.t3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.t4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.t5, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.t6, ScaleTypes.FIT));


        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        // Dodali ovo kako bi pokrenuli FetchBooksTask
        new FetchBooksTask().execute();



        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;

                    case R.id.navigation_books:
                        startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private class FetchBooksTask extends AsyncTask<Void, Void, ArrayList<BookAPI>> {
        @Override
        protected ArrayList<BookAPI> doInBackground(Void... voids) {
            ArrayList<BookAPI> books = new ArrayList<>();

            try {
                String apiKey = "6JbKR5ew39kk8AeNAcEL0fbjRsdrdA6m";
                String apiUrl = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray booksArray = jsonResponse.getJSONObject("results").getJSONArray("books");


                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject bookObject = booksArray.getJSONObject(i);
                    String title = bookObject.getString("title");
                    String imageUrl = bookObject.getString("book_image");

                    // Dodavanje novog BookAPI objekta u listu
                    books.add(new BookAPI(title, imageUrl));
                }

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<BookAPI> books) {
            if (!books.isEmpty()) {
                // Ažuriranje RecyclerView sa dobijenim podacima
                booksAdapter = new BooksAdapter(books);
                recyclerViewBooks.setAdapter(booksAdapter);
                Toast.makeText(MainActivity.this, "Dohvaćene knjige pomoću API: " + books.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Nije moguće dohvatiti knjige.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}