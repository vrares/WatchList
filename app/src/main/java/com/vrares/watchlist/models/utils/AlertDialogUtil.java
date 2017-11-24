package com.vrares.watchlist.models.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.vrares.watchlist.android.views.UserDetailsView;

public class AlertDialogUtil {

    private Context context;
    private AlertDialog alertDialog;

    public AlertDialogUtil(Context context) {
        this.context = context;
    }

    public void displayAlert(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        this.alertDialog = builder.create();
        alertDialog.show();
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    public void displayChoiceAlert(String message, final AlertDialogCallback alertDialogCallback) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogCallback.updateUser();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
