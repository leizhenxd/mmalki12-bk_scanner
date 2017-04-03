package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malki.bktelematics.utils.ImagePathCache;
import com.example.malki.telematics.R;

import java.io.ByteArrayOutputStream;

public class ManualTrackerCapture extends Activity {

    private Boolean fromConnect;

    private Button button;

    private TextView textView;

    private ImageView image;

    private String trackerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_tracker_capture);

        button = (Button) findViewById(R.id.button22);

        image = (ImageView) findViewById(R.id.imageView17);

        textView = (TextView)findViewById(R.id.textView55);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            fromConnect = extras.getBoolean("fromConnect");
            trackerID = extras.getString("trackerID");
        }

        textView.setText("of tracking device " + trackerID);

        button.setOnClickListener(clickListerner);
    }

    View.OnClickListener clickListerner = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if("CONTINUE".equals(button.getText())) {
                intentCompliance();
            }
            else {
                if(fromConnect) {
                    Intent connect = new Intent(ManualTrackerCapture.this, InsertTracker.class);
                    connect.putExtra("fromConnect", fromConnect);
                    connect.putExtra("trackerID", trackerID);
                    ManualTrackerCapture.this.startActivity(connect);
                }
                else {
                    Intent connect = new Intent(ManualTrackerCapture.this, CaptureMessage.class);
                    connect.putExtra("fromConnect", fromConnect);
                    connect.putExtra("trackerID", trackerID);
                    ManualTrackerCapture.this.startActivity(connect);
                }
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
            byte[] pic = stream.toByteArray();
            String encoded = Base64.encodeToString(pic, Base64.DEFAULT);
            //view.setImageBitmap(image);

            ImagePathCache.picturePath = "";
            ImagePathCache.manualPictrueBase64 = encoded;

            this.image.setImageBitmap(image);
            ViewGroup.LayoutParams layoutParams = this.image.getLayoutParams();
            Log.i("####width", layoutParams.width+"");
            Log.i("####width", layoutParams.height+"");
            layoutParams.width += 50;
            this.image.setLayoutParams(layoutParams);

            button.setText("CONFIRM");
        }


    }
}
