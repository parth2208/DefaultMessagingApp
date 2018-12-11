package com.parthbhardwaj.smsapp.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parthbhardwaj.smsapp.Chats;
import com.parthbhardwaj.smsapp.Utils.ContentContract;
import com.parthbhardwaj.smsapp.Utils.ItemClickListener;
import com.parthbhardwaj.smsapp.MessageActivity;
import com.parthbhardwaj.smsapp.Adapters.PromotionalAdapter;
import com.parthbhardwaj.smsapp.R;
import com.parthbhardwaj.smsapp.Utils.ValueConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PromotionalFragment extends Fragment implements ItemClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    RecyclerView recyclerView;
    PromotionalAdapter promotionalAdapter;
    List<Chats> phone_chat;
    String text;
    String cursor_filter;
    public static PromotionalFragment inst;
    Context context;
    LinearLayoutManager linearLayoutManager;
    private BroadcastReceiver receiver;

    public static PromotionalFragment instance(){
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst =this;

    }

    public BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            text = intent.getExtras().getString("mob_no");
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promotional,container,false);

        recyclerView = view.findViewById(R.id.promotional_recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;


    }


    private void setRecyclerView(List<Chats> chat_data) {
        promotionalAdapter = new PromotionalAdapter(chat_data,context);
        recyclerView.setAdapter(promotionalAdapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MessageActivity.class));
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = null;
        String[] selectionArgs = null;

        if (cursor_filter != null) {
            selection = ContentContract.SMS_SELECTION_SEARCH;
            selectionArgs = new String[]{"%" + cursor_filter + "%", "%" + cursor_filter + "%"};
        }

        return new CursorLoader(this,
                ContentContract.ALL_SMS_URI,
                null,
                selection,
                selectionArgs,ContentContract.SORT_DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {

            //allConversationAdapter.swapCursor(cursor);
            getAllSms(cursor);

        } else {
            //no sms
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        phone_chat = null;
        promotionalAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                boolean new_sms = intent.getBooleanExtra("new_sms", false);

                if (new_sms)
                    getSupportLoaderManager().restartLoader(ValueConstants.ALL_SMS_LOADER, null, context);

            }
        };

        this.registerReceiver(receiver, intentFilter);
    }


    public void getAllSms(Cursor c) {

        List<Chats> lstSms = new ArrayList<Chats>();
        Chats objChats = null;
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                try {
                    objChats = new Chats();
                    objChats.set_id(c.getLong(c.getColumnIndexOrThrow("_id")));
                    String num=c.getString(c.getColumnIndexOrThrow("address"));
                    objChats.setSender(num);
                    objChats.setMessage(c.getString(c.getColumnIndexOrThrow("body")));
                    objChats.setSms_read(c.getString(c.getColumnIndex("read")));
                    objChats.setTime(c.getLong(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objChats.setFolder_name("inbox");
                    } else {
                        objChats.setFolder_name("sent");
                    }

                } catch (Exception e) {

                } finally {

                    lstSms.add(objChats);
                    c.moveToNext();
                }
            }
        }
        c.close();

        phone_chat = lstSms;

        //Log.d(TAG,"Size before "+data.size());
        SetToRecycler(lstSms);

    }

    private void SetToRecycler(List<Chats> lstSms) {

        Set<Chats> s = new LinkedHashSet<>(lstSms);
        phone_chat = new ArrayList<>(s);
        Chats temp = new Chats();
        if (temp.getSender().length()<=6){

            setRecyclerView(phone_chat);

            convertToJson(lstSms);
        }

    }

    private void convertToJson(List<Chats> lstSms) {

        Type listType = new TypeToken<List<Chats>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(lstSms, listType);

        SharedPreferences sp = context.getSharedPreferences(ValueConstants.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ValueConstants.SMS_JSON, json);
        editor.apply();
        //List<String> target2 = gson.fromJson(json, listType);
        //Log.d(TAG, json);

    }

    @Override
    public void itemClicked(String contact, long id, String read) {

        Chats temp = new Chats();
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(ValueConstants.CONTACT_NAME, temp.getSender());
        intent.putExtra(ValueConstants.SMS_ID, temp.get_id());
        intent.putExtra(ValueConstants.READ, temp.getSms_read());
        startActivity(intent);

    }
}
