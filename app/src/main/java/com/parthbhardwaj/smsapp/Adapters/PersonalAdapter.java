package com.parthbhardwaj.smsapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parthbhardwaj.smsapp.Chats;
import com.parthbhardwaj.smsapp.R;
import com.parthbhardwaj.smsapp.Utils.ItemClickListener;

import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.PersonalHolder> {

    List<Chats> chat_phone;
    Context mContext;
    ItemClickListener itemClickListener;

    public PersonalAdapter(List<Chats> chat_phone, Context mContext) {
        this.chat_phone = chat_phone;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PersonalAdapter.PersonalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        PersonalHolder personalHolder = new PersonalHolder(view);
        return personalHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalHolder personalHolder, int i) {

        Chats chats = chat_phone.get(i);

        personalHolder.senderContact.setText(chats.getSender());
        personalHolder.message.setText(chats.getMessage());


        String firstChar = String.valueOf(chats.getSender().charAt(0));

        if (chats.getSms_read().equals("0")) {
            personalHolder.senderContact.setTypeface(personalHolder.senderContact.getTypeface(), Typeface.BOLD);
            personalHolder.message.setTypeface(personalHolder.message.getTypeface(), Typeface.BOLD);
            personalHolder.time.setTypeface(personalHolder.time.getTypeface(), Typeface.BOLD);
            personalHolder.time.setTextColor(ContextCompat.getColor(mContext,R.color.black));
        } else {
            personalHolder.senderContact.setTypeface(null, Typeface.NORMAL);
            personalHolder.message.setTypeface(null, Typeface.NORMAL);
            personalHolder.time.setTypeface(null, Typeface.NORMAL);

        }

    }

    @Override
    public int getItemCount() {
        return chat_phone.size();
    }

    public class PersonalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView senderImage;
        private TextView senderContact;
        private TextView message;
        private TextView time;
        private RelativeLayout mainLayout;

        public PersonalHolder(View itemView) {
            super(itemView);
            senderContact = (TextView) itemView.findViewById(R.id.smsSender);
            message = (TextView) itemView.findViewById(R.id.smsContent);
            time = (TextView) itemView.findViewById(R.id.time);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.small_layout_main);

            mainLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {

                chat_phone.get(getAdapterPosition()).setSms_read("1");
                notifyItemChanged(getAdapterPosition());

                itemClickListener.itemClicked(senderContact.getText().toString(),
                        chat_phone.get(getAdapterPosition()).get_id(),
                        chat_phone.get(getAdapterPosition()).getSms_read());
            }

        }
    }
}
