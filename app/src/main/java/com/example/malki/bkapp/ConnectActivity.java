package com.example.malki.bkapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import static com.google.zxing.BarcodeFormat.CODE_39;

public class ConnectActivity extends Activity{

    Activity activity;
    private Button scanVINButton;
    private Button scanTrackerButton;
    private Button marryToDatabase;
    private TextView text;
    private TextView tracker;
    private ImageView mImageView;
    private String msg = "Please scan the VIN barcode on you vehicle's COMPLIANCE PLATE";
    private String trackerID;
    Bitmap mBitmap;
    private ImageView image;

    private Typeface fontText;
    private ImageView bkhome;
    private Typeface fontButton;

    private byte [] picture;
    private String bkUsername = "AnnLynMotors";
    private String bkPassword = "12345678";
    private int vin;
    private int sn;
    private FileOutputStream fos;
    private Typeface font;

    private boolean fromConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        activity  = this;
        fontText = Typeface.createFromAsset(getAssets(), "GT-Pressura-Mono-Regular.otf");
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");



        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            trackerID = extras.getString("trackerID");
            fromConnect = extras.getBoolean("fromConnect");
        }
        //image = (ImageView) this.findViewById(R.id.imageView2);
        bkhome = (ImageView) this.findViewById(R.id.bkhome);
        scanVINButton = (Button) this.findViewById(R.id.scanVINbutton);

        text = (TextView) this.findViewById(R.id.textView17);

        scanVINButton.setTypeface(fontButton);
        text.setTypeface(fontText);

        scanVINButton.setOnClickListener(handler);


        bkhome.setOnClickListener(handler);
        text.setText(msg);



    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if(v == scanVINButton)
            {
                scan(v);
                //takeSnapShots();
            }

            else if(v == bkhome)
            {
                Intent connect = new Intent(ConnectActivity.this, MainActivity.class);
                ConnectActivity.this.startActivity(connect);
            }
        }


    };


    protected void scan(View view)
    {
//        takePictureNoPreview(this.getApplicationContext());
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {



            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(scanResult != null)
            {
                if(scanResult.getContents() == null)
                {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                }

                else
                {

                    String type = scanResult.getFormatName();

                    String barcode = scanResult.getContents();

                    if (type.equals("CODE_39"))
                    {
                        //                    generateQRCode(scanResult.getContents());

                        // GENERATES THE IMAGE!!!!!!
                        String path = intent.getStringExtra("SCAN_RESULT_IMAGE_PATH");
//                    Uri pathuri = Uri.parse(path);
//                    image.setImageURI(pathuri);



                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 300 ,200,true);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        String txt = "Success - VIN: " + barcode + " captured.";

                        if (!fromConnect)
                            txt += "It will be unpaired from BK ID " +trackerID;

                        Toast.makeText(this,txt , Toast.LENGTH_SHORT).show();

//                    Bitmap bitmap = BitmapFactory.decodeByteArray(picture , 0, picture.length);
//                    image.setImageBitmap(bitmap);

                        if(fromConnect)
                        {
                            Intent connect = new Intent(ConnectActivity.this, ConfirmPairing.class);
                            connect.putExtra("trackerID", trackerID);
                            connect.putExtra("VIN", barcode);
                            connect.putExtra("manual", false);
                            connect.putExtra("pic", encoded);
                            ConnectActivity.this.startActivity(connect);

                        }

                        else if (!fromConnect)
                        {
                            Intent connect = new Intent(ConnectActivity.this, DisconnectActivity.class);
                            connect.putExtra("trackerID", trackerID);
                            connect.putExtra("VIN", barcode);
                            connect.putExtra("pic", encoded);
                            ConnectActivity.this.startActivity(connect);


                        }

                    }

                    else
                    {
                        Toast.makeText(this, "Invalid VIN", Toast.LENGTH_SHORT).show();
                    }

                }



            }

    }

//    public void generateQRCode(String data){
//        com.google.zxing.Writer wr = new MultiFormatWriter();
//        try {
//            int width = 350;
//            int height = 350;
//            BitMatrix bm = wr.encode(data, BarcodeFormat.QR_CODE, width, height);
//            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//
//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//
//                    mBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
//                }
//            }
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        if (mBitmap != null) {
//
//            image.setImageBitmap(mBitmap);
//        }
//    }

//    public void takePictureNoPreview(Context context){
//        // open back facing camera by default
//        Camera myCamera=Camera.open();
//
//        if(myCamera!=null){
//            try{
//                //set camera parameters if you want to
//                //...
//
//                // here, the unused surface view and holder
//                SurfaceView dummy=new SurfaceView(context);
//                myCamera.setPreviewDisplay(dummy.getHolder());
//                myCamera.startPreview();
//
//                myCamera.takePicture(null, null, getJpegCallback());
//
//            }
//            catch(IOException e)
//            {
//
//            }
//            finally{
//                myCamera.unlock();
//            }
//
//        }else{
//            //booo, failed!
//        }
//    }



//    private Camera.PictureCallback getJpegCallback(){
//        Camera.PictureCallback jpeg=new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
//
//                try {
//                    picture = data;
//                    fos = new FileOutputStream("test.jpeg");
//                    fos.write(data);
//                    fos.close();
//                }  catch (IOException e) {
//                    //do something about it
//                }
//            }
//        };
//
//        return null;
//    }









}

