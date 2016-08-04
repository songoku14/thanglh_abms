package portalbeanz.com.doublefoot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.fragment.FragmentListMasseur;

/**
 * Created by thangit14 on 7/14/16.
 */
public class ActivityBooking extends ActivityBase{
    public static final String PACKAGE_ID = "PACKAGE ID";
    private int packageID;

    @Override
    protected int layoutId() {
        return R.layout.activity_booking;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initHeader("");
        showBackButton();
        showLogo();
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        packageID = getIntent().getIntExtra(PACKAGE_ID, -1);
        loadFragmentBooking();
    }

    private void loadFragmentBooking() {
        Fragment fragment = new FragmentListMasseur();
        Bundle bundle = new Bundle();
        bundle.putInt(ActivityBooking.PACKAGE_ID, packageID);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }

    public static void startActivity(FragmentActivity context, int packageID) {
        Intent intent = new Intent(context, ActivityBooking.class);
        intent.putExtra(PACKAGE_ID, packageID);
        context.startActivity(intent);
    }
}
