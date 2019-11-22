package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity {

    Button btn_setImage;
    Button btn_uploadImage;
    Button DoneBtn;
    ImageView img;
    StorageReference sRef;
    public Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btn_setImage = findViewById(R.id.btn_setImage);
        btn_uploadImage = findViewById(R.id.btn_upload);
        DoneBtn = findViewById(R.id.DoneBtn);
        img = findViewById(R.id.img_profile);
        sRef = FirebaseStorage.getInstance().getReference("profileImages");
        btn_setImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                selectImage();
            }
        });

        btn_uploadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                uploadImage();
            }
        });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(EditProfile.this, Invigilator_Home.class);
                startActivity(goBack);
            }
        });
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }
    }

    public String getExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadImage(){
        StorageReference Ref = sRef.child("Testing"+"."+getExtention(imgUri));
        Ref.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(EditProfile.this, "Image uploaded successfully.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
}
