package com.example.malki.bktelematics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
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
    private String url = "https://www.bk-gts.com/telematics/devices/disconnectreason";
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

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;


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

                Intent connect = new Intent(Message.this, Message.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(Message.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(Message.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(Message.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(Message.this, ActivityLog.class);
                        break;
                    case "CHANGE PIN":
                        connect = new Intent(Message.this, ResetPINActivity.class);
                        break;
                    case "HELP":
                        connect = new Intent(Message.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                Message.this.startActivity(connect);
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
            }

            else if (view == no)
            {
                Intent connect = new Intent(Message.this, PromptDiscActivity.class);
                connect.putExtra("trackerID", trackerID);
                connect.putExtra("VIN", VIN);
                connect.putExtra("reason", textvalue);
                Message.this.startActivity(connect);

            }

            else if(view == hamburger)
            {

                drawer.openDrawer(mDrawerList);
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
            Intent connect = new Intent(Message.this, MainActivity.class);
            Message.this.startActivity(connect);
            super.onPostExecute(result);
        }
        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
            dialog.dismiss();



        }

        public void postData(String trackerID, String reason) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sn", trackerID));
                nameValuePairs.add(new BasicNameValuePair("reason", reason));
                nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
                nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
                nameValuePairs.add(new BasicNameValuePair("mobile", mobileNumber));
                nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
                nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lon)));
                nameValuePairs.add(new BasicNameValuePair("date", time));
                if("".equals(ImagePathCache.picturePath)) {
                    Log.i("###pic size", ImagePathCache.manualPictrueBase64.length()/1000 + "");
                    nameValuePairs.add(new BasicNameValuePair("pic", ImagePathCache.manualPictrueBase64));
                }
                else {
                    nameValuePairs.add(new BasicNameValuePair("pic", ImagePathCache.getPicBase64()));
                }
                if("".equals(ImagePathCache.vinPicturePath)) {
                    Log.i("###vinpic size", ImagePathCache.manuaVinPictrueBase64.length()/1000 + "");
                    nameValuePairs.add(new BasicNameValuePair("vinPic", ImagePathCache.manuaVinPictrueBase64));
                }
                else {
                    nameValuePairs.add(new BasicNameValuePair("vinPic", ImagePathCache.getVinPicBase64()));
                }

//                httppost.addHeader("username", uName);
//                httppost.addHeader("password", pWord);
                httppost.addHeader("Authorization", autho);
                httppost.addHeader("ivector", iVector);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                Log.i("###", "begin disconnect post");
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());
                Log.i("###response", responseStr);

                httppost.releaseConnection();
                httpclient.getConnectionManager().shutdown();


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
