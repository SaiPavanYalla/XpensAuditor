package com.xa.xpensauditor;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;


import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginActivityTest extends TestCase {


    @Test
    public void testLogin(){

        ActivityScenario activityScenario=ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.email)).check(matches(isDisplayed()));

        onView(withId(R.id.email)).perform(typeText("vineethdasi22@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.btn_login)).perform(click());

        SystemClock.sleep(10000);


        ViewInteraction textView = onView(allOf(withText("XpensAuditor"), withParent(allOf(withId(R.id.toolbar), withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))), isDisplayed()));
        textView.check(matches(withText("XpensAuditor")));


    }


}
