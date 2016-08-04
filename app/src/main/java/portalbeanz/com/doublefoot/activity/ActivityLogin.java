package portalbeanz.com.doublefoot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemUserBasicInfo;
import portalbeanz.com.doublefoot.network.TaskLoginUsingEmail;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.OpenSanButtonSemiBold;

/**
 * Created by thangit14 on 6/8/16.
 */
public class ActivityLogin extends ActivityBase {

    @Bind(R.id.txt_notification)
    TextView txtNotification;
    @Bind(R.id.edt_email)
    EditText edtEmail;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.txt_new_account)
    TextView txtNewAccount;
    @Bind(R.id.btn_login_email)
    OpenSanButtonSemiBold btnLoginEmail;
    @Bind(R.id.txt_forgot_password)
    TextView txtfortPassword;
    @Bind(R.id.chb_is_masseur)
    CheckBox chbIsMasseur;
    private ItemUserBasicInfo itemUser;

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initHeader(getString(R.string.app_name));
//        hideLeftButton();
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @OnClick({R.id.txt_new_account, R.id.btn_login_email, R.id.txt_forgot_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_new_account:
                ActivityRegister.startActivity(this);
                break;
            case R.id.btn_login_email:
                login();
                break;
            case R.id.txt_forgot_password:
                gotoActivityForgotPassword();
                break;
        }
    }

    private void login() {
        if (checkValidData()) {
            registerGCM(mOnRegisterGCMListener);
        }
    }

    private OnRegisterGCMListener mOnRegisterGCMListener = new OnRegisterGCMListener() {
        @Override
        public void registerGCMComplete() {
            loginUsingEmail();
        }
    };


    private void loginUsingEmail() {
        itemUser = new ItemUserBasicInfo(chbIsMasseur.isChecked()?
                ItemUserBasicInfo.UserType.Masseur: ItemUserBasicInfo.UserType.Customer);
        itemUser.setEmail(edtEmail.getText() + "");
        itemUser.setPassword(edtPassword.getText() + "");

        String registrationId = getRegistrationId(getApplicationContext());
        itemUser.setDeviceId(registrationId);

        showLoading(getString(R.string.loading_login));
        TaskLoginUsingEmail taskLoginUsingEmail = new TaskLoginUsingEmail(this, itemUser);
        taskLoginUsingEmail.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                disMissLoading();
                ActivityMain.startActivityWithNewTask(ActivityLogin.this);
                finish();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode,errorMessage);
            }
        });
    }

    public static void startActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, ActivityLogin.class);
        activity.startActivity(intent);
    }


    public static void startActivityWithNewTask(FragmentActivity activity) {
        Intent intent = new Intent(activity, ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private void gotoActivityForgotPassword() {
        ActivityForgotPassword.startActivity(this);
    }

    private boolean checkValidData() {
        boolean isValidData = false;
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (Utils.isEmailValid(email) && !password.equalsIgnoreCase("") && password.length() <= Constant.MAX_LENGTH && password.length() >= Constant.MIN_LENGTH) {
            isValidData = true;
        } else if (email.equalsIgnoreCase("")) {
            edtEmail.setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.email_not_null)));
            edtEmail.requestFocus();
            isValidData = false;
        } else if (!Utils.isEmailValid(email)) {
            edtEmail.setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.error_email_not_valid)));
            edtEmail.requestFocus();
            isValidData = false;
        } else if (password.equalsIgnoreCase("")) {
            edtPassword.setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_not_null)));
            edtPassword.requestFocus();
            isValidData = false;
        } else if (password.length() > Constant.MAX_LENGTH) {
            edtPassword.setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_max_20_char)));
            edtPassword.requestFocus();
            isValidData = false;
        } else if (password.length() < Constant.MIN_LENGTH) {
            edtPassword.setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_min_3_char)));
            edtPassword.requestFocus();
            isValidData = false;
        }
        return isValidData;
    }

}
