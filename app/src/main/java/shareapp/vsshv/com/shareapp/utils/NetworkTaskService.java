package shareapp.vsshv.com.shareapp.utils;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by PC414506 on 13/09/16.
 */

public class NetworkTaskService extends GcmTaskService{

    @Override
    public int onRunTask(TaskParams taskParams) {

        Bundle args = taskParams.getExtras();
        int unique = args.getInt("unique");
        if (args.getInt("app", 0) == 0) {
            new TwitterStatusUpdate(NetworkTaskService.this, unique).execute(args.getString("message"));
        } else if (args.getInt("app", 0) == 2) {
            new GmailSent(args, NetworkTaskService.this, unique).execute();
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
