package com.example.malki.bktelematics;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ScanTracker extends Activity {

    private String trackerID;

    private Button scanTracker;
    private Button manual;
    private Activity activity;
    private TextView text;
    private TextView text2;
    private ImageView arrow;
    private TextView text3;

    private String url = "https://www.bk-gts.com/telematics/devices/";
    private String url2 = "/status";

    private ImageView bkhome;
    private Typeface fontText;
    private Typeface fontButton;
    private String MSG;
    private boolean fromConnect;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_tracker);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Regular-Trial.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            MSG = extras.getString("MSG");
            fromConnect = extras.getBoolean("fromConnect");
        }


        arrow = (ImageView) findViewById(R.id.imageView11);
        if (fromConnect)
        {
            arrow.setVisibility(View.INVISIBLE);
        }


        activity = this;
        text2 =
                (TextView) findViewById(R.id.textView35);
        text3 = (TextView) findViewById(R.id.textView36);
        bkhome = (ImageView) findViewById(R.id.bkhome);
        text = (TextView) findViewById(R.id.textView15);
        scanTracker = (Button) findViewById(R.id.button17);
        manual = (Button) findViewById(R.id.button11);
        manual.setTypeface(fontButton);

        manual.setOnClickListener(handler);

        text2.setTypeface(fontText);
        text3.setTypeface(fontText);
        text.setTypeface(fontText);
        scanTracker.setTypeface(fontButton);


        text.setText(MSG);
        bkhome.setOnClickListener(handler);

        scanTracker.setOnClickListener(handler);

        //if(!fromConnect)
            //checkTrackerConnection();
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

                Intent connect = new Intent(ScanTracker.this, ScanTracker.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ScanTracker.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ScanTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ScanTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ScanTracker.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(ScanTracker.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ScanTracker.this.startActivity(connect);
            }
        });

    }
//
    public void checkTrackerConnection()
    {
        new MyAsyncTask().execute();
    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == scanTracker)
            {

                scan(v);
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(ScanTracker.this, MainActivity.class);
                ScanTracker.this.startActivity(connect);
            }

            else if (v == manual)
            {
                Intent connect = new Intent(ScanTracker.this, ManualTracker.class);
                connect.putExtra("fromConnect", fromConnect);
                ScanTracker.this.startActivity(connect);
            }

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }
    };

    protected void scan(View view)
    {
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


//            dispatchTakePictureIntent();
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        ImagePathCache.picturePath = intent.getStringExtra("SCAN_RESULT_IMAGE_PATH");

        if(scanResult != null)
        {
            if(scanResult.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }

            else
            {



                String type = scanResult.getFormatName();

                trackerID = scanResult.getContents();

                if (trackerID.length() == 10)
                {
                    if(fromConnect)
                    {
                        Toast.makeText(this, "Success - Black Knight ID " + trackerID + " captured.", Toast.LENGTH_SHORT).show();
                        //new MyAsyncTask().execute();
                        Intent connect = new Intent(ScanTracker.this, InsertTracker.class);
                        connect.putExtra("trackerID", trackerID);
                        connect.putExtra("fromConnect", true);
                        ScanTracker.this.startActivity(connect);
                    }

                    else if (!fromConnect)
                    {
                        Toast.makeText(this, "Success - Black Knight ID " + trackerID + " captured.", Toast.LENGTH_SHORT).show();


                        Intent connect = new Intent(ScanTracker.this, CaptureMessage.class);
                        connect.putExtra("trackerID", trackerID);
                        connect.putExtra("fromConnect", false);
                        //connect.putExtra("display1", "Black Knight ID ");
                        //connect.putExtra("display2", " disconnected. Now please scan the VIN barcode");
                        ScanTracker.this.startActivity(connect);
                    }

                }

                else
                {
                    Toast.makeText(this, "Invalid tracker barcode", Toast.LENGTH_SHORT).show();
                }






//
//                if (type.equals("CODE_39"))
//                {
//
//                    VIN.append(barcode);
//                    scanVINButton.setVisibility(View.INVISIBLE);
//                    VIN.setVisibility(View.VISIBLE);
//                    //vin = Integer.parseInt(barcode);
//                }
//
//                else
//                {
//                    tracker.append(barcode);
//                    scanTrackerButton.setVisibility(View.INVISIBLE);
//                    tracker.setVisibility(View.VISIBLE);
//                    //sn = Integer.parseInt(barcode);
//                }


            }



        }




//        if(!(VIN.getText().equals("VIN: ")) && !(tracker.getText().equals("Tracker Barcode: ")))
//        {
//            //marryToDatabase.setVisibility(View.VISIBLE);
//            // marty to database
//            new ConnectActivity.MyAsyncTask().execute(bkUsername, bkPassword);
//
//            Intent connect = new Intent(ConnectActivity.this, MainActivity.class);
//            ConnectActivity.this.startActivity(connect);
//        }
    }

    private void handleResponse(String str)
    {
        if (str.contains("MacDetatched"))
        {
            if(Build.VERSION.SDK_INT >= 16)
            {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                String test = sdf.format(cal.getTime());

                //Intent intent = new Intent(this, ScanVIN.class);
                String msg = "Black Knight ID " + trackerID + " - disconnection detected at " + test;
                //PendingIntent pintent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                Notification not = new Notification.Builder(this).setContentText("Tracker " + trackerID + " connected")
                        .setSmallIcon(R.mipmap.ic_launcher).setStyle(new Notification.BigTextStyle().bigText(msg)).getNotification();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                not.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, not);
            }
        }
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new CustomDialog(ScanTracker.this);

        @Override
        protected void onPreExecute()
        {
//            dialog.setMessage("Checking tracker " + trackerID + "'s connection");
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0], params[1]);
            return null;
        }

        protected void onPostExecute(Double result){
//            pb.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();

            dialog.dismiss();
            super.onPostExecute(result);
        }
        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
            dialog.dismiss();



        }

        public void postData(String autho, String iVector) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url+trackerID+url2);

            try {
                // Add your data
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uName));
//                nameValuePairs.add(new BasicNameValuePair("password", pWord));
//                nameValuePairs.add(new BasicNameValuePair("ivector", iVector));

//                httppost.addHeader("username", uName);
//                httppost.addHeader("password", pWord);
                httppost.addHeader("Authorization", autho);
                httppost.addHeader("ivector", iVector);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());

                httppost.releaseConnection();
                httpclient.getConnectionManager().shutdown();


                handleResponse(responseStr);



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                //doNotification();
            } catch (IOException e) {
                // TODO Auto-generated catch block

                //doNotification();
            }
            catch (RuntimeException e)
            {
                //doNotification();
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
            }
        }

    }

}
