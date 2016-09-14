package shareapp.vsshv.com.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.Arrays;

import javax.mail.MessagingException;

import shareapp.vsshv.com.shareapp.database.DataBaseDao;

/**
 * Created by PC414506 on 06/09/16.
 */

public class GmailSent extends AsyncTask<Void, Void, Void> {
    private Exception mLastError = null;
    private String to = null;
    private String subject = null;
    private String body = null;
    private Context ctx;
    private com.google.api.services.gmail.Gmail mService = null;
    private Gmail.Users.GetProfile profileInfo = null;
    private GoogleAccountCredential mCredential = null;
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND};
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private int unique;

    public GmailSent(Bundle args, Context ctx, int unique) {
        this.to = args.getString("to");
        this.subject = args.getString("subject");
        this.body = args.getString("body");
        this.ctx = ctx;
        this.unique = unique;
        mCredential = GoogleAccountCredential.usingOAuth2(
                ctx.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    /**
     * Background task to call Gmail API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {

        try {
            Log.d("Account",to+"--"+subject+"---"+body+"--"+profileInfo.getUserId());
            Message msg = Utility.sendMessage(mService, profileInfo.getUserId(),
                    to, subject, body);
        }catch (MessagingException ex){
            ex.printStackTrace();
            mLastError = ex;
            cancel(true);
            return null;
        }catch (IOException ex){
            ex.printStackTrace();
            mLastError = ex;
            cancel(true);
            return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (! isDeviceOnline()) {
            Toast.makeText(ctx, "No network connection available.", Toast.LENGTH_SHORT).show();
        } else {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("ShareApp")
                    .build();
            try {
                SharedPreferences settings = ctx.getSharedPreferences("ShareApp",Context.MODE_PRIVATE);
                profileInfo = mService.users().getProfile(settings.getString(PREF_ACCOUNT_NAME, ""));
                mCredential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, ""));
               // Log.d("Account",profileInfo.getUserId()+"--"+settings.getString(PREF_ACCOUNT_NAME, ""));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Void output) {
        if(mLastError == null){
           // Toast.makeText(ctx, R.string.sent_success, Toast.LENGTH_SHORT).show();
            DataBaseDao dao = new DataBaseDao(ctx);
            dao.updateGmailMessage(1, unique);
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
