package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

public class LoginActivity extends Activity {

    private Button buttonSignin;
    private TextView createAccount;
    private TextView textv;
    private Typeface fontText;
    private Typeface fontButton;



    private EditText password;
    private String textVi = "Create new account";
    private String AUTHORISED = "Professional end-to-end vehicle fleet solutions.";
    private String UNAUTHORISED = "Professional end-to-end vehicle fleet solutions.";
//
//    private boolean permissionGIVEN;
    private Context context;
    private int duration = Toast.LENGTH_SHORT;
    private Toast toast;
    private CharSequence text;


    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");


        buttonSignin = (Button) this.findViewById(R.id.signinButton);
        createAccount = (TextView) this.findViewById(R.id.createAccount);
        password = (EditText) this.findViewById(R.id.passwordField);
        textv = (TextView) this.findViewById(R.id.textv);

        createAccount.setText(textVi);
        textv.setTypeface(fontText);
        buttonSignin.setTypeface(fontButton);
        createAccount.setTypeface(fontText);

//        password.setTypeface(font);

        buttonSignin.setOnClickListener(handler);
        createAccount.setOnClickListener(handler);



        pref = getSharedPreferences("myfile", 0);


        //SharedPreferences.Editor editor = pref.edit();
//        editor.putString(getString(R.string.x), "open");
        //editor.putInt(getString(R.string.pin), 123);

        //editor.commit();


        boolean value = pref.getBoolean(getString(R.string.verified_number), false);

        if (value)
        {
            textv.setText(AUTHORISED);
        }

        else
        {
            textv.setText(UNAUTHORISED);
        }

//        permissionGIVEN = false;
        context = getApplicationContext();
       // readCredentials();

//        runtimePermission();

    }



//    public void onRequestPermissionsResult(int requestCode, String [] permissions, int [] grantResults)
//    {
//        if (requestCode == 123)
//        {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                permissionGIVEN = true;
//            }
//        }
//    }




//    class VeryLongAsyncTask extends AsyncTask<Void, Void, Void> {
//        private final ProgressDialog progressDialog;
//
//        public VeryLongAsyncTask(Context ctx) {
//            progressDialog = CustomDialog.construct(ctx);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //textView.setVisibility(View.INVISIBLE);
//
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            // sleep for 5 seconds
//            try { Thread.sleep(5000); }
//            catch (InterruptedException e) { e.printStackTrace(); }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //textView.setVisibility(View.VISIBLE);
//
//            progressDialog.hide();
//        }
//    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {



            if (v == createAccount)
            {

                Intent connectIntent = new Intent(LoginActivity.this, BKSigninActivity.class);
                LoginActivity.this.startActivity(connectIntent);
            }

            else if (v == buttonSignin)
            {


                //new VeryLongAsyncTask(LoginActivity.this).execute();

                if (!(password.getText().toString().isEmpty()))
                {
                    String pinString = pref.getString(getString(R.string.pin), "00000");
                    //int pin = Integer.parseInt(pinString);

//                    Log.i("i", password.getText().toString());
//                    Log.i("i", String.valueOf(pin));
                    if (password.getText().toString().equals(pinString))
                    {
                        Intent service = new Intent(LoginActivity.this, FetchDisconnectTrackers.class);
                        service.putExtra("oauth", pref.getString(getString(R.string.oauth), null));
                        service.putExtra("ivector", pref.getString(getString(R.string.ivector), null));


                        //startService(service);
                        Intent connectIntent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(connectIntent);


                    }

                    else if (pinString.equals("00000"))
                    {
                        text = "Unauthorised device! Please create a new account.";
                        toast = Toast.makeText(context,text, duration);
                        toast.show();
                    }

                    else
                    {
                        text = "Incorrect pin. Please re-enter pin.";
                        toast = Toast.makeText(context,text, duration);
                        toast.show();
                        Log.i("entered:", password.getText().toString());



                    }



                }

                else
               {
                    text = "Please enter pin.";
                   toast = Toast.makeText(context,text, duration);
                   toast.show();
               }

//                else
//                {
//
////                    if (permissionGIVEN == true)
////                        sendSMS();
//
////                    else
//                    {
//                        text = "Unauthorised device! Please create a new account.";
//                        toast = Toast.makeText(context,text, duration);
//                        toast.show();
//                    }
//
//                }
//
//                startService(new Intent(LoginActivity.this, FetchDisconnectTrackers.class));
//                Intent connectIntent = new Intent(LoginActivity.this, MainActivity.class);
//               LoginActivity.this.startActivity(connectIntent);

            }
        }


    };

//    public void sendSMS()
//    {
//        try{
//
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage("+61416329363", null, "BLERGH", null, null);
//            Log.i("hit", sms.toString());
//        }
//
//        catch(Exception e)
//        {
//            Log.i("nope", e.toString());
//        }
//    }
//
//    private void readCredentials()
//    {
//        try
//        {
//            FileInputStream filein = openFileInput("data.txt");
//            InputStreamReader InputRead = new InputStreamReader(filein);
//
//            char [] inBuffer = new char[100];
//            String str="";
//            int i;
//
//            while((i=InputRead.read(inBuffer))>0)
//            {
//                String stringRead = String.copyValueOf(inBuffer, 0, i);
//                str += stringRead;
//            }
//
//            InputRead.close();
//            if (str.contains("Number : verified"))
//            {
//                textv.setText(AUTHORISED);
//
//            }
//
//            else
//            {
//                textv.setText(UNAUTHORISED);
//            }
//        }
//
//        catch (Exception e)
//        {
//
//            textv.setText(UNAUTHORISED);
//        }
//    }


}
