package portalbeanz.com.doublefoot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import portalbeanz.com.doublefoot.R;


/**
 * Created by thangit14 on 7/15/16.
 */
public class ActivityPerformBook extends ActivityBase{
    private DatePicker datePicker;
    @Override
    protected int layoutId() {
        return R.layout.activity_perform_book;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        datePicker = (DatePicker) findViewById(R.id.datePicker);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    public static void startActivityForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), ActivityPerformBook.class);
        fragment.startActivityForResult(intent, requestCode);
    }
}
