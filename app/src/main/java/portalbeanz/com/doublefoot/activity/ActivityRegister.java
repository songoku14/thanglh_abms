package portalbeanz.com.doublefoot.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;

import java.io.File;
import java.io.InputStream;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.dialog.DialogChooseAvatar;
import portalbeanz.com.doublefoot.model.ItemUserBasicInfo;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.network.TaskNetworkMultiPartBase;
import portalbeanz.com.doublefoot.network.TaskRegister;
import portalbeanz.com.doublefoot.network.TaskUploadAvatarProfile;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.CircleImageView;
import portalbeanz.com.doublefoot.view.FloatLabel;
import portalbeanz.com.doublefoot.view.OpenSanButtonSemiBold;
import portalbeanz.com.doublefoot.view.OpenSanTextView;

/**
 * Created by thangit14 on 6/8/16.
 */
public class ActivityRegister extends ActivityBase implements DialogChooseAvatar.OnOpenListChoiceAvatar {
    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 2;

    public static final String NEED_NOTIFY_FRAGMENT_MY_PAGE = "NEED NOTIFY FRAGMENT MY PAGE";
    private OpenSanButtonSemiBold btnRegister;
    private ItemUserBasicInfo itemUser;
    private CircleImageView imageAvatar;
    private ImageButton btnChangeAvatar;
    private FloatLabel edtName;
    private FloatLabel edtEmail;
    private FloatLabel edtPassword;
    private FloatLabel edtConfirmPassword;
    private String imagePath;
    private OpenSanTextView txtAddAvatar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox chbIsMasseur;

    private OnRegisterGCMListener mOnRegisterGcmListener = new OnRegisterGCMListener() {
        @Override
        public void registerGCMComplete() {
            registerToServer();
        }
    };
    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnRegister.setClickable(false);

            if (checkValidData()) {
                registerGCM(mOnRegisterGcmListener);
            } else {
                btnRegister.setClickable(true);
            }
        }
    };
    private View.OnClickListener onChangeAvatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onShowDialog();
        }
    };
    private Bitmap bitmapAvatar;
    private View.OnClickListener mOnEditTextClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (edtConfirmPassword.getEditText().getError() != null) {
                edtConfirmPassword.getEditText().setError(null);
            }
            if (edtEmail.getEditText().getError() != null) {
                edtEmail.getEditText().setError(null);
            }
            if (edtPassword.getEditText().getError() != null) {
                edtPassword.getEditText().setError(null);
            }
            if (edtName.getEditText().getError() != null) {
                edtName.getEditText().setError(null);
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


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initHeader(getResources().getString(R.string.title_sign_up));
        showBackButton();

        imageAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        btnChangeAvatar = (ImageButton) findViewById(R.id.btn_change_avatar);
        chbIsMasseur = (CheckBox) findViewById(R.id.chb_is_masseur);
        edtName = (FloatLabel) findViewById(R.id.edt_name);
        edtEmail = (FloatLabel) findViewById(R.id.edt_email);
        edtPassword = (FloatLabel) findViewById(R.id.edt_password);
//        edtPassword.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_LENGTH) });
        edtConfirmPassword = (FloatLabel) findViewById(R.id.edt_confirm_password);
        txtAddAvatar = (OpenSanTextView) findViewById(R.id.txt_add_avatar);

        btnRegister = (OpenSanButtonSemiBold) findViewById(R.id.btn_register);
        sharedPreferences = getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        btnRegister.setOnClickListener(mOnRegisterClickListener);
        imageAvatar.setOnClickListener(onChangeAvatarListener);
        btnChangeAvatar.setOnClickListener(onChangeAvatarListener);

        edtConfirmPassword.getEditText().setOnClickListener(mOnEditTextClick);
        edtEmail.getEditText().setOnClickListener(mOnEditTextClick);
        edtName.getEditText().setOnClickListener(mOnEditTextClick);
        edtPassword.getEditText().setOnClickListener(mOnEditTextClick);

        edtConfirmPassword.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
        edtEmail.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
        edtName.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
        edtPassword.getEditText().setOnFocusChangeListener(mOnfocusChangeListener);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onOpenListChoiceAvatar(int position) {
        switch (position) {
            case DialogChooseAvatar.CAMERA:
                onOpenCamera();
                break;
            case DialogChooseAvatar.GALLERY:
                onOpenGallery();
                break;
            default:
                return;
        }
    }

    private void onOpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
    }

    private void onOpenCamera() {
        String avatarName = "/Avatar.jpg";
        String path = Environment.getExternalStorageDirectory() + avatarName;
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile(file);

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(takePicture, PICK_IMAGE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    txtAddAvatar.setText(getResources().getString(R.string.change_avatar));
                    Uri pickedImage = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        imagePath = imagePath(pickedImage);
                    } else {
                        // Let's read picked image path using content resolver
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                        cursor.moveToFirst();
                        imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                        cursor.close();
                    }

                    // Now we need to set the GUI ImageView data with data read from the picked file.
                    bitmapAvatar = Utils.decodeBitmapFromFile(imagePath, 200, 200);
                    imageAvatar.setImageBitmap(bitmapAvatar);
                    imageAvatar.setBorderColor(Color.TRANSPARENT);

                    // At the end remember to close the cursor or you will end with the RuntimeException!
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogChooseAvatar dialogChooseAvatar = (DialogChooseAvatar) fragmentManager.findFragmentByTag(DialogChooseAvatar.class.getName());
                    if (dialogChooseAvatar != null) {
                        dialogChooseAvatar.dismiss();
                    }
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    txtAddAvatar.setText(getResources().getString(R.string.change_avatar));
                    File outFile = new File(Environment.getExternalStorageDirectory() + "/Avatar.jpg");
                    if (!outFile.exists()) {
                        Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_SHORT).show();
                    } else {

                        bitmapAvatar = Utils.decodeBitmapFromFile(outFile.getPath(), 200, 200);
                        imageAvatar.setImageBitmap(bitmapAvatar);

                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogChooseAvatar dialogChooseAvatar = (DialogChooseAvatar) fragmentManager.findFragmentByTag(DialogChooseAvatar.class.getName());
                    if (dialogChooseAvatar != null) {
                        dialogChooseAvatar.dismiss();
                    }
                }
                break;
        }
    }

    private void onShowDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogChooseAvatar dialogChoiceAvatarV2 = new DialogChooseAvatar();
        dialogChoiceAvatarV2.show(fragmentManager, DialogChooseAvatar.class.getName());
    }

    private boolean checkValidData() {
        boolean isValidData = false;
        String name = edtName.getEditText().getText().toString();
        String email = edtEmail.getEditText().getText().toString();
        String password = edtPassword.getEditText().getText().toString();
        String confirmPassword = edtConfirmPassword.getEditText().getText().toString();
        if (Utils.isEmailValid(email) && !name.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                && !confirmPassword.equalsIgnoreCase("") && password.equalsIgnoreCase(confirmPassword) &&
                password.length() <= Constant.MAX_LENGTH && password.length() >= Constant.MIN_LENGTH) {
            isValidData = true;
        } else if (name.equalsIgnoreCase("")) {
            edtName.requestFocus();
            edtName.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.name_not_null)));
            isValidData = false;
        }else if (name.equalsIgnoreCase(" ")) {
            edtName.requestFocus();
            edtName.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.display_name_can_not_contain_space)));
            isValidData = false;
        } else if (email.equalsIgnoreCase("")) {
            edtEmail.requestFocus();
            edtEmail.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.email_not_null)));
            isValidData = false;
        } else if (!Utils.isEmailValid(email)) {
            edtEmail.requestFocus();
            edtEmail.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.error_email_not_valid)));
            isValidData = false;
        } else if (password.equalsIgnoreCase("")) {
            edtPassword.requestFocus();
            edtPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_not_null)));
            isValidData = false;
        } else if (confirmPassword.equalsIgnoreCase("")) {
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.confirm_password_not_null)));
            isValidData = false;
        } else if (password.length() > Constant.MAX_LENGTH) {
            edtPassword.requestFocus();
            edtPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_max_20_char)));
            isValidData = false;
        } else if (password.length() < Constant.MIN_LENGTH) {
            edtPassword.requestFocus();
            edtPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.password_min_3_char)));
            isValidData = false;
        } else if (!password.equalsIgnoreCase(confirmPassword)) {
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.getEditText().setError(Utils.convertStringToShowErrorInEditText(this.getString(R.string.confirm_password_must_same_password)));
            isValidData = false;
        }
        return isValidData;
    }

    private void registerToServer() {
        itemUser = new ItemUserBasicInfo(chbIsMasseur.isChecked() ?
                ItemUserBasicInfo.UserType.Masseur : ItemUserBasicInfo.UserType.Customer);
        itemUser.setDisplayName(edtName.getEditText().getText() + "");
        itemUser.setEmail(edtEmail.getEditText().getText() + "");
        itemUser.setPassword(edtPassword.getEditText().getText() + "");

        String registrationId = getRegistrationId(getApplicationContext());
        itemUser.setDeviceId(registrationId);

        TaskRegister taskRegister = new TaskRegister(this, itemUser);

        taskRegister.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                disMissLoading();
                Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                if (bitmapAvatar == null) {
                    goToActivityHome();
                } else {
                    uploadAvatarToServer(bitmapAvatar);
                }
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                btnRegister.setClickable(true);
                handleError(errorCode, errorMessage);
            }
        });
    }

    private void goToActivityHome() {
        ActivityMain.startActivityWithNewTask(this);
    }

    private void uploadAvatarToServer(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        showLoading(getString(R.string.loading_upload_avatar));
        InputStream inputStream = Utils.convertToInputStream(bitmap);

        TaskUploadAvatarProfile taskUploadAvatarProfile = new TaskUploadAvatarProfile(getApplicationContext(), inputStream, "avatar.jpg");
        taskUploadAvatarProfile.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                disMissLoading();
                Toast.makeText(getApplicationContext(), R.string.notif_upload_avatar_successful, Toast.LENGTH_SHORT).show();
                goToActivityHome();
            }
        }, new TaskNetworkMultiPartBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode, errorMessage);
                goToActivityHome();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String imagePath(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static void startActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, ActivityRegister.class);
        activity.startActivity(intent);
    }
}
