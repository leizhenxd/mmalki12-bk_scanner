package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.malki.telematics.R;

public class SMSDigestActivity extends Activity {

    private TextView textFirst;
    private TextView textSecond;

    private Typeface buttonFont;
    private Typeface textFont;
    private Button confirm;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdigest);

        textFont = Typeface.createFromAsset(getAssets(), "GT-Pressura-Regular-Trial.otf");
        buttonFont = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        textFirst = (TextView) findViewById(R.id.textView24);
        textSecond = (TextView) findViewById(R.id.textView4);



        textFirst.setTypeface(textFont);
        textSecond.setTypeface(textFont);


        confirm = (Button) findViewById(R.id.button16);

        confirm.setTypeface(buttonFont);
        confirm.setOnClickListener(handler);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
        }

//        textFirst.setText("Thanks, " + name + ". That's all done.");
//        textSecond.setText("From now on, you can login each time with just your 4-digit PIN.");








    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if(view == confirm)
            {
                Intent connect = new Intent(SMSDigestActivity.this, Landing.class);
                SMSDigestActivity.this.startActivity(connect);
            }
        }
    };
}
