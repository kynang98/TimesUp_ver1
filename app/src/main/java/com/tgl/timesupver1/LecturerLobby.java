package com.tgl.timesupver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class LecturerLobby extends AppCompatActivity {
    int time;
    String ExamCode;
    String ExamName;
    String duration;
    Button startTest;
    private TextView title;
    private TextView starttime;
    private TextView examduration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_lobby);
        startTest = findViewById(R.id.button10);
        title = findViewById(R.id.textView16);
        starttime = findViewById(R.id.textView19);
        examduration = findViewById(R.id.textView24);


        Intent i = getIntent();
        time = i.getIntExtra("time",time);
        ExamCode = i.getStringExtra("code");
        ExamName = i.getStringExtra("title");
        duration = i.getStringExtra("duration");

        title.setText(ExamName);
        String am_pm;
        String newTime = Integer.toString(time);
        String hour = newTime.substring(0, newTime.length() - 2);
        String mins = newTime.substring(newTime.length() -2);
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

        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(LecturerLobby.this, "tcp://broker.hivemq.com:1883",
                        clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Toast.makeText(Invigilator_Setup_Exam.this, "MQTT success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(LecturerLobby.this, "Please connect to internet and try again.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String payload = "1";
                String topic = "TimesUp/examStart";
                byte[] encodedPayload;
                try {
                    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
