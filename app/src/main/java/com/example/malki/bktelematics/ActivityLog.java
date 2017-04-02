package com.example.malki.bktelematics;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.malki.telematics.R;
import com.example.malki.bktelematics.CustomActivityAdapter.customButtonListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ActivityLog extends Activity implements customButtonListener{
    private List <String []> list;
    private String url1 = "https://www.bk-gts.com/telematics/dealers/";
    private String url2 = "/devices/status";
    private String oauth;
    private String ivector;
    private String serverResponse;
    private String dealerID;
    private SharedPreferences pref;
    private ListView listed;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    private ImageView home;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        pref = getSharedPreferences("myfile", 0);
        dealerID = pref.getString(getString(R.string.dealerID), null);
        oauth = pref.getString(getString(R.string.oauth), null);
        ivector = pref.getString(getString(R.string.ivector), null);

        AsyncTask async = new MyAsyncTask().execute();
        //list = getActivityLog();
        try {
            async.get(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        listed = (ListView) findViewById(R.id.activitylist);

        CustomActivityAdapter listA;

        if(list != null)
        {
            listA =  new CustomActivityAdapter(ActivityLog.this, R.layout.activityitem, list);
        }

        else
        {
            List<String []> trackersList = new ArrayList <String []> ();
            listA =  new CustomActivityAdapter(ActivityLog.this, R.layout.activityitem, trackersList);
        }

        listA.setCustomButtonListener(ActivityLog.this);

        listed.setAdapter(listA);
        listed.setFastScrollEnabled(true);


        title = (TextView) this.findViewById(R.id.pageTitle);
        home = (ImageView) this.findViewById(R.id.bkhome);

        home.setVisibility(View.GONE);
        title.setText("ACTIVITY LOG");
        title.setTextSize(18);
        title.setTextColor(getResources().getColor(R.color.colorButton));


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

                Intent connect = new Intent(ActivityLog.this, ActivityLog.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ActivityLog.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ActivityLog.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ActivityLog.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ActivityLog.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(ActivityLog.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ActivityLog.this.startActivity(connect);
            }
        });


    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == hamburger)
            {
                drawer.openDrawer(mDrawerList);
            }
        }


    };


    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }


    public void onButtonClickListener(String [] value) {
//        Toast.makeText(MainActivity.this, "Button click " + value,
//                Toast.LENGTH_SHORT).show();
//        Intent connect = new Intent(ActivityLog.this, DisconnectActivity.class);
//        connect.putExtra("trackerID", value[0]);
//        connect.putExtra("VIN", value[5]);
//        ActivityLog.this.startActivity(connect);


    }



    private void getActivityLog() throws JSONException {


        JSONArray array = new JSONArray(serverResponse);
        List<String []> trackersList = new ArrayList <String []> ();

        String [] stringReponse;
        for(int i = 0; i < array.length(); i++)
        {
            try {
                JSONObject object = array.getJSONObject(i);
                stringReponse = new String[8];

                stringReponse[0] = object.getString("sn");
                stringReponse[1] = object.getString("status");
                stringReponse[2] = object.getString("model");
                stringReponse[3] = object.getString("colour");
                stringReponse[4] = object.getString("pairedTime");
                stringReponse[5] = object.getString("vin");
                stringReponse[6] = object.getString("disconnectReason");
                stringReponse[7] = object.getString("trackerStatus");

                Log.i("sn", stringReponse[0]);
                Log.i("status", stringReponse[1]);
                Log.i("model", stringReponse[2]);
                Log.i("colour", stringReponse[3]);
                Log.i("pairedTime", stringReponse[4]);
                Log.i("vin", stringReponse[5]);
                Log.i("dR", stringReponse[6]);
                Log.i("tS", stringReponse[7]);

                trackersList.add(stringReponse);

            }

            catch (JSONException e)
            {
                Log.i("JSON Exception", e.toString());
            }
        }

        list = new ArrayList<>();

        for(int j = (trackersList.size() -1); j >= 0; j--)
        {
            list.add(trackersList.get(j));
        }







//        List<String[]> testList = new ArrayList<>();
//
//        String [] array1 = {"123456", "MacConnected", "Golf", "Silver", "14/12/2016", "888888", null, "Connected"};
//
//
//
//        String [] array2 = {"123456", "MacDisconnected", "White", "Prius", "15/12/2016", "888888", "SALE", "Disconnected"};
//
//        String [] array3 = {"123456", "MacConnected", "Red", "4W", "15/12/2016", "888888", null, "Disconnected"};
//
//        String [] array4 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//
//        String [] array5 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//        String [] array6 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//        String [] array7 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//        String [] array8 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//        String [] array9 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//        String [] array10 = {"123456", "MacDetatched", "Black", "m3", "16/12/2016", "888888", null, "Disconnected"};
//
//        testList.add(array1);
//        testList.add(array2);
//        testList.add(array3);
//        testList.add(array4);
//        testList.add(array5);
//        testList.add(array6);
//        testList.add(array7);
//        testList.add(array8);
//        testList.add(array9);
//        testList.add(array10);


//        return testList;
    }



    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        //ProgressDialog dialog = new CustomDialog(FetchDisconnectTrackers.this);

        @Override
        protected void onPreExecute()
        {
            //dialog.setMessage("Checking tracker " + trackerID + "'s connection");
            //dialog.show();
            super.onPreExecute();
        }


        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result){
//            pb.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();

            //dialog.dismiss();
            super.onPostExecute(result);
        }
        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
            //dialog.dismiss();



        }

        public void postData() {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url1+dealerID+url2);

            try {
                // Add your data
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uName));
//                nameValuePairs.add(new BasicNameValuePair("password", pWord));
//                nameValuePairs.add(new BasicNameValuePair("ivector", iVector));

//                httppost.addHeader("username", uName);
//                httppost.addHeader("password", pWord);
                httppost.addHeader("Authorization", oauth);
                httppost.addHeader("ivector", ivector);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());





                Log.i("Response: ", responseStr);
                serverResponse = responseStr;

                //serverResponse = "[{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"},{\"sn\":\"1802410495\",\"status\":\"MacDisconnected\",\"model\":\"prius\",\"colour\":\"black\",\"pairedTime\":\"12:03 12/12/1992\",\"vin\":\"123456\",\"disconnectReason\":\"0\",\"trackerStatus\":\"Disconnected\"}]";


                getActivityLog();
                //handleResponse(responseStr);



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                //doNotification();
                Log.i("CP Exception", e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("IOException", e.toString());
                //doNotification();
            }
            catch (RuntimeException e)
            {
                Log.i("Runtime Exception", e.toString());
                //doNotification();
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
            } catch (JSONException e) {
                //Log.i("JSON Exception", e.toString());
            }
        }
//
    }


}
