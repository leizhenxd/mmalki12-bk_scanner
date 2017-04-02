package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

import java.util.Random;

public class AuthenticationActivity extends Activity {

    private String MSG = "Your 6 digit code to register your Black Knight Scanner account is ";
    private String msg;
    private TextView resendSMS;
    private TextView message;
    private Button confirm;
    private EditText digitcode;
    private String confirmation;
    private Typeface fontText;
    private Typeface fontButton;

    private Context context;
    private int duration = Toast.LENGTH_SHORT;
    private Toast toast;
    private CharSequence text;

    private String mobile;
    private String firstName;
    private String lastName;
    private String pin;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Regular-Trial.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");


        resendSMS = (TextView) this.findViewById(R.id.textView3);
        message = (TextView) this.findViewById(R.id.textView2);
        confirm = (Button) this.findViewById(R.id.button3);
        digitcode = (EditText) this.findViewById(R.id.editText3);

        resendSMS.setTypeface(fontText);
        confirm.setTypeface(fontButton);
        message.setTypeface(fontText);
//        digitcode.setTypeface(font);

        context = getApplicationContext();

        pref = getSharedPreferences("myfile", 0);




        resendSMS.setOnClickListener(handler);
        confirm.setOnClickListener(handler);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobile = extras.getString("mobileno");
            firstName = extras.getString("fname");
            lastName = extras.getString("lname");
            pin = extras.getString("pin");

            //The key argument here must match that used in the other activity
        }


        runtimePermission();
        sendSMS();
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v)
        {

            if(v == resendSMS)
            {
                runtimePermission();
                sendSMS();
            }

            else if (v == confirm)
            {
                if(digitcode.getText().toString().equals(confirmation))
                {
                    Log.i("first name", firstName);
                    Log.i("last name", lastName);
                    Log.i("pin", pin);
                    Log.i("no", mobile);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(getString(R.string.verified_number), true);
                    editor.putString(getString(R.string.pin), pin);
                    editor.putString(getString(R.string.first_name), firstName);
                    editor.putString(getString(R.string.last_name), lastName);

                    editor.commit();




                    Intent connect = new Intent(AuthenticationActivity.this, SMSDigestActivity.class);
                    connect.putExtra("name", firstName);
                    AuthenticationActivity.this.startActivity(connect);
                    //confirms

                }

                else
                {
                    text = "Incorrect 6 digit code!";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                    ///toast message
                }
            }
        }
    };

    public void runtimePermission()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, 123);
        }

    }


    public void sendSMS()
    {
        try{
            String code = generateRandom();
            confirmation = code;
            msg = MSG + code;

            Log.i("MSG", MSG + code);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(mobile, null, msg, null, null);
            Log.i("HIT", sms.toString());
        }

        catch(Exception e)
        {

        }
    }

    private String generateRandom()
    {
        Random rnd = new Random();
        int number = 100000 + rnd.nextInt(900000);
        String str = Integer.toString(number);
        return str;
    }





}
