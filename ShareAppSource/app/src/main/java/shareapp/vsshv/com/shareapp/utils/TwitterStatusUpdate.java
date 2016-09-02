package shareapp.vsshv.com.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import shareapp.vsshv.com.shareapp.R;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by PC414506 on 29/08/16.
 */

public class TwitterStatusUpdate extends AsyncTask<String, String, Void> {

    private static SharedPreferences mSharedPreferences;
    private static final String PREF_NAME = "sample_twitter_pref";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    Context ctx;
    private String consumerKey = null;
    private String consumerSecret = null;
    private String callbackUrl = null;
    private String oAuthVerifier = null;

    public TwitterStatusUpdate(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mSharedPreferences = ctx.getSharedPreferences(PREF_NAME, 0);
        initTwitterConfigs();
    }

    protected Void doInBackground(String... args) {

        String status = args[0];
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKey);
            builder.setOAuthConsumerSecret(consumerSecret);

            // Access Token
            String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            // Access Token Secret
            String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            // Update status
            StatusUpdate statusUpdate = new StatusUpdate(status);
            //InputStream is = getResources().openRawResource(R.mipmap.ic_launcher);
            // statusUpdate.setMedia("test.jpg", is);

            twitter4j.Status response = twitter.updateStatus(statusUpdate);

            Log.d("Status", response.getText());

        } catch (TwitterException e) {
            Log.d("Failed to post!", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
        //pDialog.dismiss();
        Log.d("onPostExecute","Posted to Twitter!");
        //Toast.makeText(TwitterOAuthAct.this, "Posted to Twitter!", Toast.LENGTH_SHORT).show();
    }

    private void initTwitterConfigs() {
        consumerKey = ctx.getString(R.string.twitter_consumer_key);
        consumerSecret = ctx.getString(R.string.twitter_consumer_secret);
        callbackUrl = ctx.getString(R.string.twitter_callback);
        oAuthVerifier = ctx.getString(R.string.twitter_oauth_verifier);
    }
}