package shareapp.vsshv.com.shareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

/**
 * Created by PC414506 on 29/08/16.
 */

public class TwitterActivity extends AppCompatActivity{

    TwitterLoginButton loginButton = null;
    Button mCompose = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twitter_act);

        mCompose = (Button)findViewById(R.id.tCompose);
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Toast.makeText(TwitterActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(TwitterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        mCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TweetComposer.Builder builder = new TweetComposer.Builder(TwitterActivity.this)
//                        .text("just setting up my Fabric.");
//                builder.show();
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(TwitterActivity.this)
                        .session(session)
                        .createIntent();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
