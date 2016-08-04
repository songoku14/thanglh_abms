package portalbeanz.com.doublefoot.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.activity.ActivityBooking;
import portalbeanz.com.doublefoot.activity.ActivityMain;
import portalbeanz.com.doublefoot.adapter.AdapterItemUser;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.network.TaskBookAppointment;
import portalbeanz.com.doublefoot.network.TaskGetListMasseur;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Utils;

import static com.fourmob.datetimepicker.date.DatePickerDialog.newInstance;

/**
 * Created by thangit14 on 6/8/16.
 */
public class FragmentListMasseur extends FragmentBase implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnTimeClickListener, OnDateSetListener {
    public static final int ACTIVITY_PROFILE_TO_BOOK = 0;

    private static final String TAG_START_TIME = "TAG START TIME";
    private static final String TAG_END_TIME = "TAG END TIME";

    private ListView lvMasseur;
    private AdapterItemUser adapterItemUser;
    private ArrayList<ItemUser> itemUsers;
    //    private int page = 0;
//    private int perpage = 15;
    private boolean loadedDate = false;
    private int packageId;

    private DatePickerDialog datePickerDialog;
    private String strStartTime = "";
    private String strEndTime = "";
    private TimePickerDialog mDialogPickStartTime;
    private TimePickerDialog mDialogPickEndTime;
    private String strDate = "";

    @Override
    protected int layoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initViews(View mRoot, Bundle savedInstanceState) {
        lvMasseur = (ListView) mRoot.findViewById(R.id.lv_masseur);
        addHeaderlvMasseur();
        lvMasseur.setOnItemClickListener(mOnItemUserClickListener);
    }

    @Override
    protected void initVariable(View mRoot, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        packageId = bundle.getInt(ActivityBooking.PACKAGE_ID);
        getListMasseur();
    }

    private void addHeaderlvMasseur() {
        View header = getActivity().getLayoutInflater().inflate(R.layout.header_list_view_masseur, null);
        lvMasseur.addHeaderView(header);
    }

    private void getListMasseur() {
        showLoading();
        TaskGetListMasseur taskGetListMasseur = new TaskGetListMasseur(getContext());
        taskGetListMasseur.request(new Response.Listener<ArrayList<ItemUser>>() {
            @Override
            public void onResponse(ArrayList<ItemUser> response) {
                loadedDate = true;
                itemUsers = response;
                adapterItemUser = new AdapterItemUser(getContext(), itemUsers);
                lvMasseur.setAdapter(adapterItemUser);
                adapterItemUser.notifyDataSetChanged();
                disMissLoading();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                loadedDate = true;
                handleError(errorCode, errorMessage);
                disMissLoading();
            }
        });
    }

    private int clickedPosition;
    private AdapterView.OnItemClickListener mOnItemUserClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ActivityDetail.startActivityForResultToBookAppointment(FragmentListMasseur.this, ACTIVITY_PROFILE_TO_BOOK, itemUsers.get(position), packageId);
//            ActivityPerformBook.startActivityForResult(FragmentListMasseur.this,ACTIVITY_PROFILE_TO_BOOK);
            if (position != 0) {
                clickedPosition = position;
                openDialogTimePicker("", position);
            }
        }
    };

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && !loadedDate) {
            showLoading();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_PROFILE_TO_BOOK) {
            if (resultCode == Activity.RESULT_OK) {
//                ((ActivityMain) getActivity()).gotoFragmentDashboard(true);
                reloadData();
            }
        }
    }

    private void reloadData() {
        loadedDate = false;
        itemUsers.clear();
        getListMasseur();
    }

    private void openDialogTimePicker(String date, int position) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf;
        if (date.equalsIgnoreCase("")) {

        } else {
            sdf = new SimpleDateFormat(getString(R.string.format_date));
            try {
                calendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        datePickerDialog =
                newInstance(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), true, itemUsers.get(position - 1).getUnavailableDates());
        datePickerDialog.setYearRange(2016, 2037);

        datePickerDialog.show(getActivity().getSupportFragmentManager(), DatePickerDialog.class.getName());

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String currentMonth = "";
        String currentDay = "";
        if (month < 9) {
            currentMonth = "0" + (month + 1);
        } else {
            currentMonth = "" + (month + 1);
        }
        if (day < 10) {
            currentDay = "0" + day;
        } else {
            currentDay = "" + day;
        }
        strDate = year + getString(R.string.souce_year) +
                currentMonth + getString(R.string.souce_month) + currentDay;

        bookAppointment();
    }

    @Override
    public void onStartTimeClick(String date) {
        if (date.equals("")) {
            strStartTime = "";
            return;
        }
        try {
            Date appointmentDate = Utils.getDateWithDefaultFormat(getActivity(), date);
            long currentTimeMilis = System.currentTimeMillis();
            if (!strStartTime.equals("")) {
                currentTimeMilis = Utils.getTimeWithDefaultFormat(getActivity(), strStartTime.toString()).getTime();
            }

            Date beforeTime;
            if (date.equals(Utils.getDefaultFormatDateString(new Date(), getActivity()))) {
                beforeTime = new Date();
            } else {
                beforeTime = Utils.getTimeWithDefaultFormat(getActivity(), "00:00");
            }
            showDialogPickStartTime(Utils.getAvailableTime(itemUsers.get(clickedPosition - 1).getBookedTimes(), appointmentDate), currentTimeMilis, beforeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndTimeClick(String date) {
        strEndTime = date;
        if (date.equals("")) {
            return;
        }
        if (checkAndToastAppointmentStartTime()) {
            try {
                Date appointmentDate = Utils.getDateWithDefaultFormat(getActivity(), date);
                Date startTime = Utils.getTimeWithDefaultFormat(getActivity(), this.strStartTime);
                showDialogPickEndTime(Utils.getAvailableTime(itemUsers.get(clickedPosition - 1).getBookedTimes(), appointmentDate), startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialogPickStartTime(HashMap<Integer, HashMap<Integer, Integer>> availableTime,
                                         long currentTimeMillis, Date beforeTime) {
        mDialogPickStartTime = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setSureStringId(getString(R.string.choose))
                .setCancelStringId(getString(R.string.cancel))
                .setTitleStringId(getString(R.string.title_dialog_pick_appointment_start_time))
                .setCyclic(false)
                .setCurrentMillseconds(currentTimeMillis)
                .setThemeColor(getResources().getColor(R.color.color_action_bar))
                .setType(Type.HOURS_MINS)
                .setHourText(getString(R.string.hour))
                .setMinuteText(getString(R.string.minute))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_dialog_pick_time))
                .setAvailableTime(getAvailableTimeWithBeforeTime(availableTime, beforeTime))
                .build();
        mDialogPickStartTime.show(getActivity().getSupportFragmentManager(), TAG_START_TIME);
    }

    private void showDialogPickEndTime(HashMap<Integer, HashMap<Integer, Integer>> availableTime, Date beforeTome) {
        mDialogPickEndTime = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setSureStringId(getString(R.string.choose))
                .setCancelStringId(getString(R.string.cancel))
                .setTitleStringId(getString(R.string.title_dialog_pick_appointment_end_time))
                .setCyclic(false)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.color_action_bar))
                .setType(Type.HOURS_MINS)
                .setHourText(getString(R.string.hour))
                .setMinuteText(getString(R.string.minute))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_dialog_pick_time))
                .setAvailableTime(getAvailableTimeWithBeforeTime(availableTime, beforeTome))
                .setPickEndTime(true)
                .build();
        mDialogPickEndTime.show(getActivity().getSupportFragmentManager(), TAG_END_TIME);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (timePickerView.getTag().equals(TAG_START_TIME)) {
            String newStartTime = getDateToString(millseconds);
            if (!newStartTime.equals(strStartTime)) {
                strStartTime = newStartTime;
                strEndTime = "";

                datePickerDialog.updateStartTime(newStartTime);
            }

        } else if (timePickerView.getTag().equals(TAG_END_TIME)) {
            strEndTime = (getDateToString(millseconds));

            datePickerDialog.updateEndTime(getDateToString(millseconds));
        }
    }

    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat(getString(R.string.format_time));
        Date d = new Date(time);
        return sf.format(d);
    }

    /**
     * the available time must be greater than before time
     *
     * @param availableTime
     * @param before
     * @return
     */
    private HashMap<Integer, HashMap<Integer, Integer>> getAvailableTimeWithBeforeTime(
            HashMap<Integer, HashMap<Integer, Integer>> availableTime, Date before) {
        HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<>();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(before);

        for (Integer key : availableTime.keySet()) {
            int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
            if (key > startHour) {
                result.put(key, availableTime.get(key));
            } else if (key == startHour) {
                HashMap<Integer, Integer> resultMinutes = new HashMap<>();
                HashMap<Integer, Integer> minutes = availableTime.get(key);
                int startMinute = startCalendar.get(Calendar.MINUTE);

                for (Integer keyChild : minutes.keySet()) {
                    if (keyChild > startMinute) {
                        resultMinutes.put(minutes.get(keyChild), minutes.get(keyChild));
                    }
                }
                if (resultMinutes.size() != 0) {
                    result.put(key, resultMinutes);
                }
            }
        }
        return result;
    }

    private void bookAppointment() {
        if (checkBookAppointmentCondition()) {
            performBookAppointment();
        }
    }

    private boolean checkBookAppointmentCondition() {

        if (checkAndToastAppointmentDate() && checkAndToastAppointmentStartTime() && checkAndToastAppointmentEndTime()) {
            return true;
        }
        return false;
    }

    private boolean checkAndToastAppointmentEndTime() {
        if (strEndTime.equals("") || strEndTime.length() == 0) {
            showShortToast(getString(R.string.notif_end_time_can_not_empty));
            return false;
        }
        return true;
    }

    private boolean checkAndToastAppointmentStartTime() {
        if (strStartTime.equals("") || strStartTime.length() == 0) {
            showShortToast(getString(R.string.notif_start_time_can_not_empty));
            return false;
        }
        return true;
    }

    private boolean checkAndToastAppointmentDate() {
        if (strDate.equals("") || strDate.length() == 0) {
            showShortToast(getString(R.string.notif_date_can_not_empty));
            return false;
        }
        return true;
    }

    private void performBookAppointment() {
        showLoading(getString(R.string.loading_book_appointment));
        final String startDate = getFullDateString(strDate,
                strStartTime);

        String endDate = getFullDateString(strDate,
                strEndTime);

        TaskBookAppointment taskBookAppointment = new TaskBookAppointment(getContext(), itemUsers.get(clickedPosition - 1), startDate, endDate, packageId);
        taskBookAppointment.request(new Response.Listener<ItemAppointment>() {
            @Override
            public void onResponse(ItemAppointment itemAppointment) {
                disMissLoading();
                showLongToast(getString(R.string.book_appointment_successful));

                ActivityMain.startActivityWithNewTask(getActivity());
                try {
                    Utils.startAlarmScheduleToNotif(getActivity(),
                            getString(R.string.notif_has_appointment_at) + " " + strStartTime,
                            Utils.getDateWithTPattern(getActivity(), startDate), itemAppointment);
                } catch (ParseException e) {
                    e.printStackTrace();
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

    private String getFullDateString(String date, String time) {
        return date + "T" + time + ":00";
    }
}
