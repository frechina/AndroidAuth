package net.imbra.login;

import android.support.test.espresso.intent.Intents;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;

import org.mockito.Mockito;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private SessionManager session;
    private String USER_NAME = "User name";
    private String USER_EMAIL = "User email";
    private String USER_PICTURE = "No picture";

    public MainActivityTest(){
        super(MainActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        session = new SessionManager(getActivity().getApplicationContext());
        UserProfile user = new UserProfile(USER_NAME,USER_EMAIL,USER_PICTURE,AuthProvider.LOCAL);
        session.createLoginSession(user);
    }

    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testUserDetailsAreShown() {
        onView(withId(R.id.tvName)).check(matches(withText(USER_NAME)));
        onView(withId(R.id.tvEmail)).check(matches(withText(USER_EMAIL)));
    }

    public void testLoginActivityStarts() {
        onView(withId(R.id.btnLogout)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        //assertEquals(session.checkIsLoggedIn(),false);
    }


}
