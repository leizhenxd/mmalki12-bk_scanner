package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malki.telematics.R;

import java.util.Arrays;
import java.util.List;

public class DisconnectActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView list;
    private TextView text;
    private Typeface font;


    //private String msg = "Thanks. Now please select a reason for this disconnection.";
    private Typeface fontText;
    private Typeface fontButton;
    private String trackerID;
    private String VIN;
    private ImageView bkhome;
    private TextView text2;

    private String [] item;
    private ArrayAdapter<String> mAdapter;
    private ImageView hamburger;
    private ListView mDrawerList;

    private DrawerLayout drawer;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disconnect);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");
        }

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");
        bkhome = (ImageView) findViewById(R.id.bkhome);

        text = (TextView) findViewById(R.id.textView16);
        text2 = (TextView) findViewById(R.id.textView40);
        list = (ListView) findViewById(R.id.disconnectReasonList);

        text.setText("ID " + trackerID + " is unpaired with VIN ending " + VIN);
        text2.setText("Select reason for disconnection");

        text.setTypeface(fontText);
        text2.setTypeface(fontText);


        item = getResources().getStringArray(R.array.disconnectReason);
        List <String> item1 = Arrays.asList(item);




        ArrayAdapter listA =  new CustomListAdapter(DisconnectActivity.this, R.layout.listdis, item1);



        list.setAdapter(listA);
        bkhome.setOnClickListener(handler);
        list.setOnItemClickListener(this);

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

                Intent connect = new Intent(DisconnectActivity.this, DisconnectActivity.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(DisconnectActivity.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(DisconnectActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(DisconnectActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(DisconnectActivity.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(DisconnectActivity.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(DisconnectActivity.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                DisconnectActivity.this.startActivity(connect);
            }
        });


    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if (v == bkhome)
            {
                Intent connect = new Intent(DisconnectActivity.this, MainActivity.class);
                DisconnectActivity.this.startActivity(connect);

            }

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }
    };

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

        String reason = ((TextView) view).getText().toString();

        Intent connect = new Intent(DisconnectActivity.this, PromptDiscActivity.class);
        connect.putExtra("reason", reason);
        connect.putExtra("trackerID", trackerID);
        connect.putExtra("VIN", VIN);

        DisconnectActivity.this.startActivity(connect);
//        if (reason.equals("Sale"))
//        {
//
//        }
//
//        else if (reason.equals("Service"))
//        {
//
//        }
//
//        else if(reason.equals("Dealership Exchange"))
//        {
//
//        }
//
//
//        else if(reason.equals("Other"))
//        {
//
//        }
//
//        else if(reason.equals("Unknown"))
//        {
//
//        }


    }
}
