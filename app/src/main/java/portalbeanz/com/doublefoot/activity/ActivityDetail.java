package portalbeanz.com.doublefoot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.dialog.DialogConfirm;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.network.TaskBookAppointment;
import portalbeanz.com.doublefoot.network.TaskCancelAppointment;
import portalbeanz.com.doublefoot.network.TaskLoadAvatarFromUrl;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.CircleImageView;

import static com.fourmob.datetimepicker.date.DatePickerDialog.newInstance;

/**
 * Created by thangit14 on 6/12/16.
 */
public class ActivityDetail extends ActivityBase implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, OnDateSetListener, DialogConfirm.OnCompleteListener, DatePickerDialog.OnTimeClickListener {
    public static final String ITEM_USER = "ITEM USER";

    private static final String BOOK_ACTION = "BOOK ACTION";
    private static final String VIEW_ACTION = "VIEW ACTION";
    private static final String CANCEL_ACTION = "CANCEL_ACTION";

    private static final String TAG_START_TIME = "TAG START TIME";
    private static final String TAG_END_TIME = "TAG END TIME";
    private static final String ITEM_APPOINTMENT = "ITEM_APPOINTMENT";
    public static final String POSITION = "POSITION";
    private static final String IS_NEW_BOOKED_APPOINTMENT = "IS_NEW_BOOKED_APPOINTMENT";
    private static final String PACKAGE_ID = "PACKAGE_ID";
    private ItemAppointment itemAppointment;
    private int position;
    private int packageId;
    private DatePickerDialog datePickerDialog;

    @Override
    public void onStartTimeClick(String date) {
            try {
                Date appointmentDate = Utils.getDateWithDefaultFormat(this, date);
                long currentTimeMilis = System.currentTimeMillis();
                if (!edtAppointmentStartTime.getText().toString().equals("")) {
                    currentTimeMilis = Utils.getTimeWithDefaultFormat(this, edtAppointmentStartTime.getText().toString()).getTime();
                }

                Date beforeTime;
                if (date.equals(Utils.getDefaultFormatDateString(new Date(), this))) {
                    beforeTime = new Date();
                } else {
                    beforeTime = Utils.getTimeWithDefaultFormat(this, "00:00");
                }
                showDialogPickStartTime(Utils.getAvailableTime(mItemUser.getBookedTimes(), appointmentDate), currentTimeMilis, beforeTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onEndTimeClick(String date) {
        if (checkAndToastAppointmentStartTime()) {
            try {
                Date appointmentDate = Utils.getDateWithDefaultFormat(this, date);
                Date startTime = Utils.getTimeWithDefaultFormat(this, edtAppointmentStartTime.getText().toString());
                showDialogPickEndTime(Utils.getAvailableTime(mItemUser.getBookedTimes(), appointmentDate), startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private enum Action {BOOK, CANCEL, VIEW_NEW, VIEW_CANCEL}

    private Action action;

    private CircleImageView imageAvatar;
    private ImageView imgTimeline;
    private TextView txtAvatar;
    //    private EditText txtFirstname;
//    private EditText txtLastname;
    private EditText txtEmail;
    private EditText txtBirthday;
    private EditText txtGender;
    private EditText txtIntroduce;
    private EditText edtAppointmentDate;
    private EditText edtAppointmentStartTime;
    private EditText edtAppointmentEndtime;
    private EditText edtWorkingExeperience;
    private EditText edtSpecialization;

    private Button btnAction;
    private ViewGroup vgAppointmentDate;
    private ViewGroup vgAppointmentStartTime;
    private ViewGroup vgAppointmentEndTime;

    private ItemUser mItemUser;
    private ProgressWheel progressWheel;
    private String mDateTime = "";
    private TimePickerDialog mDialogPickStartTime;
    private TimePickerDialog mDialogPickEndTime;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initHeader(getString(R.string.my_page_profile));
        showBackButton();
        showLogo();
        txtAvatar = (TextView) findViewById(R.id.txt_user_name);
        imageAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgTimeline = (ImageView) findViewById(R.id.img_time_line);
        edtAppointmentDate = (EditText) findViewById(R.id.edt_appointment_date);
        edtAppointmentStartTime = (EditText) findViewById(R.id.edt_appointment_start_time);
        edtAppointmentEndtime = (EditText) findViewById(R.id.edt_appointment_end_time);
        edtSpecialization = (EditText) findViewById(R.id.edt_specialization);
        edtWorkingExeperience = (EditText) findViewById(R.id.edt_working_experience);

        btnAction = (Button) findViewById(R.id.btn_action);
        vgAppointmentDate = (ViewGroup) findViewById(R.id.rl_appointment_date);
        vgAppointmentStartTime = (ViewGroup) findViewById(R.id.rl_appointment_start_time);
        vgAppointmentEndTime = (ViewGroup) findViewById(R.id.rl_appointment_end_time);

//        txtFirstname = (EditText) findViewById(R.id.edt_first_name);
//        txtLastname = (EditText) findViewById(R.id.edt_last_name);
        txtEmail = (EditText) findViewById(R.id.edt_email);
        txtBirthday = (EditText) findViewById(R.id.edt_birth_day);
        txtGender = (EditText) findViewById(R.id.edt_gender);
        txtIntroduce = (EditText) findViewById(R.id.edt_introduce);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        btnAction.setOnClickListener(this);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mItemUser = (ItemUser) getIntent().getSerializableExtra(ITEM_USER);

        packageId = getIntent().getIntExtra(PACKAGE_ID,-1);

        if (getIntent().getBooleanExtra(BOOK_ACTION, false)) {
            action = Action.BOOK;
            btnAction.setText(getString(R.string.action_book));

            setOnClickForEditAppointmentInfo();
        } else if (getIntent().getBooleanExtra(CANCEL_ACTION, false)) {
            itemAppointment = (ItemAppointment) getIntent().getSerializableExtra(ITEM_APPOINTMENT);
            position = getIntent().getIntExtra(POSITION, -1);

            if (itemAppointment.getStartDate().compareTo(new Date()) < 0) {
                btnAction.setVisibility(View.GONE);
            } else {
                btnAction.setVisibility(View.VISIBLE);
                btnAction.setText(getString(R.string.action_cancel));
            }
            action = Action.CANCEL;

            if (isMasseur()) {
                btnAction.setVisibility(View.GONE);
            }

            setTimeOfAppointment();
        } else if (getIntent().getBooleanExtra(VIEW_ACTION, false)) {
            itemAppointment = (ItemAppointment) getIntent().getSerializableExtra(ITEM_APPOINTMENT);

            boolean isNew = getIntent().getBooleanExtra(IS_NEW_BOOKED_APPOINTMENT, false);
            action = isNew ? Action.VIEW_NEW : Action.VIEW_CANCEL;

            btnAction.setText(getString(isNew ? R.string.action_new_book : R.string.action_cancel_book));
            btnAction.setEnabled(false);

            setTimeOfAppointment();
        }

        String urlImage = mItemUser.getAvatarUrl();
        if (urlImage != null && !urlImage.equalsIgnoreCase("")) {
            loadAvatarFromServer(urlImage);
        } else {
            progressWheel.stopSpinning();
            progressWheel.setVisibility(View.GONE);
        }

        txtAvatar.setText(mItemUser.getLastName() + " " + mItemUser.getFirstName());

//        txtFirstname.setText(mItemUser.getFirstName());
//        txtLastname.setText(mItemUser.getLastName());
        txtEmail.setText(mItemUser.getEmail());
        txtBirthday.setText(mItemUser.getBod());
        setGenderText(mItemUser);
        txtIntroduce.setText(mItemUser.getIntroduce());
        edtWorkingExeperience.setText(mItemUser.getWorkingExperience() + "");
        edtSpecialization.setText(mItemUser.getSpecialization());

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_profile;
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
        String newDate = year + getString(R.string.souce_year) +
                currentMonth + getString(R.string.souce_month) + currentDay;

        try {
            Date date = Utils.getDateWithDefaultFormat(this, newDate);
            if (Utils.isPastDate(this,date)) {
                showShortToast(R.string.notif_can_not_choose_past_date);
            } else {
                if (!newDate.equals(edtAppointmentDate.getText().toString())) {
                    edtAppointmentDate.setText(newDate);
                    mDateTime = edtAppointmentDate.getText() + "";
                    edtAppointmentStartTime.setText("");
                    edtAppointmentEndtime.setText("");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
        @Override
    public void onClick(View v) {
        if (v == vgAppointmentDate || v == edtAppointmentDate) {
            openDialogTimePicker(mDateTime);
        } else if (v == btnAction) {
            if (action == Action.BOOK) {
                bookAppointment();
            } else if (action == Action.CANCEL) {
                cancelAppointment();
            }
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (timePickerView.getTag().equals(TAG_START_TIME)) {
            String newStartTime = getDateToString(millseconds);
            if (!newStartTime.equals(edtAppointmentStartTime.getText().toString())) {
                edtAppointmentStartTime.setText(newStartTime);
                edtAppointmentEndtime.setText("");

                datePickerDialog.updateStartTime(newStartTime);
            }

        } else if (timePickerView.getTag().equals(TAG_END_TIME)) {
            edtAppointmentEndtime.setText(getDateToString(millseconds));

            datePickerDialog.updateEndTime(getDateToString(millseconds));
        }
    }

    @Override
    public void onComplete(int position) {
        performCancelAppointment();
    }

    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat(getString(R.string.format_time));
        Date d = new Date(time);
        return sf.format(d);
    }

    private void setTimeOfAppointment() {
        edtAppointmentDate.setText(Utils.getDefaultFormatDateString(itemAppointment.getStartDate(), this));
        edtAppointmentStartTime.setText(Utils.getDefaultFormatTimeString(itemAppointment.getStartDate(), this));
        edtAppointmentEndtime.setText(Utils.getDefaultFormatTimeString(itemAppointment.getEndDate(), this));
    }

    private void bookAppointment() {
        if (checkBookAppointmentCondition()) {
            performBookAppointment();
        }
    }

    private void cancelAppointment() {
        DialogConfirm dialogConfirm = new DialogConfirm();
        Bundle bundle = new Bundle();
        bundle.putString(DialogConfirm.TITLE, getString(R.string.title_dialog_cancel_appointment));
        bundle.putString(DialogConfirm.CONTENT, getString(R.string.confirm_cancel_appointment));
        bundle.putBoolean(DialogConfirm.RETURN_TO_ACTIVITY, true);
//        bundle.putInt(DialogConfirm.POSITION, DialogConfirm.BOOKMARK);
        dialogConfirm.setArguments(bundle);
        dialogConfirm.show(getSupportFragmentManager(), "DialogConfirm");
    }

    private void performCancelAppointment() {
        showLoading(getString(R.string.loading_canel_appointment));

        TaskCancelAppointment taskCancelAppointment = new TaskCancelAppointment(this, itemAppointment.getAppointmentId());
        taskCancelAppointment.request(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                disMissLoading();
                showLongToast(getString(R.string.cancel_appointment_successful));

                Intent resultIntent = new Intent();
                resultIntent.putExtra(POSITION, position);

                setResult(RESULT_OK, resultIntent);
                ActivityDetail.this.finish();
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                handleError(errorCode, errorMessage);
                disMissLoading();
            }
        });
    }

    private void performBookAppointment() {
        showLoading(getString(R.string.loading_book_appointment));
        final String startDate = getFullDateString(edtAppointmentDate.getText().toString(),
                edtAppointmentStartTime.getText().toString());

        String endDate = getFullDateString(edtAppointmentDate.getText().toString(),
                edtAppointmentEndtime.getText().toString());

        TaskBookAppointment taskBookAppointment = new TaskBookAppointment(this, mItemUser, startDate, endDate, packageId);
        taskBookAppointment.request(new Response.Listener<ItemAppointment>() {
            @Override
            public void onResponse(ItemAppointment itemAppointment) {
                disMissLoading();
                showLongToast(getString(R.string.book_appointment_successful));

                setResult(RESULT_OK);
                ActivityDetail.this.finish();

                try {
                    Utils.startAlarmScheduleToNotif(ActivityDetail.this,
                            getString(R.string.notif_has_appointment_at) + " " + edtAppointmentStartTime.getText(),
                            Utils.getDateWithTPattern(ActivityDetail.this, startDate), itemAppointment);
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

    private boolean checkBookAppointmentCondition() {

        if (checkAndToastAppointmentDate() && checkAndToastAppointmentStartTime() && checkAndToastAppointmentEndTime()) {
            return true;
        }
        return false;
    }

    private boolean checkAndToastAppointmentEndTime() {
        if (edtAppointmentEndtime.getText().equals("") || edtAppointmentEndtime.getText().length() == 0) {
            showShortToast(getString(R.string.notif_end_time_can_not_empty));
            return false;
        }
        return true;
    }

    private boolean checkAndToastAppointmentStartTime() {
        if (edtAppointmentStartTime.getText().equals("") || edtAppointmentStartTime.getText().length() == 0) {
            showShortToast(getString(R.string.notif_start_time_can_not_empty));
            return false;
        }
        return true;
    }

    private boolean checkAndToastAppointmentDate() {
        if (edtAppointmentDate.getText().equals("") || edtAppointmentDate.getText().length() == 0) {
            showShortToast(getString(R.string.notif_date_can_not_empty));
            return false;
        }
        return true;
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
        mDialogPickStartTime.show(getSupportFragmentManager(), TAG_START_TIME);
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
        mDialogPickEndTime.show(getSupportFragmentManager(), TAG_END_TIME);
    }

    /**
     * the available time must be greater than before time
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

    private void setOnClickForEditAppointmentInfo() {
        vgAppointmentDate.setOnClickListener(this);
        vgAppointmentStartTime.setOnClickListener(this);
        vgAppointmentEndTime.setOnClickListener(this);
        edtAppointmentStartTime.setOnClickListener(this);
        edtAppointmentEndtime.setOnClickListener(this);
        edtAppointmentDate.setOnClickListener(this);
    }

    private void setGenderText(ItemUser itemUser) {
        if (itemUser.getGender() == 0) {
            txtGender.setText(R.string.female);
        } else if (itemUser.getGender() == 1) {
            txtGender.setText(R.string.male);
        } else if (itemUser.getGender() == -1) {
            txtGender.setText("********");
        }
    }


    private void loadAvatarFromServer(String url) {
        if (!url.equals("") && url.length() != 0) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
            TaskLoadAvatarFromUrl taskLoadAvatarFromUrl = new TaskLoadAvatarFromUrl() {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (null != bitmap) {
                        progressWheel.stopSpinning();
                        progressWheel.setVisibility(View.GONE);
                        setImageAvatar(bitmap);
                        blurImageTimeLine(bitmap);
                        Log.e("FragmentMyPageV2 --> onPostExecute", "Load avatar success");
                    }
                }
            };
            taskLoadAvatarFromUrl.execute(url);
        } else {
            progressWheel.stopSpinning();
            progressWheel.setVisibility(View.GONE);
        }
    }

    private void openDialogTimePicker(String date) {
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
                newInstance(this, this,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), true, mItemUser.getUnavailableDates());
        datePickerDialog.setYearRange(2016, 2037);

        datePickerDialog.show(getSupportFragmentManager(), DatePickerDialog.class.getName());

    }

    private void setImageAvatar(Bitmap bitmap) {
        imageAvatar.setImageBitmap(bitmap);
        imageAvatar.setBorderColor(Color.WHITE);
        imageAvatar.setBorderWidth(4);
    }

    private void blurImageTimeLine(Bitmap bitmap) {
        imgTimeline.setImageBitmap(Utils.blurBitmap(getApplicationContext(), bitmap, 4.f));
    }

    public static void startActivityForResultToBookAppointment(Fragment fragment, int requestCode, ItemUser itemUser, int packageId) {
        Intent intent = new Intent(fragment.getContext(), ActivityDetail.class);
        intent.putExtra(BOOK_ACTION, true);
        intent.putExtra(ITEM_USER, itemUser);
        intent.putExtra(PACKAGE_ID, packageId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResultToCancelAppointment(Fragment fragment, int requestCode,
                                                                 ItemUser itemUser, ItemAppointment itemAppointment, int position) {
        Intent intent = new Intent(fragment.getContext(), ActivityDetail.class);
        intent.putExtra(CANCEL_ACTION, true);
        intent.putExtra(ITEM_USER, itemUser);
        intent.putExtra(ITEM_APPOINTMENT, itemAppointment);
        intent.putExtra(POSITION, position);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param isNewBookedAppointment true if has new booked appointment else view canceled appointment
     * @return
     */
    public static Intent getIntentToViewAppointment(Context activity, ItemAppointment itemAppointment, boolean isNewBookedAppointment) {
        Intent intent = new Intent(activity, ActivityDetail.class);
        intent.putExtra(VIEW_ACTION, true);
        intent.putExtra(IS_NEW_BOOKED_APPOINTMENT, isNewBookedAppointment);
        intent.putExtra(ITEM_USER, itemAppointment.getItemUser());
        intent.putExtra(ITEM_APPOINTMENT, itemAppointment);
        return intent;
    }
}
