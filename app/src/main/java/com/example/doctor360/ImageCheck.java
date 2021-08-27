package com.example.doctor360;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class ImageCheck extends AppCompatActivity {

    Button upload, view;
    ImageView pricture1;
    Uri imageUri;
    File file;
    String path;
    String filename;
    String filewithEx;
    private Bitmap bitmap;
    private static final int SELECT_PICTURE1 = 100;
    private static final String TAG = "ImageCheck";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_check);

        upload = findViewById(R.id.btnUpload);
        view = findViewById(R.id.btnView);
        pricture1 = findViewById(R.id.imgTest);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,SELECT_PICTURE1);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imageInByte=byteArrayOutputStream.toByteArray();

        String encodedImage= Base64.encodeToString(imageInByte,Base64.DEFAULT);
        Toasty.success(ImageCheck.this, "Image " + encodedImage, 300).show();
        Log.d(TAG, "uploadImage: " + encodedImage);
    }

   /* public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri path=data.getData();
        if(requestCode == SELECT_PICTURE1 && resultCode == RESULT_OK && data!=null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                pricture1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

