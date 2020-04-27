package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewPhotoActivity extends AppCompatActivity {

    String path = "";

    Button btnShowPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        Intent intent = getIntent();
        if(intent != null){
            path = intent.getStringExtra("path");
        }

        btnShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mEdtShowPicture = findViewById(R.id.txtNomPhoto);
                String nomPhoto = mEdtShowPicture.getText().toString().replace(' ', '_');

                if(!nomPhoto.isEmpty() && !path.isEmpty()){
                    ImageView imgPhoto = findViewById(R.id.imgPhoto);
                    Bitmap bitmap = null;
                    try {
                        bitmap = loadPhoto(path, nomPhoto);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imgPhoto.setImageBitmap(bitmap);
                }
            }
        });
    }

    private Bitmap loadPhoto(String path, String nomPhoto) throws FileNotFoundException {
        File file = new File(path, nomPhoto);
        return BitmapFactory.decodeStream(new FileInputStream(file));
    }
}