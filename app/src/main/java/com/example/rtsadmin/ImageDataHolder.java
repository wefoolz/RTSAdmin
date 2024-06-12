package com.example.rtsadmin;

public class ImageDataHolder {
    private static ImageDataHolder instance;
    private byte[] imageBytes;

    private ImageDataHolder() {
        // Private constructor to prevent instantiation
    }

    public static ImageDataHolder getInstance() {
        if (instance == null) {
            instance = new ImageDataHolder();
        }
        return instance;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
