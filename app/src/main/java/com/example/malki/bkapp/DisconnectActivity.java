package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class DisconnectActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView list;
    private TextView text;
    private Typeface font;


    private String msg = "Thanks. Now please select a reason for this disconnection.";
    private Typeface fontText;
    private Typeface fontButton;
    private String trackerID;
    private String VIN;
    private ImageView bkhome;

    private String [] item;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disconnect);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            VIN = extras.getString("VIN");
        }

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");
        bkhome = (ImageView) findViewById(R.id.bkhome);

        text = (TextView) findViewById(R.id.textView16);
        list = (ListView) findViewById(R.id.disconnectReasonList);

        text.setText(msg);

        text.setTypeface(fontText);


        item = getResources().getStringArray(R.array.disconnectReason);
        List <String> item1 = Arrays.asList(item);




        ArrayAdapter listA =  new CustomListAdapter(DisconnectActivity.this, R.layout.listdis, item1);



        list.setAdapter(listA);
        bkhome.setOnClickListener(handler);
        list.setOnItemClickListener(this);

    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if (v == bkhome)
            {
                Intent connect = new Intent(DisconnectActivity.this, MainActivity.class);
                DisconnectActivity.this.startActivity(connect);

            }
        }
    };

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

        String reason = ((TextView) view).getText().toString();

        Intent connect = new Intent(DisconnectActivity.this, PromptDiscActivity.class);
        connect.putExtra("reason", reason);
        connect.putExtra("trackerID", trackerID);
        connect.putExtra("VIN", VIN);

        DisconnectActivity.this.startActivity(connect);
//        if (reason.equals("Sale"))
//        {
//
//        }
//
//        else if (reason.equals("Service"))
//        {
//
//        }
//
//        else if(reason.equals("Dealership Exchange"))
//        {
//
//        }
//
//
//        else if(reason.equals("Other"))
//        {
//
//        }
//
//        else if(reason.equals("Unknown"))
//        {
//
//        }


    }
}
