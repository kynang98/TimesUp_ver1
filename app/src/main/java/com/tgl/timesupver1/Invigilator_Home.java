package com.tgl.timesupver1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class Invigilator_Home extends AppCompatActivity {

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

}
