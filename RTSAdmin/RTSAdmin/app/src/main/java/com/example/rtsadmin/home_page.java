package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Find the Web View button by its ID
        Button webViewButton = findViewById(R.id.button1);
        Button userRequest = findViewById(R.id.button2);
        Button userList = findViewById(R.id.button3);
        Button productList = findViewById(R.id.button4);
        Button verifyPayment = findViewById(R.id.button5);
        Button paymentList = findViewById(R.id.button6);

        // Set OnClickListener to the Web View button
        webViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, web_view.class);
                startActivity(intent);
            }
        });
        userRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, user_accept.class);
                startActivity(intent);
            }
        });
        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, user_list.class);
                startActivity(intent);
            }
        });
        productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, show_products.class);
                startActivity(intent);
            }
        });
        verifyPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, VerifyPayments.class);
                startActivity(intent);
            }
        });
        paymentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, HomePayments.class);
                startActivity(intent);
            }
        });
    }
}