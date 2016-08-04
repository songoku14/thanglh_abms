package portalbeanz.com.doublefoot.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.util.SmartLog;
import portalbeanz.com.doublefoot.util.Utils;

/**
 * Created by thangit14 on 6/19/16.
 */
public class AlarmReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SmartLog.logE("Alarm schedule", "receiver message");

            Bundle bundle = intent.getExtras();
            String message = bundle.getString(Utils.MESSAGE);
            ItemAppointment itemAppointment = (ItemAppointment) bundle.getSerializable(Utils.ITEM_APPOINTMENT);

            Utils.sendNotification(context, message, itemAppointment);
        } catch (Exception e) {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }

}