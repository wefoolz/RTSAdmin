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

public class user_list extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button btnRemove;
    private TextInputEditText editTextUserId;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        tableLayout = findViewById(R.id.tableLayout);

        // Call AsyncTask to fetch data from server API
        new FetchDataTask().execute();
        btnRemove = findViewById(R.id.btnRemove);
        editTextUserId = findViewById(R.id.editTextUserId);
        //user remove button
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = editTextUserId.getText().toString();
                if(!userId.isEmpty())
                {
                    if(checkId(userId))
                    {
                        removeUser(userId);
                    }
                    else{
                        Toast.makeText(user_list.this,"Please Enter Valid id",Toast.LENGTH_SHORT).show();
                    }

                }else
                {
                    Toast.makeText(user_list.this,"Please Provide Id",Toast.LENGTH_SHORT).show();
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
                URL url = new URL("http://rtsregistration.in/php/rtsuserlist.php");
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
                        Toast.makeText(user_list.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        jsonArray = new JSONArray(result);
                        // iwant clear all rows data from table
                        tableLayout.removeAllViews();
                        setColumn();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nameId = jsonObject.getString("id");
                            String userId = jsonObject.getString("username");
                            String userName = jsonObject.getString("customername");
                            String mobile = jsonObject.getString("mobilenumber");
                            String address = jsonObject.getString("address");
                            String pincode = jsonObject.getString("pincode");
                            String landmark = jsonObject.getString("landmark");
                            String email = jsonObject.getString("emailid");

                            addRowToTable(nameId, userId, userName, mobile, address, pincode, landmark, email);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addRowToTable(String nameId, String userId, String userName, String mobile, String address, String pincode, String landmark, String email) {
        TableRow row = new TableRow(this);

        TextView tvNameId = createTextView(nameId);
        TextView tvUserId = createTextView(userId);
        TextView tvUserName = createTextView(userName);
        TextView tvMobile = createTextView(mobile);
        TextView tvAddress = createTextView(address);
        TextView tvPincode = createTextView(pincode);
        TextView tvLandmark = createTextView(landmark);
        TextView tvEmail = createTextView(email);


        row.addView(tvNameId);
        row.addView(tvUserId);
        row.addView(tvUserName);
        row.addView(tvMobile);
        row.addView(tvAddress);
        row.addView(tvPincode);
        row.addView(tvLandmark);
        row.addView(tvEmail);

        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setBackgroundResource(R.drawable.cell_border_shape);
        return textView;
    }
    void removeUser(String id)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://rtsregistration.in/php/RejectUser.php?id=" + id)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            if (responseString.equalsIgnoreCase("User Rejected")) {
                Toast.makeText(user_list.this, "User Removed", Toast.LENGTH_SHORT).show();
                // Clear EditText fields if needed
                editTextUserId.setText("");
                new FetchDataTask().execute();
            } else {
                Toast.makeText(user_list.this, "User Not Removed", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(user_list.this, "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void setColumn() {
        TableRow row = new TableRow(this);

        TextView tvNameId = createTextView("ID");
        TextView tvUserId = createTextView("Username");
        TextView tvUserName = createTextView("Customer Name");
        TextView tvMobile = createTextView("Mobile");
        TextView tvAddress = createTextView("Address");
        TextView tvPincode = createTextView("Pincode");
        TextView tvLandmark = createTextView("Landmark");
        TextView tvEmail = createTextView("Email");

        row.addView(tvNameId);
        row.addView(tvUserId);
        row.addView(tvUserName);
        row.addView(tvMobile);
        row.addView(tvAddress);
        row.addView(tvPincode);
        row.addView(tvLandmark);
        row.addView(tvEmail);

        tableLayout.addView(row);
    }
    boolean checkId(String id) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nameId = jsonObject.getString("id");
                if (nameId.equalsIgnoreCase(id))
                {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
