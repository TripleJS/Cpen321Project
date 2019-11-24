package com.cpen321.ubconnect.ui.main;

import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.cpen321.ubconnect.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;

import java.io.File;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FacebookAPITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, org.junit.runner.Description description) {
            super.failed(e, description);

            // Save to external storage (usually /sdcard/screenshots)
            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/screenshots/" + getTargetContext().getPackageName());
            if (!path.exists()) {
                path.mkdirs();
            }

            // Take advantage of UiAutomator screenshot method
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            String filename = description.getClassName() + "-" + description.getMethodName() + ".png";
            device.takeScreenshot(new File(path, filename));
        }
    };


    @Test
    public void facebookAPITest() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        takeScreenshots("start");

        ViewInteraction button = onView(withId(R.id.login_button));

        button.check(matches(isDisplayed()));

        button.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.homeLayout),
                        childAtPosition(
                                allOf(withId(R.id.drawer_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        takeScreenshots("opennav");
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        takeScreenshots("navclicked");
        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        takeScreenshots("accountpage");

        onView(withId(R.id.logout)).perform(click());



        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        takeScreenshots("endresult");

        assertNotNull(onView(withId(R.id.login_button)).check(matches(isDisplayed())));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public void takeScreenshots(String description){
        // Save to external storage (usually /sdcard/screenshots)
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/screenshots/" + getTargetContext().getPackageName());
        if (!path.exists()) {
            path.mkdirs();
        }

        // Take advantage of UiAutomator screenshot method
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        String filename = description + ".png";
        device.takeScreenshot(new File(path, filename));
    }

}
