package portalbeanz.com.doublefoot.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.adapter.AdapterListNavDrawer;
import portalbeanz.com.doublefoot.fragment.FragmentLocateUs;
import portalbeanz.com.doublefoot.fragment.FragmentMyBooking;
import portalbeanz.com.doublefoot.fragment.FragmentPackages;
import portalbeanz.com.doublefoot.fragment.FragmentProfile;
import portalbeanz.com.doublefoot.fragment.FragmentSetting;
import portalbeanz.com.doublefoot.fragment.FragmentUnavailable;
import portalbeanz.com.doublefoot.model.ItemNavDrawer;

/**
 * Created by thangit14 on 6/2/16.
 */
public class ActivityMain extends ActivityBase implements ActivityBase.OnLeftActionBarClick {
    public static final int FRAGMENT_MY_BOOKING = 0;
    public static final int FRAGMENT_PACKAGES_OR_UNAVAILABLE_TIME = 1;
    public static final int FRAGMENT_PROFILE = 2;
    public static final int FRAGMENT_SETTING = 3;
    public static final int FRAGMENT_LOCATE_US = 4;

//    private ViewPager viewPager;
//    private TabLayout tabLayout;
//    private AdapterPagerMain adapterPagerMain;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ArrayList<ItemNavDrawer> itemNavDrawers;
    private AdapterListNavDrawer adapterListNavDrawer;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private int selectedPosition = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (!isLogin()) {
            showLongToast(R.string.notif_you_need_login);
            ActivityLogin.startActivityWithNewTask(this);
            this.finish();
        }

        initHeader(getString(R.string.title_activity_main));
//        hideLeftButton();
        showLogo();
        showMenuButton(this);

        setupMenuLeft();
        displayView(FRAGMENT_MY_BOOKING);

//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_dashboard)));
//        if (isCustomer()) {
//            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_schedule)));
//        } else {
//            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_unavailable)));
//        }
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_profile)));
//        tabLayout.setOnTabSelectedListener(mOnTabSelectedListener);
//
//        viewPager = (ViewPager) findViewById(R.id.pager);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.page_margin));

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

//        adapterPagerMain = new AdapterPagerMain(getSupportFragmentManager(),isCustomer());
//        viewPager.setAdapter(adapterPagerMain);
//        viewPager.setOffscreenPageLimit(2);

//        navMenuTitles= getResources().getStringArray()
    }

    private void setupMenuLeft() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        itemNavDrawers = getNavDrawerItems();
        adapterListNavDrawer = new AdapterListNavDrawer(this, itemNavDrawers);
        mDrawerList.setAdapter(adapterListNavDrawer);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemNavDrawers.get(selectedPosition).setSelected(false);
                itemNavDrawers.get(position).setSelected(true);
                selectedPosition = position;
                displayView(position);
                adapterListNavDrawer.notifyDataSetChanged();
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_menu_left, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                updateIconMenuLeft();
            }

            public void onDrawerOpened(View drawerView) {
                updateIconMenuLeft();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case FRAGMENT_MY_BOOKING:
                fragment = new FragmentMyBooking();
                break;
            case FRAGMENT_PACKAGES_OR_UNAVAILABLE_TIME:
                if (isCustomer()) {
                    fragment = new FragmentPackages();
                } else {
                    fragment = new FragmentUnavailable();
                }
                break;
            case FRAGMENT_PROFILE:
                fragment = new FragmentProfile();
                break;
            case FRAGMENT_SETTING:
                fragment = new FragmentSetting();
                break;
            case FRAGMENT_LOCATE_US:
                fragment = new FragmentLocateUs();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private ArrayList<ItemNavDrawer> getNavDrawerItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        ArrayList<ItemNavDrawer> items = new ArrayList<>();
        for (int i = 0; i < navMenuTitles.length; i++) {
            ItemNavDrawer item = new ItemNavDrawer(navMenuTitles[i], navMenuIcons.getResourceId(i, -1));
            items.add(item);
        }

        items.get(0).setSelected(true);
        if (isMasseur()) {
            items.get(FRAGMENT_PACKAGES_OR_UNAVAILABLE_TIME).setTitle(getString(R.string.title_unavailabe_time));
//            items.remove(FRAGMENT_SETTING);
//            items.remove(FRAGMENT_LOCATE_US);
        }

        navMenuIcons.recycle();
        return items;
    }

//    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
//        @Override
//        public void onTabSelected(TabLayout.Tab tab) {
//            viewPager.setCurrentItem(tab.getPosition());
//        }
//
//        @Override
//        public void onTabUnselected(TabLayout.Tab tab) {
//
//        }
//
//        @Override
//        public void onTabReselected(TabLayout.Tab tab) {
//
//        }
//    };

//    public void gotoFragmentDashboard(boolean needReload) {
//        viewPager.setCurrentItem(0);
//        if (needReload) {
//            reloadFragmentDashboard();
//        }
//    }
//
//    private void reloadFragmentDashboard() {
//        FragmentManager manager = getSupportFragmentManager();
//        List<Fragment> fragmentList = manager.getFragments();
//        for (Fragment fragment : fragmentList) {
//            if (fragment instanceof FragmentMyBooking) {
//                ((FragmentMyBooking) fragment).reloadData();
//                return;
//            }
//        }
//    }

    public static void startActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, ActivityMain.class);
        activity.startActivity(intent);
    }

    public static void startActivityWithNewTask(FragmentActivity activity) {
        Intent intent = new Intent(activity, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onLeftActionBarClicked(boolean isOpenMenuLeft) {
        if (isOpenMenuLeft) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
