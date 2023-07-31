package com.example.translineassgement;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final String Tag = "ImageUploadActivity";
    String path;

    private ImageView imageView;
    Button chooseImage, UploadImage;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);
        UploadImage = findViewById(R.id.upload);
        // click the button  then call the   pickImageFromGallery  method
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        // click upload button  to upload the image  to the api
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToServer();
                } else {
                    Toast.makeText(MainActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImageToServer() {
        // Convert the imageUri to a File object
        File imageFile = new File(path);

        // Convert the File to a RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // Create a MultipartBody.Part from the RequestBody
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        // Create an instance of the API interface
        ServiceApi apiService = RetrofitClient.getInstance().create(ServiceApi.class);

        // Call the API method to upload the image
        Call<ResponseBody> call = apiService.uploadImage(imagePart);

        // Make the API request
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                // check this method for upload image is successful or not
                if (response.isSuccessful()) {


                    // Image uploaded successfully
                    Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle API error
                    Toast.makeText(MainActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }

            // uploaded image show the error in logcat
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle the failure
                Log.e(TAG, "onFailure: Image upload failed", t);
                Toast.makeText(MainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // set the selected image through gallery in tne imageView .

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
                Uri uri = data.getData();
                Context context = MainActivity.this;
                path = RealPathUtil.getRealPath(context, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    // Pick the image from  the gallery
    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
}