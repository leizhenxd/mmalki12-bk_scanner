package com.example.malki.bkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class SignupActivity extends Activity {

    private Button confirm;
    private EditText firstName;
    private EditText lastName;
    public EditText mobileNumber;
    public String mobNo;
    private EditText pin;
    private EditText confirmPin;

    private int pin1;
    private int pin2;

    private Context context;
    private int duration = Toast.LENGTH_SHORT;
    private Toast toast;
    private CharSequence text;

    private SharedPreferences pref;

    private Typeface fontButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fontButton = Typeface.createFromAsset(getAssets(), "EngschriftDIND.otf");

        firstName = (EditText) this.findViewById(R.id.editText6);
        lastName = (EditText) this.findViewById(R.id.editText7);
        mobileNumber = (EditText) this.findViewById(R.id.editText8);
        pin = (EditText) this.findViewById(R.id.editText9);
        confirmPin = (EditText) this.findViewById(R.id.editText);

        context = getApplicationContext();
        confirm = (Button) this.findViewById(R.id.confirm);

//        firstName.setTypeface(font);
//        lastName.setTypeface(font);
//        mobileNumber.setTypeface(font);
//        pin.setTypeface(font);
//        confirmPin.setTypeface(font);

        pref = getSharedPreferences("myfile", 0);


        confirm.setTypeface(fontButton);

        pin1 = 0;
        pin2 = 1;

        confirm.setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v)
        {
            if (v == confirm)
            {

                if(firstName.getText().toString().isEmpty())
                {
                    text = "Please enter your name";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                }

                else if(lastName.getText().toString().isEmpty())
                {
                    text = "Please enter your last name";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                }

                else if(mobileNumber.getText().toString().isEmpty())
                {
                    text = "Please enter your mobile number";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                }

                else if(pin.getText().toString().isEmpty())
                {
                    text = "Please enter 4 - digit PIN number";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                }

                else if(confirmPin.getText().toString().isEmpty())
                {
                    text = "Please confirm PIN number";
                    toast = Toast.makeText(context,text, duration);
                    toast.show();
                }



                else if (pin.getText().toString().isEmpty() || confirm.getText().toString().isEmpty())
                {
//                    Log.i("first pin", pin.getText().toString());
//                    Log.i("Other pin", pin.getText().toString());

                    pin1 = Integer.parseInt(pin.getText().toString());
                    pin2 = Integer.parseInt(confirmPin.getText().toString());

                    if(pin1 != pin2)
                    {
                        text = "PIN's do not match. Please re-enter PIN";
                        pin.setText("");
                        confirmPin.setText("");
                        toast = Toast.makeText(context,text, duration);
                        toast.show();
                    }



                }

                else
                {

//                    int i;
//                    String [] files = fileList();
//                    for (i = 0; i < files.length; i++)
//                    {
//                        Log.i("1", files[i]);
//                    }



                    mobNo = mobileNumber.getText().toString();
                    String fname = firstName.getText().toString();
                    String lname = lastName.getText().toString();
                    String pinn = pin.getText().toString();

                    //writeCredentials(firstName.getText().toString(), lastName.getText().toString(), mobileNumber.getText().toString(), pin.getText().toString());
                    Intent connectMain = new Intent(SignupActivity.this, AuthenticationActivity.class);
                    connectMain.putExtra("mobileno", mobNo);
                    connectMain.putExtra("fname", fname);
                    connectMain.putExtra("lname", lname);
                    connectMain.putExtra("pin", pinn);

                    SignupActivity.this.startActivity(connectMain);
                }




            }
        }

    };

    private void writeCredentials(String fName, String lName, String mNumber, String pNumber)
    {


        try
        {
            FileOutputStream fileOut = openFileOutput("data.txt", MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);


            outputWriter.write("Firstname : " + fName + ", Lastname : " + lName + ", Mobilenumber : " + mNumber + ", PIN : " + pNumber + ", ");
            outputWriter.close();

            Log.i("Message", "Flowed here");
        }

        catch (Exception e)
        {

        }
    }


}
