package com.fourmob.datetimepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Utils {

    public static final int PULSE_ANIMATOR_DURATION = 544;

	public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
	}

	public static ObjectAnimator getPulseAnimator(View labelToAnimate, float decreaseRatio, float increaseRatio) {
        Keyframe k0 = Keyframe.ofFloat(0f, 1f);
        Keyframe k1 = Keyframe.ofFloat(0.275f, decreaseRatio);
        Keyframe k2 = Keyframe.ofFloat(0.69f, increaseRatio);
        Keyframe k3 = Keyframe.ofFloat(1f, 1f);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofKeyframe("scaleX", k0, k1, k2, k3);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofKeyframe("scaleY", k0, k1, k2, k3);
        ObjectAnimator pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(labelToAnimate, scaleX, scaleY);
        pulseAnimator.setDuration(PULSE_ANIMATOR_DURATION);

        return pulseAnimator;
    }

	public static boolean isJellybeanOrLater() {
		return Build.VERSION.SDK_INT >= 16;
	}

    /**
     * Try to speak the specified text, for accessibility. Only available on JB or later.
     * @param text Text to announce.
     */
    @SuppressLint("NewApi")
    public static void tryAccessibilityAnnounce(View view, CharSequence text) {
        if (isJellybeanOrLater() && view != null && text != null) {
            view.announceForAccessibility(text);
        }
    }

    public static boolean isTouchExplorationEnabled(AccessibilityManager accessibilityManager) {
        if (Build.VERSION.SDK_INT >= 14) {
            return accessibilityManager.isTouchExplorationEnabled();
        } else {
            return false;
        }
    }

    public static String getDateString(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static Date getDateWithDefaultFormat(Context context, String strDate) throws ParseException {
        return getDate("yyyy/MM/dd", strDate);
    }

    public static Date getDate(String format, String strDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(strDate);
        return date;
    }

    public static Date getTimeWithDefaultFormat(Context context, String strDate) throws ParseException {
        return getDate("HH:mm", strDate);
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
        HashMap<Integer, HashMap<Integer, Integer>> availableTime = new HashMap<Integer, HashMap<Integer, Integer>>();
        for (int hour = 0; hour < 24; hour++) {
            HashMap<Integer, Integer> minutes = new HashMap<Integer, Integer>(60);
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
        HashMap<Date, Date> result = new HashMap<Date, Date>();
        for (Date key : bookedTimes.keySet()) {
            if (isSameDay(key, date)) {
                result.put(key, bookedTimes.get(key));
            }
        }
        return result;
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

    public static String getDefaultFormatDateString(Date date, Context context) {
        String format = "yyyy/MM/dd";
        return getDateString(format, date);
    }
}
