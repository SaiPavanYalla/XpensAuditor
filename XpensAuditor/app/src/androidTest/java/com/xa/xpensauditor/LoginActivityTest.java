package com.xa.xpensauditor;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends TestCase {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    public LoginActivity loginActivity;

    @Before
    public void setUp() throws Exception {
        loginActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLogin(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                EditText email = loginActivity.findViewById(R.id.email);
                email.setText("mithilareddy1999@gmail.com");
                EditText pass = loginActivity.findViewById(R.id.password);
                pass.setText("Cherith@2020");
                Button loginBtn = loginActivity.findViewById(R.id.btn_login);
                loginBtn.performClick();
                assertEquals(true,loginActivity.status);
            }
        });
    }



    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}