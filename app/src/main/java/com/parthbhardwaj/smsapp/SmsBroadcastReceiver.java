package com.parthbhardwaj.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    Chats mChats;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle intentExtras = intent.getExtras();



        if (intentExtras != null){

            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String phone_no = "";

            for (int i = 0; i<sms.length; i++){

                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                long timeMillis = smsMessage.getTimestampMillis();

                Date date = new Date(timeMillis);
                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yy");
                String dateText = format.format(date);

                smsMessageStr += address + "at" + "\t" + dateText +"\n";
                smsMessageStr += smsBody + "\n";
                phone_no += address;

                mChats.setSender(address);
                mChats.setMessage(smsBody);
            }
            Toast.makeText(context,smsMessageStr, Toast.LENGTH_SHORT).show();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message",smsMessageStr);
            broadcastIntent.putExtra("mob_no",phone_no);


        }
    }
}