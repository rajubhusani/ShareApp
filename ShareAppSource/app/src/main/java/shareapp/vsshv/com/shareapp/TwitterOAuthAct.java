package shareapp.vsshv.com.shareapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import shareapp.vsshv.com.shareapp.fragments.DatePickerFragment;
import shareapp.vsshv.com.shareapp.utils.Utility;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by PC414506 on 29/08/16.
 */

public class TwitterOAuthAct extends AppCompatActivity implements View.OnClickListener{
  /* Shared preference keys */
  private static final String PREF_NAME = "sample_twitter_pref";
  private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
  private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
  private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
  private static final String PREF_USER_NAME = "twitter_user_name";
  private static final String PREF_USER_PROFILE = "twitter_user_profile";

  /* Any number for uniquely distinguish your request */
  public static final int WEBVIEW_REQUEST_CODE = 100;

  private ProgressDialog pDialog;

  private static Twitter twitter;
  private static RequestToken requestToken;

  private static SharedPreferences mSharedPreferences;

  private EditText mShareEditText;
  private TextView userName;
  private View loginLayout;
  private View shareLayout;
  private ImageView mTwitterImageView;

  private String consumerKey = null;
  private String consumerSecret = null;
  private String callbackUrl = null;
  private String oAuthVerifier = null;

  @SuppressLint("NewApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

		/* initializing twitter parameters from string.xml */
    initTwitterConfigs();

		/* Enabling strict mode */
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

		/* Setting activity layout file */
    setContentView(R.layout.t_activity);

    loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
    shareLayout = (LinearLayout) findViewById(R.id.share_layout);
    mShareEditText = (EditText) findViewById(R.id.share_text);
    userName = (TextView) findViewById(R.id.user_name);
    mTwitterImageView = (ImageView) findViewById(R.id.TwitterImageView);

		/* register button click listeners */
    findViewById(R.id.btn_login).setOnClickListener(this);
    findViewById(R.id.btn_share).setOnClickListener(this);

		/* Check if required twitter keys are set */
    if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
      Toast.makeText(this, "Twitter key and secret not configured",
              Toast.LENGTH_SHORT).show();
      return;
    }

		/* Initialize application preferences */
    mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

    boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

		/*  if already logged in, then hide login layout and show share layout */
    if (isLoggedIn) {
      loginLayout.setVisibility(View.GONE);
      shareLayout.setVisibility(View.VISIBLE);

      String username = mSharedPreferences.getString(PREF_USER_NAME, "");
      userName.setText(getString(R.string.hello,username));
      Utility.getInstance(TwitterOAuthAct.this).loadProfileImageUsingPicasa(TwitterOAuthAct.this,
              mSharedPreferences.getString(PREF_USER_PROFILE, ""), mTwitterImageView);

    } else {
      loginLayout.setVisibility(View.VISIBLE);
      shareLayout.setVisibility(View.GONE);

      Uri uri = getIntent().getData();

      if (uri != null && uri.toString().startsWith(callbackUrl)) {

        String verifier = uri.getQueryParameter(oAuthVerifier);

        try {

					/* Getting oAuth authentication token */
          AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					/* Getting user id form access token */
          long userID = accessToken.getUserId();
          final User user = twitter.showUser(userID);
          final String username = user.getName();

					/* save updated token */
          saveTwitterInfo(accessToken);

          loginLayout.setVisibility(View.GONE);
          shareLayout.setVisibility(View.VISIBLE);
          userName.setText(getString(R.string.hello, username));
          Utility.getInstance(TwitterOAuthAct.this).loadProfileImageUsingPicasa(TwitterOAuthAct.this,
                  user.getProfileImageURL(), mTwitterImageView);

        } catch (Exception e) {
          Log.e("Failed to login Twitter", e.getMessage());
        }
      }

    }
  }


  /**
   * Saving user information, after user is authenticated for the first time.
   * You don't need to show user to login, until user has a valid access toen
   */
  private void saveTwitterInfo(AccessToken accessToken) {

    long userID = accessToken.getUserId();

    User user;
    try {
      user = twitter.showUser(userID);

      String username = user.getName();
      Utility.getInstance(TwitterOAuthAct.this).loadProfileImageUsingPicasa(TwitterOAuthAct.this,
              user.getProfileImageURL(), mTwitterImageView);

			/* Storing oAuth tokens to shared preferences */
      SharedPreferences.Editor e = mSharedPreferences.edit();
      e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
      e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
      e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
      e.putString(PREF_USER_NAME, username);
      e.putString(PREF_USER_PROFILE, user.getProfileImageURL());
      e.apply();

    } catch (TwitterException e1) {
      e1.printStackTrace();
    }
  }

  /* Reading twitter essential configuration parameters from strings.xml */
  private void initTwitterConfigs() {
    consumerKey = getString(R.string.twitter_consumer_key);
    consumerSecret = getString(R.string.twitter_consumer_secret);
    callbackUrl = getString(R.string.twitter_callback);
    oAuthVerifier = getString(R.string.twitter_oauth_verifier);
  }


  private void loginToTwitter() {
    boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

    if (!isLoggedIn) {
      final ConfigurationBuilder builder = new ConfigurationBuilder();
      builder.setOAuthConsumerKey(consumerKey);
      builder.setOAuthConsumerSecret(consumerSecret);

      final Configuration configuration = builder.build();
      final TwitterFactory factory = new TwitterFactory(configuration);
      twitter = factory.getInstance();

      try {
        requestToken = twitter.getOAuthRequestToken(callbackUrl);

        /**
         *  Loading twitter login page on webview for authorization
         *  Once authorized, results are received at onActivityResult
         *  */
        final Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

      } catch (TwitterException e) {
        e.printStackTrace();
      }
    } else {

      loginLayout.setVisibility(View.GONE);
      shareLayout.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      String verifier = data.getExtras().getString(oAuthVerifier);
      try {
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

        long userID = accessToken.getUserId();
        final User user = twitter.showUser(userID);
        String username = user.getName();

        saveTwitterInfo(accessToken);

        loginLayout.setVisibility(View.GONE);
        shareLayout.setVisibility(View.VISIBLE);
        userName.setText(getString(R.string.hello, username));

      } catch (Exception e) {
        Log.e("Twitter Login Failed", e.getMessage());
      }
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_login:
        loginToTwitter();
        break;
      case R.id.btn_share:
                /*final String status = mShareEditText.getText().toString();

                if (status.trim().length() > 0) {
                    new UpdateTwitterStatus().execute(status);
                } else {
                    Toast.makeText(this, "Message is empty!!", Toast.LENGTH_SHORT).show();
                }*/
        DateTimePicker();
        break;
    }
  }

  private void DateTimePicker(){
    showDatePickerDialog();
  }

  private void showDatePickerDialog() {
    DialogFragment newFragment = new DatePickerFragment();
    Bundle args= new Bundle();
    args.putInt("app",0);
    args.putString("message",mShareEditText.getText().toString());
    newFragment.setArguments(args);
    newFragment.show(getFragmentManager(), "datePicker");
  }
}
