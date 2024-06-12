package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerifyPayments extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button btnPaid,btnUnpaid;
    private TextInputEditText editTextUserId;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payments);

        tableLayout = findViewById(R.id.tableLayout);

        // Call AsyncTask to fetch data from server API
        new FetchDataTask().execute();
        //accept the user and reject request
        btnPaid= findViewById(R.id.btnPaid);
        btnUnpaid = findViewById(R.id.btnUnpaid);
        editTextUserId = findViewById(R.id.editTextUserId);


        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paymentId = editTextUserId.getText().toString();
                if(!paymentId.isEmpty())
                {
                    JSONObject jsonObject = checkPaymentId(paymentId);

                    try {
                        if(jsonObject != null)
                        {
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String pay = jsonObject.getString("pay");
                            String pid = jsonObject.getString("pid");
                            String date = jsonObject.getString("date");
                            paid(id,email,mobile,pay,pid,date);
                        }
                        else{
                            Toast.makeText(VerifyPayments.this,"Please Enter Valid id",Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(VerifyPayments.this,"Error!!",Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                        }

                }else
                {
                    Toast.makeText(VerifyPayments.this,"Please Provide Id",Toast.LENGTH_SHORT).show();
                }

            }

        });
        btnUnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paymentId = editTextUserId.getText().toString();
                if(!paymentId.isEmpty())
                {
                    JSONObject jsonObject = checkPaymentId(paymentId);

                    try {
                        if(jsonObject != null)
                        {
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String pay = jsonObject.getString("pay");
                            String pid = jsonObject.getString("pid");
                            String date = jsonObject.getString("date");
                            unPaid(id,email,mobile,pay,pid,date);
                        }
                        else{
                            Toast.makeText(VerifyPayments.this,"Please Enter Valid id",Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(VerifyPayments.this,"Error!!",Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }

                }else
                {
                    Toast.makeText(VerifyPayments.this,"Please Provide Id",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                // Replace "YOUR_API_ENDPOINT" with the actual API endpoint
                URL url = new URL("http://rtsregistration.in/php/rtspaymentrequest.php");
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
                    if (result.startsWith("No results")) {
                        // Display the echo message in your Android app
                        Toast.makeText(VerifyPayments.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        jsonArray = new JSONArray(result);

                        tableLayout.removeAllViews();
                        setColumn();


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String pid = jsonObject.getString("pid");
                            String date = jsonObject.getString("date");


                            addRowToTable(id, email, mobile, pid, date);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addRowToTable(String id, String email, String mobile, String pid, String date) {
        TableRow row = new TableRow(this);

        TextView Id = createTextView(id);
        TextView Email = createTextView(email);
        TextView Mobile = createTextView(mobile);
        TextView Pid = createTextView(pid);
        TextView Date = createTextView(date);

        row.addView(Id);
        row.addView(Email);
        row.addView(Mobile);
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
    void paid(String id,String email,String mobile,String pay,String pid,String date)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://rtsregistration.in/php/rtspaid.php?id=" + id+"&email="+email+"&mobile="+mobile+"&pay="+pay+"&pid="+pid+"&date="+date)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();


                Toast.makeText(VerifyPayments.this, responseString, Toast.LENGTH_SHORT).show();
                // Clear EditText fields if needed
                editTextUserId.setText("");
                new FetchDataTask().execute();

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(VerifyPayments.this, "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void unPaid(String id,String email,String mobile,String pay,String pid,String date)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://rtsregistration.in/php/rtsunpaid.php?id=" + id+"&email="+email+"&mobile="+mobile+"&pay="+pay+"&pid="+pid+"&date="+date)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();


            Toast.makeText(VerifyPayments.this, responseString, Toast.LENGTH_SHORT).show();
            // Clear EditText fields if needed
            editTextUserId.setText("");
            new FetchDataTask().execute();

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(VerifyPayments.this, "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void setColumn() {
        TableRow row = new TableRow(this);

        TextView Id = createTextView("ID");
        TextView Email = createTextView("Email");
        TextView Mobile = createTextView("Mobile");
        TextView ProductId = createTextView("Product ID");
        TextView Date = createTextView("Date");


        row.addView(Id);
        row.addView(Email);
        row.addView(Mobile);
        row.addView(ProductId);
        row.addView(Date);

        tableLayout.addView(row);
    }
    JSONObject checkPaymentId(String id) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nameId = jsonObject.getString("id");
                if (nameId.equalsIgnoreCase(id))
                {
                    return jsonObject;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
