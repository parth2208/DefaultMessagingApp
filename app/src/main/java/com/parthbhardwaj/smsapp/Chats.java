package com.parthbhardwaj.smsapp;

public class Chats {

    private long _id;
    private String sender;
    private String message;
    private String sms_read;
    private long time;
    private String folder_name;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSms_read() {
        return sms_read;
    }

    public void setSms_read(String sms_read) {
        this.sms_read = sms_read;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }
}
