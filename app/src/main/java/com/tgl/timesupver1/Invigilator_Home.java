package com.tgl.timesupver1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Invigilator_Home extends AppCompatActivity {


    StorageReference sRef;
    ImageView img;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invigilator_home);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        user = (User) b.getSerializable("object");

        FloatingActionButton setupFloatActBtn;
        setupFloatActBtn = findViewById(R.id.setupFloatActBtn);

        setupFloatActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intoSetupExam = new Intent(Invigilator_Home.this, Invigilator_Setup_Exam.class);
                intoSetupExam.putExtra("object", user);
                startActivity(intoSetupExam);
            }
        });

        sRef = FirebaseStorage.getInstance().getReference("profileImages/Testing.jpg");
        img = findViewById(R.id.ProfileImageView);
        GlideApp.with(this)
                .load(sRef)
                .circleCrop()
                .into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEdit = new Intent(Invigilator_Home.this, EditProfile.class);
                gotoEdit.putExtra("object", user);
                startActivity(gotoEdit);
            }
        });


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
        redirect.putExtra("object", user);
        startActivity(redirect);
    }

    public void onBackPressed(){
        Intent goBack = new Intent(Invigilator_Home.this, MainActivity.class);
        startActivity(goBack);
    }

}
