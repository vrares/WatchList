package com.vrares.watchlist.models.utils;

import android.app.ProgressDialog;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ProgressDialogUtil {

    private ProgressDialog progressDialog;
    private Context context;

    public ProgressDialogUtil(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }

}
