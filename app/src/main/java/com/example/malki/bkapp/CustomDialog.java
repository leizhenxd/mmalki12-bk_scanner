package com.example.malki.bkapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by malki on 2/12/16.
 */


public class CustomDialog extends ProgressDialog {

    private AnimationDrawable ani;
    private ImageView dia;

    public static ProgressDialog construct(Context context) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        ImageView dia = (ImageView) findViewById(R.id.animation);
        dia.setBackgroundResource(R.drawable.logo);
        ani = (AnimationDrawable) dia.getBackground();

    }


    @Override
    public void show() {
        super.show();
        ani.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ani.stop();
    }
}
