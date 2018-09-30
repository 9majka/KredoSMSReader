package com.oshchukin.smsreader;

import android.Manifest;
import android.app.Activity;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.oshchukin.smsreader.model.Operation;
import com.oshchukin.smsreader.model.OperationManager;
import com.oshchukin.smsreader.model.OperationManagerListener;
import com.oshchukin.smsreader.model.OperationType;
import com.oshchukin.smsreader.model.Scheme;
import com.oshchukin.smsreader.model.UkrScheme;

import java.util.List;

public class MainActivity extends Activity implements OperationManagerListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1989;
    private ListView mListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listText);
        String weekdays[] = {"Sunday", "Monday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weekdays);
        mListView.setAdapter(arrayAdapter);

        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);
        } else {
            apply();
        }
    }

    private void apply() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentResolver contentResolver = getContentResolver();
                Scheme scheme = new UkrScheme();
                OperationManager.getInstance().requestList(contentResolver, scheme);
            }
        });

        OperationManager.getInstance().setListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_SMS)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                apply();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OperationManager.getInstance().setListener(null);
    }

    @Override
    public void onOperationListReady(List<Operation> list) {

        Log.e("SMSReaderMainActivity", "SetListAdapter " + list.size());
        CustomList listAdapter = new CustomList(this, list);
        mListView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();
    }
}




//        for (Operation operation : list) {
//            if(operation.type == OperationType.Credited) {
//                Log.e("SMSReaderMainActivity", "" + operation);
//
//            } else if(operation.type == OperationType.Withdraw){
//                Log.w("SMSReaderMainActivity", "" + operation);
//            } else {
//                Log.i("SMSReaderMainActivity","" + operation);
//            }
//
//
//        }