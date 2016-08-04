package portalbeanz.com.doublefoot.application;

import android.app.Application;
import android.content.res.Configuration;

import portalbeanz.com.doublefoot.util.FontUtils;

/**
 * Created by thangit14 on 2/20/16.
 */
public class AbmsApplication extends Application {
    private static AbmsApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
//        Mint.initAndStartSession(this, "e0457b4a");
        FontUtils.getInstance().initFonts(getApplicationContext());
        mInstance = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public static synchronized AbmsApplication getInstance() {
        return mInstance;
    }
}

