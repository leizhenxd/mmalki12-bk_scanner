package com.example.malki.bktelematics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.bktelematics.utils.PostUtil;
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

public class ResetPINActivity extends Activity {

    private EditText userName, password, pin, repin;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);
        findViewById(R.id.list).setVisibility(View.GONE);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");

        Button resetPin = (Button) findViewById(R.id.resetpin);
        resetPin.setTypeface(font);
        ((TextView) findViewById(R.id.title)).setTypeface(font);

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        pin = (EditText) findViewById(R.id.pin);
        repin = (EditText) findViewById(R.id.repin);

        pref = getSharedPreferences("myfile", 0);

        resetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty() == true) {
                    Toast.makeText(getApplicationContext(), "Please insert Username", Toast.LENGTH_SHORT).show();

                }
                else if (password.getText().toString().isEmpty() == true) {
                    Toast.makeText(getApplicationContext(), "Please insert Password", Toast.LENGTH_SHORT).show();
                }
                else if (pin.getText().toString().isEmpty() == true && pin.getText().toString().length() != 4) {
                    Toast.makeText(getApplicationContext(), "Please insert 4-digit PIN", Toast.LENGTH_SHORT).show();
                }
                else if (repin.getText().toString().isEmpty() == true || !repin.getText().toString().equals(pin.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "PIN's do not match. Please re-enter PIN\"", Toast.LENGTH_SHORT).show();
                }
                else {
                    new PostTask().execute(userName.getText().toString(), password.getText().toString());
                }
            }
        });

    }

    private class PostTask extends AsyncTask {
        ProgressDialog dialog = new CustomDialog(ResetPINActivity.this);

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(PostUtil.signUpUrl);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Log.e("ERROR", String.valueOf(android.os.Build.VERSION.SDK_INT));
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", objects[0].toString()));
                nameValuePairs.add(new BasicNameValuePair("password", objects[1].toString()));
//                nameValuePairs.add(new BasicNameValuePair("ivector", iVector));
                Log.i("###sign up username", objects[0].toString());
                Log.i("###sign up password", objects[1].toString());

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = EntityUtils.toString(response.getEntity());

                httppost.releaseConnection();
                httppost.getEntity().consumeContent();
                httpclient.getConnectionManager().shutdown();

                handlerResponse(responseStr);


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.i("CP Exception", e.toString());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("IO Exception", e.toString());
            }
            catch (RuntimeException e)
            {
                Log.i("Runtime Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    private void handlerResponse(String responseStr) {
        Log.i("###sign up", responseStr);
        if(!(responseStr.contains("Invalid username") || responseStr.contains("Invalid password"))) {


            String [] args;
            args = new Encryptor().encryptString(userName.getText().toString(), password.getText().toString());

            String dealerid = "";
            int startID = responseStr.length();
            Boolean present = false;
            for(int i = 0; i < responseStr.length(); i++) {
                if (responseStr.charAt(i) == 'D' && responseStr.charAt(i+1) == 'e' && i != responseStr.length()) {
                    startID = i + 10;
                    present = true;
                }

                if(present) {
                    if(responseStr.charAt(i) == ',') {
                        break;
                    }
                    else {
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
            editor.putString(getString(R.string.pin), pin.getText().toString());
            editor.commit();


            Intent connectIntent = new Intent(ResetPINActivity.this, Landing.class);
            ResetPINActivity.this.startActivity(connectIntent);

        }

        else
        {
            this.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Incorrect Username and/or Password", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
