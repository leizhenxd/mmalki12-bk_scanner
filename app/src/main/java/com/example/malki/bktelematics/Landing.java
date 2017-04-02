package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;

public class Landing extends Activity {

    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button num0;
    private ImageButton numback;
    private int count = 0;
    private String pinString;
    private Typeface font;

    private TextView text;

    private ImageView first;
    private ImageView help;
    private ImageView second;
    private ImageView third;
    private ImageView fourth;
    private String pin;

    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //startService(new Intent(Landing.this, FetchDisconnectTrackers.class));

        pref = getSharedPreferences("myfile", 0);
        boolean value = pref.getBoolean(getString(R.string.verified_number), false);
        pin = pref.getString(getString(R.string.pin), null);

//        if (!value)
//        {
//            Intent connect = new Intent(Landing.this, BKSigninActivity.class);
//            Landing.this.startActivity(connect);
//        }

        font = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        text = (TextView) findViewById(R.id.textView23);
        num0 = (Button) findViewById(R.id.button_0);
        num1 = (Button) findViewById(R.id.button_1);
        num2 = (Button) findViewById(R.id.button_2);
        num3 = (Button) findViewById(R.id.button_3);
        num4 = (Button) findViewById(R.id.button_4);
        num5 = (Button) findViewById(R.id.button_5);
        num6 = (Button) findViewById(R.id.button_6);
        num7 = (Button) findViewById(R.id.button_7);
        num8 = (Button) findViewById(R.id.button_8);
        num9 = (Button) findViewById(R.id.button_9);
        numback = (ImageButton) findViewById(R.id.button_backspace);
        //numback.setText("<");
        help = (ImageView) findViewById(R.id.imageView13);
        first = (ImageView) findViewById(R.id.light1);
        second = (ImageView) findViewById(R.id.light2);
        third = (ImageView) findViewById(R.id.light3);
        fourth = (ImageView) findViewById(R.id.light4);
        first.setAlpha(127);
        second.setAlpha(127);
        third.setAlpha(127);
        fourth.setAlpha(127);

        text.setText("ENTER YOUR PIN");
        text.setTypeface(font);
        text.setTextColor(getResources().getColor(R.color.colorHint));
        num0.setOnClickListener(handler);
        num1.setOnClickListener(handler);
        num2.setOnClickListener(handler);
        num3.setOnClickListener(handler);
        num4.setOnClickListener(handler);
        num5.setOnClickListener(handler);
        num6.setOnClickListener(handler);
        num7.setOnClickListener(handler);
        num8.setOnClickListener(handler);
        num9.setOnClickListener(handler);
        numback.setOnClickListener(handler);
        help.setOnClickListener(handler);


        count = 0;
        pinString = "";

    }

    View.OnClickListener handler = new View.OnClickListener() {

        public void onClick(View v) {

            if(v == num0)
            {
                pinString = pinString.concat("0");
                count++;

            }

            else if (v == num1)
            {
                pinString = pinString.concat("1");
                count++;
            }

            else if (v == num2)
            {
                pinString = pinString.concat("2");
                count++;
            }

            else if (v == num3)
            {
                count++;
                pinString = pinString.concat("3");
            }

            else if (v == num4)
            {
                pinString = pinString.concat("4");
                count++;
            }

            else if (v == num5)
            {
                pinString = pinString.concat("5");
                count++;
            }

            else if (v == num6)
            {
                pinString = pinString.concat("6");
                count++;
            }

            else if (v == num7)
            {
                pinString = pinString.concat("7");
                count++;
            }

            else if (v == num8)
            {
                pinString = pinString.concat("8");
                count++;
            }

            else if (v == num9)
            {
                pinString = pinString.concat("9");
                count++;
            }

            else if (v == numback)
            {
                if(count > 0)
                {
                    count--;


                    int length = pinString.length();

                    if (length == 1)
                    {
                        pinString = "";
                        //count = -1;
                    }

                    else if (length > 1)
                    {
                        //count = pinString.length() - 2;
                        pinString =  pinString.replaceAll(pinString, pinString.substring(0, pinString.length() -2));
                    }
                }


            }

            else if(v == help)
            {
                Intent connect = new Intent(Landing.this, Help.class);
                connect.putExtra("verified", false);
                Landing.this.startActivity(connect);
            }


            if(count == 1)
            {
                first.setImageResource(R.drawable.pinon);
                first.setAlpha(200);
                second.setAlpha(127);
                third.setAlpha(127);
                fourth.setAlpha(127);
                second.setImageResource(R.drawable.pinoff);
                third.setImageResource(R.drawable.pinoff);
                fourth.setImageResource(R.drawable.pinoff);
                //count++;
            }

            else if(count == 2)
            {
                first.setImageResource(R.drawable.pinon);
                second.setImageResource(R.drawable.pinon);

                first.setAlpha(200);
                second.setAlpha(200);
                third.setAlpha(127);
                fourth.setAlpha(127);
                third.setImageResource(R.drawable.pinoff);
                fourth.setImageResource(R.drawable.pinoff);
                //count++;
            }

            else if(count == 3)
            {
                first.setImageResource(R.drawable.pinon);
                first.setImageResource(R.drawable.pinon);
                third.setImageResource(R.drawable.pinon);

                first.setAlpha(200);
                second.setAlpha(200);
                third.setAlpha(200);
                fourth.setAlpha(127);
                fourth.setImageResource(R.drawable.pinoff);
                //count++;
            }

            else if (count == 4)
            {
                first.setImageResource(R.drawable.pinon);
                first.setImageResource(R.drawable.pinon);
                third.setImageResource(R.drawable.pinon);
                fourth.setImageResource(R.drawable.pinon);
                //count++;

                first.setAlpha(200);
                second.setAlpha(200);
                third.setAlpha(200);
                fourth.setAlpha(200);
                Log.i("Length", Integer.toString(pinString.length()));
                Log.i("pin entered", pinString);
                if(pinString.length() == 4)
                {
                    if (checkPIN(pinString))
                    {
                        first.setAlpha(255);
                        second.setAlpha(255);
                        third.setAlpha(255);
                        fourth.setAlpha(255);
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        pinString = "";

                        Intent service = new Intent(Landing.this, FetchDisconnectTrackers.class);

                        service.putExtra("oauth", pref.getString(getString(R.string.oauth), null));
                        service.putExtra("ivector", pref.getString(getString(R.string.ivector), null));

                        //startService(service);


                        Intent connect = new Intent(Landing.this, MainActivity.class);

                        //Intent connect = new Intent(Landing.this, ActivityLog.class);
                        Landing.this.startActivity(connect);

                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to authenticate! Try Again.", Toast.LENGTH_SHORT).show();
                        first.setAlpha(127);
                        second.setAlpha(127);
                        third.setAlpha(127);
                        fourth.setAlpha(127);

                        first.setImageResource(R.drawable.pinoff);
                        second.setImageResource(R.drawable.pinoff);
                        third.setImageResource(R.drawable.pinoff);
                        fourth.setImageResource(R.drawable.pinoff);
                        count = 0;
                        pinString = "";
                    }
                }


            }

            else if (count == 0)
            {
                first.setImageResource(R.drawable.pinoff);
                second.setImageResource(R.drawable.pinoff);
                third.setImageResource(R.drawable.pinoff);
                fourth.setImageResource(R.drawable.pinoff);

                first.setAlpha(127);
                second.setAlpha(127);
                third.setAlpha(127);
                fourth.setAlpha(127);
            }






        }

    };

    private Boolean checkPIN(String pinToCheck)
    {

        //Log.i("PIN SAVED", pin);

        if(pinToCheck.equals(pin))
        {
            return true;
        }

        else
        {
            return false;
        }
    }

}
