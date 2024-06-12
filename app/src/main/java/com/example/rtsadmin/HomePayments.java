package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePayments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_payments);
        Button paymentlist = findViewById(R.id.button1);
        Button unpaidlist = findViewById(R.id.button2);
        Button rejectedlist = findViewById(R.id.button3);

        paymentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePayments.this, PaymentList.class);
                startActivity(intent);
            }
        });

        unpaidlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePayments.this, UnpaidList.class);
                startActivity(intent);
            }
        });
        rejectedlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePayments.this, RejectedList.class);
                startActivity(intent);
            }
        });
    }
}