package portalbeanz.com.doublefoot.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import portalbeanz.com.doublefoot.R;


/**
 * Created by thangit14 on 6/8/16.
 */
public class DialogChooseAvatar extends BaseDialog implements View.OnClickListener {
    private String title;
    private LinearLayout btnCamera;
    private LinearLayout btnGallery;
    private OnOpenListChoiceAvatar onOpenListChoiceAvatarListener;
    public static final int CAMERA = 0;
    public static final int GALLERY = 1;

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        title = getResources().getString(R.string.list_choice_avatar);
        setTitleDialog(title);
        mButtonPositive.setVisibility(View.GONE);

        btnCamera = (LinearLayout) rootView.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);

        btnGallery = (LinearLayout) rootView.findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(this);
        hideButtonActions();
    }

    @Override
    public void onClick(View view) {
        if (view == btnCamera){
            onOpenListChoiceAvatarListener.onOpenListChoiceAvatar(CAMERA);
        } else {
            onOpenListChoiceAvatarListener.onOpenListChoiceAvatar(GALLERY);
        }
    }

    @Override
    protected int getLayoutDialog() {
        return R.layout.dialog_choice_avatar;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onOpenListChoiceAvatarListener = (OnOpenListChoiceAvatar) activity;
    }

    public interface OnOpenListChoiceAvatar{
        public void onOpenListChoiceAvatar(int position);
    }
}
