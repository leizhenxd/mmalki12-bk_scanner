package com.example.malki.bktelematics;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;


public class FetchDisconnectTrackers extends Service {

    private static String TAG = FetchDisconnectTrackers.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;
    private String oauth;
    private String ivector;
    int mStartMode;

    private String url = "https://www.bk-gts.com/telematics/dealers/121/devices/status";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            oauth = extras.getString("oauth");
            ivector = extras.getString("ivector");
        }

        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
        return mStartMode;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(TAG, "onCreate");
        mythread = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy");
        if (!isRunning) {
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Log.d(TAG, "onStart");

        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            oauth = extras.getString("oauth");
            ivector = extras.getString("ivector");
        }

        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
    }

//    public void readWebPage() {
//        HttpClient client = new DefaultHttpClient();
//        HttpGet request = new HttpGet(url);
//        // Get the response
//
//        request.addHeader("Authorization", oauth);
//        request.addHeader("ivector", ivector);
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        String response_str = null;
//        try {
//            response_str = client.execute(request, responseHandler);
//            if (!response_str.equalsIgnoreCase("")) {
//                Log.i("Response:", response_str);
//                Log.d(TAG, "Got Response");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    class MyThread extends Thread {
        static final long DELAY = 3000;

        @Override
        public void run() {
            while (isRunning) {
                //Log.d(TAG, "Running");
                try {
                    new MyAsyncTask().execute();

                   //new MyAsyncTask().execute();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }
//
//    private void handleResponse(String str){
//        if (str.contains("MacDetat"))
//        {
//
//        }
//    }
//
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
            HttpGet httppost = new HttpGet(url);

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
            }
        }
//
    }

}