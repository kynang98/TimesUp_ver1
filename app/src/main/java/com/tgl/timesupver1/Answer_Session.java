package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class Answer_Session extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    LinkedList<Integer> selected_ans;
    TextView question;
    RadioGroup rad;
    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;
    Button btn_next;
    Button btn_prev;
    Button btn_submit;
    String current_no;
    String question_title;
    User user;
    LinkedList<String> answer_sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer__session);
        question = findViewById(R.id.question);
        rad = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        mDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        selected_ans = new LinkedList<>();
        answer_sheet = new LinkedList<>();
        current_no = intent.getStringExtra("question_no");
        question_title = intent.getStringExtra("code");
        Bundle b = intent.getExtras();
        user = (User) b.getSerializable("object");
        if(intent.getStringArrayExtra("answer_list") != null){
            String[] ans_list = intent.getStringArrayExtra("answer_list");
            String[] ans_sheet = intent.getStringArrayExtra("answer_sheet");
            for(int i = 0; i< ans_list.length; i++){
                selected_ans.add(Integer.parseInt(ans_list[i]));
                answer_sheet.add(ans_sheet[i]);
            }
            if(selected_ans.size() >= Integer.parseInt(current_no)) {
                if (selected_ans.get(Integer.parseInt(current_no) - 1) == findViewById(R.id.option1).getId()) {
                    rad.check(selected_ans.get(Integer.parseInt(current_no) - 1));
                } else if (selected_ans.get(Integer.parseInt(current_no) - 1) == findViewById(R.id.option2).getId()) {
                    rad.check(selected_ans.get(Integer.parseInt(current_no) - 1));
                } else if (selected_ans.get(Integer.parseInt(current_no) - 1) == findViewById(R.id.option3).getId()) {
                    rad.check(selected_ans.get(Integer.parseInt(current_no) - 1));
                } else if (selected_ans.get(Integer.parseInt(current_no) - 1) == findViewById(R.id.option4).getId()) {
                    rad.check(selected_ans.get(Integer.parseInt(current_no) - 1));
                }
            }
        }

        mRef = mDatabase.getReference("Questions/"+question_title+"/"+current_no);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int i = 0;
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        if(i == 0){
                            current_no = d.getValue().toString();
                        }else if (i == 1){
                            option1.setText(d.getValue().toString());
                        }else if( i == 2){
                            option2.setText(d.getValue().toString());
                        }else if( i == 3){
                            option3.setText(d.getValue().toString());
                        }else if( i == 4){
                            option4.setText(d.getValue().toString());
                        }else if( i == 6){
                            question.setText(d.getValue().toString());
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
                if(Integer.parseInt(current_no) != 8) {
                    if (selected_ans.size() < Integer.parseInt(current_no)) {
                        selected_ans.add(rad.getCheckedRadioButtonId());
                        if (rad.getCheckedRadioButtonId() == option1.getId()) {
                            answer_sheet.add(option1.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option2.getId()) {
                            answer_sheet.add(option2.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option3.getId()) {
                            answer_sheet.add(option3.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option4.getId()) {
                            answer_sheet.add(option4.getText().toString());
                        } else {
                            answer_sheet.add("");
                        }
                    } else if (selected_ans.size() >= Integer.parseInt(current_no)) {
                        selected_ans.set(Integer.parseInt(current_no) - 1, rad.getCheckedRadioButtonId());
                        if (rad.getCheckedRadioButtonId() == option1.getId()) {
                            answer_sheet.set(Integer.parseInt(current_no) - 1, option1.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option2.getId()) {
                            answer_sheet.set(Integer.parseInt(current_no) - 1, option2.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option3.getId()) {
                            answer_sheet.set(Integer.parseInt(current_no) - 1, option3.getText().toString());
                        } else if (rad.getCheckedRadioButtonId() == option4.getId()) {
                            answer_sheet.set(Integer.parseInt(current_no) - 1, option4.getText().toString());
                        } else {
                            answer_sheet.set(Integer.parseInt(current_no) - 1, "");
                        }

                    }

                    current_no = String.valueOf(Integer.parseInt(current_no) + 1);
                    Intent redirect1 = new Intent(Answer_Session.this, Answer_Session.class);
                    String[] ans_list = new String[selected_ans.size()];
                    for (int i = 0; i < ans_list.length; i++) {
                        ans_list[i] = selected_ans.get(i).toString();
                    }
                    String[] ans_sheet = new String[selected_ans.size()];
                    for (int i = 0; i < ans_sheet.length; i++) {
                        ans_sheet[i] = answer_sheet.get(i);
                    }
                    redirect1.putExtra("question_no", current_no);
                    redirect1.putExtra("answer_list", ans_list);
                    redirect1.putExtra("answer_sheet", ans_sheet);
                    redirect1.putExtra("code", question_title);
                    redirect1.putExtra("object", user);
                    startActivity(redirect1);
                }else{
                    Toast.makeText(Answer_Session.this, "This is the last question.", Toast.LENGTH_LONG).show();
                }
            }
        });


        // prev button
        btn_prev = findViewById(R.id.btn_prev);
        if(current_no != "1") {
            btn_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!current_no.equals("1")) {
                        if(selected_ans.size() > Integer.parseInt(current_no)){
                            selected_ans.set(Integer.parseInt(current_no) -1,rad.getCheckedRadioButtonId());
                            if(rad.getCheckedRadioButtonId() == option1.getId()){
                                answer_sheet.set(Integer.parseInt(current_no)-1, option1.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option2.getId()){
                                answer_sheet.set(Integer.parseInt(current_no)-1, option2.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option3.getId()){
                                answer_sheet.set(Integer.parseInt(current_no)-1, option3.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option4.getId()){
                                answer_sheet.set(Integer.parseInt(current_no)-1, option4.getText().toString());
                            }else{
                                answer_sheet.set(Integer.parseInt(current_no)-1,"");
                            }
                        }else if(selected_ans.size() <= Integer.parseInt((current_no))){
                            selected_ans.add(rad.getCheckedRadioButtonId());
                            if(rad.getCheckedRadioButtonId() == option1.getId()){
                                answer_sheet.add(option1.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option2.getId()){
                                answer_sheet.add(option2.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option3.getId()){
                                answer_sheet.add(option3.getText().toString());
                            }else if(rad.getCheckedRadioButtonId() == option4.getId()){
                                answer_sheet.add(option4.getText().toString());
                            }else{
                                answer_sheet.add("");
                            }
                        }
                        current_no = String.valueOf(Integer.parseInt(current_no) - 1);
                        Intent redirect2 = new Intent(Answer_Session.this, Answer_Session.class);
                        String[] ans_list = new String[selected_ans.size()];
                        for( int i = 0; i < ans_list.length; i++){
                            ans_list[i] = selected_ans.get(i).toString();
                        }
                        String[] ans_sheet = new String[selected_ans.size()];
                        for( int i = 0; i < ans_sheet.length; i++){
                            ans_sheet[i] = answer_sheet.get(i);
                        }
                        redirect2.putExtra("question_no", current_no);
                        redirect2.putExtra("answer_list", ans_list);
                        redirect2.putExtra("answer_sheet", ans_sheet);
                        redirect2.putExtra("object", user);
                        redirect2.putExtra("code", question_title);
                        startActivity(redirect2);
                    }else{
                        Toast.makeText(Answer_Session.this, "This is the first question.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        // submit button
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (selected_ans.size() < Integer.parseInt(current_no)) {
                    selected_ans.add(rad.getCheckedRadioButtonId());
                    if (rad.getCheckedRadioButtonId() == option1.getId()) {
                        answer_sheet.add(option1.getText().toString());
                    } else if (rad.getCheckedRadioButtonId() == option2.getId()) {
                        answer_sheet.add(option2.getText().toString());
                    } else if (rad.getCheckedRadioButtonId() == option3.getId()) {
                        answer_sheet.add(option3.getText().toString());
                    } else if (rad.getCheckedRadioButtonId() == option4.getId()) {
                        answer_sheet.add(option4.getText().toString());
                    } else {
                        answer_sheet.add("");
                    }
                }
                    String[] ans_sheet = new String[selected_ans.size()];
                    for( int i = 0; i < ans_sheet.length; i++){
                        ans_sheet[i] = answer_sheet.get(i);
                    }
                Intent redirect3 = new Intent(Answer_Session.this, Candidate_Home.class);
                    redirect3.putExtra("answer_sheet", ans_sheet);
                    redirect3.putExtra("object", user);
                    redirect3.putExtra("code", question_title);
                startActivity(redirect3);

            }
        });
    }

    public void onBackPressed(){
        AlertDialog alertDialog = new AlertDialog.Builder(Answer_Session.this).create();
        alertDialog.setTitle("Closing");
        alertDialog.setMessage("Are you sure!?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        ActivityCompat.finishAffinity(Answer_Session.this);
                        System.exit(0);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
