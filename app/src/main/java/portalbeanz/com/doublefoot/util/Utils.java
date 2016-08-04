package portalbeanz.com.doublefoot.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityDetail;
import portalbeanz.com.doublefoot.broadcast.AlarmReceive;
import portalbeanz.com.doublefoot.model.ItemAppointment;

/**
 * Created by thangit14 on 2/1/16.
 */
public class Utils {
    public static final String MESSAGE = "message";
    public static final String ITEM_APPOINTMENT = "ITEM_APPOINTMENT";

    public static InputStream convertToInputStream(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageInByte = stream.toByteArray();
        System.out.println("........length......" + imageInByte);

        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        return bis;
    }

    public static CharSequence convertStringToShowErrorInEditText(String string) {
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, string.length(), 0);
        return spannableStringBuilder;
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blur) {
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), false);
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = (Bitmap.createBitmap(bitmapCopy.getWidth(), bitmapCopy.getHeight(), Bitmap.Config.ARGB_8888));

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmapCopy);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(blur);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        bitmapCopy.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }

    public static Bitmap readBitmapFromFile(Context context, String imageName) {
        Bitmap bitmap = null;
        try {
            //read avatar file
            FileInputStream fileInputStream = context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void writeBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        try {
            Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), false);
            //write file
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmapCopy.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //Cleanup
            outputStream.close();

            bitmapCopy.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap decodeBitmapFromFile(String imgPath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int w = options.outWidth;
        int h = options.outHeight;
        int inSampleSize = 1;
        if (w > width || h > height) {
            int halfW = w / 2;
            int halfH = h / 2;
            while ((halfH / inSampleSize) > height
                    && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getDefaultFormatDateString(Date date, Context context) {
        String format = context.getResources().getString(R.string.format_date);
        return getDateString(format, date);
    }

    public static String getDefaultFormatTimeString(Date date, Context context) {
        String format = context.getResources().getString(R.string.format_time);
        return getDateString(format, date);
    }

    public static String getDateString(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static String getDefaultFormatAM_PMString(Date date, Context context) {
        String format = context.getResources().getString(R.string.format_am_pm);
        return getDateString(format, date);
    }

    public static Date getDate(String format, String strDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(strDate);
        return date;
    }

    public static Date getDateWithTPattern(Context context, String strDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(strDate);
        return date;
    }

    public static Date getDateWithDefaultFormat(Context context, String strDate) throws ParseException {
        return getDate(context.getString(R.string.format_date), strDate);
    }

    public static Date getTimeWithDefaultFormat(Context context, String strDate) throws ParseException {
        return getDate(context.getString(R.string.format_time), strDate);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        return sameDay;
    }

    public static void sortByDate(ArrayList<ItemAppointment> itemAppointments) {
        Collections.sort(itemAppointments, new Comparator<ItemAppointment>() {
            @Override
            public int compare(ItemAppointment lhs, ItemAppointment rhs) {
                return lhs.getStartDate().compareTo(rhs.getStartDate());
            }
        });
    }

    public static HashMap<Integer, HashMap<Integer, Integer>> getAvailableTime(HashMap<Date, Date> bookedTimes, Date date) {
        HashMap<Integer, HashMap<Integer, Integer>> availableTime = createFullAvailableTime();

        HashMap<Date, Date> bookedTimeInOneDate = getBookedTimeInOneDate(bookedTimes, date);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        for (Date keyBooked : bookedTimeInOneDate.keySet()) {
            startCalendar.setTime(keyBooked);
            int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
            for (int hourIndex = 0; hourIndex < 24; hourIndex++) {

                if (hourIndex == startHour) {
                    int startMinute = startCalendar.get(Calendar.MINUTE);
                    int endMinute;

                    endCalendar.setTime(bookedTimeInOneDate.get(keyBooked));

                    int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

                    //startHour = endHour = hourIndex
                    if (endHour == hourIndex) {
                        endMinute = endCalendar.get(Calendar.MINUTE);
                        removeMinute(startMinute, endMinute, hourIndex, availableTime);
                    }
                    //startHour = hourIndex != endHour
                    else {
                        for (int i = hourIndex; i < endHour + 1; i++) {
                            if (i == hourIndex) {
                                if (startMinute == 0) {
                                    availableTime.remove(startHour);
                                } else {
                                    removeMinute(startMinute, 59, hourIndex, availableTime);
                                }

                            } else if (i == endHour) {

                                endMinute = endCalendar.get(Calendar.MINUTE);
                                if (endMinute == 59) {
                                    availableTime.remove(endHour);
                                } else {
                                    removeMinute(0, endMinute, endHour, availableTime);
                                }

                            } else {

                                availableTime.remove(i);
                            }
                        }
                    }
                }
            }
        }
        return availableTime;
    }

    private static HashMap<Integer, HashMap<Integer, Integer>> createFullAvailableTime() {
        HashMap<Integer, HashMap<Integer, Integer>> availableTime = new HashMap<>();
        for (int hour = 0; hour < 24; hour++) {
            HashMap<Integer, Integer> minutes = new HashMap<>(60);
            for (int minute = 0; minute < 60; minute++) {
                minutes.put(minute, minute);
            }

            availableTime.put(hour, minutes);
        }
        return availableTime;
    }

    private static HashMap<Integer, HashMap<Integer, Integer>> removeMinute(int startMinute, int endMinute,
                                                                            int keyHour, HashMap<Integer, HashMap<Integer, Integer>> hashMap) {
        HashMap<Integer, Integer> values = hashMap.get(keyHour);
        for (int i = startMinute; i < endMinute + 1; i++) {
            values.remove(i);
        }
//        hashMap.remove(keyHour);
//        hashMap.put(keyHour, values);
        return hashMap;
    }

    private static HashMap<Date, Date> getBookedTimeInOneDate(HashMap<Date, Date> bookedTimes, Date date) {
        HashMap<Date, Date> result = new HashMap<>();
        for (Date key : bookedTimes.keySet()) {
            if (isSameDay(key, date)) {
                result.put(key, bookedTimes.get(key));
            }
        }
        return result;
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public static void startAlarmScheduleToNotif(Context context, String msg, Date date, ItemAppointment itemAppointment) {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 5);
        cal.setTime(date);

        Intent intent = new Intent(context, AlarmReceive.class);
        Bundle bundle = new Bundle();

        bundle.putString(MESSAGE, msg);
        bundle.putSerializable(ITEM_APPOINTMENT, itemAppointment);
        intent.putExtras(bundle);

        PendingIntent sender = PendingIntent.getBroadcast(context, itemAppointment.getAppointmentId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60 * 60 * 1000, sender);
        SmartLog.logE("Alarm schedule", "start alarm");
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public static void sendNotification(Context context, String msg, ItemAppointment itemAppointment) {
        context = context.getApplicationContext();
        String title = context.getString(R.string.app_name);
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = ActivityDetail.getIntentToViewAppointment(context, itemAppointment, true);


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setTicker(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(2, mBuilder.build());
    }

    public static boolean isPastDate(Context context,Date date) {
        try {
            return date.before(getDateWithDefaultFormat(context, getDefaultFormatDateString(new Date(), context)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
