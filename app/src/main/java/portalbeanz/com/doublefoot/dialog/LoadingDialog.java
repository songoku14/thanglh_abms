package portalbeanz.com.doublefoot.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import portalbeanz.com.doublefoot.R;


/**
 * Created by thangit14 on 1/26/16.
 */
public class LoadingDialog extends BaseDialog{
    public static final java.lang.String TITLE_DIALOG = "TITLE DIALOG";
    public static final String CONTENT_DIALOG = "CONTENT DIALOG";
    private TextView mTextContent;
    private String contentDialog;

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        hideTitleDialog();
        String textContent = getArguments().getString(CONTENT_DIALOG);
        mTextContent = (TextView) rootView.findViewById(R.id.txt_content);
        mTextContent.setText(textContent);
        setCancelable(false);
        hideButtonActions();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutDialog() {
        return R.layout.dialog_load_master_data;
    }

    public void setContentDialog(String contentDialog) {
        mTextContent.setText(contentDialog);
    }

    public void dismissDialog() {
        dismiss();
    }
}
