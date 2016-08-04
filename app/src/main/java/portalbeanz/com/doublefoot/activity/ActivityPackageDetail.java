package portalbeanz.com.doublefoot.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.adapter.AdapterListPackageChild;
import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.network.TaskGetPackageDetail;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.MySingleton;

/**
 * Created by thangit14 on 7/13/16.
 */
public class ActivityPackageDetail extends ActivityBase implements AdapterListPackageChild.MyOnClickListener {

    private static final String ITEM_PACKAGE = "ITEM_PACKAGE";
    private ListView listPackageChild;
    private AdapterListPackageChild adapterListPackageChild;
    private ItemPackage itemPackage;
    private ArrayList<ItemPackage> itemPackageChild;
    private View header;

    @Override
    protected int layoutId() {
        return R.layout.activity_package_detal;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initHeader(getString(R.string.title_activity_package_detail));
        showBackButton();
        showLogo();
        listPackageChild = (ListView) findViewById(R.id.list_package_child);
    }

    private void addHeaderListPackageChild() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        header = layoutInflater.inflate(R.layout.header_list_package_child, null);

        ViewGroup vgContactUS = (ViewGroup) header.findViewById(R.id.rl_contact_us);
        vgContactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneToContact();
            }
        });

        TextView txtPackageDetail = (TextView) header.findViewById(R.id.txt_package_detail);
        txtPackageDetail.setText(itemPackage.getDescription());

        ((TextView)header.findViewById(R.id.txt_package_name)).setText(itemPackage.getTitle());

        ImageLoader imageLoader = MySingleton.getInstance(this).getImageLoader();
        NetworkImageView imgAvatar = (NetworkImageView) header.findViewById(R.id.img_avatar);
        imgAvatar.setImageUrl(itemPackage.getImage(),imageLoader);

        listPackageChild.addHeaderView(header);
    }

    private void callPhoneToContact() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.PHONE_CONTACT));
        startActivity(intent);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        itemPackage = (ItemPackage) getIntent().getSerializableExtra(ITEM_PACKAGE);
        addHeaderListPackageChild();

        getPackageDetail();
    }

    private void getPackageDetail() {
        showLoading();
        TaskGetPackageDetail taskGetPackageDetail = new TaskGetPackageDetail(this, itemPackage.getId());
        taskGetPackageDetail.request(new Response.Listener<ArrayList<ItemPackage>>() {
            @Override
            public void onResponse(ArrayList<ItemPackage> response) {
                disMissLoading();
                itemPackageChild = response;
                adapterListPackageChild = new AdapterListPackageChild(ActivityPackageDetail.this, itemPackageChild, ActivityPackageDetail.this);
                listPackageChild.setAdapter(adapterListPackageChild);
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode, errorMessage);
            }
        });
    }

    public static void startActivity(FragmentActivity fragmentActivity, ItemPackage itemPackage) {
        Intent intent = new Intent(fragmentActivity, ActivityPackageDetail.class);
        intent.putExtra(ITEM_PACKAGE, itemPackage);
        fragmentActivity.startActivity(intent);

    }

    @Override
    public void onBookClick(int packageID) {
        ActivityBooking.startActivity(this,packageID);
    }
}
