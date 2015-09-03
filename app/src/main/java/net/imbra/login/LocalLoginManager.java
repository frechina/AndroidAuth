package net.imbra.login;

import android.util.Log;

public class LocalLoginManager {
    private static final String TAG = "LocalLoginManager";

    // Class attributes
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final int RESULT_CODE_WRONG_USERNAME = 200;
    public static final int RESULT_CODE_WRONG_PASSWORD = 201;

    private OnAuthListener mOnAuthListener;

    public LocalLoginManager() {
    }

    public void setOnAuthListener(OnAuthListener onAuthListener){
        this.mOnAuthListener = onAuthListener;
    }

    public interface OnAuthListener{
        void onLoginSuccess(UserProfile user);
        void onLoginError(int resultCode);
    }

    public void login(String username, String password){
        Log.e(TAG, "login");
        // TODO: Local Auth Service call logic

        // if login error
        // mOnAuthListener.onLoginError("Login error")

        if (!username.equals("test") && !password.equals("test")){
            mOnAuthListener.onLoginError(RESULT_CODE_WRONG_USERNAME);
        }else {

            // if login success
            // TODO: return the authenticated UserProfile
            mOnAuthListener.onLoginSuccess(new UserProfile("example", "example@example.com",
                    "", AuthProvider.LOCAL));
        }
    }
}
