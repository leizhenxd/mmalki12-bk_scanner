package com.example.malki.bkapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Message extends Activity {

    private Button yes;
    private Button no;
    private TextView text;
    private String textvalue;
    private String trackerID;
    private String VIN;

    private double lon;
    private double lat;

    private Typeface buttonFont;
    private String url = "https://www.bk-gts.com/telematics/devices";
    private String autho;
    private String iVector;
    private Typeface textFont;
    private SharedPreferences pref;

    private String firstName;
    private String lastName;
    private String mobileNumber;
    GPSTracker gps;
    private String time;

    private String msg = "Thanks. You wrote:\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        textFont = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        buttonFont = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");


        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            textvalue = extras.getString("text");
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");

        }

        pref = getSharedPreferences("myfile", 0);




        autho = pref.getString(getString(R.string.oauth), null);
        iVector = pref.getString(getString(R.string.ivector), null);
        firstName = pref.getString(getString(R.string.first_name), null);
        lastName = pref.getString(getString(R.string.last_name), null);
        mobileNumber = pref.getString(getString(R.string.last_name), null);


        yes = (Button) findViewById(R.id.button14);
        no = (Button) findViewById(R.id.button15);
        text = (TextView) findViewById(R.id.textView9);
        text.setText(msg + textvalue + "\n" + "\n" + "\n" + "Do you wish to submit this reason?");
        yes.setTypeface(buttonFont);
        no.setTypeface(buttonFont);
        text.setTypeface(textFont);

        yes.setOnClickListener(handler);
        no.setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view == yes)
            {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                time = sdf.format(cal.getTime());

                gps = new GPSTracker(Message.this);

                if(gps.canGetLocation())
                {
                    lat = gps.getLatitude();
                    lon = gps.getLongitude();
                }

                new MyAsyncTask().execute(trackerID, textvalue);
                Intent connect = new Intent(Message.this, MainActivity.class);
                Message.this.startActivity(connect);

            }

            else if (view == no)
            {
                Intent connect = new Intent(Message.this, PromptDiscActivity.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("VIN", VIN);
                connect.putExtra("reason", textvalue);
                Message.this.startActivity(connect);

            }
        }
    };

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new ProgressDialog(Message.this);

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Disconnecting  " + trackerID);
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

        public void postData(String trackerID, String reason) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url+trackerID+"/disconnectreason");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("reason", reason));
                nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
                nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
                nameValuePairs.add(new BasicNameValuePair("mobile", mobileNumber));
                nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
                nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lon)));
                nameValuePairs.add(new BasicNameValuePair("date", time));


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
                Log.i("CP Exception", e.toString());
                //doNotification();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("IO Exception", e.toString());

                //doNotification();
            }
            catch (RuntimeException e)
            {
                //doNotification();
                Log.i("Runtime Exception", e.toString());
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
            }
        }

    }

    public void handleResponse(String str)
    {
        String txt = "Thanks " + VIN + "status successfully updated";
        // CALL FROM MAIN THREAD FIRST!
        //Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
        Log.i("Response", str);
    }
}
