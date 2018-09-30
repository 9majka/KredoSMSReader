package com.oshchukin.smsreader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oshchukin.smsreader.model.Operation;
import com.oshchukin.smsreader.model.OperationType;

import java.util.List;

public class CustomList extends ArrayAdapter<Operation> {
    private List<Operation> mList;

    public CustomList(Context context, List<Operation> list) {
        super(context, R.layout.list_item, list);

        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("aaaaa", "getView: " + position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }



        TextView body = (TextView) convertView.findViewById(R.id.body);
        TextView diff = (TextView) convertView.findViewById(R.id.txt);

        body.setText(mList.get(position).toString());
        diff.setText("" + mList.get(position).getDiff());

/*        if(mList.get(position).type == OperationType.Credited) {
            body.setBac
        }*/

        return convertView;
    }
}
        
   