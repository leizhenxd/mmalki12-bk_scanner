package com.example.malki.bktelematics;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

import java.text.SimpleDateFormat;
//import java.time.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class InsertTracker extends Activity {

    private String trackerID;
    private Button button;
    private TextView textView;
    private NotificationManager mnotification;

    private String FIRST_HALF = "Tracker ";
    private String SECOND_HALF = " successfully captured.";

    private Notification.Builder builder;
    private Typeface fontText;
    private Typeface fontButton;

    private ImageView bkhome;
    private SharedPreferences pref;

    private TextView textSecond;
    private TextView hint;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    private String url = "https://www.bk-gts.com/telematics/devices/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tracker);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        button = (Button) this.findViewById(R.id.button2);
        textView = (TextView) this.findViewById(R.id.text3);
        hint = (TextView) this.findViewById(R.id.textView26);
        textSecond = (TextView) this.findViewById(R.id.textView25);

        button.setTypeface(fontButton);
        textView.setTypeface(fontText);

        hint.setTypeface(fontText);
        textSecond.setTypeface(fontText);

        pref = getSharedPreferences("myfile", 0);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
        }

        builder = new Notification.Builder(this);

        bkhome.setOnClickListener(handler);

        textView.setText(FIRST_HALF + trackerID + SECOND_HALF);

        button.setOnClickListener(handler);

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

                Intent connect = new Intent(InsertTracker.this, InsertTracker.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(InsertTracker.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(InsertTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(InsertTracker.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(InsertTracker.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(InsertTracker.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                InsertTracker.this.startActivity(connect);
            }
        });
    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }


    View.OnClickListener handler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if (v == button)
            {
//                String [] array;
//                array = new Encryptor().encryptString("12345678");
//
//                Log.i("oauth", array[0]);
//                Log.i("iVector", array[1]);

                //new MyAsyncTask().execute(array[0], array[1]);
                url = url + trackerID + "/status";
                new MyAsyncTask().execute(pref.getString(getString(R.string.oauth), null), pref.getString(getString(R.string.ivector), null));

                //MAKE API CALL TO CHECK TRACKER HAS BEEN INSERTED


                Intent connect = new Intent(InsertTracker.this, CaptureMessage.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("fromConnect", true);
                connect.putExtra("display1", "Black Knight ID ");
                connect.putExtra("display2", " connected. Please scan the VIN barcode");
                InsertTracker.this.startActivity(connect);





            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(InsertTracker.this, MainActivity.class);
                InsertTracker.this.startActivity(connect);
            }

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }

    };

    public void handleResponse(String str)
    {
        if(str.contains("MacNew"))
        {

            Toast.makeText(this, "Black Knight ID " + trackerID + " connected.", Toast.LENGTH_LONG).show();

            //push notification

            if(Build.VERSION.SDK_INT >= 16)
            {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                String test = sdf.format(cal.getTime());

                //Intent intent = new Intent(this, ScanVIN.class);
                String msg = "Black Knight ID " + trackerID + " - connection detected at " + test;
                //PendingIntent pintent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                Notification not = new Notification.Builder(this).setContentText("Tracker " + trackerID + " connected")
                        .setSmallIcon(R.mipmap.ic_launcher).setStyle(new Notification.BigTextStyle().bigText(msg)).getNotification();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                not.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, not);
            }




            Intent connect = new Intent(InsertTracker.this, ScanVIN.class);
            connect.putExtra("trackerID", trackerID);
            connect.putExtra("display1", "Thank you for installing Black Knight ID ");
            connect.putExtra("display2", ". Now please scan the vehicle's VIN");
            connect.putExtra("fromConnect", true);
            InsertTracker.this.startActivity(connect);

        }


        else
        {
            Toast.makeText(this, "Black Knight ID " + trackerID + " not connected! Please connect and try again.", Toast.LENGTH_LONG).show();


//            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
//            String test = sdf.format(cal.getTime());
//
//            Intent intent = new Intent(this, ScanVIN.class);
//            PendingIntent pintent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
//
//            Notification not = new Notification.Builder(this).setContentText("Tracker " + trackerID + " connected")
//                    .setContentText("Black Knight ID " + trackerID + " - connection detected at " + test).setSmallIcon(R.drawable.bk).getNotification();
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//            not.flags |= Notification.FLAG_AUTO_CANCEL;
//
//            notificationManager.notify(0, not);

        }



    }


    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new CustomDialog(InsertTracker.this);

        @Override
        protected void onPreExecute()
        {
            //dialog.setMessage("Checking tracker " + trackerID + "'s connection");
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
            HttpGet httppost = new HttpGet(url);

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


    //GET REQUEST TO CHECK IF CONNECTED
}
