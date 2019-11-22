package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    EditText emailID, password;
    Button btnSignUp;
    TextView tvSignIn;
    private DatabaseReference firebase;
    Button bleBtn;

    int PERMISSION_REQUEST_CODE = 1;
    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailID = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_pw);
        btnSignUp = findViewById(R.id.signup_signup);
        tvSignIn = findViewById(R.id.signup_signin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }


        btnSignUp.setOnClickListener(new View.OnClickListener(){
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
                else if(!(email.isEmpty()) && !(pwd.isEmpty())){
                    firebase = FirebaseDatabase.getInstance().getReference("Users/Students");
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
                                        Toast.makeText(MainActivity.this, "Unable to read", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (pass.equals(pwd)) {
                                            Intent i = new Intent(MainActivity.this, Candidate_Home.class);
                                            i.putExtra("object", user);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Wrong password, please try again.", Toast.LENGTH_SHORT).show();
                                            password.setText("");
                                        }
                                    }
                                }
                            }else{
                                //firebase.child("")
                                Toast.makeText(MainActivity.this, "No such record, creating new account", Toast.LENGTH_SHORT).show();
                                String ID = firebase.push().getKey();
                                User user = new User("DEFAULT", email, pwd, "Asia Pacific University");
                                user.setFirebaseID(ID);
                                firebase.child(ID).setValue(user);
                                Intent i = new Intent(MainActivity.this, Candidate_Home.class);
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
                    Toast.makeText(MainActivity.this, "Unexpected error occurred, please restart your application.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(MainActivity.this, "Clicked on button.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        Intent intent = new Intent(MainActivity.this, Answer_Session.class);
        intent.putExtra("question_no", "1");
        String[] answers = null;
        intent.putExtra("answer_list", answers);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == PERMISSION_REQUEST_CODE)
        {
            //Do something based on grantResults
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "coarse location permission granted");
            }
            else
            {
                Log.d(TAG, "coarse location permission denied");
            }
        }
    }

    public void onBackPressed(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Closing");
        alertDialog.setMessage("You are about to close the app!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        ActivityCompat.finishAffinity(MainActivity.this);
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
