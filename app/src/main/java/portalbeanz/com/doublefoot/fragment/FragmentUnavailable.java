package portalbeanz.com.doublefoot.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.network.TaskGetUnavailableDate;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.network.TaskSetUnavailableDate;

/**
 * Created by thangit14 on 6/13/16.
 */
public class FragmentUnavailable extends FragmentBase implements View.OnClickListener {
    private CalendarPickerView calendar;
    private Button btnSetUnavailable;

    @Override
    protected int layoutId() {
        return R.layout.fragment_unavailable;
    }

    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        calendar = (CalendarPickerView) mRoot.findViewById(R.id.calendar_view);
        btnSetUnavailable = (Button) mRoot.findViewById(R.id.btn_set_unavailable);
        btnSetUnavailable.setOnClickListener(this);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {
        loadDateUnavailableFromServer();
        initCalendarView();
    }

    private void loadDateUnavailableFromServer() {
        showLoading(getString(R.string.loading_data));
        TaskGetUnavailableDate taskGetUnavailableDate = new TaskGetUnavailableDate(getContext());
        taskGetUnavailableDate.request(new Response.Listener<List<Date>>() {
            @Override
            public void onResponse(List<Date> response) {
                disMissLoading();
                changeCalendarView(response);
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                disMissLoading();
                handleError(errorCode, errorMessage);
            }
        });
    }

    private void initCalendarView() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    private void changeCalendarView(List<Date> dates) {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(removeOldDate(dates,today));
    }

    private Collection<Date> removeOldDate(List<Date> dates, Date today) {
        ArrayList<Date> result = new ArrayList<>();
        for (Date date : dates) {
            if (date.compareTo(today) >= 0) {
                result.add(date);
            }
        }
        return result;
    }


    @Override
    public void onClick(View v) {
        if (v == btnSetUnavailable) {
            showLoading(getString(R.string.loading_set_un_available_date));
            List<Date> dates = calendar.getSelectedDates();
            TaskSetUnavailableDate taskSetUnavailableDate = new TaskSetUnavailableDate(getContext(), dates);
            taskSetUnavailableDate.request(new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    showLongToast(getString(R.string.notif_update_un_available_successful));
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
}
