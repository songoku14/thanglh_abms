package com.fourmob.datetimepicker.date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.R;
import com.fourmob.datetimepicker.Utils;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DatePickerDialog extends DialogFragment implements View.OnClickListener, DatePickerController {

    private static final String KEY_SELECTED_YEAR = "year";
    private static final String KEY_SELECTED_MONTH = "month";
    private static final String KEY_SELECTED_DAY = "day";
    private static final String KEY_VIBRATE = "vibrate";

    private ViewGroup vgAppointmentDate;
    private ViewGroup vgAppointmentStartTime;
    private ViewGroup vgAppointmentEndTime;

    private EditText edtAppointmentStartTime;
    private EditText edtAppointmentEndtime;

//    private TimePickerDialog mDialogPickStartTime;
//    private TimePickerDialog mDialogPickEndTime;

    private static final int MAX_YEAR = 2037;
    private static final int MIN_YEAR = 1902;

    private static final int UNINITIALIZED = -1;
    private static final int MONTH_AND_DAY_VIEW = 0;
    private static final int YEAR_VIEW = 1;

    public static final int ANIMATION_DELAY = 500;
    public static final String KEY_WEEK_START = "week_start";
    public static final String KEY_YEAR_START = "year_start";
    public static final String KEY_YEAR_END = "year_end";
    public static final String KEY_CURRENT_VIEW = "current_view";
    public static final String KEY_LIST_POSITION = "list_position";
    public static final String KEY_LIST_POSITION_OFFSET = "list_position_offset";

    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());
    private static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    private DateFormatSymbols mDateFormatSymbols = new DateFormatSymbols();

    private final Calendar mCalendar = Calendar.getInstance();
    private HashSet<OnDateChangedListener> mListeners = new HashSet<OnDateChangedListener>();
    private OnDateSetListener mCallBack;
    private OnTimeClickListener onTimeClickListener;

    private AccessibleDateAnimator mAnimator;
    private boolean mDelayAnimation = true;
    private long mLastVibrate;
    private int mCurrentView = UNINITIALIZED;

    private int mWeekStart = mCalendar.getFirstDayOfWeek();
    private int mMaxYear = MAX_YEAR;
    private int mMinYear = MIN_YEAR;

    private String mDayPickerDescription;
    private String mYearPickerDescription;
    private String mSelectDay;
    private String mSelectYear;

    private TextView mDayOfWeekView;
    private DayPickerView mDayPickerView;
    private Button mDoneButton;
    private Button mCancelButton;
    private LinearLayout mMonthAndDayView;
    private TextView mSelectedDayTextView;
    private TextView mSelectedMonthTextView;
    private Vibrator mVibrator;
    private YearPickerView mYearPickerView;
    private TextView mYearView;

    private boolean mVibrate = true;
    private boolean mCloseOnSingleTapDay;
    private List<Date> mListDateUnavailable;

    private void adjustDayInMonthIfNeeded(int month, int year) {
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int daysInMonth = Utils.getDaysInMonth(month, year);
        if (day > daysInMonth) {
            mCalendar.set(Calendar.DAY_OF_MONTH, daysInMonth);
        }
    }

    public DatePickerDialog() {
        // Empty constructor required for dialog fragment. DO NOT REMOVE
    }

    public static DatePickerDialog newInstance(OnDateSetListener onDateSetListener, int year, int month, int day) {
        return newInstance(onDateSetListener, year, month, day, true);
    }

    public static DatePickerDialog newInstance(OnDateSetListener onDateSetListener, int year, int month, int day, boolean vibrate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog();
        datePickerDialog.initialize(onDateSetListener, null, year, month, day, vibrate);
        return datePickerDialog;
    }

    public static DatePickerDialog newInstance(OnDateSetListener onDateSetListener, OnTimeClickListener onTimeClickListener, int year, int month, int day, boolean vibrate, List<Date> listDateDisable) {
        DatePickerDialog datePickerDialog = new DatePickerDialog();
        datePickerDialog.initialize(onDateSetListener, onTimeClickListener, year, month, day, vibrate, listDateDisable);
        return datePickerDialog;
    }

    public void setVibrate(boolean vibrate) {
        mVibrate = vibrate;
    }

    private void setCurrentView(int currentView) {
        setCurrentView(currentView, false);
    }

    private void setCurrentView(int currentView, boolean forceRefresh) {
        long timeInMillis = mCalendar.getTimeInMillis();
        switch (currentView) {
            case MONTH_AND_DAY_VIEW:
                ObjectAnimator monthDayAnim = Utils.getPulseAnimator(mMonthAndDayView, 0.9F, 1.05F);
                if (mDelayAnimation) {
                    monthDayAnim.setStartDelay(ANIMATION_DELAY);
                    mDelayAnimation = false;
                }
                mDayPickerView.onDateChanged();
                if (mCurrentView != currentView || forceRefresh) {
                    mMonthAndDayView.setSelected(true);
                    mYearView.setSelected(false);
                    mAnimator.setDisplayedChild(MONTH_AND_DAY_VIEW);
                    mCurrentView = currentView;
                }
                monthDayAnim.start();
                String monthDayDesc = DateUtils.formatDateTime(getActivity(), timeInMillis, DateUtils.FORMAT_SHOW_DATE);
                mAnimator.setContentDescription(mDayPickerDescription + ": " + monthDayDesc);
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectDay);
                break;
            case YEAR_VIEW:
                ObjectAnimator yearAnim = Utils.getPulseAnimator(mYearView, 0.85F, 1.1F);
                if (mDelayAnimation) {
                    yearAnim.setStartDelay(ANIMATION_DELAY);
                    mDelayAnimation = false;
                }
                mYearPickerView.onDateChanged();
                if (mCurrentView != currentView || forceRefresh) {
                    mMonthAndDayView.setSelected(false);
                    mYearView.setSelected(true);
                    mAnimator.setDisplayedChild(YEAR_VIEW);
                    mCurrentView = currentView;
                }
                yearAnim.start();
                String dayDesc = YEAR_FORMAT.format(timeInMillis);
                mAnimator.setContentDescription(mYearPickerDescription + ": " + dayDesc);
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectYear);
                break;
        }
    }

    private void updateDisplay(boolean announce) {
        /*if (mDayOfWeekView != null) {
            mDayOfWeekView.setText(mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
                    Locale.getDefault()).toUpperCase(Locale.getDefault()));
        }

        mSelectedMonthTextView.setText(mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                Locale.getDefault()).toUpperCase(Locale.getDefault()));*/

        if (this.mDayOfWeekView != null) {
            this.mCalendar.setFirstDayOfWeek(mWeekStart);
            this.mDayOfWeekView.setText(mDateFormatSymbols.getWeekdays()[this.mCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()));
//            this.mDayOfWeekView.setText(mDateFormatSymbols.getMonths()[this.mCalendar.get(Calendar.MONTH)].toUpperCase(Locale.getDefault()));
        }

        this.mSelectedMonthTextView.setText(mDateFormatSymbols.getMonths()[this.mCalendar.get(Calendar.MONTH)].toUpperCase(Locale.getDefault()));

        mSelectedDayTextView.setText(DAY_FORMAT.format(mCalendar.getTime()));
        mYearView.setText(YEAR_FORMAT.format(mCalendar.getTime()));

        // Accessibility.
        long millis = mCalendar.getTimeInMillis();
        mAnimator.setDateMillis(millis);
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        String monthAndDayText = DateUtils.formatDateTime(getActivity(), millis, flags);
        mMonthAndDayView.setContentDescription(monthAndDayText);

        if (announce) {
            flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
            String fullDateText = DateUtils.formatDateTime(getActivity(), millis, flags);
            Utils.tryAccessibilityAnnounce(mAnimator, fullDateText);
        }
    }

    private void updatePickers() {
        Iterator<OnDateChangedListener> iterator = mListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDateChanged();
        }
    }

    public int getFirstDayOfWeek() {
        return mWeekStart;
    }

    public int getMaxYear() {
        return mMaxYear;
    }

    public int getMinYear() {
        return mMinYear;
    }

    public SimpleMonthAdapter.CalendarDay getSelectedDay() {
        return new SimpleMonthAdapter.CalendarDay(mCalendar);
    }

    public void initialize(OnDateSetListener onDateSetListener, OnTimeClickListener onTimeClickListener, int year, int month, int day, boolean vibrate) {
        if (year > MAX_YEAR)
            throw new IllegalArgumentException("year end must < " + MAX_YEAR);
        if (year < MIN_YEAR)
            throw new IllegalArgumentException("year end must > " + MIN_YEAR);
        mCallBack = onDateSetListener;
        this.onTimeClickListener = onTimeClickListener;
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mVibrate = vibrate;
    }

    public void initialize(OnDateSetListener onDateSetListener, OnTimeClickListener onTimeClickListener, int year, int month, int day, boolean vibrate, List<Date> listDateDisable) {
        if (year > MAX_YEAR)
            throw new IllegalArgumentException("year end must < " + MAX_YEAR);
        if (year < MIN_YEAR)
            throw new IllegalArgumentException("year end must > " + MIN_YEAR);
        mCallBack = onDateSetListener;
        this.onTimeClickListener = onTimeClickListener;
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mVibrate = vibrate;
        mListDateUnavailable = listDateDisable;
    }

    public void onClick(View view) {
        tryVibrate();
        if (view.getId() == R.id.date_picker_year)
            setCurrentView(YEAR_VIEW);
        else if (view.getId() == R.id.date_picker_month_and_day)
            setCurrentView(MONTH_AND_DAY_VIEW);
        else if (view == vgAppointmentStartTime || view == edtAppointmentStartTime) {
            onTimeClickListener.onStartTimeClick(getDateString());
        } else if (view == vgAppointmentEndTime || view == edtAppointmentEndtime) {
            onTimeClickListener.onEndTimeClick(getDateString());
        }
    }

    public void updateStartTime(String text) {
        edtAppointmentStartTime.setText(text);
    }

    public void updateEndTime(String text) {
        edtAppointmentEndtime.setText(text);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mVibrator = ((Vibrator) activity.getSystemService("vibrator"));
        if (bundle != null) {
            mCalendar.set(Calendar.YEAR, bundle.getInt(KEY_SELECTED_YEAR));
            mCalendar.set(Calendar.MONTH, bundle.getInt(KEY_SELECTED_MONTH));
            mCalendar.set(Calendar.DAY_OF_MONTH, bundle.getInt(KEY_SELECTED_DAY));
            mVibrate = bundle.getBoolean(KEY_VIBRATE);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = layoutInflater.inflate(R.layout.date_picker_dialog, null);

        mDayOfWeekView = ((TextView) view.findViewById(R.id.date_picker_header));
        mMonthAndDayView = ((LinearLayout) view.findViewById(R.id.date_picker_month_and_day));
        mMonthAndDayView.setOnClickListener(this);
        mSelectedMonthTextView = ((TextView) view.findViewById(R.id.date_picker_month));
        mSelectedDayTextView = ((TextView) view.findViewById(R.id.date_picker_day));
        mYearView = ((TextView) view.findViewById(R.id.date_picker_year));
        mYearView.setOnClickListener(this);

        edtAppointmentStartTime = (EditText) view.findViewById(R.id.edt_appointment_start_time);
        edtAppointmentEndtime = (EditText) view.findViewById(R.id.edt_appointment_end_time);
        vgAppointmentStartTime = (ViewGroup) view.findViewById(R.id.rl_appointment_start_time);
        vgAppointmentEndTime = (ViewGroup) view.findViewById(R.id.rl_appointment_end_time);
        addOnClickTime();

        int listPosition = -1;
        int currentView = MONTH_AND_DAY_VIEW;
        int listPositionOffset = 0;
        if (bundle != null) {
            mWeekStart = bundle.getInt(KEY_WEEK_START);
            mMinYear = bundle.getInt(KEY_YEAR_START);
            mMaxYear = bundle.getInt(KEY_YEAR_END);
            currentView = bundle.getInt(KEY_CURRENT_VIEW);
            listPosition = bundle.getInt(KEY_LIST_POSITION);
            listPositionOffset = bundle.getInt(KEY_LIST_POSITION_OFFSET);
        }

        Activity activity = getActivity();
        mDayPickerView = new DayPickerView(activity, this, mListDateUnavailable);
        mYearPickerView = new YearPickerView(activity, this);

        Resources resources = getResources();
        mDayPickerDescription = resources.getString(R.string.day_picker_description);
        mSelectDay = resources.getString(R.string.select_day);
        mYearPickerDescription = resources.getString(R.string.year_picker_description);
        mSelectYear = resources.getString(R.string.select_year);

        mAnimator = ((AccessibleDateAnimator) view.findViewById(R.id.animator));
        mAnimator.addView(mDayPickerView);
        mAnimator.addView(mYearPickerView);
        mAnimator.setDateMillis(mCalendar.getTimeInMillis());

        AlphaAnimation inAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        inAlphaAnimation.setDuration(300L);
        mAnimator.setInAnimation(inAlphaAnimation);

        AlphaAnimation outAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
        outAlphaAnimation.setDuration(300L);
        mAnimator.setOutAnimation(outAlphaAnimation);

        mDoneButton = ((Button) view.findViewById(R.id.done));
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onDoneButtonClick();
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.this.dismiss();
            }
        });

        updateDisplay(false);
        setCurrentView(currentView, true);

        if (listPosition != -1) {
            if (currentView == MONTH_AND_DAY_VIEW) {
                mDayPickerView.postSetSelection(listPosition);
            }
            if (currentView == YEAR_VIEW) {
                mYearPickerView.postSetSelectionFromTop(listPosition, listPositionOffset);
            }
        }
        return view;
    }

    private void addOnClickTime() {
        vgAppointmentStartTime.setOnClickListener(this);
        vgAppointmentEndTime.setOnClickListener(this);
        edtAppointmentStartTime.setOnClickListener(this);
        edtAppointmentEndtime.setOnClickListener(this);
    }

    private void removeOnClickTime(){
        vgAppointmentStartTime.setOnClickListener(null);
        vgAppointmentEndTime.setOnClickListener(null);
        edtAppointmentStartTime.setOnClickListener(null);
        edtAppointmentEndtime.setOnClickListener(null);
    }


    private void onDoneButtonClick() {
        tryVibrate();
        boolean isUnavailableDate = checkUnavailableDate(mCalendar);
        if (mCallBack != null && !isUnavailableDate) {
            if (edtAppointmentEndtime.getText().equals("") || edtAppointmentEndtime.getText().length() == 0) {
                Toast.makeText(DatePickerDialog.this.getActivity(), "You must to enter time", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mCallBack.onDateSet(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        dismiss();
    }

    private String getDateString() {
        return Utils.getDateString("yyyy/MM/dd", mCalendar.getTime());
    }

    private boolean checkUnavailableDate(Calendar calendar) {
        if (mListDateUnavailable == null || mListDateUnavailable.size() == 0) {
            return false;
        }
        Calendar calendarUnavailable = Calendar.getInstance();
        for (Date date : mListDateUnavailable) {
            calendarUnavailable.setTime(date);
            if (calendarUnavailable.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    calendarUnavailable.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    public void onDayOfMonthSelected(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);


        if (isPastDate(getActivity(),mCalendar.getTime())) {
            Toast.makeText(getActivity(), "You can not choose a past date", Toast.LENGTH_SHORT).show();

            Calendar currentCalendar = Calendar.getInstance();

            mCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            mCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            mCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));

            removeOnClickTime();
        } else {
            updatePickers();
            updateDisplay(true);
            edtAppointmentEndtime.setText("");
            edtAppointmentStartTime.setText("");
            onTimeClickListener.onStartTimeClick("");
            onTimeClickListener.onEndTimeClick("");

            addOnClickTime();

            if (mCloseOnSingleTapDay) {
                onDoneButtonClick();
            }
        }
    }

    public boolean isPastDate(Context context, Date date) {
        try {
            return date.before(Utils.getDateWithDefaultFormat(context, Utils.getDefaultFormatDateString(new Date(), context)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }



    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(KEY_SELECTED_YEAR, mCalendar.get(Calendar.YEAR));
        bundle.putInt(KEY_SELECTED_MONTH, mCalendar.get(Calendar.MONTH));
        bundle.putInt(KEY_SELECTED_DAY, mCalendar.get(Calendar.DAY_OF_MONTH));
        bundle.putInt(KEY_WEEK_START, mWeekStart);
        bundle.putInt(KEY_YEAR_START, mMinYear);
        bundle.putInt(KEY_YEAR_END, mMaxYear);
        bundle.putInt(KEY_CURRENT_VIEW, mCurrentView);

        int listPosition = -1;
        if (mCurrentView == 0) {
            listPosition = mDayPickerView.getMostVisiblePosition();
        }
        if (mCurrentView == 1) {
            listPosition = mYearPickerView.getFirstVisiblePosition();
            bundle.putInt(KEY_LIST_POSITION_OFFSET, mYearPickerView.getFirstPositionOffset());
        }
        bundle.putInt(KEY_LIST_POSITION, listPosition);
        bundle.putBoolean(KEY_VIBRATE, mVibrate);
    }

    public void onYearSelected(int year) {
        adjustDayInMonthIfNeeded(mCalendar.get(Calendar.MONTH), year);
        mCalendar.set(Calendar.YEAR, year);
        updatePickers();
        setCurrentView(MONTH_AND_DAY_VIEW);
        updateDisplay(true);
    }

    public void registerOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        mListeners.add(onDateChangedListener);
    }

    public void setFirstDayOfWeek(int startOfWeek) {
        if (startOfWeek < Calendar.SUNDAY || startOfWeek > Calendar.SATURDAY) {
            throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and " +
                    "Calendar.SATURDAY");
        }
        mWeekStart = startOfWeek;
        if (mDayPickerView != null) {
            mDayPickerView.onChange();
        }
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        mCallBack = onDateSetListener;
    }

    public void setYearRange(int minYear, int maxYear) {
        if (maxYear < minYear)
            throw new IllegalArgumentException("Year end must be larger than year start");
        if (maxYear > MAX_YEAR)
            throw new IllegalArgumentException("max year end must < " + MAX_YEAR);
        if (minYear < MIN_YEAR)
            throw new IllegalArgumentException("min year end must > " + MIN_YEAR);
        mMinYear = minYear;
        mMaxYear = maxYear;
        if (mDayPickerView != null)
            mDayPickerView.onChange();
    }

    public void tryVibrate() {
        if (mVibrator != null && mVibrate) {
            long timeInMillis = SystemClock.uptimeMillis();
            if (timeInMillis - mLastVibrate >= 125L) {
                mVibrator.vibrate(5L);
                mLastVibrate = timeInMillis;
            }
        }
    }

    public void setCloseOnSingleTapDay(boolean closeOnSingleTapDay) {
        mCloseOnSingleTapDay = closeOnSingleTapDay;
    }

    public static abstract interface OnDateChangedListener {
        public abstract void onDateChanged();
    }

    public static abstract interface OnDateSetListener {
        public abstract void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day);
    }

    public interface OnTimeClickListener {
        void onStartTimeClick(String date);

        void onEndTimeClick(String date);
    }

}