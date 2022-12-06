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

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.acl.Group;

@LargeTest
@RunWith(AndroidJUnit4.class)

public class CreateGroupActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @After
    public void signoutandclear() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.signOut();
        }
    }
    public static void opendrawer(){
        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));
        SystemClock.sleep(5000);
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
        SystemClock.sleep(5000);
    }

    public static void selectgroup(){
        ViewInteraction checkedTextView = onView(
                allOf(withId(com.google.android.material.R.id.design_menu_item_text), withText("GROUP")));

        checkedTextView.check(matches(isDisplayed()));
        SystemClock.sleep(5000);
        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_group)));
        navigationMenuItemView.perform(click());
        SystemClock.sleep(5000);
    }

    public static void signout(){
        SystemClock.sleep(5000);
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options")));
        overflowMenuButton.perform(click());
        SystemClock.sleep(5000);
        ViewInteraction materialTextView = onView(
                allOf(withId(androidx.recyclerview.R.id.title), withText("Account Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.constraintlayout.widget.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());
        SystemClock.sleep(5000);
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.sign_out), withText("Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                12),
                        isDisplayed()));
        materialButton3.perform(click());
        SystemClock.sleep(5000);
        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("SignOut"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());
        SystemClock.sleep(5000);
        ViewInteraction imageView3 = onView(
                allOf(withContentDescription("XpensAuditor"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));
    }

    public static void clickfabgl(){
        SystemClock.sleep(5000);
        ViewInteraction fabButton = onView(
                allOf(withId(R.id.fabgl)));
        fabButton.perform(click());
        SystemClock.sleep(5000);
    }

    public static void addgroup(){
        ViewInteraction groupNameEditText = onView(
                allOf(withId(R.id.groupName)));
        groupNameEditText.perform(replaceText("newgroup: " + ((int)Math.random()*10000)), closeSoftKeyboard());
        SystemClock.sleep(5000);

        ViewInteraction groupMemberEditText = onView(
                allOf(withId(R.id.emailIds)));
        groupMemberEditText.perform(replaceText("drkanaki@ncsu.edu atharvagole5@gmail.com"), closeSoftKeyboard());
        SystemClock.sleep(5000);

        ViewInteraction inviteMemberButton = onView(
                allOf(withId(R.id.btn_invite_member)));
        inviteMemberButton.perform(click());
    }

    public static void login(){
        SystemClock.sleep(5000);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.emailIds),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("drkanaki@ncsu.edu"), closeSoftKeyboard());

        SystemClock.sleep(5000);

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("abcd1234"), closeSoftKeyboard());
        SystemClock.sleep(5000);

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
    }

    @Test
    public void createGroupActivityTest() {
        login();
        opendrawer();
        selectgroup();
        clickfabgl();
        addgroup();

        pressBack();
        pressBack();
        pressBack();
        GroupListActivityTest.signout();
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

