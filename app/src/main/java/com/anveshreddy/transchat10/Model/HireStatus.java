package com.anveshreddy.transchat10.Model;

public class HireStatus {
    private String Status;
    private String receiver;

    public HireStatus(String status,String receiver) {
        this.Status = status;
        this.receiver=receiver;
    }
    public HireStatus() {

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
