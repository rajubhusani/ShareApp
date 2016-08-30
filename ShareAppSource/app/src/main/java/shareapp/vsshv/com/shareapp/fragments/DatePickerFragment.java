package shareapp.vsshv.com.shareapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by PC414506 on 29/08/16.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        Calendar calendar = Calendar.getInstance();
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Bundle args = getArguments();
        if(args.getInt("app") == 0){
            showTimePickerDialog(day, month, year, args.getString("message"), args.getInt("app"));
        }
    }

    public void showTimePickerDialog(int day, int month, int year, String message, int app) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args= new Bundle();
        args.putInt("day",day);
        args.putInt("month",month);
        args.putInt("year",year);
        args.putString("message",message);
        args.putInt("app",app);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }
}

