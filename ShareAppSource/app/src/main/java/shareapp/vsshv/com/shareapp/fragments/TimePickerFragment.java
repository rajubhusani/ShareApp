package shareapp.vsshv.com.shareapp.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import shareapp.vsshv.com.shareapp.utils.Utility;

/**
 * Created by PC414506 on 29/08/16.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Bundle bundle = getArguments();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.AM_PM,24);
        calendar.set(Calendar.YEAR, bundle.getInt("year"));
        calendar.set(Calendar.MONTH, bundle.getInt("month"));
        calendar.set(Calendar.DAY_OF_MONTH, bundle.getInt("day"));
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

        Utility.getInstance(getActivity()).scheduleActivities(getActivity(), calendar, bundle);
    }
}