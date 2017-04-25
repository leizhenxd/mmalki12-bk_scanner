package com.example.malki.bktelematics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class BKSigninActivity extends Activity {

    private Button signIn;
    private EditText bkUsername;
    private EditText bkPassword;
    private TextView textView;
    private String url = "https://www.bk-gts.com/telematics/users/verification";
    private boolean failed;
    private Context context;
    private Activity activity;
    private int duration = Toast.LENGTH_SHORT;
    private Toast toast;
    private CharSequence text;
    private ImageView help;

    private Typeface fontText;
    private Typeface fontButton;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bksignin);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        fontText = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        activity = this;

        signIn = (Button) this.findViewById(R.id.signin);
        bkUsername = (EditText) this.findViewById(R.id.editText2);
        bkPassword = (EditText) this.findViewById(R.id.editText4);
        help = (ImageView) this.findViewById(R.id.imageView16);
        //textView = (TextView) this.findViewById(R.id.textView4);

        signIn.setTypeface(fontButton);
//        bkUsername.setTypeface(font);
//        textView.setTypeface(fontText);
//        bkPassword.setTypeface(font);
        ((TextView)this.findViewById(R.id.textView20)).setTypeface(fontText);
        ((TextView)this.findViewById(R.id.textView21)).setTypeface(fontText);
        ((TextView)this.findViewById(R.id.textView22)).setTypeface(fontText);

        context = getApplicationContext();
        failed = false;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        pref = getSharedPreferences("myfile", 0);


        help.setOnClickListener(handler);
        signIn.setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if (v == signIn)
            {



                if (bkUsername.getText().toString().isEmpty() == true)
                {
                    text = "Please insert Username";

                    toast = Toast.makeText(context, text, duration);
                    toast.show();

                }

                else if(bkPassword.getText().toString().isEmpty() == true)
                {
                    text = "Please insert Password";

                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                else
                {


                    executePost(bkUsername.getText().toString(), bkPassword.getText().toString());



                }
            }

            else if(v == help)
            {
                Intent connect = new Intent(BKSigninActivity.this, Help.class);
                connect.putExtra("verified", false);
                BKSigninActivity.this.startActivity(connect);
            }
        }
    };

    public void executePost(String u, String p)
    {
        new MyAsyncTask().execute(u, p);
    }

    public void handleResponse(String responseStr)
    {


        if(!(responseStr.contains("Invalid username") || responseStr.contains("Invalid password")))
        {


            String [] args;
            args = new Encryptor().encryptString(bkUsername.getText().toString(), bkPassword.getText().toString());

            String dealerid = "";
            int startID = responseStr.length();
            Boolean present = false;
            for(int i = 0; i < responseStr.length(); i++)
            {
                if (responseStr.charAt(i) == 'D' && responseStr.charAt(i+1) == 'e' && i != responseStr.length())
                {
                    startID = i + 10;
                    present = true;
                }

                if(present)
                {
                    if(responseStr.charAt(i) == ',')
                    {

                        break;

                    }

                    else
                    {
                        if(startID == i)
                        {
                            dealerid = dealerid.concat(Character.toString(responseStr.charAt(i)));
                            startID++;
                        }
                    }

                }
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(getString(R.string.oauth), args[0]);
            editor.putString(getString(R.string.ivector), args[1]);
            editor.putString(getString(R.string.dealerID), dealerid);
            editor.commit();


            Intent connectIntent = new Intent(BKSigninActivity.this, SignupActivity.class);
            BKSigninActivity.this.startActivity(connectIntent);

        }

        else
        {
            activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(activity, "Incorrect Username and/or Password", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        ProgressDialog dialog = new CustomDialog(BKSigninActivity.this);

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

        public void postData(String uName, String pWord) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Log.e("ERROR", String.valueOf(android.os.Build.VERSION.SDK_INT));
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uName));
                nameValuePairs.add(new BasicNameValuePair("password", pWord));
//                nameValuePairs.add(new BasicNameValuePair("ivector", iVector));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());

                handleResponse(responseStr);

                httppost.releaseConnection();
                httppost.getEntity().consumeContent();
                httpclient.getConnectionManager().shutdown();



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
