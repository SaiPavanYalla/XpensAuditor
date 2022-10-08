package com.xa.xpensauditor;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactUsTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void contactUsTest() {
        SystemClock.sleep(2000);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("vineethdasi22@gmail.com"), closeSoftKeyboard());

        SystemClock.sleep(2000);

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("123456"), closeSoftKeyboard());

        SystemClock.sleep(2000);

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        SystemClock.sleep(5000);

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        SystemClock.sleep(2000);

        ViewInteraction materialTextView = onView(
                allOf(withId(androidx.recyclerview.R.id.title), withText("Contact Us"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.constraintlayout.widget.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        SystemClock.sleep(2000);

        ViewInteraction textView = onView(
                allOf(withId(R.id.tvNumber5), withText("mithilareddy1999@gmail.com"),
                        withParent(allOf(withId(R.id.RelativeView5),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("mithilareddy1999@gmail.com")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tvNumber7), withText("sahithi.ammana@gmail.com"),
                        withParent(allOf(withId(R.id.RelativeView7),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("sahithi.ammana@gmail.com")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.fab),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        SystemClock.sleep(2000);

        pressBack();

        SystemClock.sleep(2000);
        ViewInteraction overflowMenuButton2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton2.perform(click());

        SystemClock.sleep(2000);
        ViewInteraction materialTextView33 = onView(
                allOf(withId(androidx.recyclerview.R.id.title), withText("Account Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.constraintlayout.widget.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView33.perform(click());
        SystemClock.sleep(2000);

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.sign_out), withText("Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                12),
                        isDisplayed()));
        materialButton4.perform(click());
        SystemClock.sleep(2000);
        ViewInteraction textView22 = onView(
                allOf(withId(android.R.id.message), withText("Do you Really want to SignOut ?"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView22.check(matches(withText("Do you Really want to SignOut ?")));

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("SignOut"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());
        SystemClock.sleep(2000);
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
}
