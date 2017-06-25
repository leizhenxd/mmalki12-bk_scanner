package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malki.telematics.R;
import com.example.malki.bktelematics.HelpAdapter.customButtonListener;

import java.util.ArrayList;

import java.util.List;

public class Help extends Activity implements customButtonListener {

    private boolean verified = false;
    private ListView listed;
    private List <String []> list;

    private ImageView home;
    private TextView title;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ImageView hamburger;

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        listed = (ListView) findViewById(R.id.helplist);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            verified = extras.getBoolean("verified");
        }

        list = getHelpPage();

        title = (TextView) this.findViewById(R.id.pageTitle);
        home = (ImageView) this.findViewById(R.id.bkhome);

        home.setVisibility(View.GONE);
        title.setText("HELP");
        title.setTextSize(18);
        title.setTextColor(getResources().getColor(R.color.colorButton));
        HelpAdapter listA =  new HelpAdapter(Help.this, R.layout.help_item, list);
        listA.setCustomButtonListener(Help.this);

        listed.setAdapter(listA);
        listed.setFastScrollEnabled(true);

        if(verified) {

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.navList);
            addDrawerItems();

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                    String itemPressed = ((TextView) view).getText().toString();

                    Intent connect = new Intent(Help.this, Help.class);
                    switch (itemPressed) {
                        case "HOME":
                            connect = new Intent(Help.this, MainActivity.class);
                            break;
                        case "CONNECT/NEW":
                            connect = new Intent(Help.this, ScanTracker.class);
                            connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                            connect.putExtra("fromConnect", true);
                            break;
                        case "DISCONNECT/SALE":
                            connect = new Intent(Help.this, ScanTracker.class);
                            connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                            connect.putExtra("fromConnect", false);
                            break;
                        case "ACTIVITY":
                            connect = new Intent(Help.this, ActivityLog.class);
                            break;
                        case "CHANGE PIN":
                            connect = new Intent(Help.this, ResetPINActivity.class);
                            break;
                        case "HELP":
                            connect = new Intent(Help.this, Help.class);
                            connect.putExtra("verified", true);
                            break;
                    }

                    Help.this.startActivity(connect);
                }
            });


        }

        else
        {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.setEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            hamburger = (ImageView) findViewById(R.id.list);
            hamburger.setVisibility(View.INVISIBLE);
        }
    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "CHANGE PIN", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
    public void onButtonClickListener(String [] value) {
    }

    public List<String []> getHelpPage()
    {
        List <String []> helpList = new ArrayList<String []>();
        String [] title;
        String [] body;
        String [] tempArray;
        if(verified)
        {
            title = getResources().getStringArray(R.array.help_title_verified);
            body = getResources().getStringArray(R.array.help_body_verified);

        }

        else
        {
            title = getResources().getStringArray(R.array.help_title_general);
            body = getResources().getStringArray(R.array.help_body_general);
        }

        Log.i("title.length", String.valueOf(title.length));
        for(int i = 0; i < title.length; i ++)
        {
            tempArray = new String[2];
            tempArray[0] = title[i];
            tempArray[1] = body[i];


            helpList.add(i, tempArray);


        }

        return helpList;
    }
}
