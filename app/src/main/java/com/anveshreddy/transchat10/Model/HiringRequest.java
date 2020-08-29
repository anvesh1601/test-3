package com.anveshreddy.transchat10.Model;

public class HiringRequest {
    private String sender;
    private String receiver;
    private String UserName;
    private String Email;
    private String Amount;
    private String AdditionalBenfits;
    private String RequestStatus;

    public HiringRequest(String additionalBenfits,String sender,String receiver,String UserName,String Email,String RequestStatus,String Amount) {
        this.AdditionalBenfits = additionalBenfits;
        this.Amount=Amount;
        this.Email=Email;
        this.receiver=receiver;
        this.RequestStatus=RequestStatus;
        this.sender=sender;
        this.UserName=UserName;

    }

    public HiringRequest() {
    }

    public String getAdditionalBenfits() {
        return AdditionalBenfits;
    }

    public void setAdditionalBenfits(String additionalBenfits) {
        AdditionalBenfits = additionalBenfits;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
