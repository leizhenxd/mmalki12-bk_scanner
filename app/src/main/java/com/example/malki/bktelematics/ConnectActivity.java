package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.malki.telematics.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class ConnectActivity extends Activity{

    Activity activity;
    private Button scanVINButton;
    private Button scanTrackerButton;
    private Button marryToDatabase;
    private TextView text;
    private TextView tracker;
    private ImageView mImageView;
    private String msg = "Most vehicles have a VIN barcode on the Compliance Plate";

    private TextView text2;
    private TextView text3;

    private String trackerID;
    Bitmap mBitmap;
    private ImageView image;

    private Typeface fontText;
    private ImageView bkhome;
    private Typeface fontButton;

    private byte [] picture;

    private int vin;
    private int sn;
    private FileOutputStream fos;
    private Typeface font;

    private boolean fromConnect;

    private ImageView hamburger;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;


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
        text2 = (TextView) this.findViewById(R.id.textView30);
        text3 = (TextView) this.findViewById(R.id.textView31);

        text = (TextView) this.findViewById(R.id.textView17);

        scanVINButton.setTypeface(fontButton);
        text.setTypeface(fontText);
        text2.setTypeface(fontText);
        text3.setTypeface(fontText);

        scanVINButton.setOnClickListener(handler);


        bkhome.setOnClickListener(handler);
        text.setText(msg);

        hamburger = (ImageView) this.findViewById(R.id.list);
        hamburger.setOnClickListener(handler);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                String itemPressed = ((TextView) view).getText().toString();

                Intent connect = new Intent(ConnectActivity.this, ConnectActivity.class);
                switch(itemPressed)
                {
                    case "HOME":
                        connect = new Intent(ConnectActivity.this, MainActivity.class);
                        break;
                    case "CONNECT/NEW":
                        connect = new Intent(ConnectActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Select a Black Knight tracking device and scan the barcode");
                        connect.putExtra("fromConnect", true);
                        break;
                    case "DISCONNECT/SALE":
                        connect = new Intent(ConnectActivity.this, ScanTracker.class);
                        connect.putExtra("MSG", "Please unplug the tracker to scan the barcode");
                        connect.putExtra("fromConnect", false);
                        break;
                    case "ACTIVITY":
                        connect = new Intent(ConnectActivity.this, ActivityLog.class);
                        break;
                    case "HELP":
                        connect = new Intent(ConnectActivity.this, Help.class);
                        connect.putExtra("verified", true);
                        break;
                }

                ConnectActivity.this.startActivity(connect);
            }
        });


    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "CONNECT/NEW", "DISCONNECT/SALE", "ACTIVITY", "HELP" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
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

            else if(v == hamburger)
            {

                drawer.openDrawer(mDrawerList);
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

