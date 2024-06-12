package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class web_view extends AppCompatActivity {
    TextInputEditText editTexturl,editTextchange;
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        editTexturl = findViewById(R.id.editTexturl);
        update=findViewById(R.id.buttonSubmit);
        editTextchange = findViewById(R.id.editTextchangeurl);

        getUrl();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextchange!=null){
                    setUrl();
                }
                else{
                    editTextchange.setError("Please Enter Url");
                }
            }
        });

    }
    void getUrl()
    {
        OkHttpClient client = new OkHttpClient();

        // Replace "http://your_server/get_url.php" with the actual URL of your PHP script
        Request request = new Request.Builder()
                .url("http://rtsregistration.in/php/web_view.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    final String url = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTexturl.setText(url);
                        }
                    });
                } else {
                    // Handle the case when the URL retrieval failed
                    // You can display an error message or take appropriate action
                    final String errorMessage = "Failed to retrieve URL: " + response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(web_view.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the case when the request failed
                // You can display an error message or take appropriate action
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("failed to make request"+e.getMessage());
                        Toast.makeText(web_view.this, "Failed to make request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    void setUrl()
    {
        OkHttpClient client = new OkHttpClient();

        // Replace "http://your_server/get_url.php" with the actual URL of your PHP script
        Request request = new Request.Builder()
                .url("http://rtsregistration.in/php/web_view_update.php?new_url="+editTextchange.getText().toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    final String url = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getUrl();
                            editTextchange.setText("");
                        }
                    });
                } else {
                    // Handle the case when the URL retrieval failed
                    // You can display an error message or take appropriate action
                    final String errorMessage = "Failed to Update URL: " + response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(web_view.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the case when the request failed
                // You can display an error message or take appropriate action
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("failed to make request"+e.getMessage());
                        Toast.makeText(web_view.this, "Failed to make request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}