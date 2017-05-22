package com.project.mobileapp.devtools.Helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ABDUL Samad on 5/18/2017.
 */

public class Dialogs {
    ProgressDialog progressDialog;
    AlertDialog.Builder dialog;
    public Dialogs(Context context)
    {
        dialog=new AlertDialog.Builder(context);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        progressDialog=new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public void showProgressDialog()
    {
        progressDialog.show();
    }
    public void hideProgessDialog()
    {
    progressDialog.cancel();
    }
    public void showDialog(String title,String message)
    {
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}
