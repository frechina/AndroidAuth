package net.imbra.login;

import java.io.Serializable;

public class UserProfile implements Serializable {

    // Class attributes
    private String mName;
    private String mEmail;
    private String mPicture;
    private AuthProvider mAuthProvider;

    public static final String KEY_USER_PROFILE = "userProfile";

    public UserProfile(String name, String email, String picture, AuthProvider authProvider) {
        this.mName = name;
        this.mEmail = email;
        this.mPicture = picture;
        this.mAuthProvider = authProvider;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPicture() {
        return mPicture;
    }

    public AuthProvider getAuthProvider() {
        return mAuthProvider;
    }
}
