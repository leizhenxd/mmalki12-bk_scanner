package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;

public class ScanVIN extends Activity {

    private String trackerID;

    private String DISPLAY1;
    private String DISPLAY2;


    private TextView displayMessage;
    private Button scanVIN;
    private ImageView bkhome;
    private Button manual;
    private TextView scanMessage;
    private TextView manualMessage;
    private boolean fromConnect;
    private Typeface fontText;
    private Typeface fontButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vin);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            DISPLAY1 = extras.getString("display1");
            DISPLAY2 = extras.getString("display2");
            fromConnect = extras.getBoolean("fromConnect");

        }

        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        displayMessage = (TextView) this.findViewById(R.id.textView8);


        scanVIN = (Button) this.findViewById(R.id.button4);

        manualMessage = (TextView) this.findViewById(R.id.textView10);
        manual = (Button) this.findViewById(R.id.button5);

        scanVIN.setTypeface(fontButton);
        manual.setTypeface(fontButton);
        displayMessage.setTypeface(fontText);

        manualMessage.setTypeface(fontText);

        displayMessage.setText(DISPLAY1 + trackerID + DISPLAY2);

//        scanMessage.setText("Scan the VIN barcode on your vehicle’s COMPLIANCE PLATE");
        scanVIN.setText("SCAN VIN");

        manualMessage.setText("(Or if compliance plate has no readable barcode...)");
        manual.setText("MANUAL VIN CAPTURE");
        bkhome.setOnClickListener(handler);

        //font = Typeface.createFromAsset(getAssets(), "GT-Pressura-Regular-Trial.otf");

        scanVIN.setOnClickListener(handler);
        manual.setOnClickListener(handler);

    }

    View.OnClickListener handler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(v == scanVIN)
            {
                if (fromConnect)
                {
                    Intent connect = new Intent(ScanVIN.this, ConnectActivity.class);
                    connect.putExtra("trackerID", trackerID);
                    connect.putExtra("fromConnect", true);
                    ScanVIN.this.startActivity(connect);

                }

                else if (!fromConnect)
                {
                    Intent connect = new Intent(ScanVIN.this, ConnectActivity.class);
                    connect.putExtra("trackerID", trackerID);
                    connect.putExtra("fromConnect", false);
                    ScanVIN.this.startActivity(connect);

                }
            }

            else if (v == manual)
            {
                Intent connect = new Intent(ScanVIN.this, Manual.class);
                connect.putExtra("trackerID", trackerID);

                if(fromConnect)
                {
                    connect.putExtra("fromConnect", true);
                }

                else if (!fromConnect)
                {
                    connect.putExtra("fromConnect", false);
                }

                ScanVIN.this.startActivity(connect);
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(ScanVIN.this, MainActivity.class);
                ScanVIN.this.startActivity(connect);
            }

        }
    };

}
