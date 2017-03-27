package com.example.malki.bkapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.EntityBuilder;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.Header;
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

    private Typeface fontText;
    private Typeface fontButton;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bksignin);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        activity = this;

        signIn = (Button) this.findViewById(R.id.signin);
        bkUsername = (EditText) this.findViewById(R.id.editText2);
        bkPassword = (EditText) this.findViewById(R.id.editText4);
        textView = (TextView) this.findViewById(R.id.textView4);

        signIn.setTypeface(fontButton);
//        bkUsername.setTypeface(font);
        textView.setTypeface(fontText);
//        bkPassword.setTypeface(font);

        context = getApplicationContext();
        failed = false;


        pref = getSharedPreferences("myfile", 0);



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

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(getString(R.string.oauth), args[0]);
            editor.putString(getString(R.string.ivector), args[1]);
            editor.commit();

            //Log.i("response", String.valueOf(result));
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



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block

            } catch (IOException e) {
                // TODO Auto-generated catch block

            }
            catch (RuntimeException e)
            {
//                text = "Incorrect Username and/or Password";
//                toast = Toast.makeText(context,text, duration);
//                toast.show();
            }
        }

    }

}
