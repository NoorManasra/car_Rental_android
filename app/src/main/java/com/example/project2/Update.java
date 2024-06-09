package com.example.project2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    private EditText updateIdEditText;
    private EditText updateNameEditText;
    private EditText updatePriceEditText;
    private EditText updateStateEditText;
    private EditText updateDescriptionEditText;
    private EditText updateImageEditText;
    private EditText updateNumberOfCarsEditText;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        // Initialize EditText fields and Button
        updateIdEditText = findViewById(R.id.updateIdEditText);
        updateNameEditText = findViewById(R.id.updateNameEditText);
        updatePriceEditText = findViewById(R.id.updatePriceEditText);
        updateStateEditText = findViewById(R.id.updateStateEditText);
        updateDescriptionEditText = findViewById(R.id.updateDescriptionEditText);
        updateImageEditText = findViewById(R.id.updateImageEditText);
        updateNumberOfCarsEditText = findViewById(R.id.updateNumberOfCarsEditText);
        updateButton = findViewById(R.id.updateButton);

        // Set OnClickListener for the updateButton
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values from EditText fields
                final String updatedId = updateIdEditText.getText().toString();
                final String updatedName = updateNameEditText.getText().toString();
                final String updatedPrice = updatePriceEditText.getText().toString();
                final String updatedState = updateStateEditText.getText().toString();
                final String updatedDescription = updateDescriptionEditText.getText().toString();
                final String updatedImage = updateImageEditText.getText().toString();
                final String updatedNumberOfCars = updateNumberOfCarsEditText.getText().toString();

                // Send POST request to the PHP script
                String url = "http://172.18.0.1/Android/V1/updateCars.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Print the JSON response for debugging
                                Log.d("JSON Response", response);

                                // Handle response from the server
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean error = jsonObject.getBoolean("error");
                                    if (!error) {
                                        Toast.makeText(Update.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        String errorMessage = jsonObject.getString("message");
                                        Toast.makeText(Update.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Update.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                error.printStackTrace();
                                Toast.makeText(Update.this, "Error updating record", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Parameters to be sent in the POST request
                        Map<String, String> params = new HashMap<>();
                        params.put("updatedId", updatedId);
                        params.put("updatedName", updatedName);
                        params.put("updatedPrice", updatedPrice);
                        params.put("updatedState", updatedState);
                        params.put("updatedDescription", updatedDescription);
                        params.put("updatedImage", updatedImage);
                        params.put("updatedNumberOfCars", updatedNumberOfCars);
                        return params;
                    }
                };
                // Add the request to the RequestQueue
                Volley.newRequestQueue(Update.this).add(stringRequest);
            }
        });
    }
}
