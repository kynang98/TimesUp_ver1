package com.tgl.timesupver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Exam_Lobby extends AppCompatActivity {

    int time;
    String Time;
    String ExamCode;
    String ExamName;
    String duration;
    private TextView title;
    private TextView starttime;
    private TextView examduration;
    private DatabaseReference firebase;
    Button gotoanswer;
    User user;

    public void unsubscribe(MqttAndroidClient client){
        final String topic = "TimesUp/examStart";
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam__lobby);
        title = findViewById(R.id.textView14);
        starttime = findViewById(R.id.textView20);
        examduration = findViewById(R.id.textView21);
        gotoanswer = findViewById(R.id.button27);

        firebase = FirebaseDatabase.getInstance().getReference("Hall/Time's Up");
        Intent i = getIntent();
        Bundle b = i.getExtras();
        user = (User) b.getSerializable("object");


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ExamCode = dataSnapshot.child("ExamCode").getValue(String.class);
                    ExamName = dataSnapshot.child("ExamName").getValue(String.class);
                    duration = dataSnapshot.child("duration").getValue(String.class);
                    time = dataSnapshot.child("time").getValue(Integer.class);
                    title.setText(ExamName);
                    String am_pm;
                    String newTime = Integer.toString(time);
                    String hour = newTime.substring(0, newTime.length() - 2);
                    String mins = newTime.substring(newTime.length() - 2);
                    if (time > 1159 && time < 1300) {
                        am_pm = "PM";
                    } else if (time > 1299) {
                        am_pm = "PM";
                        int temp = Integer.parseInt(hour);
                        temp = temp - 12;
                        hour = Integer.toString(temp);
                    } else {
                        am_pm = "AM";
                    }
                    starttime.setText(hour + ":" + mins + am_pm);
                    examduration.setText(duration);

//                    String clientId = MqttClient.generateClientId();
//                    final MqttAndroidClient client =
//                            new MqttAndroidClient(Exam_Lobby.this, "tcp://broker.hivemq.com:1883",
//                                    clientId);
//
//                    try {
//                        MqttConnectOptions options = new MqttConnectOptions();
//                        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//                        IMqttToken token = client.connect(options);
//                        token.setActionCallback(new IMqttActionListener() {
//                            @Override
//                            public void onSuccess(IMqttToken asyncActionToken) {
//                                // We are connected
//                                //Toast.makeText(Invigilator_Setup_Exam.this, "MQTT success", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(Exam_Lobby.this, Answer_Session.class);
//                                i.putExtra("time", time);
//                                i.putExtra("code", ExamCode);
//                                i.putExtra("title", ExamName);
//                                i.putExtra("duration", duration);
//                                i.putExtra("object", user);
//                            }
//
//                            @Override
//                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                                Toast.makeText(Exam_Lobby.this, "Please connect to internet and try again.", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
//
//                    String topic = "TimesUp/examStart";
//                    int qos = 1;
//                    try {
//                        IMqttToken subToken = client.subscribe(topic, qos);
//                        subToken.setActionCallback(new IMqttActionListener() {
//                            @Override
//                            public void onSuccess(IMqttToken asyncActionToken) {
//                                Toast.makeText(Exam_Lobby.this, "MQTT SUCCESS", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onFailure(IMqttToken asyncActionToken,
//                                                  Throwable exception) {
//                                // The subscription could not be performed, maybe the user was not
//                                // authorized to subscribe on the specified topic e.g. using wildcards
//
//                            }
//                        });
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    System.out.println("lol");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Exam_Lobby.this,"Lol wtf", Toast.LENGTH_SHORT).show();
            }
        });


        gotoanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exam_Lobby.this, Answer_Session.class);
                i.putExtra("time", time);
                i.putExtra("code", ExamCode);
                i.putExtra("title", ExamName);
                i.putExtra("duration", duration);
                i.putExtra("object", user);
                startActivity(i);
            }
        });

    }



}
