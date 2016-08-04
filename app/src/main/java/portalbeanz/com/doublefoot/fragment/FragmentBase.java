package portalbeanz.com.doublefoot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import portalbeanz.com.doublefoot.activity.ActivityBase;

/**
 * Created by thangit14 on 6/8/16.
 */
public abstract class FragmentBase extends Fragment{
    protected View mRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(layoutId(), null);
        initViews(mRoot, savedInstanceState);
        initVariable(mRoot, savedInstanceState);
        return mRoot;
    }

    protected abstract void initViews(View mRoot, Bundle savedInstanceState);

    protected abstract void initVariable(View mRoot, Bundle savedInstanceState);

    protected abstract int layoutId();

    public final void showLoading(String content) {
        ((ActivityBase) getActivity()).showLoading(content);
    }

    public final void showLoading() {
        ((ActivityBase) getActivity()).showLoading();
    }

    public final void disMissLoading() {
        ((ActivityBase) getActivity()).disMissLoading();
    }

    public void showLongToast(int stringResourceID) {

        showToast(getString(stringResourceID), Toast.LENGTH_LONG);
    }

    public void showLongToast(String message) {

        showToast(message, Toast.LENGTH_LONG);
    }

    public void showShortToast(int stringResourceID) {

        showToast(getString(stringResourceID), Toast.LENGTH_SHORT);
    }

    public void showShortToast(String message) {

        showToast(message, Toast.LENGTH_SHORT);
    }

    public void showToast(String message, int length) {

        Toast.makeText(getActivity(), message, length).show();
    }

    public void handleError(int errorCode, String errorMessage) {
        ((ActivityBase)getActivity()).handleError(errorCode, errorMessage, true);
    }
}
