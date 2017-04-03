package com.example.malki.bktelematics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.test.mock.MockPackageManager;
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
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ConfirmPairing extends Activity {

    private String trackerID;
    private String VIN;

    private Button yes;
   // private Button no;

    private String autho;
    private String time;
    private String iVector;
    private SharedPreferences pref;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    private double lat;
    private String ending;
    private double lon;

    private ImageView animation;
    private AnimationDrawable ani;

    private Typeface fontText;
    private boolean manual;
    private Typeface fontButton;


    private String url = "https://www.bk-gts.com/telematics/vehicles/pairing";

    private TextView text2;
    private TextView text3;
    private TextView text;
    private Boolean confirmed;
    private ImageView bkhome;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pairing);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        confirmed = false;
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");
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
        text2 = (TextView) this.findViewById(R.id.textView33);
        text3 = (TextView) this.findViewById(R.id.textView34);
        animation = (ImageView) this.findViewById(R.id.imageView10);
        animation.setBackgroundResource(R.drawable.pairinganimation);
        ani = (AnimationDrawable) animation.getBackground();

        //text.setText(MSG1 + trackerID + MSG2 + ending + VIN );

        text2.setText("You're pairing ID " + trackerID + " with vehicle VIN " + ending + VIN);
        text3.setText("Please confirm this is correct and that tracker " + trackerID + " has been inserted");


        yes = (Button) this.findViewById(R.id.button8);
        //no = (Button) this.findViewById(R.id.button9);

        text2.setTypeface(fontText);
        text3.setTypeface(fontText);
        text.setTypeface(fontText);
        yes.setTypeface(fontButton);
        //no.setTypeface(fontButton);

        yes.setOnClickListener(handler);
        //no.setOnClickListener(handler);
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

        ani.start();

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

                Intent connect = new Intent(ConfirmPairing.this, ConfirmPairing.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ConfirmPairing.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ConfirmPairing.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ConfirmPairing.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ConfirmPairing.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(ConfirmPairing.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ConfirmPairing.this.startActivity(connect);
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
            if (v == yes)
            {
                if (confirmed)
                {
                    Intent connect = new Intent(ConfirmPairing.this, MainActivity.class);
                    ConfirmPairing.this.startActivity(connect);
                }

                else {

                    ani.stop();
                    //POST TO BLACK KNIGHT DB

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+11:00"));
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, yyyy/MM/dd");
                    time = sdf.format(cal.getTime());

//                    gps = new GPSTracker(ConfirmPairing.this);
//
//                    if (gps.canGetLocation()) {
//                        lat = gps.getLatitude();
//                        lon = gps.getLongitude();
//                    } else {
//                        gps.showSettingsAlert();
//                        Toast.makeText(getApplicationContext(), "Could not find location", Toast.LENGTH_SHORT).show();
//                    }


                    new MyAsyncTask().execute(trackerID, VIN);


                    String txt = "PAIRING COMPLETE! Black Knight ID " + trackerID + " has been successfully paired with VIN " + ending + VIN;
                    confirmed = true;

                    text.setText("SUCCESS");
                    text2.setText("Pairing Complete");

                    text3.setVisibility(View.INVISIBLE);
                    animation.setImageResource(R.drawable.bkanimation01);

                    yes.setText("HOME");

                    Toast.makeText(v.getContext(), txt, Toast.LENGTH_SHORT).show();
                    // Flash to SAY successfully paired ID and VIN
                }

            }




            else if(v == bkhome)
            {
                Intent connect = new Intent(ConfirmPairing.this, MainActivity.class);
                ConfirmPairing.this.startActivity(connect);
            }

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
            }
        }

    };

   private void handleResponse(String responseStr)
    {

        Log.i("ConfirmPairing Response", responseStr);
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
            Log.i("###", url);
            try {
                // Add your data


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sn", sn));
                nameValuePairs.add(new BasicNameValuePair("vinfull", vinfull));
                nameValuePairs.add(new BasicNameValuePair("pic", ImagePathCache.getPicBase64()));
                if("".equals(ImagePathCache.picturePath)) {
                    Log.i("###pic size", ImagePathCache.manualPictrueBase64.length()/1000 + "");
                    nameValuePairs.add(new BasicNameValuePair("pic", ImagePathCache.manualPictrueBase64));
                }
                else {
                    nameValuePairs.add(new BasicNameValuePair("pic", ImagePathCache.getPicBase64()));
                }
                if("".equals(ImagePathCache.vinPicturePath)) {
                    Log.i("###vinpic size", ImagePathCache.manuaVinPictrueBase64.length()/1000 + "");
                    nameValuePairs.add(new BasicNameValuePair("vinpic", ImagePathCache.manuaVinPictrueBase64));
                }
                else {
                    nameValuePairs.add(new BasicNameValuePair("vinpic", ImagePathCache.getVinPicBase64()));
                }
                nameValuePairs.add(new BasicNameValuePair("vinpic", ImagePathCache.getVinPicBase64()));
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
                Log.i("###", "begin post");
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());
                Log.i("###", responseStr);
                httppost.releaseConnection();
                httppost.getEntity().consumeContent();
                httpclient.getConnectionManager().shutdown();


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
