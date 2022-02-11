package com.example.weather.ultils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogHelper {
    String title, message;
    Context context;

    public AlertDialogHelper(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
    }

    public AlertDialog createDialog() {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .create();
    }
}
