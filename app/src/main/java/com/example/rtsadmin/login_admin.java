package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
public class login_admin extends AppCompatActivity {

    private TextInputEditText editTextUsername; // Added EditText for username
    private TextInputEditText editTextPassword;
    private CheckBox checkBoxShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        editTextUsername = findViewById(R.id.editTextUsername); // Initialized editTextUsername
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);

        checkBoxShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    // Hide password
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        // Get the TextView for "Forgot Password?"
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Set a click listener for the TextView
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the forgot_password activity
                Intent intent = new Intent(login_admin.this, forgot_password.class);
                startActivity(intent);
            }
        });

        // Get the Login Button
        Button btnLogin = findViewById(R.id.btnLogin);

        // Set a click listener for the Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract data from EditText fields
                String name = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if(!name.isEmpty()&&!password.isEmpty())
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://rtsregistration.in/php/loginadmin.php?username=" + name +
                                    "&password=" + password)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        System.out.println(responseString);

                        if (responseString.equalsIgnoreCase("Login Successful")) {
                            Toast.makeText(login_admin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            // Clear EditText fields if needed
                            editTextUsername.setText(""); // Clear username
                            editTextPassword.setText(""); // Clear password
                            Intent intent = new Intent(login_admin.this, home_page.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(login_admin.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                        Toast.makeText(login_admin.this, "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(login_admin.this, "Enter Username or Password!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}