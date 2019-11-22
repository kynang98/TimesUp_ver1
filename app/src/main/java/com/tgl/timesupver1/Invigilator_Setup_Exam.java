package com.tgl.timesupver1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Invigilator_Setup_Exam extends AppCompatActivity {

    ListView ExamListView;
    String[] examCode;
    String[] examName;
    private Spinner codespinner;
    private TimePicker timespinner;
    private Spinner durationspinner;
    private DatabaseReference firebase;
    private Button submit;
    private EditText examtitle;
    int time;
    String duration;
    String ExamCode;
    String ExamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invigilator__setup__exam);
        firebase = FirebaseDatabase.getInstance().getReference("Questions");
        codespinner = findViewById(R.id.spinner4);
        timespinner = findViewById(R.id.datePicker1);
        timespinner.setIs24HourView(false);
        durationspinner = findViewById(R.id.spinner3);
        submit = findViewById(R.id.button4);
        examtitle = findViewById(R.id.editText3);

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
                    Toast.makeText(Invigilator_Setup_Exam.this, "Please connect to internet and try again.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> codeList = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String titlename = d.getKey();
                        codeList.add(titlename);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Invigilator_Setup_Exam.this, android.R.layout.simple_spinner_item, codeList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        codespinner.setAdapter(arrayAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Invigilator_Setup_Exam.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        final List<String> durationList = new ArrayList<>();
        durationList.add("15 minutes");
        durationList.add("30 minutes");
        durationList.add("45 minutes");
        durationList.add("1 hour");
        durationList.add("1 hour 15 minutes");
        durationList.add("1 hour 30 minutes");
        durationList.add("1 hour 45 minutes");
        durationList.add("2 hours");
        durationList.add("2 hours 15 minutes");
        durationList.add("2 hours 30 minutes");
        durationList.add("2 hours 45 minutes");
        durationList.add("3 hours");
        durationList.add("3 hours 15 minutes");
        durationList.add("3 hours 30 minutes");
        durationList.add("3 hours 45 minutes");
        durationList.add("4 hours");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(Invigilator_Setup_Exam.this, android.R.layout.simple_spinner_item, durationList);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationspinner.setAdapter(arrayAdapter2);

        Resources res = getResources();
        examCode = res.getStringArray(R.array.examCode);
        examName = res.getStringArray(R.array.examName);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timespinner.getHour();
                    minute = timespinner.getMinute();
                }
                else{
                    hour = timespinner.getCurrentHour();
                    minute = timespinner.getCurrentMinute();
                }
                time = (hour*100)+minute;
                ExamCode = codespinner.getSelectedItem().toString();
                ExamName = examtitle.getText().toString();
                duration = durationspinner.getSelectedItem().toString();

                String payload = ExamCode;
                String topic = "TimesUp/examCode";
                byte[] encodedPayload;
                try {
                    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(Invigilator_Setup_Exam.this, Exam_Lobby.class);
                i.putExtra("time", time);
                i.putExtra("code", ExamCode);
                i.putExtra("title", ExamName);
                i.putExtra("duration", duration);
                startActivity(i);
            }
        });

    }
}
