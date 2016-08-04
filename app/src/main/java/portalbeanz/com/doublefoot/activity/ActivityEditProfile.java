package portalbeanz.com.doublefoot.activity;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialogOrigin;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.dialog.DialogChooseAvatar;
import portalbeanz.com.doublefoot.dialog.DialogGender;
import portalbeanz.com.doublefoot.fragment.FragmentProfile;
import portalbeanz.com.doublefoot.model.ItemGender;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.network.TaskNetworkMultiPartBase;
import portalbeanz.com.doublefoot.network.TaskUpdateProfile;
import portalbeanz.com.doublefoot.network.TaskUploadAvatarProfile;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.OpenSanEditText;
import portalbeanz.com.doublefoot.view.RoundedImageView;
import portalbeanz.com.doublefoot.view.SoftKeyboardLsnedRelativeLayout;

import static com.fourmob.datetimepicker.date.DatePickerDialogOrigin.newInstance;


/**
 * Created by thangit14 on 6/12/16.
 */
public class ActivityEditProfile extends ActivityBase
        implements View.OnClickListener, com.fourmob.datetimepicker.date.DatePickerDialogOrigin.OnDateSetListener,
        DialogChooseAvatar.OnOpenListChoiceAvatar,
        DialogGender.OnCompleteListener {

    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    public static final String IMAGE_NAME = "IMAGE NAME";
    public static final String ITEM_USER = "USER NAME";
    public static final String DATA = "DATA";
    public static final String NEED_RELOAD_PROFILE = "NEED RELOAD PROFILE";
    public static final String NEED_RELOAD_AVATAR = "NEED RELOAD AVATAR";
    private static final int IMAGE_SIZE = 200;
    private RelativeLayout containerImage;
    private RelativeLayout containerChangePassword;

    private View lineBottom;

    private SoftKeyboardLsnedRelativeLayout containerFrame;

    private OpenSanEditText edtDisplayName;
    //    private OpenSanEditText edtFirstName;
//    private OpenSanEditText edtLastName;
    private OpenSanEditText edtBirthDay;
    private OpenSanEditText edtGender;
    private OpenSanEditText edtIntroduce;
    private OpenSanEditText edtSpecialization;
    private OpenSanEditText edtWorkingExperience;
    private OpenSanEditText edtEmail;

    private ImageView imgChangeDisplayName;
    //    private ImageView imgChangeFirstName;
//    private ImageView imgChangeLastName;
    private ImageView imgChangeBirthDay;
    private ImageView imgChangeGender;
    private ImageView imgChangeIntroduce;
    private ImageView imgChangePassword;
    private ImageView imgChangeSpecialization;
    private ImageView imgChangeWorkingExperience;

    private ImageView imgTimeline;
    private RoundedImageView imageAvatar;

    private boolean isFirstNameAvailable;
    private boolean isLastNameAvailable;
    private boolean isIntroduceAvailable;
    private boolean isSpecializationAvailable;
    private boolean isWorkingExperienceAvailable;

    //    private ItemUser itemUser;
    private Bitmap bitmapAvatar;
    private String imagePath;

    private Button btnSave;
    private ItemGender mItemGender;
    private String mDateTime;

    //special when loadBasicProfile done
    private boolean isUploadBasicProfileDone;
    //special when loadAvatar done
    private boolean isUploadAvatarDone;
    private Intent intentUpdateFragmentProfile;
    private boolean isChangeProfile = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isDisplayNameAvailable;
    private int heightImage;

    private SharedPreferences shareNetworkSession;
    private RelativeLayout.LayoutParams layoutParams;
    private ItemUser itemUser;

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initHeader(getString(R.string.profile_info));
        showBackButton();
        
        containerImage = (RelativeLayout) findViewById(R.id.container_image);

        edtDisplayName = (OpenSanEditText) findViewById(R.id.edt_display_name);
//        edtFirstName = (OpenSanEditText) findViewById(R.id.edt_first_name);
//        edtLastName = (OpenSanEditText) findViewById(R.id.edt_last_name);
        edtBirthDay = (OpenSanEditText) findViewById(R.id.edt_birth_day);
        edtGender = (OpenSanEditText) findViewById(R.id.edt_gender);
        edtIntroduce = (OpenSanEditText) findViewById(R.id.edt_introduce);
        edtEmail = (OpenSanEditText) findViewById(R.id.edt_email);
        edtWorkingExperience = (OpenSanEditText) findViewById(R.id.edt_working_experience);
        edtSpecialization = (OpenSanEditText) findViewById(R.id.edt_specialization);

        imgChangeDisplayName = (ImageView) findViewById(R.id.img_change_display_name);
//        imgChangeFirstName = (ImageView) findViewById(R.id.img_change_first_name);
//        imgChangeLastName = (ImageView) findViewById(R.id.img_change_last_name);
        imgChangeBirthDay = (ImageView) findViewById(R.id.img_birth_day);
        imgChangeGender = (ImageView) findViewById(R.id.img_change_gender);
        imgChangeIntroduce = (ImageView) findViewById(R.id.img_change_introduce);
        imgChangePassword = (ImageView) findViewById(R.id.img_change_password);
        imgChangeSpecialization = (ImageView) findViewById(R.id.img_change_specialization);
        imgChangeWorkingExperience = (ImageView) findViewById(R.id.img_change_working_experience);

        containerChangePassword = (RelativeLayout) findViewById(R.id.container_change_password);

        lineBottom = findViewById(R.id.line_bottom);

        containerFrame = (SoftKeyboardLsnedRelativeLayout) findViewById(R.id.container_frame);
        layoutParams = (RelativeLayout.LayoutParams) containerImage.getLayoutParams();
        heightImage = layoutParams.height;
        containerFrame.addSoftKeyboardLsner(new SoftKeyboardLsnedRelativeLayout.SoftKeyboardLsner() {
            @Override
            public void onSoftKeyboardShow() {
                layoutParams.height = 0;
            }

            @Override
            public void onSoftKeyboardHide() {
                layoutParams.height = heightImage;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (containerFrame.isKeyboardShown()) {
                    hideKeyboard();
                }
                ActivityEditProfile.this.finish();
            }
        });

        imageAvatar = (RoundedImageView) findViewById(R.id.img_avatar);
        imageAvatar.setBorderColor(Color.WHITE);
        imageAvatar.setBorderWidth(4f);
        imgTimeline = (ImageView) findViewById(R.id.img_time_line);

        btnSave = (Button) findViewById(R.id.btn_save);

        shareNetworkSession = getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, MODE_PRIVATE);
        if (shareNetworkSession.getString(Constant.KIND, "").equalsIgnoreCase(Constant.KIND_EMAIL)) {
            containerChangePassword.setVisibility(View.VISIBLE);
            lineBottom.setVisibility(View.VISIBLE);
            imgChangePassword.setOnClickListener(this);
            containerChangePassword.setOnClickListener(this);
        } else {
            containerChangePassword.setVisibility(View.GONE);
            lineBottom.setVisibility(View.GONE);
        }

        imgChangeDisplayName.setOnClickListener(this);
        imgChangeSpecialization.setOnClickListener(this);
        imgChangeWorkingExperience.setOnClickListener(this);
//        imgChangeFirstName.setOnClickListener(this);
//        imgChangeLastName.setOnClickListener(this);
        imgChangeBirthDay.setOnClickListener(this);
        imgChangeGender.setOnClickListener(this);
        imgChangeIntroduce.setOnClickListener(this);
        imageAvatar.setOnClickListener(this);
        imgTimeline.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        itemUser = (ItemUser) getIntent().getSerializableExtra(ITEM_USER);

        sharedPreferences = getSharedPreferences(Constant.PROFILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String fileName = sharedPreferences.getString(Constant.AVATAR_NAME, "");
        if (!fileName.equalsIgnoreCase("")) {
            Bitmap bitmap = Utils.readBitmapFromFile(getApplicationContext(), fileName);
            if (bitmap != null) {
                imageAvatar.setImageBitmap(bitmap);
                imgTimeline.setImageBitmap(Utils.blurBitmap(getApplicationContext(), bitmap, FragmentProfile.BLUR_VALUE));
//                imageAvatar.setBorderColor(Color.WHITE);
//                imageAvatar.setBorderWidth(4);
            }
        }


        if (null != itemUser) {
            edtDisplayName.setText(itemUser.getDisplayName());
//            edtFirstName.setText(itemUser.getFirstName());
//            edtLastName.setText(itemUser.getLastName());
            edtBirthDay.setText(itemUser.getBod());
            mDateTime = itemUser.getBod();
            mItemGender = new ItemGender(itemUser.getGender() == 0 ? 0 : 1);
            setGenderText(mItemGender);
            edtIntroduce.setText(itemUser.getIntroduce());
            edtEmail.setText(itemUser.getEmail());
            if (itemUser.isMasseur()) {
                edtWorkingExperience.setText(itemUser.getWorkingExperience());
                edtSpecialization.setText(itemUser.getSpecialization());
            }

        }
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void onClick(View v) {
//        if (v == imgChangeFirstName) {
//            isChangeProfile = true;
//            if (isFirstNameAvailable) {
//        edtFirstName.setFocusable(false);
//                isFirstNameAvailable = !isFirstNameAvailable;
//        hideKeyboard(edtFirstName);
//            } else {
//        edtFirstName.setFocusableInTouchMode(true);
//                isFirstNameAvailable = !isFirstNameAvailable;
//        showKeyboard(edtFirstName);
//            }
//        } else if (v == imgChangeLastName) {
//            isChangeProfile = true;
//            if (isLastNameAvailable) {
//                edtLastName.setFocusable(false);
//                isLastNameAvailable = !isLastNameAvailable;
//                hideKeyboard(edtLastName);
//            } else {
//                edtLastName.setFocusableInTouchMode(true);
//                isLastNameAvailable = !isLastNameAvailable;
//                showKeyboard(edtLastName);
//            }

//        } else
        if (v == imgChangeBirthDay) {
            isChangeProfile = true;
            openDialogTimePicker(mDateTime);
        } else if (v == imgChangeGender) {
            isChangeProfile = true;
            openDialogGender();
        } else if (v == imgChangeIntroduce) {
            isChangeProfile = true;
            if (isIntroduceAvailable) {
                edtIntroduce.setFocusable(false);
                isIntroduceAvailable = !isIntroduceAvailable;
                hideKeyboard(edtIntroduce);
            } else {
                edtIntroduce.setFocusableInTouchMode(true);
                isIntroduceAvailable = !isIntroduceAvailable;
                showKeyboard(edtIntroduce);
            }

        } else if (v == imgChangeDisplayName) {
            isChangeProfile = true;
            if (isDisplayNameAvailable) {
                edtDisplayName.setFocusable(false);
                isDisplayNameAvailable = !isDisplayNameAvailable;
                hideKeyboard(edtDisplayName);
            } else {
                edtDisplayName.setFocusableInTouchMode(true);
                isDisplayNameAvailable = !isDisplayNameAvailable;
                showKeyboard(edtDisplayName);
            }
        } else if (v == imgChangeSpecialization) {
            isChangeProfile = true;
            if (isSpecializationAvailable) {
                edtSpecialization.setFocusable(false);
                isSpecializationAvailable = !isSpecializationAvailable;
                hideKeyboard(edtSpecialization);
            } else {
                edtSpecialization.setFocusableInTouchMode(true);
                isSpecializationAvailable = !isSpecializationAvailable;
                showKeyboard(edtSpecialization);
            }
        } else if (v == imgChangeWorkingExperience) {
            isChangeProfile = true;
            if (isWorkingExperienceAvailable) {
                edtWorkingExperience.setFocusable(false);
                isWorkingExperienceAvailable = !isWorkingExperienceAvailable;
                hideKeyboard(edtWorkingExperience);
            } else {
                edtWorkingExperience.setFocusableInTouchMode(true);
                isWorkingExperienceAvailable = !isWorkingExperienceAvailable;
                showKeyboard(edtWorkingExperience);
            }
        } else if (v == imgTimeline) {

        } else if (v == imageAvatar) {
            onShowDialog();
        } else if (v == btnSave) {
            uploadProfile();
        } else if (v == imgChangePassword || v == containerChangePassword) {
            gotoActivityChangePass();
        }
    }

    private void gotoActivityChangePass() {
        startActivity(new Intent(getApplicationContext(), ActivityChangePassword.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
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
                    bitmapAvatar = Utils.decodeBitmapFromFile(imagePath, IMAGE_SIZE, IMAGE_SIZE);
                    setImageAvatar(bitmapAvatar);

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
                    File outFile = new File(Environment.getExternalStorageDirectory() + "/Avatar.jpg");
                    if (!outFile.exists()) {
                        Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_SHORT).show();
                    } else {

                        bitmapAvatar = Utils.decodeBitmapFromFile(outFile.getPath(), IMAGE_SIZE, IMAGE_SIZE);
                        setImageAvatar(bitmapAvatar);
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

    @Override
    public void onComplete(ItemGender itemGender) {
        this.mItemGender = itemGender;
        setGenderText(itemGender);
    }

    private void setImageAvatar(Bitmap bitmapAvatar) {
        imageAvatar.setImageBitmap(bitmapAvatar);
        imageAvatar.setBorderColor(Color.WHITE);
        imageAvatar.setBorderWidth(4f);

        imgTimeline.setImageBitmap(Utils.blurBitmap(getApplicationContext(), bitmapAvatar, Constant.BLUR_VALUE));
    }

    private void showKeyboard(OpenSanEditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        editText.requestFocus();
    }

    private void hideKeyboard(OpenSanEditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void openDialogGender() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogGender dialogGenderV2 = new DialogGender();
        Bundle bundle = new Bundle();
        bundle.putBoolean(DialogGender.RETURN_FRAGMENT, false);
        bundle.putSerializable(DialogGender.ITEM_SELECTED, mItemGender);
        dialogGenderV2.setArguments(bundle);
        dialogGenderV2.show(fragmentManager, DialogGender.class.getName());
    }

    private void openDialogTimePicker(String date) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf;
        if (date != null && !date.equalsIgnoreCase("")) {
            sdf = new SimpleDateFormat(getString(R.string.format_date));
            try {
                calendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final DatePickerDialogOrigin datePickerDialog = newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(1920, calendar.get(Calendar.YEAR));
        datePickerDialog.show(getSupportFragmentManager(), DatePickerDialog.class.getName());

    }

    @Override
    public void onDateSet(DatePickerDialogOrigin datePickerDialog, int year, int month, int day) {
        String currentMonth = "";
        String currentDay = "";
        if (month < 9) {
            currentMonth = "0" + (month + 1);
        } else {
            currentMonth = "" + (month + 1);
        }
        if (day < 10) {
            currentDay = "0" + day;
        } else {
            currentDay = "" + day;
        }
        edtBirthDay.setText(year + getString(R.string.souce_year) +
                currentMonth + getString(R.string.souce_month) + currentDay);
        mDateTime = edtBirthDay.getText() + "";
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

    private void uploadProfile() {
        intentUpdateFragmentProfile = new Intent();
        showLoading(getString(R.string.loading_updating_profile));
        uploadAvatarToServer(bitmapAvatar);
        uploadBasicProfile();
    }

    private void uploadBasicProfile() {
        if (!isChangeProfile) {
            isUploadBasicProfileDone = true;
            if (isUploadAvatarDone) {
                disMissLoading();
                updateFragmentProfile();
            }
            return;
        }
        if (itemUser != null) {
            itemUser.setDisplayName(edtDisplayName.getText() + "");
//            itemUser.setFirstName(edtFirstName.getText() + "");
//            itemUser.setLastName(edtLastName.getText() + "");
            itemUser.setBod(edtBirthDay.getText() + "");
            itemUser.setGender(mItemGender.getGender());
            itemUser.setIntroduce(edtIntroduce.getText() + "");
        }
        TaskUpdateProfile taskUpdateProfile = new TaskUpdateProfile(getApplicationContext(), itemUser);
        taskUpdateProfile.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                isUploadBasicProfileDone = true;
                intentUpdateFragmentProfile.putExtra(NEED_RELOAD_PROFILE, itemUser);
                if (isUploadAvatarDone) {
                    disMissLoading();
                    updateFragmentProfile();
                    hideKeyboard();
                    saveUserName(edtDisplayName.getText().toString());
                }
                Toast.makeText(getApplicationContext(), R.string.update_profile_success, Toast.LENGTH_SHORT).show();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                isUploadBasicProfileDone = true;
                if (isUploadAvatarDone) {
                    disMissLoading();
                    updateFragmentProfile();
                }
                Log.e("ActivityProfileBasicInfoV2 --> onErrorListener", "errorCode = " + errorCode + " " + errorMessage);
            }
        });
    }

    private void saveUserName(String userName) {
        editor.putString(Constant.USER_NAME, userName);
        editor.commit();
    }

    private void updateFragmentProfile() {
        setResult(RESULT_OK, intentUpdateFragmentProfile);
        finish();
    }

    private void uploadAvatarToServer(Bitmap bitmap) {
        if (null == bitmap) {
            isUploadAvatarDone = true;
            if (isUploadBasicProfileDone) {
                intentUpdateFragmentProfile.putExtra(NEED_RELOAD_AVATAR, false);
                disMissLoading();
                updateFragmentProfile();
            }
            return;
        }
        InputStream inputStream = Utils.convertToInputStream(bitmap);

        TaskUploadAvatarProfile taskUploadAvatarProfile = new TaskUploadAvatarProfile(getApplicationContext(), inputStream, "avatar.jpg");
        taskUploadAvatarProfile.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                cacheImageAvatar(bitmapAvatar);
                isUploadAvatarDone = true;
                intentUpdateFragmentProfile.putExtra(NEED_RELOAD_AVATAR, true);
                Toast.makeText(getApplicationContext(), R.string.update_avatar_success, Toast.LENGTH_SHORT).show();
                if (aBoolean) Log.e("ActivityRegisterV2 --> onResponse", "Upload avatar Success");
                if (isUploadBasicProfileDone) {
                    disMissLoading();
                    updateFragmentProfile();
                }
            }
        }, new TaskNetworkMultiPartBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                intentUpdateFragmentProfile.putExtra(NEED_RELOAD_AVATAR, false);
                isUploadAvatarDone = true;
                Log.e("ActivityRegisterV2 --> onErrorListener", "errorCode: " + errorCode + " : " + errorMessage);
                if (isUploadBasicProfileDone) {
                    disMissLoading();
                    updateFragmentProfile();
                }
            }
        });
    }

    private void cacheImageAvatar(Bitmap bitmap) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PROFILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String fileName = Constant.AVATAR_NAME;
        Utils.writeBitmapToFile(getApplicationContext(), bitmap, fileName);
        editor.putString(Constant.AVATAR_NAME, fileName);
        editor.commit();
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

    private void onShowDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogChooseAvatar dialogChoiceAvatarV2 = new DialogChooseAvatar();
        dialogChoiceAvatarV2.show(fragmentManager, DialogChooseAvatar.class.getName());
    }

    private void setGenderText(ItemGender itemGender) {
        if (itemGender.getGender() == 0) {
            edtGender.setText(R.string.female);
        } else {
            edtGender.setText(R.string.male);
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void startActivityForResult(FragmentProfile fragmentProfile, int requestCode, ItemUser itemUser) {
        Intent intent = new Intent(fragmentProfile.getContext(), ActivityEditProfile.class);
        intent.putExtra(ITEM_USER, itemUser);
        fragmentProfile.startActivityForResult(intent, requestCode);

    }
}