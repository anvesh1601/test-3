package com.anveshreddy.transchat10.Model;

public class Chat {
  private  String sender;
    private  String receiver;
    private  String smessage;


    public Chat(String sender,String receiver,String smessage,String rmessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.smessage = smessage;


    }

    public Chat() {

    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSmessage() {
        return smessage;
    }

    public void setSmessage(String smessage) {
        this.smessage = smessage;
    }
}
