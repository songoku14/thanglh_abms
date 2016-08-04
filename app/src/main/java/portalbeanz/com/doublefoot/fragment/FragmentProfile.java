package portalbeanz.com.doublefoot.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityChangePassword;
import portalbeanz.com.doublefoot.activity.ActivityEditProfile;
import portalbeanz.com.doublefoot.activity.ActivityLogin;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.network.TaskLoadAvatarFromUrl;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.OpenSanTextView;
import portalbeanz.com.doublefoot.view.RoundedImageView;

/**
 * Created by thangit14 on 6/8/16.
 */
public class FragmentProfile extends FragmentBase {
    public static final int ACTIVITY_PROFILE_BASIC_INFO = 1;

    public static final float BLUR_VALUE = 4.f;
    private static final int DIALOG_CONFIRM = 2;

    @Bind(R.id.img_time_line)
    ImageView imgTimeLine;
    @Bind(R.id.img_avatar)
    RoundedImageView imgAvatar;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @Bind(R.id.img_my_page_profile)
    ImageView imgMyPageProfile;
    @Bind(R.id.img_my_page_change_password)
    ImageView imgMyPageChangePassword;
    @Bind(R.id.img_my_page_logout)
    ImageView imgMyPageLogout;
    @Bind(R.id.txt_user_name)
    OpenSanTextView txtUserName;
    private ItemUser itemUser;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static boolean isLoadProfile = false;
    private static boolean isLoadingProfile = false;

    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        ButterKnife.bind(this, mRoot);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cover_profile);
        blurImageTimeLine(bitmap, BLUR_VALUE);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(Constant.PROFILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isLoadingProfile = true;
        loadProfile();

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_my_page_profile, R.id.img_my_page_change_password, R.id.img_my_page_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_my_page_profile:
                ActivityEditProfile.startActivityForResult(this, ACTIVITY_PROFILE_BASIC_INFO, itemUser);
                break;
            case R.id.img_my_page_change_password:
                ActivityChangePassword.startActivity(getContext());
                break;
            case R.id.img_my_page_logout:
                logoutUser();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_PROFILE_BASIC_INFO:
                if (resultCode == Activity.RESULT_OK) {
                    boolean needReloadProfile = data.getBooleanExtra(ActivityEditProfile.NEED_RELOAD_AVATAR, false);
                    if (needReloadProfile) {
                        loadProfileFromLocal(BLUR_VALUE);
                    }
                    if (data.hasExtra(ActivityEditProfile.NEED_RELOAD_PROFILE)) {
                        itemUser = (ItemUser) data.getSerializableExtra(ActivityEditProfile.NEED_RELOAD_PROFILE);
                        updateUserName(itemUser);
                    }
                }
                break;
            case DIALOG_CONFIRM:
                if (resultCode == Activity.RESULT_OK) {
                    logoutUser();
                }
                break;
        }
    }

    private void loadProfile() {
        if (sharedPreferences.contains(Constant.AVATAR_NAME) && sharedPreferences.contains(Constant.USER_NAME)) {
            loadProfileFromLocal(BLUR_VALUE);
        }
        loadProfileFromServer();
    }

    private void loadProfileFromServer() {
        progressWheel.spin();
        progressWheel.setVisibility(View.VISIBLE);
        TaskGetProfile taskGetProfile = new TaskGetProfile(getActivity().getApplicationContext());
        taskGetProfile.request(new Response.Listener<ItemUser>() {

            @Override
            public void onResponse(ItemUser itemUser1) {
                Log.e("FragmentMyPageV2 --> onResponse", "load profile form server success");
                itemUser = itemUser1;
                if (null != itemUser.getAvatarUrl() && !itemUser.getAvatarUrl().equalsIgnoreCase("")) {
                    loadAvatarFromServer(itemUser.getAvatarUrl());
                } else {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.GONE);
                    isLoadingProfile = false;
                    isLoadProfile = true;
                }
                String userName = updateUserName(itemUser);
                cacheUsername(userName);
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                handleError(errorCode, errorMessage);
                progressWheel.stopSpinning();
                progressWheel.setVisibility(View.GONE);
                isLoadingProfile = false;
            }
        });
    }

    private void loadAvatarFromServer(String url) {
        TaskLoadAvatarFromUrl taskLoadAvatarFromUrl = new TaskLoadAvatarFromUrl() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (progressWheel == null) {
                    return;
                }
                if (null != bitmap) {
                    setImageAvatar(bitmap);
//                    blurImageTimeLine(bitmap, BLUR_VALUE);
                    cacheImageAvatar(bitmap);
                    isLoadProfile = true;
                    Log.e("FragmentMyPageV2 --> onPostExecute", "Load avatar success");
                }
                progressWheel.stopSpinning();
                progressWheel.setVisibility(View.GONE);
                isLoadingProfile = false;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if (progressWheel == null) {
                    return;
                }
                progressWheel.stopSpinning();
                progressWheel.setVisibility(View.GONE);
                isLoadingProfile = false;
            }
        };
        taskLoadAvatarFromUrl.execute(url);
    }

    private boolean loadProfileFromLocal(float blurValue) {
        //load avatar from local
        String fileName = sharedPreferences.getString(Constant.AVATAR_NAME, "");
        if (!fileName.equalsIgnoreCase("")) {
            updateAvatarAndTimeLine(fileName, blurValue);
        } else {
            return false;
        }
        //load user name from local
        String displayName = sharedPreferences.getString(Constant.USER_NAME, "");
        if (!displayName.equalsIgnoreCase("")) {
            txtUserName.setText(displayName);
        } else {
            return false;
        }
        Log.e("FragmentMyPageV2 --> loadProfileFromLocal", "load profile from local success");
        return true;
    }

    private void cacheImageAvatar(Bitmap bitmap) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            String fileName = Constant.AVATAR_NAME;
            Utils.writeBitmapToFile(getActivity().getApplicationContext(), bitmap, fileName);
            editor.putString(Constant.AVATAR_NAME, fileName);
            editor.commit();
        }
    }

    private void cacheUsername(String userName) {
        editor.putString(Constant.USER_NAME, userName);
        editor.commit();
    }

    private String updateUserName(ItemUser item) {
        if (txtUserName == null) {
            return "";
        }
        if (null != item.getDisplayName() && !item.getDisplayName().equalsIgnoreCase("")) {
            txtUserName.setText(item.getDisplayName());
        }
        return txtUserName.getText() + "";
    }

    private void updateAvatarAndTimeLine(String imageName, float blurValue) {
        Bitmap bitmap = Utils.readBitmapFromFile(getActivity().getApplicationContext(), imageName);
        if (null != bitmap) {
            //blue and set avatar file
//            blurImageTimeLine(bitmap, blurValue);
            setImageAvatar(bitmap);
        }
    }

    private void setImageAvatar(Bitmap bitmap) {
        if (imgAvatar != null) {
            imgAvatar.setImageBitmap(bitmap);
            imgAvatar.setBorderColor(Color.WHITE);
            imgAvatar.setBorderWidth(4f);
        }
    }


    private void blurImageTimeLine(Bitmap bitmap, float blurValue) {
        if (imgTimeLine == null) {
            return;
        }
        imgTimeLine.setImageBitmap(Utils.blurBitmap(getActivity().getApplicationContext(), bitmap, blurValue));
    }

    private void logoutUser() {
        showLoading(getString(R.string.loading_logout));
        isLoadProfile = false;
        TaskLogout taskLogout = new TaskLogout(getActivity().getApplicationContext());
        taskLogout.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                if (aBoolean) {
                    disMissLoading();
                    clearCacheProfileInLocal();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();

                    gotoActivityLogin();
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

    private void gotoActivityLogin() {
        ActivityLogin.startActivityWithNewTask(getActivity());
    }

    private void clearCacheProfileInLocal() {
        editor.clear();
        editor.commit();
        // set default image
        imgTimeLine.setImageResource(android.R.color.transparent);
        imgAvatar.setImageResource(R.drawable.ic_user_default);
        txtUserName.setText("");
    }
}
