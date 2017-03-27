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

public class DisconnectTracker extends Activity {

    private String MSG = "Please disconnect the Black Knight tracking device in your vehicle and scan then barcode";

    private TextView text;
    private Typeface fontText;
    private Typeface fontButton;

    private ImageView bkhome;
    private Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnect_tracker);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        text = (TextView) this.findViewById(R.id.textView14);
        text.setTypeface(fontText);
        scan = (Button) this.findViewById(R.id.button10);
        scan.setTypeface(fontButton);
        scan.setOnClickListener(handler);
        bkhome.setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if (v == scan)
            {
                //Scan
                // POST disconnect tracker ID

                // Flash "Success - Black Knight ID trackerID captured"

                Intent connect = new Intent(DisconnectTracker.this, ScanVIN.class);
                DisconnectTracker.this.startActivity(connect);
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(DisconnectTracker.this, MainActivity.class);
                DisconnectTracker.this.startActivity(connect);
            }
        }
    };
}
