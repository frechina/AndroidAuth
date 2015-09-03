package net.imbra.login.test;

import net.imbra.login.AuthProvider;
import net.imbra.login.UserProfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;


public class UserProfileTest {
    private UserProfile user;
    private String USER_NAME = "User name";
    private String USER_EMAIL = "User email";
    private String USER_PICTURE = "No picture";
    private AuthProvider USER_AUTH_PROVIDER = AuthProvider.LOCAL;

    @Before
    public void setUp() throws Exception {
        user = new UserProfile(USER_NAME, USER_EMAIL, USER_PICTURE, USER_AUTH_PROVIDER);
    }

    @Test
    public void testGetName() {
        String actual_name = user.getName();
        String expected_name = USER_NAME;

        assertThat(actual_name, is(equalTo(expected_name)));
    }

    @Test
    public void testGetEmail() {
        String actual_email = user.getEmail();
        String expected_email = USER_EMAIL;

        assertThat(actual_email, is(equalTo(expected_email)));
    }

    @Test
    public void testGetPicture() {
        String actual_picture = user.getPicture();
        String expected_picture = USER_PICTURE;

        assertThat(actual_picture, is(equalTo(expected_picture)));
    }

    @Test
    public void testGetAuthProvider() {
        AuthProvider actual_auth_provider = user.getAuthProvider();
        AuthProvider expected_auth_provider = USER_AUTH_PROVIDER;

        assertThat(actual_auth_provider, is(equalTo(expected_auth_provider)));
    }
}
