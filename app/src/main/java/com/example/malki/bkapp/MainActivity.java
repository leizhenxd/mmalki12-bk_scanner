package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private Button buttonConnect;
    private Button buttonDisconnect;
    private Typeface fontText;
    private Typeface fontButton;
    private ImageView bkhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");
        bkhome = (ImageView) this.findViewById(R.id.bkhome);


        buttonConnect = (Button) this.findViewById(R.id.buttonConnect);
        buttonDisconnect = (Button) this.findViewById(R.id.buttonDisconnect);

        buttonConnect.setTypeface(fontButton);
        buttonDisconnect.setTypeface(fontButton);


        bkhome.setOnClickListener(handler);
        buttonConnect.setOnClickListener(handler);
        buttonDisconnect.setOnClickListener(handler);
//        final Activity activity = this;
//        buttonConnect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Scan");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
//            }
//
//        });
//        readCredentials();

    }

        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v)
            {
                if(v == buttonConnect)
                {
                    Intent connectIntent = new Intent(MainActivity.this, ScanTracker.class);
//                    connectIntent.putExtra("trackerID", "1223144145");
                    connectIntent.putExtra("MSG", "Please scan the barcode of the Black Knight tracking device to be installed in your vehicle");
                    connectIntent.putExtra("fromConnect", true);
                    MainActivity.this.startActivity(connectIntent);
                }

                else if(v == buttonDisconnect)
                {
                   Intent connectIntent = new Intent(MainActivity.this, ScanTracker.class);
                    connectIntent.putExtra("MSG", "Please disconnect the Black Knight tracking device in your vehicle and scan the barcode");
                    connectIntent.putExtra("fromConnect", false);
                    MainActivity.this.startActivity(connectIntent);
                }

                else if(v == bkhome)
                {
                    Intent connect = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(connect);
                }
            }


        };

//
//    private void readCredentials()
//    {
//        try
//        {
//            FileInputStream filein = openFileInput("data.txt");
//            InputStreamReader InputRead = new InputStreamReader(filein);
//
//            char [] inBuffer = new char[100];
//            String str="";
//            int i;
//
//            while((i=InputRead.read(inBuffer))>0)
//            {
//                String stringRead = String.copyValueOf(inBuffer, 0, i);
//                str += stringRead;
//            }
//
//            InputRead.close();
//            Log.i("data file: ", str);
//        }
//
//        catch (Exception e)
//        {
//
//
//        }
//
//    }



//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null)
//        {
//            if(result.getContents() == null)
//            {
//                Log.d("MainActivity", "Cancelled scan");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            else
//            {
//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//            }
//        }
//
//        else
//        {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}
