package portalbeanz.com.doublefoot.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpStatus;

import java.io.IOException;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.dialog.LoadingDialog;
import portalbeanz.com.doublefoot.model.ItemUserBasicInfo;
import portalbeanz.com.doublefoot.network.exception.ExceptionConstant;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.NetworkConstant;
import portalbeanz.com.doublefoot.util.SmartLog;

/**
 * Created by thangit14 on 1/21/16.
 */
public abstract class ActivityBase extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_REG_ID = "REGISTRATION ID";
    private static final String PROPERTY_APP_VERSION = "APP VERSION";

    private static boolean isOpenMenuLeft = false;

    private GoogleCloudMessaging mGcm;
    private String mRegid;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String mCurrentContentLoading;
    private boolean mIsDialogShowing;
    private boolean isActivityPaused;
    private boolean mInterrupted;
    private LoadingDialog dialogLoadingData;

    private ImageView btnActionbarLeft;
    private TextView txtActionBarTittle;
    private ImageView imgLogo;

    private String TAG = getClass().getSimpleName();
    protected Button mRightButton;
    protected ImageButton btnBack;
    private OnLeftActionBarClick onLeftActionBarClick;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmartLog.logE(TAG, "Create");

        setContentView(layoutId());
        initViews(savedInstanceState);
        initVariables(savedInstanceState);

        setupSharePreference();

    }

    protected abstract int layoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initVariables(Bundle savedInstanceState);

    protected void initHeader(String tittle) {
        txtActionBarTittle = (TextView) findViewById(R.id.txt_action_bar_title);
        txtActionBarTittle.setText(tittle);
        mRightButton = (Button) findViewById(R.id.btn_right_action_bar);
        imgLogo = (ImageView) findViewById(R.id.ic_logo);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnActionbarLeft = (ImageView) findViewById(R.id.btn_left_action_bar);
        btnActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftActionBarClick != null) {
                    onLeftActionBarClick.onLeftActionBarClicked(isOpenMenuLeft);
                }
            }
        });
        showLogo();
    }

    protected void updateIconMenuLeft() {
        isOpenMenuLeft = !isOpenMenuLeft;
        btnActionbarLeft.setImageResource(isOpenMenuLeft ? R.drawable.ic_close : R.drawable.ic_menu_left);
    }

    protected void showLogo() {
        imgLogo.setVisibility(View.VISIBLE);
        txtActionBarTittle.setVisibility(View.GONE);
    }

    protected void hideLeftButton() {
        btnBack.setVisibility(View.GONE);
        btnActionbarLeft.setVisibility(View.GONE);
    }

    protected void showMenuButton(OnLeftActionBarClick onLeftActionBarClick) {
        btnBack.setVisibility(View.GONE);
        btnActionbarLeft.setVisibility(View.VISIBLE);
        this.onLeftActionBarClick = onLeftActionBarClick;
    }

    public interface OnLeftActionBarClick {
        void onLeftActionBarClicked(boolean isOpenMenuLeft);
    }


    protected void showBackButton() {
        btnBack.setVisibility(View.VISIBLE);
        btnActionbarLeft.setVisibility(View.INVISIBLE);
    }


    private void setupSharePreference() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    public void saveUserType(ItemUserBasicInfo.UserType type) {
        setupSharePreference();
        editor.putString(Constant.USER_TYPE, type.toString());
        editor.commit();
    }

    public boolean isCustomer() {
        setupSharePreference();
        String type = sharedPreferences.getString(Constant.USER_TYPE, "");
        return ItemUserBasicInfo.UserType.Customer.toString().equals(type);
    }

    public boolean isMasseur() {
        setupSharePreference();
        String type = sharedPreferences.getString(Constant.USER_TYPE, "");
        return ItemUserBasicInfo.UserType.Masseur.toString().equals(type);
    }

    public boolean isLogin() {
        setupSharePreference();
        boolean isLogin = !sharedPreferences.getString(Constant.ACCESS_TOKEN, "").equalsIgnoreCase("");
        return isLogin;
    }

    public ItemUserBasicInfo.UserType getUserType() {
        return isCustomer() ? ItemUserBasicInfo.UserType.Customer : ItemUserBasicInfo.UserType.Masseur;
    }

    public final void showLoading() {
        this.showLoading(getString(R.string.loading_data));
    }

    public final void showLoading(String content) {
        mCurrentContentLoading = content;
        if (mIsDialogShowing) {
            return;
        }
        mIsDialogShowing = true;
        if (isActivityPaused) {
            mInterrupted = true;
        } else {
            mInterrupted = false;
            dialogLoadingData = new LoadingDialog();
            Bundle bundle = new Bundle();
            bundle.putString(LoadingDialog.CONTENT_DIALOG, mCurrentContentLoading);
            dialogLoadingData.setArguments(bundle);
            dialogLoadingData.show(getSupportFragmentManager(), "DialogLoadingData");
        }
    }

    public final void disMissLoading() {
        mInterrupted = false;
        mIsDialogShowing = false;
        if (null != dialogLoadingData) {
            dialogLoadingData.dismissDialog();
            dialogLoadingData = null;
        }
    }

    public void disMissLoading(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disMissLoading();
            }
        }, delay);
    }

    public final boolean isShowLoading() {
        return mIsDialogShowing;
    }


    @Override
    protected void onPause() {
        super.onPause();

        isActivityPaused = true;
        if (null != dialogLoadingData && mIsDialogShowing) {
            mInterrupted = true;
            dialogLoadingData.dismissDialog();
            dialogLoadingData = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityPaused = false;
        if (mIsDialogShowing && mInterrupted) {
            mInterrupted = false;
            mIsDialogShowing = false;
            showLoading(mCurrentContentLoading);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleAnalytics.getInstance(this).reportActivityStart(this);
//        checkDefaultSettingDateTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        GoogleAnalytics.getInstance(this).reportActivityStop(this);
//        isActivityAction = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmartLog.logE(TAG, "destroy");
    }

    public void handleError(int errorCode, String errorMessage, boolean showToast) {
        SmartLog.logE(TAG, errorCode + " " + errorMessage);

        switch (errorCode) {
            case HttpStatus.SC_UNAUTHORIZED:
                break;

            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                break;

            case ExceptionConstant.NO_NETWORK:
                errorMessage = getString(R.string.notif_no_network);
                break;

            case ExceptionConstant.VERSION_IS_OLDER:
                break;

            case ExceptionConstant.VERSION_SERVER_IS_OLDER:
                break;

            case ExceptionConstant.ACCOUNT_OR_PASSWORD_WRONG:
                errorMessage=getString(R.string.notif_wrong_username_pass);
                break;

            case ExceptionConstant.SESSION_INVALID:
                errorMessage = getString(R.string.notif_session_invalid);
                this.finish();
                clearAllData();
                ActivityLogin.startActivityWithNewTask(this);
                break;
        }
        if (showToast) {
            showLongToast(errorMessage);
        }
    }

    private void clearAllData() {
        setupSharePreference();
        editor.clear();
        editor.commit();
    }

    public void handleError(int errorCode, String errorMessage) {
        handleError(errorCode, errorMessage, true);
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            setupSharePreference();
        }
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        if (editor == null) {
            setupSharePreference();
        }
        return editor;
    }


    public void showLongToast(int stringResourceID) {

        showToast(getString(stringResourceID), Toast.LENGTH_LONG);
    }

    public void showLongToast(String message) {

        showToast(message, Toast.LENGTH_LONG);
    }

    public void showShortToast(int stringResourceID) {

        showToast(getString(stringResourceID), Toast.LENGTH_SHORT);
    }

    public void showShortToast(String message) {

        showToast(message, Toast.LENGTH_SHORT);
    }

    public void showToast(String message, int length) {

        Toast.makeText(this, message, length).show();
    }

    protected final void showRightButtonBar() {
        mRightButton.setVisibility(View.VISIBLE);
    }

    protected void registerGCM(OnRegisterGCMListener onRegisterGCMListener) {
        showLoading();
        if (checkPlayServices()) {
            mGcm = GoogleCloudMessaging.getInstance(this);
            mRegid = getRegistrationId(getApplicationContext());
            if (mRegid.isEmpty()) {
                registerInBackground(onRegisterGCMListener);
            } else {
                onRegisterGCMListener.registerGCMComplete();
            }
        } else {
            disMissLoading();
        }
    }

    private void registerInBackground(final OnRegisterGCMListener onRegisterGCMListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    mRegid = mGcm.register(NetworkConstant.SENDER_ID);
                    msg = "Device registered, registration ID=" + mRegid;

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplication(), mRegid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                SmartLog.logE(TAG, "REG ID = " + mRegid);

                return msg;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                onRegisterGCMListener.registerGCMComplete();
            }
        }.execute(null, null, null);
    }

    public interface OnRegisterGCMListener {
        void registerGCMComplete();
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    protected String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES,
                Context.MODE_PRIVATE);
    }
}
