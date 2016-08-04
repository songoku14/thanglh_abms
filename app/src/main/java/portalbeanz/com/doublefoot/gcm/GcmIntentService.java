package portalbeanz.com.doublefoot.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityDetail;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.SmartLog;
import portalbeanz.com.doublefoot.util.Utils;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GCMIntentService";
    private static final int BOOK_APPOINTMENT_NOTIF = 1;
    private static final int CANCEL_APPOINTMENT_NOTIF = 2;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        SmartLog.logE(TAG,"messageType = "+ messageType);


        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            try {

                if (GoogleCloudMessaging.
                        MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    sendNotification("Send error: " + extras.toString());

                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_DELETED.equals(messageType)) {
                    sendNotification("Deleted messages on server: " +
                            extras.toString());
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                // This loop represents the service doing some work.
//                for (int i=0; i<5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                // Post notification of received message.
                    String content = extras.getString("message");
                    sendNotification(content);

                    SmartLog.logE(TAG, "Received: " + extras.toString());
                    SmartLog.logE(TAG, "Content: " + content);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) throws JSONException, ParseException {
        String title = getApplicationContext().getString(R.string.app_name);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        JSONObject jsonObject = new JSONObject(msg);
        int type = jsonObject.getInt("type");

        ItemAppointment itemAppointment = JsonUtil.getItemAppointment(getApplicationContext(), jsonObject.getJSONObject("appointment"), true);

        boolean isNewBookedAppointment = type == BOOK_APPOINTMENT_NOTIF;

        Intent intent = ActivityDetail.getIntentToViewAppointment(getApplicationContext(), itemAppointment, isNewBookedAppointment);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        String content = getString(R.string.notif_has_appointment_at) + " " +
                Utils.getDateString("HH:mm yyyy/MM/dd", itemAppointment.getStartDate());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(content))
                        .setContentText(content);
        mBuilder.setTicker(content);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
