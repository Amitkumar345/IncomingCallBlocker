package com.example.block;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;

import static android.content.Context.TELECOM_SERVICE;

public class IncomingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            cutCallForOlderAndroidVersion(context,intent);
        }else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P){
            cutTheCall(context);
        }
    }
    private void cutCallForOlderAndroidVersion(Context context, Intent intent){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_RING,true);

        try {
            // Get the getITelephony() method
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            // Disable access check
            method.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(telephonyManager);
            // Get the endCall method from ITelephony
            Class<?> telephonyInterfaceClass =Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
            Toast.makeText(context, "Auto Call Rejection", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "rejectCall method didn't work "+ e.toString() ,Toast.LENGTH_SHORT).show();
            Log.d(context.toString(), e.toString());
            e.printStackTrace();
        }
    }

    private boolean cutTheCall(Context context) {
        boolean callDisconnected = false;
        TelecomManager telecomManager = (TelecomManager) context.getApplicationContext().getSystemService(TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || telecomManager == null) {
            return false;
        }

        if (telecomManager.isInCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                callDisconnected = telecomManager.endCall();
            }
        }
        if(callDisconnected){
            Toast.makeText(context, "Call Rejected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

