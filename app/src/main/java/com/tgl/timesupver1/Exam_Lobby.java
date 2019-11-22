package com.tgl.timesupver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Exam_Lobby extends AppCompatActivity {

    int time;
    String ExamCode;
    String ExamName;
    String duration;
    private TextView title;
    private TextView starttime;
    private TextView examduration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam__lobby);

        title = findViewById(R.id.textView14);
        starttime = findViewById(R.id.textView20);
        examduration = findViewById(R.id.textView21);

        Intent i = new Intent();
        time = i.getIntExtra("time",time);
        ExamCode = i.getStringExtra("code");
        ExamName = i.getStringExtra("title");
        duration = i.getStringExtra("duration");

        title.setText(ExamName);
        String am_pm;
        String newTime = Integer.toString(time);
        String hour = newTime.substring(0,newTime.length()-2);
        String mins = newTime.substring(2);
        if(time>1159&&time<1300){
            am_pm="PM";
        }else if(time>1299){
            am_pm="PM";
            int temp = Integer.parseInt(hour);
            temp = temp-12;
            hour = Integer.toString(temp);
        }else{
            am_pm = "AM";
        }

        starttime.setText(hour+":"+mins+am_pm);
        examduration.setText(duration);
    }


}
