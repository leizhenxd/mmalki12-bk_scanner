package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.bktelematics.utils.ImagePathCache;
import com.example.malki.telematics.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;

public class ScanVIN extends Activity {

    private String trackerID;

    private String DISPLAY1;
    private String DISPLAY2;


    private TextView displayMessage;
    private TextView displayMessageHint;

    private TextView scanHint;
    private Button scanVIN;
    private ImageView bkhome;
    private Button manual;
    private TextView scanMessage;
    private TextView manualMessage;
    private boolean fromConnect;
    private Typeface fontText;
    private Typeface fontButton;
    private Activity activity;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vin);

        activity  = this;


        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            //DISPLAY1 = extras.getString("display1");
            //DISPLAY2 = extras.getString("display2");
            fromConnect = extras.getBoolean("fromConnect");

        }

        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        displayMessage = (TextView) this.findViewById(R.id.textView8);
        scanHint = (TextView) this.findViewById(R.id.textView37);
        displayMessageHint = (TextView) this.findViewById(R.id.textView38);

        scanVIN = (Button) this.findViewById(R.id.button4);

        manualMessage = (TextView) this.findViewById(R.id.textView10);
        manual = (Button) this.findViewById(R.id.button5);

        scanVIN.setTypeface(fontButton);
        manual.setTypeface(fontButton);
        displayMessage.setTypeface(fontText);
        displayMessageHint.setTypeface(fontText);
        scanHint.setTypeface(fontText);


        manualMessage.setTypeface(fontText);

        displayMessage.setText("Please locate your vehicle's Compliance Plate containing the 17-digit VIN and barcode");
        displayMessageHint.setText("(usually on the door pillar)");
        scanHint.setText("Auto scan the VIN barcode");
//        scanMessage.setText("Scan the VIN barcode on your vehicle’s COMPLIANCE PLATE");
        scanVIN.setText("SCAN VIN");


        manualMessage.setText("(Or if the barcode does not scan)");
        manual.setText("ENTER MANUALLY");
        bkhome.setOnClickListener(handler);

        //font = Typeface.createFromAsset(getAssets(), "GT-Pressura-Regular-Trial.otf");

        scanVIN.setOnClickListener(handler);
        manual.setOnClickListener(handler);

        hamburger = (ImageView) this.findViewById(R.id.list);
        hamburger.setOnClickListener(handler);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                String itemPressed = ((TextView) view).getText().toString();

                Intent connect = new Intent(ScanVIN.this, ScanVIN.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ScanVIN.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ScanVIN.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ScanVIN.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ScanVIN.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(ScanVIN.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(ScanVIN.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ScanVIN.this.startActivity(connect);
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    View.OnClickListener handler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(v == scanVIN)
            {
//                if (fromConnect)
//                {
//                    Intent connect = new Intent(ScanVIN.this, ConnectActivity.class);
//                    connect.putExtra("trackerID", trackerID);
//                    connect.putExtra("fromConnect", true);
//                    ScanVIN.this.startActivity(connect);
//
//                }
//
//                else if (!fromConnect)
//                {
//                    Intent connect = new Intent(ScanVIN.this, ConnectActivity.class);
//                    connect.putExtra("trackerID", trackerID);
//                    connect.putExtra("fromConnect", false);
//                    ScanVIN.this.startActivity(connect);
//
//                }
                scan(v);
            }

            else if (v == manual)
            {
                Intent connect = new Intent(ScanVIN.this, Compliance.class);
                connect.putExtra("trackerID", trackerID);

                if(fromConnect){
                    connect.putExtra("fromConnect", true);

                }

                else if (!fromConnect)
                {
                    connect.putExtra("fromConnect", false);

                }

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

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }

        }
    };

    protected void scan(View view)
    {
//        takePictureNoPreview(this.getApplicationContext());
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {



        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanResult != null)
        {
            if(scanResult.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }

            else
            {

                String type = scanResult.getFormatName();

                String barcode = scanResult.getContents();

                //if (type.equals("CODE_39") || type.equals(""))
                {
                    //                    generateQRCode(scanResult.getContents());

                    // GENERATES THE IMAGE!!!!!!
                    ImagePathCache.vinPicturePath = intent.getStringExtra("SCAN_RESULT_IMAGE_PATH");
//                    Uri pathuri = Uri.parse(path);
//                    image.setImageURI(pathuri);


                    String txt = "Success - VIN: " + barcode + " captured.";

                    if (!fromConnect)
                        txt += "It will be unpaired from BK ID " +trackerID;

                    Toast.makeText(this,txt , Toast.LENGTH_SHORT).show();

//                    Bitmap bitmap = BitmapFactory.decodeByteArray(picture , 0, picture.length);
//                    image.setImageBitmap(bitmap);

                    if(fromConnect)
                    {
                        Intent connect = new Intent(ScanVIN.this, ConfirmPairing.class);
                        connect.putExtra("trackerID", trackerID);
                        connect.putExtra("VIN", barcode);
                        connect.putExtra("manual", false);
                        ScanVIN.this.startActivity(connect);

                    }

                    else if (!fromConnect)
                    {
                        Intent connect = new Intent(ScanVIN.this, DisconnectActivity.class);
                        connect.putExtra("trackerID", trackerID);
                        connect.putExtra("VIN", barcode);
                        ScanVIN.this.startActivity(connect);


                    }

                }

//                else
//                {
//                    Toast.makeText(this, "Invalid VIN", Toast.LENGTH_SHORT).show();
//                }

            }



        }

    }

}
