package com.iot.iotproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

public class ElementActivity extends AppCompatActivity {

    private TextView t;
    private MqttAndroidClient mqttAndroidClient;
    private String total = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);

        t = (TextView)findViewById(R.id.textView);

        final Button buttonRealTime = (Button)findViewById(R.id.buttonRealTime);
        final Button buttonHistory = (Button)findViewById(R.id.buttonHistory);

        Log.d("iot", "connesso");
        final String serverUri = "tcp://iot.eclipse.org:1883";

        String clientId = MqttClient.generateClientId();

        String elementName = getIntent().getStringExtra("elementName");
        Log.d("cacca", "dasdasd "+elementName);
        String tab = getIntent().getStringExtra("tab");
        Log.d("pappa", "asdasd "+tab);

        final String subscriptionTopic = "CT/"+tab+"/"+elementName;
        Log.d("iot", "topic: "+subscriptionTopic);


        try {
            mqttAndroidClient = new MqttAndroidClient(this, serverUri, clientId);
            Log.d("iotdr", "client created");
        }catch(Exception e){}


        try {
            mqttAndroidClient.connect().setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("iotdr", "Connected");
                    try {

                        mqttAndroidClient.subscribe(subscriptionTopic, 0);
                        Log.d("iotdr", "mi sono subscriptorionsdas");






                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("iotdr", "Connection error");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        buttonRealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHistory.setBackground(getDrawable(R.drawable.roundedbutton_unselected));
                buttonRealTime.setBackground(getDrawable(R.drawable.roundedbutton_selected));
                t.setVisibility(View.VISIBLE);
                t.setText("...");

                mqttAndroidClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("iotdr", "mi è arrivato un messaggio");

                        Log.d("iotdr", "po: "+message);
                        JSONObject jsonObject = new JSONObject(message.toString());
                        Log.d("iotdr", "evviva "  +jsonObject);
                        total = ""+jsonObject.get("total");

                        t.setText(total);

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });



            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRealTime.setBackground(getDrawable(R.drawable.roundedbutton_unselected));
                buttonHistory.setBackground(getDrawable(R.drawable.roundedbutton_selected));
                String url = "http://iotdrwebserver.azurewebsites.net/api/University/getHistory";
                t.setVisibility(View.INVISIBLE);
                //t.setText("Le banane sono gialle");
            }
        });



        // param elementName, startTime, endTime






    }
}
