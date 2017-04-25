package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;

public class Manual extends Activity {

    private String trackerID;
    private Boolean fromConnect;
    private ImageView view;
    private TextView text2;
    private Button button;

    private Typeface fontButton;
    private ImageView bkhome;
    private Typeface fontText;

    private String manualVin;

    private byte [] pic;

    private String MESSAGE = "If your Compliance Plate does not have a barcode, capture an image of the VIN number on your Compliance Plate.";

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            fromConnect = extras.getBoolean("fromConnect");
            manualVin = extras.getString("VIN");
        }

        text2 = (TextView) this.findViewById(R.id.textView39);
        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        button = (Button) this.findViewById(R.id.button6);
        view = (ImageView) this.findViewById(R.id.imageView);

       // text.setText(MESSAGE);
        button.setTypeface(fontButton);
        text2.setTypeface(fontText);

        button.setOnClickListener(handler);
        bkhome.setOnClickListener(handler);

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

                Intent connect = new Intent(Manual.this, Manual.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(Manual.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(Manual.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(Manual.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(Manual.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(Manual.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(Manual.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                Manual.this.startActivity(connect);
            }
        });

        text2.setText("Use the camera to capture a photo of the full Compliance Plate for vehicle with VIN ending " + manualVin);
    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == button)
            {
                intentCompliance();

            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(Manual.this, MainActivity.class);
                Manual.this.startActivity(connect);
            }

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }

    };

    public void intentCompliance()
    {
        Intent takePhoto = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePhoto.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePhoto, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            pic = stream.toByteArray();
            String encoded = Base64.encodeToString(pic, Base64.DEFAULT);
            //view.setImageBitmap(image);



            ImagePathCache.vinPicturePath = "";
            ImagePathCache.manuaVinPictrueBase64 = encoded;

            Toast.makeText(getApplicationContext(), "Success - Compliance plate image captured", Toast.LENGTH_SHORT).show();
            if(fromConnect){
                Intent connect = new Intent(Manual.this, ConfirmPairing.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("fromConnect", true);
                connect.putExtra("VIN", manualVin);
                connect.putExtra("manual", true);
                Manual.this.startActivity(connect);
            }

            else if (!fromConnect)
            {
                Intent connect = new Intent(Manual.this, DisconnectActivity.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("fromConnect", false);
                connect.putExtra("VIN", manualVin);
                connect.putExtra("manual", true);
                Manual.this.startActivity(connect);

            }


        }


    }


}
