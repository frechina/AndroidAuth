package net.imbra.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LocalAuthActivity extends Activity implements LocalLoginManager.OnAuthListener {
    private static final String TAG = "LocalAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_auth);

        String username = "", password = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString(LocalLoginManager.KEY_USERNAME);
            password = bundle.getString(LocalLoginManager.KEY_PASSWORD);
        }

        LocalLoginManager mLocalLoginManager = new LocalLoginManager();
        mLocalLoginManager.setOnAuthListener(this);
        mLocalLoginManager.login(username, password);

    }

    @Override
    public void onLoginSuccess(UserProfile user) {
        Intent output = new Intent();
        output.putExtra("user_name", user.getName());
        output.putExtra("user_email", user.getEmail());
        output.putExtra("user_picture", user.getPicture());
        setResult(RESULT_OK, output);

        finish();
    }

    @Override
    public void onLoginError(int resultCode) {
        Log.e(TAG, "Login error: Wrong username or password");
        Intent output = new Intent();
        setResult(resultCode, output);
        finish();
    }
}
