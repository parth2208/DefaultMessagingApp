package com.parthbhardwaj.smsapp.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parthbhardwaj.smsapp.R;

import java.text.DateFormat;
import java.util.Date;

public class ComposeSmsDialog extends DialogFragment {

    private static final String TAG = "ComposeSmsDialog";


    TextInputEditText phone_no, text_sms;
    AppCompatImageButton send_btn;
    Context context;

    TextView dismiss_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogue_text_compose,container,false);


        phone_no = view.findViewById(R.id.dialog_editText_phone_no);
        text_sms = view.findViewById(R.id.dialog_editText_msg);
        send_btn = view.findViewById(R.id.dialog_msg_send_btn);

        dismiss_btn = view.findViewById(R.id.dismiss_dialog);
        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        sendSms();

        return view;
    }




    private boolean checkinputs(String phone_no, String text) {
        Log.d(TAG, "checkinputs: Checking if input field is filled or not");

        if (phone_no.equals("") || text.equals("") ) {

            Toast.makeText(getActivity(), "Please fill all the field.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void sendSms(){

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone_no.getText().toString();
                String msg = text_sms.getText().toString();

                if (checkinputs(number,msg)) {

                    try{

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number,null,msg,null,null);

                        Toast.makeText(context, "SMS sent to" + number , Toast.LENGTH_SHORT).show();


                    }catch (Exception e ){

                        e.printStackTrace();
                    }

                    getDialog().dismiss();

                }



            }
        });



    }
}
