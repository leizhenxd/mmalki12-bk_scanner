package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

public class MainActivity extends Activity {

    private Button buttonConnect;
    private Button buttonDisconnect;
    private Typeface fontText;
    private Typeface fontButton;
    private ImageView bkhome;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

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

                Intent connect = new Intent(MainActivity.this, MainActivity.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(MainActivity.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(MainActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(MainActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(MainActivity.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(MainActivity.this, Help.class);
                        connect.putExtra("verified", true);

                        break;
                }

                MainActivity.this.startActivity(connect);
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
                if(v == buttonConnect)
                {
                    Intent connectIntent = new Intent(MainActivity.this, ScanTracker.class);
//                    connectIntent.putExtra("trackerID", "1223144145");
                    connectIntent.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                    connectIntent.putExtra("fromConnect", true);
                    MainActivity.this.startActivity(connectIntent);
                }

                else if(v == buttonDisconnect)
                {
                   Intent connectIntent = new Intent(MainActivity.this, ScanTracker.class);
                    connectIntent.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                    connectIntent.putExtra("fromConnect", false);
                    MainActivity.this.startActivity(connectIntent);
                }

                else if(v == bkhome)
                {
                    Intent connect = new Intent(MainActivity.this, MainActivity.class);
                    MainActivity.this.startActivity(connect);
                }


                else if(v == hamburger)
                {

                    drawer.openDrawer(mDrawerList);
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
