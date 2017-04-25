package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

public class ManualTracker extends Activity {

    private TextView text;
    private TextView text2;

    private EditText edittxt1;
    private EditText edittxt2;

    private Typeface textFont;
    private Typeface buttonFont;

    private EditText tracker;
    private EditText confirmtracker;

    private Boolean fromConnect;
    private Button next;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_tracker);

        textFont = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        buttonFont = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            fromConnect = extras.getBoolean("fromConnect");
        }

        text = (TextView) findViewById(R.id.textView41);
        text2 = (TextView) findViewById(R.id.textView42);

        next = (Button) findViewById(R.id.button9);
        tracker = (EditText) findViewById(R.id.editText12);
        confirmtracker = (EditText) findViewById(R.id.editText13);

        text.setTypeface(textFont);
        text2.setTypeface(textFont);
        next.setTypeface(buttonFont);

        next.setOnClickListener(handler);

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

                Intent connect = new Intent(ManualTracker.this, ManualTracker.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ManualTracker.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ManualTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ManualTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ManualTracker.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(ManualTracker.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(ManualTracker.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ManualTracker.this.startActivity(connect);
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view == next)
            {
                if(tracker.getText().toString().equals(confirmtracker.getText().toString()) && tracker.getText().toString().length() == 10)
                {
                        Intent connect = new Intent(ManualTracker.this, ManualTrackerCapture.class);
                        connect.putExtra("fromConnect", fromConnect);
                        connect.putExtra("trackerID", tracker.getText().toString());
                        ManualTracker.this.startActivity(connect);
                }

                else
                {
                    if(tracker.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please enter 10-digit tracker ID", Toast.LENGTH_SHORT ).show();
                    }

                    else if (confirmtracker.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please confirm tracker ID", Toast.LENGTH_SHORT ).show();
                    }

                    else if(tracker.getText().toString().length() != 10 || confirmtracker.getText().toString().length() != 10)
                    {
                        Toast.makeText(getApplicationContext(), "Tracker ID must be 10 digits.", Toast.LENGTH_SHORT ).show();

                    }

                    Toast.makeText(getApplicationContext(), "Fields do not match! Please re-enter.", Toast.LENGTH_SHORT ).show();


                }
            }

            else if(view == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }
    };
}
