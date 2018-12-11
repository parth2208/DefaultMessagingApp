package com.parthbhardwaj.smsapp.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthbhardwaj.smsapp.Chats;
import com.parthbhardwaj.smsapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private static final String TAG = "MessageAdapter";

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    String number;
    String sms;

    private Context mContext;
    private List<Chats> mChat;

    public MessageAdapter(Context mContext, List<Chats> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Chats chats = mChat.get(i);
        viewHolder.show_message.setText(chats.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder(View itemView){

            super(itemView);

            show_message = itemView.findViewById(R.id.msg_show);

        }
    }


    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {



        }
    };

    public void senderNo(String num){

        number =num;
    }

    public void sentData(String text_msg){

        sms = text_msg;
    }


    @Override
    public int getItemViewType(int position) {

        if (mChat.get(position).getSender().equals(number)){

            return MSG_TYPE_LEFT;
        }else {
            return MSG_TYPE_RIGHT;
        }


    }
}
