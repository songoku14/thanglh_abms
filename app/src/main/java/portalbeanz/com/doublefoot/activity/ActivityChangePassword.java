package portalbeanz.com.doublefoot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.dialog.DialogConfirm;
import portalbeanz.com.doublefoot.network.TaskChangePassword;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.FloatLabel;

/**
 * Created by datnx on 1/26/15.
 */
public class ActivityChangePassword extends ActivityBase implements DialogConfirm.OnCompleteListener {

    private FloatLabel edtOldPassword;
    private FloatLabel edtNewPassword;
    private FloatLabel edtConfirmNewPassword;
//    private Button btnDone;

    private View.OnClickListener mOnEditTextClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (edtOldPassword.getEditText().getError() != null) {
                edtOldPassword.getEditText().setError(null);
            }
            if (edtNewPassword.getEditText().getError() != null) {
                edtNewPassword.getEditText().setError(null);
            }
            if (edtConfirmNewPassword.getEditText().getError() != null) {
                edtConfirmNewPassword.getEditText().setError(null);
            }
        }
    };

    private View.OnFocusChangeListener mOnfocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (((EditText) v).getError() != null) {
                    ((EditText) v).setError(null);
                }
            }
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidData()) {
                DialogConfirm dialogConfirm = new DialogConfirm();
                Bundle bundle = new Bundle();
                bundle.putString(DialogConfirm.TITLE, getString(R.string.basic_info_change_password));
                bundle.putString(DialogConfirm.CONTENT, getString(R.string.confirm_un_change_password));
                bundle.putBoolean(DialogConfirm.RETURN_TO_ACTIVITY, true);
                bundle.putInt(DialogConfirm.POSITION, DialogConfirm.BOOKMARK);
                dialogConfirm.setArguments(bundle);
                dialogConfirm.show(getSupportFragmentManager(), "DialogConfirm");
            }
        }
    };

    private void changePassword(String oldPassword, String newPassword) {
        showLoading();
        TaskChangePassword taskChangePassword = new TaskChangePassword(getApplicationContext(), oldPassword, newPassword);
        taskChangePassword.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), getString(R.string.change_pass_success), Toast.LENGTH_SHORT).show();
                    disMissLoading();
                    finish();
                }
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode, errorMessage);
            }
        });
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initHeader(getString(R.string.basic_info_change_password));
        showRightButtonBar();
        showBackButton();
        edtOldPassword = (FloatLabel) findViewById(R.id.edt_old_password);
        edtNewPassword = (FloatLabel) findViewById(R.id.edt_new_password);
        edtConfirmNewPassword = (FloatLabel) findViewById(R.id.edt_confirm_new_password);
//        btnDone = (Button) findViewById(R.id.btn_done);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        edtOldPassword.getEditText().setOnClickListener(mOnEditTextClick);
        edtNewPassword.getEditText().setOnClickListener(mOnEditTextClick);
        edtConfirmNewPassword.getEditText().setOnClickListener(mOnEditTextClick);

        edtOldPassword.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
        edtNewPassword.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
        edtConfirmNewPassword.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);

        mRightButton.setOnClickListener(onClickListener);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_change_password;
    }

    private boolean isValidData() {
        boolean isValidData = false;
        String oldPassword = edtOldPassword.getEditText().getText().toString();
        String newPassword = edtNewPassword.getEditText().getText().toString();
        String confirmNewPassword = edtConfirmNewPassword.getEditText().getText().toString();
        if (!oldPassword.equalsIgnoreCase("") && !newPassword.equalsIgnoreCase("") && !confirmNewPassword.equalsIgnoreCase("")
                && newPassword.equalsIgnoreCase(confirmNewPassword) && newPassword.length() <= Constant.MAX_LENGTH
                && newPassword.length() >= Constant.MIN_LENGTH && !oldPassword.equalsIgnoreCase(newPassword)) {
            isValidData = true;
        } else if (oldPassword.equalsIgnoreCase("")) {
            edtOldPassword.requestFocus();
            edtOldPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_not_null)));
            isValidData = false;
        } else if (newPassword.equalsIgnoreCase("")) {
            edtNewPassword.requestFocus();
            edtNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_not_null)));
            isValidData = false;
        } else if (confirmNewPassword.equalsIgnoreCase("")) {
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_not_null)));
            isValidData = false;
        } else if (newPassword.length() > Constant.MAX_LENGTH) {
            edtNewPassword.requestFocus();
            edtNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_max_20_char)));
            isValidData = false;
        } else if (newPassword.length() < Constant.MIN_LENGTH) {
            edtNewPassword.requestFocus();
            edtNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_min_3_char)));
            isValidData = false;
        } else if (confirmNewPassword.length() > Constant.MAX_LENGTH) {
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_max_20_char)));
            isValidData = false;
        } else if (confirmNewPassword.length() < Constant.MIN_LENGTH) {
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_min_3_char)));
            isValidData = false;
        } else if (oldPassword.equalsIgnoreCase(newPassword)) {
            edtNewPassword.requestFocus();
            edtNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.newpassword_not_equal_oldpassword)));
            isValidData = false;
        } else if (!newPassword.equalsIgnoreCase(confirmNewPassword)) {
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.confirm_password_must_same_password)));
            isValidData = false;
        }
        return isValidData;
    }

    @Override
    public void onComplete(int position) {
        String oldPassword = edtOldPassword.getEditText().getText().toString();
        String newPassword = edtNewPassword.getEditText().getText().toString();
        changePassword(oldPassword, newPassword);
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ActivityChangePassword.class);
        context.startActivity(intent);
    }
}

