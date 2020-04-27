package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    Context context;
    File file;
    Uri uri;
    Bitmap photo =null;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //On indique le chemin de sauvegarde temporaire du fichier photo
        File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        context = this;

        Button btnCamera = findViewById(R.id.btnCamera);
        File photofile = new File(photostorage, "temp.jpg");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On vérifie s'il y a bien une caméra sur le téléphone
                if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                {

                    //Mode de fonctionnement de la caméra
                    //On n'instancie pas une activity mais l'objet capture de la caméra
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                        if(intent.resolveActivity(getPackageManager()) != null)
                        {
                            //On reste sur la même activity, on active la camera par dessus
                            startActivityForResult(intent, 100);
                        }
                }

            else
                {
                    //Message tél n'a pas de caméra
                }
            }

        });

        Button btnSavePhoto = findViewById(R.id.btnSavePhoto);

        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtNomPhoto = findViewById(R.id.txtNomPhoto);
                //Règle conventionnelle : remplacer les espaces par des _
                String nomPhoto = txtNomPhoto.getText().toString().trim().toLowerCase().replace(' ','_');

                if(photo != null && !nomPhoto.isEmpty())
                {
                    try
                    {
                        path = SavePhoto(photo,nomPhoto + ".jpg");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button btnVoirPhotoSaved = findViewById(R.id.btnVoirPhotoSaved);

        btnVoirPhotoSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewPhotoActivity.class);
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bitmap photo = null;

        if(requestCode == 100 & resultCode == RESULT_OK) //mode camera
        {
            if (intent != null && intent.hasExtra("data")) {
                Bundle extras = intent.getExtras();
                photo = (Bitmap) extras.get("data");

                if(photo != null)
                {
                    ImageView imgPhoto = findViewById(R.id.imgPhoto);
                    imgPhoto.setImageBitmap(photo);
                }
            }
        }
        else
        {

        }
    }

    private String SavePhoto(Bitmap bitmap,String nomPhoto) throws IOException {

        //Répertoire ou l'on sauvegarde l'image
        File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //fichier du nom de la photo
        File path = new File(photostorage, nomPhoto);

        FileOutputStream fos = new FileOutputStream(path);
        //enregistre la photo en jpg
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        //on ferme le fichier
        fos.close();
        //on retourne le chemin
        return path.getParent();
    }

    private Bitmap loadPhoto(String path,String photoName)
    {

        try {
            File f = new File(path, photoName);
            photo = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (Exception e)
        {
            Log.e("Tag",e.getMessage());
        }
        return photo;
    }
}
