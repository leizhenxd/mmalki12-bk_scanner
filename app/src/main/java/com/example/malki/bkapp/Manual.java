package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Manual extends Activity {

    private String trackerID;
    private Boolean fromConnect;
    private TextView text;
    private ImageView view;
    private Button button;

    private Typeface fontButton;
    private ImageView bkhome;
    private Typeface fontText;

    private byte [] pic;

    private String MESSAGE = "If your Compliance Plate does not have a barcode, capture an image of the VIN number on your Compliance Plate.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            fromConnect = extras.getBoolean("fromConnect");
        }

        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        text = (TextView) this.findViewById(R.id.textView11);
        button = (Button) this.findViewById(R.id.button6);
        view = (ImageView) this.findViewById(R.id.imageView);

        text.setText(MESSAGE);
        text.setTypeface(fontText);
        button.setTypeface(fontButton);

        button.setOnClickListener(handler);
        bkhome.setOnClickListener(handler);


    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == button)
            {
                intentCompliance();

            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(Manual.this, MainActivity.class);
                Manual.this.startActivity(connect);
            }
        }

    };

    public void intentCompliance()
    {
        Intent takePhoto = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePhoto.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePhoto, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            pic = stream.toByteArray();
            String encoded = Base64.encodeToString(pic, Base64.DEFAULT);
            //view.setImageBitmap(image);

            Intent connect = new Intent(Manual.this, Compliance.class);
            connect.putExtra("trackerID", trackerID);
            connect.putExtra("pic", encoded);

            if(fromConnect){
                connect.putExtra("fromConnect", true);

            }

            else if (!fromConnect)
            {
                connect.putExtra("fromConnect", false);

            }
            Toast.makeText(getApplicationContext(), "Success - Compliance plate image captured", Toast.LENGTH_SHORT).show();
            Manual.this.startActivity(connect);
        }


    }


}
