package com.example.malki.bkapp;

import android.provider.Settings;
import android.util.Base64;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by malki on 14/11/16.
 */

public class Encryptor
{
    public String encrypt (String key, String iVector, String value)
    {
        try{

            IvParameterSpec iv = new IvParameterSpec(iVector.getBytes("UTF-8"));
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            ciph.init(Cipher.ENCRYPT_MODE, spec, iv);

            byte[] encrypted = ciph.doFinal(value.getBytes());

            //System.out.println("Encryption working");
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String key, String iVector, String encrypted)
    {
        try{
            IvParameterSpec iv = new IvParameterSpec(iVector.getBytes("UTF-8"));
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            ciph.init(Cipher.DECRYPT_MODE, spec, iv);

            byte [] original = ciph.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

            //System.out.println("Decryption working");
            return new String(original);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public char [] randomise()
    {
        char [] iv = new char[16];

        final String alphabetus = "abcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int n = alphabetus.length();

        Random r = new Random();

        for (int i = 0; i < iv.length; i++)
        {
            iv[i] = alphabetus.charAt(r.nextInt(n));
        }

        return iv;
    }

    public String [] encryptString(String username, String pass)
    {
        final String key = "sk_api_ec#L1b3@gR5&C*VhB2Y47%0d!";
//        String iVector = new String (randomise());

        String iVector = new String(randomise());
        String encrypted = encrypt(key, iVector, pass);
        String decrypted = decrypt(key, iVector, encrypted);

        String valueS = username + ":" + encrypted;
        String oauth;

        //System.out.println("Encrypted password:" + encrypted);

        try {

            byte[] value = valueS.getBytes("UTF-8");
             oauth = "Basic " + Base64.encodeToString(value, Base64.NO_WRAP);

        }

        catch(Exception e)
        {
            oauth = null;
        }


        String [] args = {oauth, iVector, decrypted};



        return args;


    }



//    public static void main(String[] args)
//    {
//        // Invalid AES key length 14 bytes
//        String key = "Bar12345Bar12345";
//
//        // Wrong IV length must be 16 bytes long
//        // array from 0-15
//        //char iv [] = {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
//        String iVector = new String (randomise());
//
//        System.out.print(encrypt(key, iVector, "Hello world"));
//    }
}
