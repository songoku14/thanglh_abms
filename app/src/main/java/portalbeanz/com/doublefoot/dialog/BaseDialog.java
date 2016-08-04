package portalbeanz.com.doublefoot.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import portalbeanz.com.doublefoot.R;


/**
 * Created by thangit14 on 1/26/16.
 */
public abstract class BaseDialog extends DialogFragment {
    protected View mRoot;
    private ViewStub mViewStub;
    private int subId = 99;
    private TextView mTextViewTitleDialog;
    protected RelativeLayout mTitleBarDialog;
    protected Button mButtonPositive;
    protected Button mButtonNegative;
    private View.OnClickListener mOnPositiveListener;
    private View.OnClickListener mOnNegativeListener;
    private View mLineSeperator;
    private LinearLayout mLayoutActions;
    public BaseDialog(){
    }
    protected void hideButtonActions() {
//        mLineSeperator.setVisibility(View.GONE);
        mLayoutActions.setVisibility(View.GONE);
    }
    protected void setBackgroundContetTransparent(){

    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        mRoot = inflater.inflate(R.layout.dialog_base,null);
        mLayoutActions = (LinearLayout) mRoot.findViewById(R.id.layout_actions);
//        mLineSeperator = mRoot.findViewById(R.id.line_seperator);
        mTitleBarDialog = (RelativeLayout) mRoot.findViewById(R.id.title_dialog);
        mTextViewTitleDialog = (TextView) mRoot.findViewById(R.id.txt_title_dialog);

        mViewStub = (ViewStub) mRoot.findViewById(R.id.view_stub);
        mViewStub.setLayoutResource(getLayoutDialog());
        View content = mViewStub.inflate();
        initActions();
        initViews(content, savedInstanceState);

        return mRoot;
    }
    private void initActions(){
        mButtonPositive = (Button) mRoot.findViewById(R.id.btn_positive);
        mButtonNegative = (Button) mRoot.findViewById(R.id.btn_negative);
        mButtonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mButtonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnNegativeListener(View.OnClickListener onNegativeListener) {
        mButtonNegative.setOnClickListener(onNegativeListener);
    }

    public void setOnPositiveListener(View.OnClickListener onPositiveListener){
        mButtonPositive.setOnClickListener(onPositiveListener);;
    }

    protected abstract void initViews(View rootView, Bundle savedInstanceState);


    protected abstract int getLayoutDialog();

    public final void  setTitleDialog(String titleDialog){
        mTextViewTitleDialog.setText(titleDialog);
    }

    public final void hideTitleDialog(){
        mTitleBarDialog.setVisibility(View.GONE);
    }

}
