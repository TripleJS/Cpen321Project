package com.cpen321.ubconnect.ui.main;


import android.os.Environment;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cpen321.ubconnect.ui.main.TestUtils.withRecyclerView;
import static junit.framework.TestCase.assertNotNull;
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

        assertNotNull(onView(withId(R.id.suggestedRecyclerView)));

        takeScreenshots("afterswipeleft");
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

        assertNotNull(onView(withId(R.id.QuestioToAnswer)));

        takeScreenshots("afterswiperight");
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

    private int getRVcount(){
        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.suggestedRecyclerView);
        return recyclerView.getAdapter().getItemCount();
    }


}
