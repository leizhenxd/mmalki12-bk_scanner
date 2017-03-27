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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.content.ByteArrayBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ConfirmPairing extends Activity {

    private String trackerID;
    private String VIN;

    private Button yes;
    private Button no;

    private String autho;
    private String time;
    private String iVector;
    private SharedPreferences pref;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    private double lat;
    private double lon;


    private Typeface fontText;
    private boolean manual;
    private Typeface fontButton;


    private String url = "https://www.bk-gts.com/telematics/vehicles/pairing";

    private String MSG1 = "Do you wish to pair Black Knight ID ";
    private String ending;
    private String MSG2 = " with VIN ";

    private TextView text;
    private String picture;
    private ImageView bkhome;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pairing);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");


        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");
            picture = extras.getString("pic");
            manual = extras.getBoolean("manual");

        }

        if (manual)
            ending = "ending ";
        else
            ending = "";
        pref = getSharedPreferences("myfile", 0);

//        String [] args;
//        args = new Encryptor().encryptString("AnnlynMotors", "12345678");
//
//        autho = args[0];
//        iVector = args[1];

        // EDITED LINES!!!!
        autho = pref.getString(getString(R.string.oauth), null);
        iVector = pref.getString(getString(R.string.ivector), null);
        firstName = pref.getString(getString(R.string.first_name), null);
        lastName = pref.getString(getString(R.string.last_name), null);
        mobileNumber = pref.getString(getString(R.string.mobile), null);


        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        text = (TextView) this.findViewById(R.id.textView13);
        text.setText(MSG1 + trackerID + MSG2 + ending + VIN );

        yes = (Button) this.findViewById(R.id.button8);
        no = (Button) this.findViewById(R.id.button9);

        text.setTypeface(fontText);
        yes.setTypeface(fontButton);
        no.setTypeface(fontButton);

        yes.setOnClickListener(handler);
        no.setOnClickListener(handler);
        bkhome.setOnClickListener(handler);

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
            if (v == yes)
            {
                //POST TO BLACK KNIGHT DB

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                time = sdf.format(cal.getTime());

                gps = new GPSTracker(ConfirmPairing.this);

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




                new MyAsyncTask().execute(trackerID, VIN);



                String txt = "PAIRING COMPLETE! Black Knight ID " + trackerID + " has been successfully paired with VIN " + ending + VIN;
                Toast.makeText(v.getContext(), txt , Toast.LENGTH_SHORT).show();
                // Flash to SAY successfully paired ID and VIN

                Intent connect = new Intent(ConfirmPairing.this, MainActivity.class);
                ConfirmPairing.this.startActivity(connect);
            }

            else if (v == no)
            {
                Intent connect = new Intent(ConfirmPairing.this, MainActivity.class);
                ConfirmPairing.this.startActivity(connect);
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(ConfirmPairing.this, MainActivity.class);
                ConfirmPairing.this.startActivity(connect);
            }
        }

    };

   private void handleResponse(String responseStr)
    {

        Log.i("Response", responseStr);
//
//        if(!(responseStr.contains("Invalid username") || responseStr.contains("Invalid password")))
//        {
//
//
//            String [] args;
//            args = new Encryptor().encryptString(bkUsername.getText().toString(), bkPassword.getText().toString());
//
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString(getString(R.string.oauth), args[0]);
//            editor.putString(getString(R.string.ivector), args[1]);
//            editor.commit();
//
//            //Log.i("response", String.valueOf(result));
//            Intent connectIntent = new Intent(BKSigninActivity.this, SignupActivity.class);
//            BKSigninActivity.this.startActivity(connectIntent);
//
//        }
//
//        else
//        {
////            failed = true;
//        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new CustomDialog(ConfirmPairing.this);

        @Override
        protected void onPreExecute()
        {
//            dialog.setMessage("Verifying Black Knight credentials");
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

        public void postData(String sn, String vinfull) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                // Add your data


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sn", sn));
                nameValuePairs.add(new BasicNameValuePair("vinfull", vinfull));
                nameValuePairs.add(new BasicNameValuePair("pic", picture));
                nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
                nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
                nameValuePairs.add(new BasicNameValuePair("mobile", mobileNumber));
                nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
                nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lon)));
                nameValuePairs.add(new BasicNameValuePair("date", time));


                //ByteArrayBody bab = new ByteArrayBody(pic, "pic.jpg");


                httppost.addHeader("Authorization", autho);
                httppost.addHeader("ivector", iVector);



                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());




               handleResponse(responseStr);



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.i("CP Exception", e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("IO Exception", e.toString());

            }
            catch (RuntimeException e)
            {
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
                Log.i("Runtime Exception", e.toString());
            }
        }

    }

}
