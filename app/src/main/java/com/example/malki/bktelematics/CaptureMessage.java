package com.example.malki.bktelematics;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malki.telematics.R;

public class CaptureMessage extends Activity {

    private Boolean fromConnect;
    private Button confirm;
    private TextView text1;
    private TextView text2;
    private TextView text3;

    private Typeface textFont;
    private Typeface buttonFont;

    private String trackerID;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_message);

        textFont = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        buttonFont = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        confirm = (Button) findViewById(R.id.button18);
        text1 = (TextView) findViewById(R.id.textView27);
        text2 = (TextView) findViewById(R.id.textView28);
        text3 = (TextView) findViewById(R.id.textView29);

        text1.setTypeface(textFont);
        text2.setTypeface(textFont);
        text3.setTypeface(textFont);

        confirm.setTypeface(buttonFont);


        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            fromConnect = extras.getBoolean("fromConnect");


        }

        if(fromConnect)
        {
            text2.append(" connection");
        }

        else
        {
            text2.append(" disconnection");
        }

        confirm.setOnClickListener(handler);

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

                Intent connect = new Intent(CaptureMessage.this, CaptureMessage.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(CaptureMessage.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(CaptureMessage.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(CaptureMessage.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(CaptureMessage.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(CaptureMessage.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(CaptureMessage.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                CaptureMessage.this.startActivity(connect);
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY","CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == confirm)
            {

                if(fromConnect)
                {
                    Intent connect = new Intent(CaptureMessage.this, ScanVIN.class);
                    connect.putExtra("trackerID", trackerID);
                    connect.putExtra("fromConnect", true);
                    connect.putExtra("display1", "Black Knight ID ");
                    connect.putExtra("display2", " connected. Please scan the VIN barcode");
                    CaptureMessage.this.startActivity(connect);
                }



                else
                {
                    Intent connect = new Intent(CaptureMessage.this, ScanVIN.class);
                    connect.putExtra("trackerID", trackerID);
                    connect.putExtra("fromConnect", false);
                    connect.putExtra("display1", "Black Knight ID ");
                    connect.putExtra("display2", " connected. Please scan the VIN barcode");
                    CaptureMessage.this.startActivity(connect);
                }


            }

            else if(view == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }
    };

}
