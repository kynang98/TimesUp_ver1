package com.tgl.timesupver1;

import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class User {
    private StorageReference sRef;
    Image profileImage;

    User(){
        sRef = FirebaseStorage.getInstance().getReference("profileImages");
    }

    public void getProfileImage(ImageView img){

    }
}
