package shareapp.vsshv.com.shareapp;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by PC414506 on 29/08/16.
 */

public class MyApplication extends Application {

    private static final String TWITTER_KEY = "ipQtUJZfR7wcWNUeKuIFZOKqE";
    private static final String TWITTER_SECRET = "fay9y4Jw9dubDi7fagRa9A8Sx00MwKNENfTf563YbwxxZqoLff";

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
    }
}
