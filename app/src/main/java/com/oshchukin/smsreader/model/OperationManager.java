package com.oshchukin.smsreader.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperationManager {

    private static Float sCriticalDiff = 2.0f;

    private static OperationManager sInstance = null;
    private OperationManagerListener mListener;

    public static OperationManager getInstance() {
        if(sInstance == null) {
            sInstance = new OperationManager();
        }
        return sInstance;
    }

    public void setListener(OperationManagerListener listener) {
        mListener = listener;
    }

    public void requestList(final ContentResolver contentResolver, final Scheme scheme) {
        new RequestTask(contentResolver, scheme).execute();
    }

    private List<Operation> runRequest(ContentResolver contentResolver, Scheme scheme) {
        List<String> bodyList = getSmsBodyByFilter(contentResolver, scheme.getSMSFilter());
        List<Operation> operationList = parseBodyListByScheme(bodyList, scheme);

        processOperations(operationList);

        return operationList;
    }

    private List<String> getSmsBodyByFilter(ContentResolver contentResolver, String filter) {

        List<String> result = new ArrayList<>();
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (cursor != null) {
            totalSMS = cursor.getCount();
            if (cursor.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));

                    int smsType = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE)));
                    if(Telephony.Sms.MESSAGE_TYPE_INBOX == smsType) {
                        if(filter.equalsIgnoreCase(number)) {
                            result.add(body);
                        }
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } else {
            Log.w("SMSReader", "empty list");
        }

        return result;
    }

    private List<Operation> parseBodyListByScheme(List<String> bodyList, Scheme scheme) {
        List<Operation> result = new ArrayList<>();

        for (String body : bodyList) {
            //Log.i("SMSReader", s);
            Operation operation = parseBody(body, scheme);
            if(operation != null) {
                result.add(operation);
            }
        }
        return result;
    }

    private Operation parseBody(String body, Scheme scheme) {
        Operation result = null;

        if(body.matches(scheme.getCreditedPatternString())) {

            result = getOperation(body, scheme.getCreditedPattern(), OperationType.Credited, scheme);
        } else if(body.matches(scheme.getWithDrawnPatternString())) {

            result = getOperation(body, scheme.getWithDrawnPattern(), OperationType.Withdraw, scheme);
        } else {

            result = new Operation(OperationType.Info, body);
        }

        return result;
    }

    private Operation getOperation(String body, Pattern pattern, OperationType type, Scheme scheme) {
        Operation result = null;

        Matcher matcher = pattern.matcher(body);
        if(matcher.matches()) {
            result = new Operation(type, body,
                    Float.parseFloat(getGroup(matcher, scheme.getAmountGroupTag())),
                    Float.parseFloat(getGroup(matcher, scheme.getRemainsGroupTag())));
        } else {
            Log.e("ERROR_ERROR", body);
        }

        return result;
    }

    private static String getGroup(Matcher matcher, String name) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return matcher.group(name);
        }
        return "ERROR";
    }

    public void cancelRequest() {

    }


    private boolean ok(Float left, Float currentAmount, Float previousLeft) {
        Float previousShould = left + currentAmount;
        Float diff = previousLeft - previousShould;
        if(Math.abs(diff) > sCriticalDiff) {
            Log.e("SMSReader", diff.toString());
            return false;
        }

        return true;
    }

    private void processOperations(List<Operation> list) {

        Collections.reverse(list);

        ArrayList<Operation> notInfo = new ArrayList<>();
        for (Operation operation : list) {
            if(operation.type != OperationType.Info){
                notInfo.add(operation);
            }
        }

        int size = notInfo.size();
        for (int i = 0; i < size - 1; i++) {
            Operation cur = notInfo.get(i);
            Operation next = notInfo.get(i + 1);

            next.setPreviousLeft(cur.getCurrentLeft());
        }

        Collections.reverse(list);
    }


    class RequestTask extends AsyncTask<Void, Void, List<Operation>>
    {
        final ContentResolver mContentResolver;
        final Scheme mScheme;
        RequestTask(final ContentResolver contentResolver, final Scheme scheme) {
            mContentResolver = contentResolver;
            mScheme = scheme;
        }

        @Override
        protected List<Operation> doInBackground(Void... voids) {
            return runRequest(mContentResolver, mScheme);
        }

        @Override
        protected void onPostExecute(List<Operation> operations) {
            super.onPostExecute(operations);

            if(mListener != null) {
                mListener.onOperationListReady(operations);
            }
        }
    }
}


//                    switch ()) {
//                            case :
//                            type = "inbox";
//                            break;
//                            case Telephony.Sms.MESSAGE_TYPE_SENT:
//                            type = "sent";
//                            break;
//                            case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
//                            type = "outbox";
//                            break;
//default:
//        break;
//        }