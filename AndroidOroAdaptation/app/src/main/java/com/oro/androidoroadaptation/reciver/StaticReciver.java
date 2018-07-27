package com.oro.androidoroadaptation.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StaticReciver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("wxj", "StaticReciver :onReceive: intent.action = "+intent.getAction());
    }
}
