package com.example.textmessageapp;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.MediaType;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private SMSReceiver smsReceiver = new SMSReceiver();
    public static TextView textReceived;
    private TextView textSent;
    private EditText sender;
    private Button sendButton;
    private String receivedTexts = "";
    private String message;
    private static final SmsManager smsManager = SmsManager.getDefault();
    private Handler handler = new Handler() {
        @Override
        public void publish(LogRecord logRecord) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textReceived = findViewById(R.id.textView);
        textSent = findViewById(R.id.textView2);
        sender = findViewById(R.id.editTextTextPersonName);
        sendButton = findViewById(R.id.button);

        // Request necessary permissions
// Request necessary permissions
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"}, 100);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = sender.getText().toString();

                // Send SMS message
                String phoneNumber = "5555215554";  // Replace with the desired recipient phone number
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                // AI Component
                String response = generateAIResponse(message);  // Pass the user's message as the prompt
                addResponse(response);
                smsManager.sendTextMessage(phoneNumber, null, response, null, null);
            }
        });
    }

    public static void setTextReceived(String text) {
        if (textReceived != null) {
            textReceived.setText(text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(smsReceiver);
    }

    private void addResponse(String response) {
        receivedTexts += "\n\n" + response;
        textSent.setText(receivedTexts);
    }
    private String generateAIResponse(String prompt) {
        String response;

        // Convert the prompt to lowercase for case-insensitive matching
        prompt = prompt.toLowerCase();

        // Check the current state based on the received message
        if (prompt.contains("hello") || prompt.contains("hi")) {
            response = getRandomResponse(new String[]{"Hello!", "Hi there!", "Hey!"});
        } else if (prompt.contains("how are you")) {
            response = getRandomResponse(new String[]{"I'm good, thanks!", "I'm doing well.", "Feeling great!"});
        } else if (prompt.contains("bye")) {
            response = getRandomResponse(new String[]{"Goodbye!", "Farewell!", "Take care!"});
        } else {
            response = getRandomResponse(new String[]{"I'm sorry, I didn't understand.", "Could you please rephrase that?", "I'm still learning, so I'm not sure."});
        }
        System.out.println("Prompt: " + prompt);
        System.out.println("Response: " + response);

        return response;
    }


    private String getRandomResponse(String[] responses) {
        int randomIndex = new Random().nextInt(responses.length);
        return responses[randomIndex];
    }

}
