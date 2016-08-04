package portalbeanz.com.doublefoot.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import portalbeanz.com.doublefoot.R;


/**
 * Created by thanglh on 21/04/2014.
 */
public class DialogConfirm extends BaseDialog implements View.OnClickListener {

    public static final String RETURN_TO_FRAGMENT = "RETURN TO FRAGMENT";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String RETURN_TO_ACTIVITY = "RETURN TO ACTIVITY";
    public static final String POSITION = "position";
    public static final int BOOKMARK = 1;
    public static final int PHONE_NUMBER = 2;
    private String mTitle;
    private String mContent;
    private TextView txtContent;
    private boolean returnToFragment = false;
    private boolean returnToActivity = false;
    private OnCompleteListener mListener;
//    private Activity mTagetActivity;

    private int position = 0;

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        returnToFragment = getArguments().getBoolean(RETURN_TO_FRAGMENT);
        returnToActivity = getArguments().getBoolean(RETURN_TO_ACTIVITY);
        mTitle = getArguments().getString(TITLE);
        mContent = getArguments().getString(CONTENT);
        position = getArguments().getInt(POSITION);
        setTitleDialog(mTitle);
        txtContent = (TextView) rootView.findViewById(R.id.txt_content);
        txtContent.setText(mContent);

        setOnPositiveListener(this);
        setOnNegativeListener(this);
    }

    @Override
    protected int getLayoutDialog() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonPositive) {
            if (returnToFragment) {
                Intent intent = getActivity().getIntent();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
            if (returnToActivity) {
                this.mListener.onComplete(position);
            }
        }
        dismiss();
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(int position);
    }


    //    make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTargetFragment() == null) {
            try {
                this.mListener = (OnCompleteListener) activity;
            } catch (final ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
            }
        }
    }
}
