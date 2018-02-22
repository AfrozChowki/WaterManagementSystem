package com.afroz.watermanagementsystem;

/**
 * Created by root on 17/2/18.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.widget.Toast;
public class SMSReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //---get the SMS message passed in--
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String senderNumber="";
        String msg="";
        String lat="",lon="";

        int n=0,j=0;
        boolean mstatus = false;

        if (bundle != null)
        {
            //---retrieve the SMS message received--
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            char  strs;
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                //str += "SMS from " + msgs[i].getOriginatingAddress();
                //str += ":";
                senderNumber = msgs[i].getOriginatingAddress();
                msg = msgs[i].getMessageBody().toString();
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            //---display the new SMS message--
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            //---send a broadcast intent to update the SMS received in the activity--
           /* if(msg.length()<4)
            {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(senderNumber, null, lat + " " + lon, null, null);
                    Toast.makeText(context, "Location Sent", Toast.LENGTH_LONG).show();
            }

            else {
              */

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);

            if(msg.length()<3)
            {
                broadcastIntent.putExtra("moisture",str);
            }
            else {


                strs = ' ';
                n = str.indexOf(strs);
                n = n + 1;      //sequential count of space
                j = n - 2;
              /*  i=0;
                while(!(strs[i]==" "))
                    i++;
                    */
                broadcastIntent.putExtra("upperTank", str.substring(0, 0 + j));
                broadcastIntent.putExtra("lowerTank", str.substring(n, n + 4));

                mstatus = str.contains("ON");
                if (mstatus) {
                    broadcastIntent.putExtra("motorStatus", "ON");
                } else if (str.contains("OFF")) {
                    broadcastIntent.putExtra("motorStatus", "OFF");
                }

            }
            context.sendBroadcast(broadcastIntent);

        }
    }

}