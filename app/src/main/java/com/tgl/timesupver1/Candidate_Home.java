package com.tgl.timesupver1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Candidate_Home extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate__home);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        user = (User) b.getSerializable("object");

        Button btn_viewResult = findViewById(R.id.btn_viewResult);
        btn_viewResult.setText("View Result");
        btn_viewResult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openNextActivity(null); // Open Student Result
            }
        });

        Button btn_connectExam = findViewById(R.id.btn_connectExam);
        btn_connectExam.setText("Connect/Join Exam");
        btn_connectExam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openNextActivity(new BleScanActivity());
            }
        });
    }

    public void openNextActivity(Activity page){

        Intent i = new Intent(Candidate_Home.this, page.getClass());
        i.putExtra("object", user);
        startActivity(i);
    }
}
