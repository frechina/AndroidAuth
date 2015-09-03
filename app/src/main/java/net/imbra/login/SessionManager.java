package net.imbra.login;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

public class SessionManager {
    private static final String TAG = "SessionManager";

    // Class attributes
    private final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SecurePreferences mPref;
    private SecurePreferences.Editor mEditor;


    public SessionManager(Context context){
        mPref = new SecurePreferences(context,null, "secure_prefs");
        mEditor = mPref.edit();
    }

    public void createLoginSession(UserProfile user){
        mEditor.putBoolean(KEY_IS_LOGGED_IN, true);

        String userProfile = new Gson().toJson(user);
        mEditor.putString(UserProfile.KEY_USER_PROFILE, userProfile);

        mEditor.commit();
        Log.e(TAG, "createLoginSession");
    }

    public boolean checkIsLoggedIn(){
       return mPref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public UserProfile getUserProfile(){
        return new Gson().fromJson(mPref.getString(UserProfile.KEY_USER_PROFILE, ""),
                                    UserProfile.class);
    }

    public AuthProvider getAuthProvider(){
        return getUserProfile().getAuthProvider();
    }

    public void logout(){
        mEditor.clear();
        mEditor.commit();
        Log.e(TAG, "logout");
    }
}