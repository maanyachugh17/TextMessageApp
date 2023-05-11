package com.example.textmessageapp;

import android.telephony.SmsManager;

public class SMSSender {
    public static void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
