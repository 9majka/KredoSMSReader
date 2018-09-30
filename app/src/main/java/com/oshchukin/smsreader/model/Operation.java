package com.oshchukin.smsreader.model;

public class Operation {
    public String body = null;
    float amount = 0.0f;
    float currentLeft = 0.0f;

    public OperationType type = null;
    private float previousLeft = 0.0f;
    private float previousLeftExpected = 0.0f;
    private float diff = 0.0f;

    public Operation(OperationType type, String body, float amount, float left) {
        this.type = type;
        this.body = body;
        this.amount = amount;
        this.currentLeft = left;

        if(type == OperationType.Credited) {
            this.previousLeftExpected = left - amount;
        } else if(type == OperationType.Withdraw) {
            this.previousLeftExpected = amount + left;
        }
    }

    public Operation(OperationType type, String body) {
        this(type, body, 0.0f, 0.0f);
    }

    public void setPreviousLeft(float prev) {
        this.previousLeft = prev;

        diff = previousLeftExpected - previousLeft;
    }

    public float getCurrentLeft() {
        return currentLeft;
    }

    public float getAmount() {
        return amount;
    }

    public float getDiff() {
        return diff;
    }

    @Override
    public String toString() {
        if(type != OperationType.Info) {
            return String.format("%s %.2f:%.2f:%.2f", body, amount, currentLeft, diff);
        } else {
           return body;
        }
    }
}