package uber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uber.sdk.android.core.auth.AccessTokenManager;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.core.auth.LoginCallback;
import com.uber.sdk.android.core.auth.LoginManager;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.AccessToken;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.error.ErrorParser;
import com.uber.sdk.rides.client.model.UserProfile;
import com.uber.sdk.rides.client.services.RidesService;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shareapp.vsshv.com.shareapp.R;

import static com.uber.sdk.rides.client.utils.Preconditions.checkNotNull;
import static com.uber.sdk.rides.client.utils.Preconditions.checkState;

/**
 * Created by PC414506 on 29/08/16.
 */

public class UberActivity extends AppCompatActivity implements RideRequestButtonCallback {
    //Please update CLIENT_ID and REDIRECT_URI below with your app's values.

    public static String CLIENT_ID = null;
    public static String SERVER_TOKEN = null;
    public static String REDIRECT_URI = null;

    private static final String LOG_TAG = "UberActivity";

    private static final int LOGIN_BUTTON_CUSTOM_REQUEST_CODE = 1112;
    private static final int CUSTOM_BUTTON_REQUEST_CODE = 1113;

    private Button customButton;
    private AccessTokenManager accessTokenManager;
    private LoginManager loginManager;
    private SessionConfiguration configuration;

    private RelativeLayout mLoginLayout = null;
    private LinearLayout mride_layout = null;
    private TextView user_name = null;
    private Button uber_search_button = null;
    private TextInputLayout uber_source = null;
    private TextInputLayout uber_dest = null;
    private RideRequestButton mRideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uber_layout);

        CLIENT_ID = getResources().getString(R.string.uber_client_id);
        REDIRECT_URI = getResources().getString(R.string.uber_redirect_uri);
        SERVER_TOKEN = getResources().getString(R.string.uber_server_token);

        configuration = new SessionConfiguration.Builder()
                .setClientId(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS))
                .build();

        validateConfiguration(configuration);

        accessTokenManager = new AccessTokenManager(this);

        //Use a custom button with an onClickListener to call the LoginManager directly
        loginManager = new LoginManager(accessTokenManager,
                new SampleLoginCallback(),
                configuration,
                CUSTOM_BUTTON_REQUEST_CODE);

        customButton = (Button) findViewById(R.id.custom_uber_button);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.login(UberActivity.this);
            }
        });
        mLoginLayout = (RelativeLayout) findViewById(R.id.login_layout);
        mride_layout = (LinearLayout) findViewById(R.id.ride_layout);
        user_name = (TextView) findViewById(R.id.user_name);
        uber_search_button = (Button) findViewById(R.id.uber_search_button);
        uber_source = (TextInputLayout) findViewById(R.id.uber_source);
        uber_dest = (TextInputLayout) findViewById(R.id.uber_dest);

        uber_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RideParameters rideParams = new RideParameters.Builder()
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco") // Price estimate will only be provided if this is provided
                .setProductId("") // Optional. If not provided, the cheapest product will be used
                .build();
        configuration = new SessionConfiguration.Builder()
                .setClientId(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .setServerToken(SERVER_TOKEN)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS))
                .build();

        ServerTokenSession session = new ServerTokenSession(configuration);
        mRideButton = (RideRequestButton) findViewById(R.id.uber_request_button);
       /*
        mRideButton.setRideParameters(rideParams);
        mRideButton.setSession(session);
        mRideButton.loadRideInformation();*/
        RideRequestActivityBehavior rideRequestActivityBehavior = new RideRequestActivityBehavior(this,
                1234, configuration);
        mRideButton.setRequestBehavior(rideRequestActivityBehavior);
        mRideButton.setRideParameters(rideParams);
        mRideButton.setSession(session);
        mRideButton.loadRideInformation();
    }

    @Override
    public void onRideInformationLoaded() {
        Toast.makeText(this, "Estimates have been refreshed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(ApiError apiError) {
        Toast.makeText(this, apiError.getClientErrors().get(0).getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e("SampleActivity", "Error obtaining Metadata", throwable);
        Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginManager.isAuthenticated()) {
            loadProfileInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, String.format("onActivityResult requestCode:[%s] resultCode [%s]",
                requestCode, resultCode));

        //Allow each a chance to catch it.
        //whiteButton.onActivityResult(requestCode, resultCode, data);

       // blackButton.onActivityResult(requestCode, resultCode, data);

        loginManager.onActivityResult(this, requestCode, resultCode, data);
    }

    private class SampleLoginCallback implements LoginCallback {

        @Override
        public void onLoginCancel() {
            Toast.makeText(UberActivity.this, R.string.user_cancels_message, Toast.LENGTH_LONG).show();
            mLoginLayout.setVisibility(View.VISIBLE);
            mride_layout.setVisibility(View.GONE);
        }

        @Override
        public void onLoginError(@NonNull AuthenticationError error) {
            Toast.makeText(UberActivity.this,
                    getString(R.string.login_error_message, error.name()), Toast.LENGTH_LONG)
                    .show();
            mLoginLayout.setVisibility(View.VISIBLE);
            mride_layout.setVisibility(View.GONE);
        }

        @Override
        public void onLoginSuccess(@NonNull AccessToken accessToken) {

            loadProfileInfo();
        }

        @Override
        public void onAuthorizationCodeReceived(@NonNull String authorizationCode) {
            Toast.makeText(UberActivity.this, getString(R.string.authorization_code_message, authorizationCode),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void loadProfileInfo() {
        Session session = loginManager.getSession();
        RidesService service = UberRidesApi.with(session).build().createService();

        service.getUserProfile()
                .enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UberActivity.this, getString(R.string.greeting, response.body().getFirstName()), Toast.LENGTH_SHORT).show();
                            mLoginLayout.setVisibility(View.GONE);
                            mride_layout.setVisibility(View.VISIBLE);
                            user_name.setText(getString(R.string.greeting, response.body().getFirstName()));
                        } else {
                            ApiError error = ErrorParser.parseError(response);
                            Toast.makeText(UberActivity.this, error.getClientErrors().get(0).getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.m, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*accessTokenManager = new AccessTokenManager(this);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            accessTokenManager.removeAccessToken();
            Toast.makeText(this, "AccessToken cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_copy) {
            AccessToken accessToken = accessTokenManager.getAccessToken();

            String message = accessToken == null ? "No AccessToken stored" : "AccessToken copied to clipboard";
            if (accessToken != null) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("UberSampleAccessToken", accessToken.getToken());
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }*/

        return super.onOptionsItemSelected(item);
    }


    /**
     * Validates the local variables needed by the Uber SDK used in the sample project
     * @param configuration
     */
    private void validateConfiguration(SessionConfiguration configuration) {
        String nullError = "%s must not be null";
        String sampleError = "Please update your %s in the gradle.properties of the project before " +
                "using the Uber SDK Sample app. For a more secure storage location, " +
                "please investigate storing in your user home gradle.properties ";

        checkNotNull(configuration, String.format(nullError, "SessionConfiguration"));
        checkNotNull(configuration.getClientId(), String.format(nullError, "Client ID"));
        checkNotNull(configuration.getRedirectUri(), String.format(nullError, "Redirect URI"));
        checkState(!configuration.getClientId().equals("Client ID"),
                String.format(sampleError, CLIENT_ID));
        checkState(!configuration.getRedirectUri().equals("Redirect URI"),
                String.format(sampleError, REDIRECT_URI));
    }
}
