package portalbeanz.com.doublefoot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import portalbeanz.com.doublefoot.fragment.FragmentUnavailable;
import portalbeanz.com.doublefoot.fragment.FragmentMyBooking;
import portalbeanz.com.doublefoot.fragment.FragmentProfile;
import portalbeanz.com.doublefoot.fragment.FragmentListMasseur;

/**
 * Created by thangit14 on 6/8/16.
 */
public class AdapterPagerMain extends FragmentStatePagerAdapter {
    private final boolean isCustomer;
    private int numberOfTabs = 3;


    public AdapterPagerMain(FragmentManager fm, boolean isCustomer) {
        super(fm);
        this.isCustomer = isCustomer;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentMyBooking fragmentMyBooking = new FragmentMyBooking();
                return fragmentMyBooking;
            case 1:
                if (isCustomer) {
                    FragmentListMasseur fragmentListMasseur = new FragmentListMasseur();
                    return fragmentListMasseur;
                } else {
                    FragmentUnavailable fragmentUnavailable = new FragmentUnavailable();
                    return fragmentUnavailable;
                }
            case 2:
                FragmentProfile fragmentProfile = new FragmentProfile();
                return fragmentProfile;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
