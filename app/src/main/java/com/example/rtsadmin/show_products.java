package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

public class show_products extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button btnshow, btnshowall,btnshow2;
    private TextView editTextDate;
    private TextInputEditText editTextId2;
    JSONArray jsonArray;

    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        new FetchDataTaskall().execute();
        tableLayout = findViewById(R.id.tableLayout);
        editTextDate = findViewById(R.id.editTextDate);
        btnshow = findViewById(R.id.btnShow);
        btnshow2 = findViewById(R.id.btnShow2);
//        btnshowall=findViewById(R.id.btnShowAll);

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
                    Toast.makeText(show_products.this,"Please Provide Date",Toast.LENGTH_SHORT).show();
                }
                editTextDate.setText("");

            }

        });

//        btnshowall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new FetchDataTaskall().execute();
//            }
//        });

        btnshow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextId2 = findViewById(R.id.editTextId2);

                try {
                    String id = editTextId2.getText().toString();
                    int iid = Integer.parseInt(id);

                    if(checkId(id))
                    {
                        JSONObject jsonObject =  getJsonObject(id);
                        if(jsonObject!=null)
                        {
                            editTextId2.setText("");
                            Intent intent = new Intent(show_products.this,show_products_id.class);
                            intent.putExtra("jsonObject",jsonObject.toString());
                            startActivity(intent);
                        }else{
                            Toast.makeText(show_products.this, "Not Found", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(show_products.this, "Id Does't match", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e)
                {
                    Toast.makeText(show_products.this, "Please Provide valid id", Toast.LENGTH_SHORT).show();
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
                URL url = new URL("http://rtsregistration.in/php/rtsfetchbydate.php?date="+date);
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
                        Toast.makeText(show_products.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        jsonArray = new JSONArray(result);
                        // clear all rows data from table
                        tableLayout.removeAllViews();
                        setColumn();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Id = jsonObject.getString("id");
                            String ownerName = jsonObject.getString("owner_name");
                            String contact = jsonObject.getString("owner_contact");
                            String pay = jsonObject.getString("pay");
                            if(pay.equalsIgnoreCase("true"))
                            {
                                pay="Paid";
                            }else{
                                pay="Not Paid";
                            }
                            String regDate = jsonObject.getString("reg_date");

                            addRowToTable(Id, ownerName, contact, pay,regDate);
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
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://rtsregistration.in/php/rtsfetchall.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                } else {
                    result = "Server returned non-OK response: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "Error fetching data: " + e.getMessage();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
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
                        Toast.makeText(show_products.this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("iiiiiiiiiiii"+result);
                        jsonArray = new JSONArray(result);
                        // clear all rows data from table
                        tableLayout.removeAllViews();
                        setColumn();


                        for (int i = 0; i < jsonArray.length(); i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Id = jsonObject.getString("id");
                            String ownerName = jsonObject.getString("owner_name");
                            String contact = jsonObject.getString("owner_contact");
                            String pay = jsonObject.getString("pay");
                            if(pay.equalsIgnoreCase("true"))
                            {
                                pay="Paid";
                            }else{
                                pay="Not Paid";
                            }
                            String regDate = jsonObject.getString("reg_date");

                            addRowToTable(Id, ownerName, contact, pay,regDate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FetchDataTaskall", "Error: " + e.getMessage());

                }
            }
        }

    }
    private void addRowToTable(String ID, String ownerName, String contact, String pay,String regDate) {
        TableRow row = new TableRow(this);

        TextView tvId = createTextView(ID);
        TextView tvOwnerName = createTextView(ownerName);
        TextView tvcontact = createTextView(contact);
        TextView tvpay = createTextView(pay);
        TextView tvregdate = createTextView(regDate);


        row.addView(tvId);
        row.addView(tvOwnerName);
        row.addView(tvcontact);
        row.addView(tvpay);
        row.addView(tvregdate);


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

        TextView tvNameId = createTextView("Product ID");
        TextView tvUserId = createTextView("Owner");
        TextView tvUserName = createTextView("Owner Contact");
        TextView tvMobile = createTextView("Pay");
        TextView tvdate = createTextView("Register Date");

        row.addView(tvNameId);
        row.addView(tvUserId);
        row.addView(tvUserName);
        row.addView(tvMobile);
        row.addView(tvdate);

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
    JSONObject getJsonObject(String id) {
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
