package com.example.malki.bktelematics.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by rad on 2017/4/1.
 */

public class ImagePathCache {
    public static String picturePath = "";
    public static String vinPicturePath = "";

    public static String manualPictrueBase64 = "";
    public static String manuaVinPictrueBase64 = "";

    public static String getPicBase64(){
        if("".equals(picturePath)) return "";
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeFile(ImagePathCache.picturePath,bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300 ,200,true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getVinPicBase64(){
        if("".equals(vinPicturePath)) return "";
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeFile(ImagePathCache.vinPicturePath,bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300 ,200,true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);


    }
}
