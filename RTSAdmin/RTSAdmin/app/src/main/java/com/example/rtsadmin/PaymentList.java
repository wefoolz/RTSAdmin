package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentList extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button btnshow,btnshowall;
    private TextView editTextDate;
    private TextInputEditText editTextId2;
    JSONArray jsonArray;

    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        tableLayout = findViewById(R.id.tableLayout);
        editTextDate = findViewById(R.id.editTextDate);
        btnshow = findViewById(R.id.btnShow);
        btnshowall = findViewById(R.id.btnShowAll);


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Call AsyncTask to fetch data from server API


        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = editTextDate.getText().toString();
                if(!date.isEmpty())
                {
                    new FetchDataTask().execute();
                }else
                {
                    Toast.makeText(PaymentList.this,"Please Provide Date",Toast.LENGTH_SHORT).show();
                }
                editTextDate.setText("");

            }

        });
        btnshowall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchDataTaskall().execute();
            }
        });

    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                // Replace "YOUR_API_ENDPOINT" with the actual API endpoint
                URL url = new URL("http://rtsregistration.in/php/rtspaymentsfetch.php?date="+date);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Check if the result contains the echo message
                    if (result.startsWith("0 results")) {
                        // Display the echo message in your Android app
                        Toast.makeText(PaymentList.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        jsonArray = new JSONArray(result);
                        // clear all rows data from table
                        tableLayout.removeAllViews();
                        setColumn();


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String pay = jsonObject.getString("pay");
                            if(pay.equalsIgnoreCase("true"))
                            {
                                pay="Paid";
                            }else{
                                pay="Not Paid";
                            }
                            String pid = jsonObject.getString("pid");
                            String date = jsonObject.getString("date");


                            addRowToTable(id, email, mobile,pay, pid, date);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private class FetchDataTaskall extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                // Replace "YOUR_API_ENDPOINT" with the actual API endpoint
                URL url = new URL("http://rtsregistration.in/php/rtspaymentsfetchall.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Check if the result contains the echo message
                    if (result.startsWith("0 results")) {
                        // Display the echo message in your Android app
                        Toast.makeText(PaymentList.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        jsonArray = new JSONArray(result);
                        // clear all rows data from table
                        tableLayout.removeAllViews();
                        setColumn();


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String pay = jsonObject.getString("pay");
                            if(pay.equalsIgnoreCase("true"))
                            {
                                pay="Paid";
                            }else{
                                pay="Not Paid";
                            }
                            String pid = jsonObject.getString("pid");
                            String date = jsonObject.getString("date");


                            addRowToTable(id, email, mobile,pay, pid, date);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addRowToTable(String id, String email, String mobile,String pay, String pid, String date) {
        TableRow row = new TableRow(this);

        TextView Id = createTextView(id);
        TextView Email = createTextView(email);
        TextView Mobile = createTextView(mobile);
        TextView Pay = createTextView(pay);
        TextView Pid = createTextView(pid);
        TextView Date = createTextView(date);

        row.addView(Id);
        row.addView(Email);
        row.addView(Mobile);
        row.addView(Pay);
        row.addView(Pid);
        row.addView(Date);


        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setBackgroundResource(R.drawable.cell_border_shape);
        return textView;
    }

    private void setColumn() {
        TableRow row = new TableRow(this);

        TextView Id = createTextView("ID");
        TextView Email = createTextView("Email");
        TextView Mobile = createTextView("Mobile");
        TextView pay =createTextView("Pay");
        TextView ProductId = createTextView("Product ID");
        TextView Date = createTextView("Date");


        row.addView(Id);
        row.addView(Email);
        row.addView(Mobile);
        row.addView(pay);
        row.addView(ProductId);
        row.addView(Date);

        tableLayout.addView(row);
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editTextDate.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }


}
