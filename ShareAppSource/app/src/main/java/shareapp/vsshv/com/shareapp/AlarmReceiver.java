package shareapp.vsshv.com.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import shareapp.vsshv.com.shareapp.utils.TwitterStatusUpdate;

/**
 * Created by PC414506 on 29/08/16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Receiver called", Toast.LENGTH_SHORT).show();
        if(intent != null){
            if(intent.getIntExtra("app", 0) == 0){
                new TwitterStatusUpdate(context).execute(intent.getStringExtra("message"));
            }
        }
    }
}
