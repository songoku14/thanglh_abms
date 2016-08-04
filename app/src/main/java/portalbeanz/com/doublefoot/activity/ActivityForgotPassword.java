package portalbeanz.com.doublefoot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.network.TaskResetPassword;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.FloatLabel;

/**
 * Created by thangit14 on 6/18/16.
 */
public class ActivityForgotPassword extends ActivityBase implements View.OnClickListener {
    private FloatLabel edtEmail;
    private Button btnForgotPass;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        edtEmail = (FloatLabel) findViewById(R.id.edt_email);
        btnForgotPass = (Button) findViewById(R.id.btn_forgot_password);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        initHeader(getResources().getString(R.string.forgot_password));
        showBackButton();
        btnForgotPass.setOnClickListener(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_forget_password;
    }


    @Override
    public void onClick(View v) {
        if (v == btnForgotPass) {
            if (checkValidData()) {
                String email = edtEmail.getEditText().getText().toString();
                sendRequestForgotPassword(email);
            }
        }
    }

    private boolean checkValidData() {
        boolean isValidData = false;
        String email = edtEmail.getEditText().getText().toString();
        if (Utils.isEmailValid(email) ) {
            isValidData = true;
        } else if (email.equalsIgnoreCase("")) {
            edtEmail.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.email_not_null)));
            edtEmail.requestFocus();
            isValidData = false;
        } else if (!Utils.isEmailValid(email)) {
            edtEmail.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.error_email_not_valid)));
            edtEmail.requestFocus();
            isValidData = false;
        }
        return isValidData;
    }

    private void sendRequestForgotPassword(String email) {
        showLoading();
        TaskResetPassword taskResetPassword = new TaskResetPassword(getApplicationContext(), email);
        taskResetPassword.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                Toast.makeText(getApplicationContext(), R.string.reset_pass_success, Toast.LENGTH_SHORT).show();
                disMissLoading();
                finish();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                handleError(errorCode,errorMessage);
                disMissLoading();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ActivityForgotPassword.class);
        context.startActivity(intent);
    }
}
