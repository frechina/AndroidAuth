package net.imbra.login;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


public class GoogleAuthActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = "GoogleAuthActivity";

    // Intent request codes
    public static final int REQUEST_CODE_GOOGLE_LOGIN = 1000;

    // Class attributes
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_auth);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GOOGLE_LOGIN) {

            if (resultCode != RESULT_OK) {
                finish();
                return;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.e(TAG, "onConnectionSuspended");
        finish();
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.e(TAG, "onConnected: User is connected");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AuthMode authMode = (AuthMode) bundle.getSerializable(AuthMode.KEY_MODE);
            switch (authMode){
                case LOGIN:
                    googleAccess();
                    break;
                case REVOKE:
                    revokeGoogleAccess();
                    break;
            }

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            resolveSignInError(result);

        }
    }

    private void resolveSignInError(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, REQUEST_CODE_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void googleAccess(){
        String user_email = Plus.AccountApi.getAccountName(mGoogleApiClient);

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        String user_name = "", user_picture = "";
        if (person != null) {
            user_name = person.getDisplayName();
            if (person.hasImage()) {
                user_picture = person.getImage().getUrl();
            }
        }

        Log.e(TAG, "Google login complete!");

        Intent output = new Intent();
        output.putExtra(UserProfile.KEY_USER_PROFILE,
                new UserProfile(user_name, user_email,user_picture, AuthProvider.GOOGLE));
        setResult(RESULT_OK, output);

        finish();
    }

    private void revokeGoogleAccess() {
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            Intent i = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    });
        }
    }

}
