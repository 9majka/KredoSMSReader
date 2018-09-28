package com.oshchukin.smsreader.model;

import java.util.List;

public interface OperationManagerListener {
    void onOperationListReady(List<Operation> list);
}
