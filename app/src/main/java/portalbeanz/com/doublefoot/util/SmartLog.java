package portalbeanz.com.doublefoot.util;

import android.util.Log;

/**
 * Created by thangit14 on 3/29/16.
 */
public class SmartLog {

    public static void logE(String tag, String message) {
        if (Config.DEBUG_MODE) {
            Log.e(tag, message);
        }
    }

    public static void logE(String tag, String message, Exception e) {
        if (Config.DEBUG_MODE) {
            Log.e(tag, message,e);
        }
    }

    public static void log(String tag, String message) {
        if (Config.DEBUG_MODE) {
            Log.d(tag, message);
        }
    }
}
