package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Compliance extends Activity {

    private TextView text;
    private EditText VIN;
    private EditText confirmVIN;
    private Button button;

    private String trackerID;
    private Typeface fontText;
    private Typeface fontButton;

    private ImageView bkhome;
    private boolean fromConnect;

    private String MSG = "Thanks. Now please enter the LAST 6 DIGITS of the vehicle’s VIN.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compliance);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        text = (TextView) this.findViewById(R.id.textView12);
        VIN = (EditText) this.findViewById(R.id.editText5);
        confirmVIN = (EditText) this.findViewById(R.id.editText10);
        button = (Button) this.findViewById(R.id.button7);
        confirmVIN.setHint("Confirm VIN");

        text.setTypeface(fontText);
        button.setTypeface(fontButton);
        bkhome = (ImageView) this.findViewById(R.id.bkhome);


        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            fromConnect = extras.getBoolean("fromConnect");

        }

        text.setText(MSG);

        bkhome.setOnClickListener(handler);


        button.setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick (View v)
        {
            if (v == button)
            {
                if (VIN.getText().toString() == null || confirmVIN.getText().toString() == null)
                {
                    // TOAST PLEASE CONFIRM VIN
                    Toast.makeText(getApplicationContext(), "Please confirm last 6 digits of VIN", Toast.LENGTH_SHORT).show();
                }

                else if(VIN.getText().toString().equals(confirmVIN.getText().toString()))
                {
                    if (!VIN.getText().toString().isEmpty()) {
                        //FLASH "Success - VIN entry VIN.getText().toString() captured"
                        Toast.makeText(getApplicationContext(), "Success - VIN entry " + VIN.getText().toString() + " captured", Toast.LENGTH_SHORT).show();
                        // DIAGLOG "Do you wish to pair BlackKnight ID trackerID with VIN VIN.getText().toString"

                        // VERY VERY IMPORTAN
                        Intent connect;



                        if (fromConnect)
                        {

                            connect = new Intent(Compliance.this, ConfirmPairing.class);
                            connect.putExtra("fromConnect", true);
                            connect.putExtra("trackerID", trackerID);
                            connect.putExtra("VIN", VIN.getText().toString());
                            connect.putExtra("manual", true);

                            Compliance.this.startActivity(connect);
                        }

                        else if (!fromConnect)
                        {
                            connect = new Intent(Compliance.this, DisconnectActivity.class);
                            connect.putExtra("fromConnect", false);
                            connect.putExtra("trackerID", trackerID);
                            connect.putExtra("VIN", VIN.getText().toString());
                            connect.putExtra("manual", true);

                            Compliance.this.startActivity(connect);
                        }



                    }

                    else
                        Toast.makeText(getApplicationContext(), "Please enter VIN.", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    // FLASH "VIN's do not match. Please re-enter last 6 digits of VIN Compliance Plate"
                    Toast.makeText(getApplicationContext(), "VIN's do not match. Please re-enter last 6 digits of VIN Compliance Plate", Toast.LENGTH_SHORT).show();

                }


            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(Compliance.this, MainActivity.class);
                Compliance.this.startActivity(connect);
            }
        }

    };
}
