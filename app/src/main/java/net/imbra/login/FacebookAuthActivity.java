package net.imbra.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FacebookAuthActivity extends Activity implements FacebookCallback<LoginResult> {
    private static final String TAG = "FacebookAuthActivity";

    // Class attributes
    private final List<String> FACEBOOK_PERMISSIONS = Arrays.asList("public_profile", "email");

    private CallbackManager mFacebookCallbackManager;
    private LoginManager mFacebookLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_auth);

        FacebookSdk.sdkInitialize(getApplicationContext());

        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginManager = LoginManager.getInstance();
            mFacebookLoginManager.registerCallback(mFacebookCallbackManager, this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AuthMode authMode = (AuthMode) bundle.getSerializable(AuthMode.KEY_MODE);
            switch (authMode){
                case LOGIN:
                    mFacebookLoginManager.logInWithReadPermissions(this, FACEBOOK_PERMISSIONS);
                    break;
                case REVOKE:
                    revokeFacebookAccess();
                    break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.e(TAG, "FacebookCallback > onSuccess");

        GraphRequest meRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                Log.e(TAG, "FacebookCallback > GraphRequest > onCompleted");

                try {
                    String user_name = jsonObject.getString("name");
                    String user_email = jsonObject.getString("email");
                    String user_picture = jsonObject.getJSONObject("cover").getString("source");

                    Intent output = new Intent();
                    output.putExtra(UserProfile.KEY_USER_PROFILE,
                            new UserProfile(user_name, user_email,user_picture,
                                                AuthProvider.FACEBOOK));
                    setResult(RESULT_OK, output);

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

                finish();
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "name, email, cover");

        meRequest.setParameters(params);
        meRequest.executeAsync();
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "FacebookCallback > onCancel");
        finish();
    }

    @Override
    public void onError(FacebookException exception) {
        Log.e(TAG, "FacebookCallback > onError: " + exception.getMessage());
        finish();
    }

    private void revokeFacebookAccess(){
        Log.e(TAG, "revokeFacebookAccess");
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token != null) {
            String graphPath = token.getUserId() + "/permissions";

            final GraphRequest requestLogout = new GraphRequest(token, graphPath, null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    mFacebookLoginManager.logOut();

                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            requestLogout.executeAsync();
        }else{
            Log.e(TAG, "revokeFacebookAccess > Token is NULL");
        }
    }
}
