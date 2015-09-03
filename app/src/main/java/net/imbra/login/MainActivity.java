package net.imbra.login;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;


public class MainActivity extends SherlockActivity implements OnClickListener{
    private static final String TAG = "MainActivity";

    // Class attributes
    private SessionManager session;
    private TextView txtName;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.tvName);
        txtEmail = (TextView) findViewById(R.id.tvEmail);
        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        if (!session.checkIsLoggedIn()){
            startLoginActivity();
            finish();
        }else {
            UserProfile user = session.getUserProfile();
            showUserDetails(user);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO handle clicking the app icon/logo
                return false;
            case R.id.menu_refresh:
                return true;
            case R.id.menu_both:
                return true;
            case R.id.menu_text:
                return true;
            case R.id.menu_logo:
                return true;
            case R.id.menu_up:
                return true;
            case R.id.menu_nav_tabs:
                return true;
            case R.id.menu_nav_label:
                return true;
            case R.id.menu_nav_drop_down:
                return true;
            case R.id.menu_bak_none:
                item.setChecked(true);
                getSupportActionBar().setBackgroundDrawable(null);
                return true;
            case R.id.menu_bak_gradient:
                item.setChecked(true);
                // REMOVED: getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ad_action_bar_gradient_bak));
                getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ad_action_bar_gradient_bak));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showUserDetails(UserProfile user){
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        switch (user.getAuthProvider()){
            case LOCAL:
                break;
            case GOOGLE:
                break;
            case FACEBOOK:
                break;
            default:
                break;
        }
    }

    public void startLoginActivity(){
        Log.e(TAG, "startLoginActivity");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                logout();
                break;
            default:
                break;
        }
    }

    public void logout(){
        Log.e(TAG, "logout");
        AuthProvider authProvider = session.getAuthProvider();
        if (authProvider != null) {
            switch (authProvider) {
                case LOCAL:
                    startLoginActivity();
                    break;
                case FACEBOOK:
                    Intent facebookIntent = new Intent(this, FacebookAuthActivity.class);
                    facebookIntent.putExtra(AuthMode.KEY_MODE, AuthMode.REVOKE);
                    startActivity(facebookIntent);
                    break;
                case GOOGLE:
                    Intent googleIntent = new Intent(this, GoogleAuthActivity.class);
                    googleIntent.putExtra(AuthMode.KEY_MODE, AuthMode.REVOKE);
                    startActivity(googleIntent);
                    break;
            }
        }else{
            startLoginActivity();
        }
        session.logout();
        finish();
    }
}
