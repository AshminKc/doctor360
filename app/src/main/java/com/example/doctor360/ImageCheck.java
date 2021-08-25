package com.example.doctor360;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class ImageCheck extends AppCompatActivity {

    Button upload, view;
    ImageView pricture1;
    Uri imageUri;
    File file;
    String path;
    String filename;
    String filewithEx;
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
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, SELECT_PICTURE1);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = pricture1.toString();
                File file = new File(name1);
                String path = file.getPath();
                String fileN = path.substring(path.lastIndexOf("/")+1);
                String newFile = fileN.replace("}", ".");
                String fileWithExtension = newFile+"jpg";
                Toasty.success(ImageCheck.this,"Image " + fileWithExtension, 300).show();
                Log.d(TAG, "onClick: Image " + fileWithExtension);
            }
        });
    }

    public String getPath(Uri uri)
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PICTURE1 && resultCode == RESULT_OK){
            imageUri = data.getData();
            file = new File(imageUri.getPath());
            path = file.toString();
            filename=path.substring(path.lastIndexOf(":")+1);
            filewithEx = filename+".jpg";
            Picasso.with(ImageCheck.this).load(data.getData()).noPlaceholder().centerCrop().fit().into(pricture1);
           // Toasty.success(ImageCheck.this,"Image " + filewithEx, 300).show();
            //Log.d(TAG, "onActivityResult: Image " + filewithEx);
           // imgView.setImageURI(imageUri);

        }
    }

}

