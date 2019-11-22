package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Answer_Session extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    QuestionPaper qp = new QuestionPaper();
    TextView question;
    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;
    Button btn_next;
    Button btn_prev;
    Button btn_submit;
    String current_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer__session);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        mDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        current_no = intent.getStringExtra("question_no");
        mRef = mDatabase.getReference("Questions/Form 3 Geografi/"+current_no);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int i = 0;
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        if(i == 0){
                            current_no = d.getValue().toString();
                        }else if (i == 1){
                            question.setText(d.getValue().toString());
                        }else if( i == 2){
                            option1.setText(d.getValue().toString());
                        }else if( i == 3){
                            option2.setText(d.getValue().toString());
                        }else if( i == 4){
                            option3.setText(d.getValue().toString());
                        }else if( i == 5){
                            option4.setText(d.getValue().toString());
                        }else{

                        }
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //next button
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                current_no = String.valueOf(Integer.parseInt(current_no)+1);
                Intent redirect = new Intent(Answer_Session.this, Answer_Session.class);
                redirect.putExtra("question_no", current_no);
                startActivity(redirect);
            }
        });


        // prev button
        btn_prev = findViewById(R.id.btn_prev);
        if(current_no != "1") {
            btn_prev.setVisibility(View.VISIBLE);
            btn_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!current_no.equals("1")) {
                        current_no = String.valueOf(Integer.parseInt(current_no) - 1);
                        Intent redirect = new Intent(Answer_Session.this, Answer_Session.class);
                        redirect.putExtra("question_no", current_no);
                        startActivity(redirect);
                    }else{
                        Toast.makeText(Answer_Session.this, "This is the first question.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


        // submit button
    }
}
