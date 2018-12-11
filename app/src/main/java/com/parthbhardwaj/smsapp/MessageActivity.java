package com.parthbhardwaj.smsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parthbhardwaj.smsapp.Adapters.MessageAdapter;
import com.parthbhardwaj.smsapp.Utils.ValueConstants;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView back_btn, send_btn;
    EditText text_msg;
    Context context;

    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);


        toolbar = findViewById(R.id.msg_toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        back_btn = findViewById(R.id.back_btn);
        send_btn = findViewById(R.id.send_btn);
        text_msg = findViewById(R.id.type_msg);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });


        sendSms();

    }


    private boolean checkinputs(String text) {
        Log.d(TAG, "checkinputs: Checking if input field is filled or not");

        if (text.equals("") ) {

            Toast.makeText(context, "Please fill all the field.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void sendSms(){

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = getIntent().getStringExtra(ValueConstants.CONTACT_NAME);
                messageAdapter.senderNo(number);
                String msg = text_msg.getText().toString();
                messageAdapter.sentData(msg);

                if (checkinputs(msg)) {

                    try{

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number,null,msg,null,null);

                        Toast.makeText(context, "SMS sent to" + number , Toast.LENGTH_SHORT).show();


                    }catch (Exception e ){

                        e.printStackTrace();
                    }

                }



            }
        });

    }
}
