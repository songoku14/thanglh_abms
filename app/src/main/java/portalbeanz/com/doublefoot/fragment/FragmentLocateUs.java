package portalbeanz.com.doublefoot.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.util.Constant;

/**
 * Created by thangit14 on 7/11/16.
 */
public class FragmentLocateUs extends FragmentBase {
    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        ViewGroup vgContactUs = (ViewGroup) mRoot.findViewById(R.id.rl_contact_us);
        vgContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneToContact();
            }
        });
    }

    private void callPhoneToContact() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.PHONE_CONTACT));
        startActivity(intent);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_locate_us;
    }
}
