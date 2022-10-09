package com.xa.xpensauditor;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
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

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteTransactionTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @After
    public void signoutandclear()
    {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.signOut();
        }
    }

    @Test
    public void deleteTransactionTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("sahithi.ammana@gmail.com"), closeSoftKeyboard());
        SystemClock.sleep(1000);
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("sahithi"), closeSoftKeyboard());
        SystemClock.sleep(1000);
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());
        SystemClock.sleep(1000);
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());
        SystemClock.sleep(1000);
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.addTransAmt),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("222"), closeSoftKeyboard());
        SystemClock.sleep(1000);
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.addShopName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("qqwerty"), closeSoftKeyboard());
        SystemClock.sleep(1000);
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btAddTransaction), withText("Add Transaction"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                4),
                        isDisplayed()));
        materialButton2.perform(click());
        SystemClock.sleep(1000);
        pressBack();
        SystemClock.sleep(1000);
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));
        SystemClock.sleep(1000);
        ViewInteraction textView = onView(
                allOf(withId(android.R.id.title), withText("Select an action"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ListView.class))),
                        isDisplayed()));
        textView.check(matches(withText("Select an action")));
        SystemClock.sleep(1000);
        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());
        SystemClock.sleep(1000);
        ViewInteraction textView2 = onView(
                allOf(withText("ALL TRANSACTION"),
                        withParent(allOf(withContentDescription("ALL TRANSACTION"),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("ALL TRANSACTION")));
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
