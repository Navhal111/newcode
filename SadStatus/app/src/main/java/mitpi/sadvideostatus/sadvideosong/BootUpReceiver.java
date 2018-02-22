package mitpi.sadvideostatus.sadvideosong;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gcm.MyGcmListenerService;
import gcm.MyInstanceIDListenerService;
import gcm.QuickstartPreferences;
import gcm.RegistrationIntentService;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent startServiceIntent = new Intent(context, MyGcmListenerService.class);
            context.startService(startServiceIntent);

            Intent notificationServiceIntent = new Intent(context, MyInstanceIDListenerService.class);
            context.startService(notificationServiceIntent);

            Intent notificationServiceIntent1 = new Intent(context, QuickstartPreferences.class);
            context.startService(notificationServiceIntent1);

            Intent notificationServiceIntent2 = new Intent(context, RegistrationIntentService.class);
            context.startService(notificationServiceIntent2);

        }
    }
}