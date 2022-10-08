package com.xa.xpensauditor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"messsageReceived",Toast.LENGTH_SHORT).show();
        Log.d("yomsgrec","MsgRec");
        Intent i=new Intent(context,SMSReaderActivity.class);
        context.startActivity(i);
    }
}
