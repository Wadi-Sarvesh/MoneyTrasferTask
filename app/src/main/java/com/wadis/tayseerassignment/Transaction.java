package com.wadis.tayseerassignment;

import java.util.UUID;

public class Transaction {

    public String Sender;
    public String Receiver;
    public String amtRec;
    public String Purpose;
    public String TrxnID;

    public String getTrxnID() {
        return TrxnID;
    }

    public String getAmtRec() {
        return amtRec;
    }

    public String getPurpose() {
        return Purpose;
    }


    public String getSender() {
        return Sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public Transaction(String sender, String receiver, String amtRec, String purpose) {
        Sender = sender;
        Receiver = receiver;
        this.amtRec = amtRec;
        Purpose = purpose;
        TrxnID = UUID.randomUUID().toString();
    }


}
