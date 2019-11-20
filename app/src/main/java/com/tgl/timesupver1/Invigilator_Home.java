package com.tgl.timesupver1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Invigilator_Home extends AppCompatActivity {
    StorageReference sRef;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invigilator_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        FloatingActionButton setupFloatActBtn;
        setupFloatActBtn = findViewById(R.id.setupFloatActBtn);

        setupFloatActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intoSetupExam = new Intent(Invigilator_Home.this, Invigilator_Setup_Exam.class);
                startActivity(intoSetupExam);
            }
        });

        sRef = FirebaseStorage.getInstance().getReference("profileImages/Testing.jpg");
        img = findViewById(R.id.imageView);
        GlideApp.with(this)
                .load(sRef)
                .circleCrop()
                .into(img);
       /* Button btn_uploadQuestion = findViewById(R.id.btn_upload_question);
        btn_uploadQuestion.setText("Upload Question");
        btn_uploadQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openNextActivity(new Invigilator_Upload_Question());
            }
        });

        Button btn_setExam = findViewById(R.id.btn_setExam);
        btn_setExam.setText("Setup Exam");
        btn_setExam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openNextActivity(new Invigilator_Setup_Exam());
            }
        });

        Button btn_viewResult = findViewById(R.id.btn_viewResult);
        btn_viewResult.setText("View Result");
        btn_viewResult.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               openNextActivity(new Invigilator_View_Result());
           }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setText("Log Out");
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openNextActivity(new MainActivity());
            }
        });


*/
    }

    public void openNextActivity(Activity page){

        Intent redirect = new Intent(this, page.getClass());
        startActivity(redirect);
    }

    public void onBackPressed(){
        Intent goBack = new Intent(Invigilator_Home.this, MainActivity.class);
        startActivity(goBack);
    }

}
