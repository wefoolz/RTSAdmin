package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class UnpaidList extends AppCompatActivity {

    private TableLayout tableLayout;
    private TextInputEditText editTextId2;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_list);
        tableLayout = findViewById(R.id.tableLayout);
        new UnpaidList.FetchDataTask().execute();

    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                // Replace "YOUR_API_ENDPOINT" with the actual API endpoint
                URL url = new URL("http://rtsregistration.in/php/rtsunpaidfetch.php");
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
                        Toast.makeText(UnpaidList.this, result, Toast.LENGTH_SHORT).show();
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
}