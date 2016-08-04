package portalbeanz.com.doublefoot.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityPackageDetail;
import portalbeanz.com.doublefoot.adapter.AdapterListPackages;
import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.network.TaskGetListPackages;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;

/**
 * Created by thangit14 on 7/11/16.
 */
public class FragmentPackages extends FragmentBase {
    private ListView lvPackages;
    private AdapterListPackages adapterListPackages;
    private ArrayList<ItemPackage> itemPackages;
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                ItemPackage itemPackage = itemPackages.get(position - 1);
                ActivityPackageDetail.startActivity(getActivity(), itemPackage);
            }
        }
    };

    @Override
    protected int layoutId() {
        return R.layout.fragment_packages;
    }

    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        lvPackages = (ListView) mRoot.findViewById(R.id.list_packages);
        lvPackages.setOnItemClickListener(mOnItemClickListener);

        View header = getActivity().getLayoutInflater().inflate(R.layout.header_list_packages, null);
        lvPackages.addHeaderView(header);

//        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_list_packages, null);
//        lvPackages.addFooterView(footer);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {
        getPackages();
    }

    private void getPackages() {
        showLoading();
        TaskGetListPackages taskGetListPackages = new TaskGetListPackages(getContext());
        taskGetListPackages.request(new Response.Listener<ArrayList<ItemPackage>>() {
            @Override
            public void onResponse(ArrayList<ItemPackage> datas) {
                itemPackages = datas;
                adapterListPackages = new AdapterListPackages(getContext(), itemPackages);
                lvPackages.setAdapter(adapterListPackages);
                disMissLoading();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                handleError(errorCode, errorMessage);
                disMissLoading();
            }
        });

    }
}
