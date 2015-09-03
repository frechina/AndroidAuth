package net.imbra.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
    private static final String TAG = "LoginActivity";

    // Intent request codes
    private static final int REQUEST_CODE_LOCAL_AUTH = 100;
    private static final int REQUEST_CODE_GOOGLE_AUTH = 101;
    private static final int REQUEST_CODE_FACEBOOK_AUTH = 102;

    // Class attributes
    private SessionManager session;
    private EditText etUsrName;
    private EditText etPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        etUsrName = (EditText) findViewById(R.id.etUsrName);
        etPasswd = (EditText) findViewById(R.id.etPasswd);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(this);
        Button btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);
            btnGoogleLogin.setOnClickListener(this);
        Button btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
            btnFacebookLogin.setOnClickListener(this);
        TextView lnkPasswordForgotten = (TextView) findViewById(R.id.lnkPasswordForgotten);
            lnkPasswordForgotten.setOnClickListener(this);
        TextView lnkRegister = (TextView) findViewById(R.id.lnkRegister);
            lnkRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO: API CALL

        switch (v.getId()) {
            case R.id.btnLogin:

                if (!isEmpty(etUsrName) && !isEmpty(etPasswd)) {
                    String username = etUsrName.getText().toString();
                    String password = etPasswd.getText().toString();

                    startLocalAuthActivity(username, password);
                }else{
                    Toast.makeText(this, "You must enter username and password", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnGoogleLogin:
                startGoogleAuthActivity();
                break;

            case R.id.btnFacebookLogin:
                startFacebookAuthActivity();
                break;

            case R.id.lnkPasswordForgotten:
                // TODO: start PasswordForgotten activity
                break;

            case R.id.lnkRegister:
                // TODO: start Register activity
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CODE_GOOGLE_AUTH) ||
                (requestCode == REQUEST_CODE_FACEBOOK_AUTH) ||
                (requestCode == REQUEST_CODE_LOCAL_AUTH)){
            if (resultCode == RESULT_OK) {
                if (data != null){
                    Log.e(TAG, "AUTH REQUEST > RESULT_OK");
                    UserProfile user = (UserProfile) data.getSerializableExtra(UserProfile.KEY_USER_PROFILE);
                    session.createLoginSession(user);

                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Log.e(TAG, "Something went wrong: data is NULL");
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.e(TAG, "Account picker dialog closed without selecting an account");
            }else if (resultCode == LocalLoginManager.RESULT_CODE_WRONG_USERNAME){
                Toast.makeText(this, "The provided username is unknown", Toast.LENGTH_SHORT).show();
            }else if (resultCode == LocalLoginManager.RESULT_CODE_WRONG_PASSWORD){
                Toast.makeText(this, "The provided password is not valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocalAuthActivity(String username, String password){
        Intent intent = new Intent(this, LocalAuthActivity.class);
            intent.putExtra(LocalLoginManager.KEY_USERNAME, username);
            intent.putExtra(LocalLoginManager.KEY_PASSWORD, password);
        startActivityForResult(intent, REQUEST_CODE_LOCAL_AUTH);
    }

    private void startGoogleAuthActivity(){
        Intent intent = new Intent(this, GoogleAuthActivity.class);
            intent.putExtra(AuthMode.KEY_MODE, AuthMode.LOGIN);
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_AUTH);
    }

    private void startFacebookAuthActivity(){
        Intent intent = new Intent(this, FacebookAuthActivity.class);
            intent.putExtra(AuthMode.KEY_MODE, AuthMode.LOGIN);
        startActivityForResult(intent, REQUEST_CODE_FACEBOOK_AUTH);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
