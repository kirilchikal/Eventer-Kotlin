package com.example.eventer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)


    @Test
    fun goToRegisterPageAndBackTest() {
        val goToRegisterViewBTN = onView(allOf(withId(R.id.signUpTextView), withText("Sign up"),
                isDisplayed()))
        goToRegisterViewBTN.perform(click())

        val goBackToLoginViewBTN = onView(allOf(withId(R.id.signInTextView), withText("Sign in"),
                isDisplayed()))
        goBackToLoginViewBTN.perform(click())
    }

    @Test
    fun failedLoginTest() {
        onView(allOf(withId(R.id.login_btn))).perform(click())
        onView(withId(R.id.bottomNavigationView)).check(doesNotExist())     // check whether MainActivity is not displayed
    }

    @Test
    fun failedLoginTest2() {
        onView(allOf(withId(R.id.email))).perform(replaceText("fake@test.kt"))
        onView(withId(R.id.password)).perform(replaceText(""))
        onView(allOf(withId(R.id.login_btn))).perform(click())
        onView(withId(R.id.bottomNavigationView)).check(doesNotExist())     // check whether MainActivity is not displayed
    }

    @Test
    fun failedLoginTest3() {
        onView(allOf(withId(R.id.email))).perform(replaceText("fake@test.kt"))
        onView(withId(R.id.password)).perform(replaceText("pass"))
        onView(allOf(withId(R.id.login_btn))).perform(click())
        onView(withId(R.id.bottomNavigationView)).check(doesNotExist())     // check whether MainActivity is not displayed
    }

    @Test
    fun successLoginTest() {
        onView(allOf(withId(R.id.email))).perform(replaceText("test1@test.kt"))
        onView(withId(R.id.password)).perform(replaceText("eventer"))
        onView(allOf(withId(R.id.login_btn))).perform(click())
        onView(allOf(withId(R.id.fragment_chats), isDisplayed()))       // check if MainActivity is displayed
    }


}
