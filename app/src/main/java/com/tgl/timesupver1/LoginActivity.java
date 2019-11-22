package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignIn;
    TextView tvSignUp;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailID = findViewById(R.id.login_email);
        password = findViewById(R.id.login_pw);
        btnSignIn = findViewById(R.id.login_signin);
        tvSignUp = findViewById(R.id.login_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                final String email = emailID.getText().toString();
                final String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailID.setError("Please enter Email");
                    emailID.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty()) && !(pwd.isEmpty())){
                    firebase = FirebaseDatabase.getInstance().getReference("Users/Invigilator");
                    firebase.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for(DataSnapshot data: dataSnapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    String ID = data.getKey();
                                    user.setFirebaseID(ID);
                                    String pass = data.child("password").getValue(String.class);
                                    if (pass.isEmpty()) {
                                        Toast.makeText(LoginActivity.this, "Unable to read", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (pass.equals(pwd)) {
                                            Intent i = new Intent(LoginActivity.this, Invigilator_Home.class);
                                            i.putExtra("object", user);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Wrong password, please try again.", Toast.LENGTH_SHORT).show();
                                            password.setText("");
                                        }
                                    }
                                }
                            }else{
                                //firebase.child("")
                                Toast.makeText(LoginActivity.this, "No such record, creating new account", Toast.LENGTH_SHORT).show();
                                String ID = firebase.push().getKey();
                                User user = new User("DEFAULT", email, pwd, "Asia Pacific University");
                                user.setFirebaseID(ID);
                                firebase.child(ID).setValue(user);
                                Intent i = new Intent(LoginActivity.this, Invigilator_Home.class);
                                i.putExtra("object", user);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Unexpected error occurred, please restart your application.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
