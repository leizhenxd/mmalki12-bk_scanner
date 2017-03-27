package com.example.malki.bkapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Activity activity;
    private TextView text;

    private String url = "https://www.bk-gts.com/telematics/devices/";
    private String url2 = "/status";

    private ImageView bkhome;
    private Typeface fontText;
    private Typeface fontButton;
    private String MSG;
    private boolean fromConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_tracker);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            MSG = extras.getString("MSG");
            fromConnect = extras.getBoolean("fromConnect");
        }



        activity = this;

        bkhome = (ImageView) findViewById(R.id.bkhome);
        text = (TextView) findViewById(R.id.textView15);
        scanTracker = (Button) findViewById(R.id.button11);

        text.setTypeface(fontText);
        scanTracker.setTypeface(fontButton);


        text.setText(MSG);
        bkhome.setOnClickListener(handler);

        scanTracker.setOnClickListener(handler);

        //if(!fromConnect)
            //checkTrackerConnection();

    }
//
    public void checkTrackerConnection()
    {
        new MyAsyncTask().execute();
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
        }
    };

    protected void scan(View view)
    {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {


//            dispatchTakePictureIntent();
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

                trackerID = scanResult.getContents();

                if (trackerID.length() == 10)
                {
                    if(fromConnect)
                    {
                        Toast.makeText(this, "Success - Black Knight ID " + trackerID + " captured.", Toast.LENGTH_SHORT).show();

                        //new MyAsyncTask().execute();
                        Intent connect = new Intent(ScanTracker.this, InsertTracker.class);
                        connect.putExtra("trackerID", trackerID);
//                    connect.putExtra("fromConnect", true);
                        ScanTracker.this.startActivity(connect);
                    }

                    else if (!fromConnect)
                    {
                        Toast.makeText(this, "Success - Black Knight ID " + trackerID + " captured.", Toast.LENGTH_SHORT).show();


                        Intent connect = new Intent(ScanTracker.this, ScanVIN.class);
                        connect.putExtra("trackerID", trackerID);
                        connect.putExtra("fromConnect", false);
                        connect.putExtra("display1", "Black Knight ID ");
                        connect.putExtra("display2", " disconnected. Now please scan the VIN barcode");
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
                        .setSmallIcon(R.mipmap.launcher).setStyle(new Notification.BigTextStyle().bigText(msg)).getNotification();

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
