package shareapp.vsshv.com.shareapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import shareapp.vsshv.com.shareapp.AlarmReceiver;
import shareapp.vsshv.com.shareapp.R;

/**
 * Created by PC414506 on 29/08/16.
 */

public class Utility {

    private static Utility sUtilityObj= null;

    public static Utility getInstance(Context ctx){
        return (sUtilityObj == null)?new Utility():sUtilityObj;
    }

    public void scheduleActivities(Context context, Calendar cal, Bundle args){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("app", args.getInt("app"));
        intent.putExtra("message", args.getString("message"));
        int uniqueId = (int)System.currentTimeMillis();
        intent.putExtra("BroadCastId", uniqueId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                 alarmIntent);
    }

    public void loadProfileImageUsingPicasa(Context ctx, String profileUrl, ImageView view){
        Picasso.with(ctx)
                .load(profileUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(80, 80)
                .into(view);
    }
}
