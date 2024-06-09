package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    public ArrayList<Car> carList = new ArrayList<>();
    private CarAdapter adapter;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchEditText = findViewById(R.id.searchEditText);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarAdapter(this, carList);
        recyclerView.setAdapter(adapter);



        // Initialize RequestQueue
        queue = Volley.newRequestQueue(this);

        // Fetch data from server
        fetchDataFromServer();
        // Inside onCreate() method after fetchDataFromServer()
// Set up the search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchCars(query);
                } else {
                    carList.clear(); // Clear the list if the search query is empty
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        // Set up the navigation drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_settings);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        int itemId = menuItem.getItemId();

        if (itemId == R.id.nav_settings) {
            // Handle settings action
            return true;
        } else if (itemId == R.id.nav_rec) {
            // Handle received action
            return true;
        } else if (itemId == R.id.nav_add) {
            // Handle add action
            return true;
        } else if (itemId == R.id.nav_logout) {
            logout(); // Call the logout method
            return true;
        }

        return false;
    }
    private void logout() {
        // Implement your logout logic here
        // For example, clear user session, reset preferences, etc.

        // Start the LoginActivity to log the user out
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity to prevent going back to it using the back button
    }

    private void fetchDataFromServer() {
        String url = "http://172.18.0.1/Android/V1/readCars.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("ID");
                                String carName = jsonObject.getString("carName");
                                String description = jsonObject.getString("Description");
                                double price = jsonObject.getDouble("Price");
                                int state = jsonObject.getInt("State");

                              String imageUrl = jsonObject.getString("imageUrl");
                                int numberOfCars = jsonObject.getInt("numberOfCars");
                               /* int numberOfCars = jsonObject.optInt("numberOfCars", 0);*/


                                Car car = new Car(id, carName, description, price, state, imageUrl, numberOfCars);
                                carList.add(car);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
    private void searchCars(String query) {
        String url = "http://172.18.0.1/Android/V1/searchCars.php?query=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        carList.clear(); // Clear existing data
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("ID");
                                String carName = jsonObject.getString("carName");
                                String description = jsonObject.getString("Description");
                                double price = jsonObject.getDouble("Price");
                                int state = jsonObject.getInt("State");

                                String imageUrl = jsonObject.getString("imageUrl");
                                int numberOfCars = jsonObject.getInt("numberOfCars");

                                Car car = new Car(id, carName, description, price, state, imageUrl, numberOfCars);
                                carList.add(car);
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter about data change
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }


}
