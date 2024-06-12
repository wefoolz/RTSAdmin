package com.example.rtsadmin;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullSizeImage extends AppCompatActivity {

    private Button download;
    private ImageView imageView;

    private static final int REQUEST_WRITE_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image);
        download = findViewById(R.id.download);
        imageView = findViewById(R.id.fullSizeImageView);

        // Retrieve the image bytes from ImageDataHolder
        byte[] imageBytes = ImageDataHolder.getInstance().getImageBytes();

        // Convert byte array to Bitmap
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(imageBitmap);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for permission before downloading the image
                checkStoragePermission();
            }
        });
    }

    private void checkStoragePermission() {
        // Check if the WRITE_EXTERNAL_STORAGE permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, download the image
            downloadImage();
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, download the image
                downloadImage();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadImage() {
        // Get the image bytes from ImageDataHolder
        byte[] imageBytes = ImageDataHolder.getInstance().getImageBytes();

        // Create a temporary file to store the image
        String fileName = "downloaded_image.jpg";
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            // Save the image bytes to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(imageBytes);
            outputStream.close();

            // Use DownloadManager to download the file
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            // Create a content:// URI for the temporary file
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Downloaded Image");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Use setDestinationInExternalPublicDir if you want to specify a different directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            downloadManager.enqueue(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}