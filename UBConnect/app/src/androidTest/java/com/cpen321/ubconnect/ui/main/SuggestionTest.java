package com.cpen321.ubconnect.ui.main;


import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;

import java.io.File;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cpen321.ubconnect.ui.main.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SuggestionTest {

    private boolean isInHomePage = false;

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class);

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

//    @Rule
//    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

//    @Rule
//    public ActivityTestRule<HomeActivity> activityRule
//            = new ActivityTestRule<>(
//            HomeActivity.class,
//            true,     // initialTouchMode
//            false);   // launchActivity. False to customize the intent


    @Before
    public void goToHomePage(){
        if(!isInHomePage){
            onView(withId(R.id.login_button)).perform(click());
            isInHomePage = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    @Test
    public void suggestionSwipeLeft(){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        takeScreenshots("beforeswipeleft");

        onView(withId(R.id.suggestedRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(HomeActivity.class.getName()));

        onView(withRecyclerView(R.id.suggestedRecyclerView)
                .atPositionOnView(0, R.id.suggestionContent))
                .check(matches(not(withText(containsString("hello my name is john nice")))));

        takeScreenshots("afterswipeleft");
    }

    @Test
    public void suggestionNotRepeatingSameQuestion(){
        int index = 0;
        int LIMIT = 500;

        takeScreenshots("beforeswipeleft2");
        while (index < LIMIT){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.suggestedRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));

            onView(withRecyclerView(R.id.suggestedRecyclerView)
                    .atPositionOnView(0, R.id.suggestionContent))
                    .check(matches(not(withText(containsString("hello my name is john nice")))));

            index++;
        }

    }

    @Test
    public void suggestionSwipeRight(){

        takeScreenshots("beforeswiperight");

        onView(withId(R.id.suggestedRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeRight()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(OtherAnswersActivity.class.getName()));

        onView(withId(R.id.QuestioToAnswer)).check(matches((withText(containsString("hello my name is john nice")))));

        takeScreenshots("afterswiperight");
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

    private String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

//    @Rule
//    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);


    private int getRVcount(RecyclerView recyclerView){
        return recyclerView.getAdapter().getItemCount();
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
