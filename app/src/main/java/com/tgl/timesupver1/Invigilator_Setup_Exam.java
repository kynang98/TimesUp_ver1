package com.tgl.timesupver1;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class Invigilator_Setup_Exam extends AppCompatActivity {

    ListView ExamListView;
    String[] examCode;
    String[] examName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invigilator__setup__exam);

        Resources res = getResources();
        examCode = res.getStringArray(R.array.examCode);
        examName = res.getStringArray(R.array.examName);

//        ExamListAdapter examListAdapter = new ExamListAdapter(this, examCode, examName);
//        ExamListView.setAdapter(examListAdapter);

        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(Invigilator_Setup_Exam.this, "tcp://broker.hivemq.com:1883",
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
                    // Something went wrong e.g. connection timeout or firewall problems
                    //Toast.makeText(Invigilator_Setup_Exam.this, "MQTT failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }



//        ExamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//                String payload = examCode[i];
//                String topic = "TimesUp/examCode";
//                byte[] encodedPayload;
//                try {
//                    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
//                    MqttMessage message = new MqttMessage(encodedPayload);
//                    client.publish(topic, message);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
