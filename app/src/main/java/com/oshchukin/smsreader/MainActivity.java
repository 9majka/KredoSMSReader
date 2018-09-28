package com.oshchukin.smsreader;

import android.app.Activity;

import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.oshchukin.smsreader.model.Operation;
import com.oshchukin.smsreader.model.OperationManager;
import com.oshchukin.smsreader.model.OperationManagerListener;
import com.oshchukin.smsreader.model.OperationType;
import com.oshchukin.smsreader.model.Scheme;

import java.util.List;

public class MainActivity extends Activity implements OperationManagerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentResolver contentResolver = getContentResolver();
                Scheme scheme = new Scheme();
                OperationManager.getInstance().requestList(contentResolver, scheme);
            }
        });

        OperationManager.getInstance().setListener(this);

        ContentResolver contentResolver = getContentResolver();
        Scheme scheme = new Scheme();
        OperationManager.getInstance().requestList(contentResolver, scheme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OperationManager.getInstance().setListener(null);
    }

    @Override
    public void onOperationListReady(List<Operation> list) {

        for (Operation operation : list) {
            if(operation.type == OperationType.Credited) {
                Log.e("SMSReaderMainActivity", operation.body);
            } else if(operation.type == OperationType.Withdraw){
                Log.i("SMSReaderMainActivity", operation.body);
            } else {
                Log.w("SMSReaderMainActivity", operation.body);
            }

        }
    }
}
