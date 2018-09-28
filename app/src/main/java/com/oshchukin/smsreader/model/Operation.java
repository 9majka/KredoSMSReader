package com.oshchukin.smsreader.model;

public class Operation {
    public String body = null;
    Float amount = null;
    Float left = null;
    public OperationType type = null;

    public Operation(OperationType type, String body, Float amount, Float left) {
        this.type = type;
        this.body = body;
        this.amount = amount;
        this.left = left;
    }

    public Operation(OperationType type, String body) {
        this(type, body, null, null);
    }
}