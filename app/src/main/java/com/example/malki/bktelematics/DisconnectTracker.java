package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malki.telematics.R;

public class DisconnectTracker extends Activity {

    private String MSG = "Please disconnect the Black Knight tracking device in your vehicle and scan then barcode";

    private TextView text;
    private Typeface fontText;
    private Typeface fontButton;

    private ImageView bkhome;
    private Button scan;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

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

                Intent connect = new Intent(DisconnectTracker.this, DisconnectTracker.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(DisconnectTracker.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(DisconnectTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(DisconnectTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(DisconnectTracker.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(DisconnectTracker.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                DisconnectTracker.this.startActivity(connect);
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
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

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }
    };
}
