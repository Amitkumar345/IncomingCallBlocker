package com.example.block;

import android.os.Build;
import android.telecom.Call;
import android.telecom.CallScreeningService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CustomCallScreeningService extends CallScreeningService  {


    @Override
    public void onScreenCall(@NonNull Call.Details details) {
        if(details.getCallDirection() == details.DIRECTION_INCOMING){
            respondToCall(details,buildResponse());
        }
    }

    private CallResponse buildResponse(){
        return new CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSilenceCall(true)
                .setSkipCallLog(false)
                .setSkipNotification(true)
                .build();
    }
}
