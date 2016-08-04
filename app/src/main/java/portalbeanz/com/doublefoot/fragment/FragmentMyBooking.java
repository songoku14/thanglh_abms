package portalbeanz.com.doublefoot.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityDetail;
import portalbeanz.com.doublefoot.adapter.AdapterListAppointment;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.network.TaskGetListAppointment;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;

/**
 * Created by thangit14 on 6/8/16.
 */
public class FragmentMyBooking extends FragmentBase {
    private static final int ACTIVITY_PROFILE = 0;
    private ListView lvAppointment;
    private AdapterListAppointment adapterListAppointment;
    private ArrayList<ItemAppointment> itemAppointments = new ArrayList();
    private TextView txtNoAppointment;

    @Override
    protected int layoutId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        lvAppointment = (ListView) mRoot.findViewById(R.id.list_item_appointment);
        txtNoAppointment = (TextView) mRoot.findViewById(R.id.txt_no_appointment);

        adapterListAppointment = new AdapterListAppointment(getContext(), itemAppointments, true);
        lvAppointment.setAdapter(adapterListAppointment);
        lvAppointment.setOnItemClickListener(mOnItmListAppointmentClickListener);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {
        getListAppointment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_PROFILE) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra(ActivityDetail.POSITION, -1);
                if (position > 0) {
                    itemAppointments.remove(position);

                    removeHeaderIfNoItem(position);

                    adapterListAppointment.notifyDataSetChanged();
                }
            }
        }

    }

    private void removeHeaderIfNoItem(int position) {
        if (position == itemAppointments.size()) {
            if (itemAppointments.get(position - 1).isHeaderType()) {
                itemAppointments.remove(position - 1);
            }
        } else {
            if (itemAppointments.get(position).isHeaderType() &&
                    itemAppointments.get(position - 1).isHeaderType()) {
                itemAppointments.remove(position - 1);
            }
        }
    }

    private void getListAppointment() {
        showLoading(getString(R.string.loading_data));
        TaskGetListAppointment taskGetListAppointment = new TaskGetListAppointment(getContext());
        taskGetListAppointment.request(new Response.Listener<ArrayList<ItemAppointment>>() {
            @Override
            public void onResponse(ArrayList<ItemAppointment> response) {
                disMissLoading();
                if (response == null || response.size() == 0) {
                    lvAppointment.setVisibility(View.GONE);
                    txtNoAppointment.setVisibility(View.VISIBLE);
                } else {
                    lvAppointment.setVisibility(View.VISIBLE);
                    txtNoAppointment.setVisibility(View.GONE);

                    itemAppointments.addAll(response);
                    adapterListAppointment.notifyDataSetChanged();
                }
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode, errorMessage);
            }
        });
    }

    private AdapterView.OnItemClickListener mOnItmListAppointmentClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (itemAppointments.get(position).isItemType()) {
                ActivityDetail.startActivityForResultToCancelAppointment(FragmentMyBooking.this, ACTIVITY_PROFILE,
                        itemAppointments.get(position).getItemUser(), itemAppointments.get(position), position);
            }
        }
    };


    public void reloadData() {
        itemAppointments.clear();
        getListAppointment();
    }
}
