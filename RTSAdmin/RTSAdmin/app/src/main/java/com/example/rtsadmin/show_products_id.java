package com.example.rtsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class show_products_id extends AppCompatActivity {

    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products_id);



        // Creating a ScrollView and LinearLayout programmatically
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);

        jsonObject=null;
        String jsonObjectString = getIntent().getStringExtra("jsonObject");
        try {
            jsonObject = new JSONObject(jsonObjectString);
            // Now you have the JSONObject, jsonObject, use it as needed
            // Adding TextViews with data
            linearLayout.addView(createTextView("<b>ID:</b> "+jsonObject.getString("id")));
            linearLayout.addView(createTextView("<b>District:</b> "+jsonObject.getString("district")));
            linearLayout.addView(createTextView("<b>Tehsil:</b> "+jsonObject.getString("tahasil")));
            linearLayout.addView(createTextView("<b>Establishment Name:</b> "+jsonObject.getString("establishment_name")));
            linearLayout.addView(createTextView("<b>Establishment Date:</b> "+jsonObject.getString("est_date")));
            linearLayout.addView(createTextView("<b>Establishment Address:</b> "+jsonObject.getString("est_address")));
            linearLayout.addView(createTextView("<b>Establishment Landmark:</b> "+jsonObject.getString("est_landmark")));
            linearLayout.addView(createTextView("<b>Establishment Pincode:</b> "+jsonObject.getString("est_pincode")));
            linearLayout.addView(createTextView("<b>Establishment Email:</b> "+jsonObject.getString("est_email")));
            linearLayout.addView(createTextView("<b>Establishment Contact:</b> "+jsonObject.getString("est_contact")));
            linearLayout.addView(createTextView("<b>Owner Name:</b> "+jsonObject.getString("owner_name")));
            linearLayout.addView(createTextView("<b>Owner Address:</b> "+jsonObject.getString("owner_address")));
            linearLayout.addView(createTextView("<b>Owner Landmark:</b> "+jsonObject.getString("owner_landmark")));
            linearLayout.addView(createTextView("<b>Owner Pincode:</b> "+jsonObject.getString("owner_pincode")));
            linearLayout.addView(createTextView("<b>Owner Email:</b> "+jsonObject.getString("owner_email")));
            linearLayout.addView(createTextView("<b>Owner Contact:</b> "+jsonObject.getString("owner_contact")));
            linearLayout.addView(createTextView("<b>Register Date:</b> "+jsonObject.getString("reg_date")));
            linearLayout.addView(createTextView("<b>Male:</b> "+jsonObject.getString("male")));
            linearLayout.addView(createTextView("<b>Female:</b> "+jsonObject.getString("female")));
            linearLayout.addView(createTextView("<b>Other:</b> "+jsonObject.getString("other")));
            linearLayout.addView(createTextView("<b>Category Details:</b> "+jsonObject.getString("category_details")));
            if(jsonObject.getString("pay").equalsIgnoreCase("true"))
            {
                linearLayout.addView(createTextView("<b>Pay:</b> "+"Paid"));
            }else {
                linearLayout.addView(createTextView("<b>Pay:</b> "+"Unpaid"));
            }
            try {
                JSONObject images = getPhotos();

                if (images != null && images.length() >= 3) {

                    String photoBase64 = images.get("photo").toString();
                    String ownerIdBase64 = images.get("owner_id").toString();
                    String productPhotoBase64 = images.get("product_photo").toString();

                    // Decode base64 strings to byte arrays
                    byte[] photoBytes = Base64.decode(photoBase64, Base64.DEFAULT);
                    byte[] ownerIdBytes = Base64.decode(ownerIdBase64, Base64.DEFAULT);
                    byte[] productPhotoBytes = Base64.decode(productPhotoBase64, Base64.DEFAULT);

                    // Convert byte arrays to Bitmaps
                    Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                    Bitmap ownerIdBitmap = BitmapFactory.decodeByteArray(ownerIdBytes, 0, ownerIdBytes.length);
                    Bitmap productPhotoBitmap = BitmapFactory.decodeByteArray(productPhotoBytes, 0, productPhotoBytes.length);

                    // Create TextViews for labels
                    TextView textViewPhotoLabel = createTextView("Photo:");
                    TextView textViewOwnerIdLabel = createTextView("Owner ID Photo:");
                    TextView textViewProductPhotoLabel = createTextView("Product Photo:");

                    // Create ImageViews for images
                    ImageView imageViewPhoto = new ImageView(this);
                    imageViewPhoto.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    imageViewPhoto.setImageBitmap(photoBitmap);

                    ImageView imageViewOwnerId = new ImageView(this);
                    imageViewOwnerId.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    imageViewOwnerId.setImageBitmap(ownerIdBitmap);

                    ImageView imageViewProductPhoto = new ImageView(this);
                    imageViewProductPhoto.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    imageViewProductPhoto.setImageBitmap(productPhotoBitmap);

                    // Add TextViews and ImageViews to LinearLayout
                    linearLayout.addView(textViewPhotoLabel);
                    linearLayout.addView(imageViewPhoto);

                    linearLayout.addView(textViewOwnerIdLabel);
                    linearLayout.addView(imageViewOwnerId);

                    linearLayout.addView(textViewProductPhotoLabel);
                    linearLayout.addView(imageViewProductPhoto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
            }


            setContentView(scrollView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(Html.fromHtml(text));
        //textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }


    private JSONObject getPhotos() {
        try {
            String id = jsonObject.getString("id");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://rtsregistration.in/php/getPhotos.php?id=" + id)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            JSONObject images=null;
            String responseString = response.body().string();
                try {
                     images = new JSONObject(responseString);
                    // Proceed with further processing
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSONException
                    Toast.makeText(this, "Error parsing JSON ", Toast.LENGTH_SHORT).show();
                }
            return images;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(show_products_id.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
        return null;
    }



}
