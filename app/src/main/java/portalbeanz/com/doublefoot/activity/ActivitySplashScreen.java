package portalbeanz.com.doublefoot.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.util.Constant;

/**
 * Created by thangit14 on 6/13/16.
 */
public class ActivitySplashScreen extends ActivityBase {
    private SharedPreferences sharedPreferences;
    private String session;
//    @Bind(R.id.btn_masseur)
//    OpenSanButtonSemiBold btnMasseur;
//    @Bind(R.id.btn_customer)
//    OpenSanButtonSemiBold btnCustomer;
//
//    @Override
//    protected int layoutId() {
//        return R.layout.activity_splash_screen;
//    }
//
//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//        ButterKnife.bind(this);
//
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//
//    }
//
//    @OnClick({R.id.btn_masseur, R.id.btn_customer})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_masseur:
//                ActivityMain.startActivity(this,false);
//                break;
//            case R.id.btn_customer:
//                ActivityMain.startActivity(this,true);
//                break;
//        }
//    }



    @Override
    protected int layoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getString(Constant.ACCESS_TOKEN, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.equals("")) {
                    ActivityLogin.startActivity(ActivitySplashScreen.this);
                } else {
                    ActivityMain.startActivityWithNewTask(ActivitySplashScreen.this);
                }
            }
        }, 2 * 1000);

    }
}
