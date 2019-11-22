package com.tgl.timesupver1;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class QuestionPaper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    public LinkedList<Question> Questions = new LinkedList<Question>();

    QuestionPaper(){

    }

    public void getQuestions(){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Form 1 Geografi/1");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int i = 0;
                    Questions.add(new Question());
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        if(i == 0){
                            Questions.get(0).set_no(d.getKey());
                        }else if (i == 1){
                            Questions.get(0).set_question(d.getKey());
                        }else if( i == 6){
                            Questions.get(0).set_answer(d.getKey());
                            i = 0;
                        }else{
                            Questions.get(0).add_option(d.getKey());
                        }
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
