package com.tongdao.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tongdao.sdk.tools.Log;
import com.tongdao.sdk.tools.TongDaoSavingTool;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by agonch on 10/25/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainApplicationTest {
    private static final String USER_INFO_DATA = "LQ_USER_INFO_DATA";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            clearSharedPrefs(InstrumentationRegistry.getTargetContext());
            super.beforeActivityLaunched();
        }
    };

    /**
     * Clears everything in the SharedPreferences
     */
    private void clearSharedPrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void identifyTongDaoTest() throws InterruptedException{
        onView(withId(R.id.identify_tv)).perform(click());
        Thread.sleep(5000);
        String cache = Log.readCache();
        assertThat("Call not successful",cache, containsString("\"action\":\"identify\""));
        assertThat("Call not successful",cache, containsString("!connection"));
        assertThat("Call not successful",cache, containsString("!location"));
        assertThat("Call not successful",cache, containsString("!device"));
        assertThat("Call not successful",cache, containsString("!fingerprint"));
        assertThat("Call not successful",cache, containsString("Track Event response , 204"));
    }

    @Test
    public void trackTongDaoTest() throws InterruptedException{
        Log.readCache();
        onView(withId(R.id.track_tv)).perform(click());
        Thread.sleep(5000);
        String cache = Log.readCache();
        assertThat("Call not successful",cache, containsString("\"action\":\"track\""));
        assertThat("Call not successful",cache, containsString("\"event\":\"trackTestingName\""));
        assertThat("Call not successful",cache, containsString("Track Event response , 204"));
    }
}
