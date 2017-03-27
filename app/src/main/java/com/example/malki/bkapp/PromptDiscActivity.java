package com.example.malki.bkapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class PromptDiscActivity extends Activity {

    private String trackerID;
    private String reason;
    private String VIN;

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private double lon;
    private double lat;
    private String time;

    private String MSG = "This vehicle ";
    private String MSG2 = " Is this correct?";

    private String url = "https://www.bk-gts.com/telematics/devices";

    private TextView text;
    private Button confirm;
    private Button cancel;
    private EditText other;

    private ImageView bkhome;
    private String autho;
    private Typeface fontText;
    private Typeface fontButton;
    private String iVector;
    private String reasonString;

    private SharedPreferences pref;
    GPSTracker gps;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_disc);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");
            reason = extras.getString("reason");
        }

        text = (TextView) this.findViewById(R.id.textView);
        confirm = (Button) this.findViewById(R.id.button12);
        cancel = (Button) this.findViewById(R.id.button13);
        other = (EditText) this.findViewById(R.id.editText11);
        bkhome = (ImageView) this.findViewById(R.id.bkhome);

        text.setTypeface(fontText);
        confirm.setTypeface(fontButton);
        cancel.setTypeface(fontButton);

        pref = getSharedPreferences("myfile", 0);

        String [] args;
        args = new Encryptor().encryptString("AnnlynMotors", "12345678");

        autho = args[0];
        iVector = args[1];

        //autho = pref.getString(getString(R.string.oauth), null);
        //iVector = pref.getString(getString(R.string.ivector), null);
        firstName = pref.getString(getString(R.string.first_name), null);
        lastName = pref.getString(getString(R.string.last_name), null);
        mobileNumber = pref.getString(getString(R.string.last_name), null);





        other.setHint("Please type reason");
        reasonString = populateview();

        if (reasonString == null)
        {
            text.setText("Please enter OTHER reason for your disconnection.");

            reason = other.getText().toString();

        }

        else
        {
            other.setVisibility(View.INVISIBLE);
            text.setText(MSG + reasonString + MSG2);
        }
        confirm.setVisibility(View.VISIBLE);

        bkhome.setOnClickListener(handler);
        confirm.setOnClickListener(handler);
        cancel.setOnClickListener(handler);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == confirm)
            {




                if (reasonString == null)
                {
                    Intent connect = new Intent(PromptDiscActivity.this, Message.class);
                    connect.putExtra("trackerID", trackerID);
                    connect.putExtra("VIN", VIN);
                    String realReason = "\"" + other.getText().toString() + "\"";
                    connect.putExtra("text", realReason);
                    PromptDiscActivity.this.startActivity(connect);
                }

                else
                {
                    reason = other.getText().toString();

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                    time = sdf.format(cal.getTime());

                    gps = new GPSTracker(PromptDiscActivity.this);

                    if(gps.canGetLocation())
                    {
                        lat = gps.getLatitude();
                        lon = gps.getLongitude();
                    }

                    else
                    {
                        gps.showSettingsAlert();
                        Toast.makeText(getApplicationContext(), "Could not find location", Toast.LENGTH_SHORT).show();
                    }


                    new MyAsyncTask().execute(trackerID, reason);
                    Intent connect = new Intent(PromptDiscActivity.this, MainActivity.class);
                    PromptDiscActivity.this.startActivity(connect);
                }
            }

            else if (v == cancel)
            {
                Intent connect = new Intent(PromptDiscActivity.this, DisconnectActivity.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("VIN", VIN);
                PromptDiscActivity.this.startActivity(connect);
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(PromptDiscActivity.this, MainActivity.class);
                PromptDiscActivity.this.startActivity(connect);
            }
        }
    };

    public String populateview()
    {
        if (reason.equals("Sale"))
        {
            return "has been SOLD.";
        }

        else if (reason.equals("Service"))
        {
            return "is being moved to SERVICE.";
        }

        else if (reason.equals("Pre-Delivery"))
        {
            return "is being moved to PRE-DELIVERY.";
        }

        else if(reason.equals("Dealership Exchange"))
        {
            return "is being EXCHANGED with a vehicle from another dealership.";
        }

        else
            return null;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new ProgressDialog(PromptDiscActivity.this);

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
                Log.i("Runtime Exception", e.toString());
                //doNotification();
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
            }
        }

    }

    public void handleResponse(String str)
    {
        String txt = "Thanks " + VIN + "status successfully updated";
        //CALL FROM MAIN THREAD FIRST!!
        //Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
        Log.i("Response", str);
    }







}
