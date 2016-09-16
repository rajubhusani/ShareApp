package uber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivity;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestViewError;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.Arrays;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.datasets.UberDataSet;

/**
 * Created by PC414506 on 12/09/16.
 */

public class UberRideRequest extends AppCompatActivity{

    private static final int WIDGET_REQUEST_CODE = 1234;
    private static String CLIENT_ID = null;
    private static String SERVER_TOKEN = null;
    private static String REDIRECT_URI = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_request);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Ride Request");
        }

        Bundle extras = getIntent().getExtras();

        CLIENT_ID = getResources().getString(R.string.uber_client_id);
        REDIRECT_URI = getResources().getString(R.string.uber_redirect_uri);
        SERVER_TOKEN = getResources().getString(R.string.uber_server_token);

        UberDataSet set = (UberDataSet)extras.getSerializable("uberData");

        SessionConfiguration configuration = new SessionConfiguration.Builder()
                .setClientId(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS))
                .build();

        RideParameters rideParametersCheapestProduct = new RideParameters.Builder()
                .setPickupLocation(set.getSourceLat(), set.getSourceLng(),
                        set.getPickUpNick(), set.getSourceAddress())
                .setDropoffLocation(set.getDestLat(), set.getDestLng(),
                        set.getDropOffNick(), set.getDestinationAddress())
                .build();

        ServerTokenSession session = new ServerTokenSession(configuration);
        RideRequestButton uberButtonWhite = (RideRequestButton) findViewById(R.id.uber_button_white);
        RideRequestActivityBehavior rideRequestActivityBehavior = new RideRequestActivityBehavior(this,
                WIDGET_REQUEST_CODE, configuration);
        uberButtonWhite.setRequestBehavior(rideRequestActivityBehavior);
        uberButtonWhite.setRideParameters(rideParametersCheapestProduct);
        uberButtonWhite.setSession(session);
        uberButtonWhite.loadRideInformation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WIDGET_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED && data != null) {
            if (data.getSerializableExtra(RideRequestActivity.AUTHENTICATION_ERROR) != null) {
                AuthenticationError error = (AuthenticationError) data.getSerializableExtra(RideRequestActivity
                        .AUTHENTICATION_ERROR);
                Toast.makeText(UberRideRequest.this, "Auth error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d("=======", "Error occurred during authentication: " + error.toString
                        ().toLowerCase());
            } else if (data.getSerializableExtra(RideRequestActivity.RIDE_REQUEST_ERROR) != null) {
                RideRequestViewError error = (RideRequestViewError) data.getSerializableExtra(RideRequestActivity
                        .RIDE_REQUEST_ERROR);
                Toast.makeText(UberRideRequest.this, "RideRequest error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d("=======", "Error occurred in the Ride Request Widget: " + error.toString().toLowerCase());
            }
        }
    }
}
