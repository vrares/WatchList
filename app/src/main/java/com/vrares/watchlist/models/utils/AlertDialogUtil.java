package com.vrares.watchlist.models.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogUtil {

    private Context context;
    private AlertDialog alertDialog;

    public AlertDialogUtil(Context context) {
        this.context = context;
    }

    public void displayAlert(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        this.alertDialog = builder.create();
        alertDialog.show();

    }
}
