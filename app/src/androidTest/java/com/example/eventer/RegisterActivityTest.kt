package com.example.eventer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RegisterActivity::class.java)


    @Test
    fun goToRegisterPageAndBackTest() {
        val goToRegisterViewBTN = onView(allOf(withId(R.id.signInTextView), isDisplayed()))
        goToRegisterViewBTN.perform(click())
        val goBackToLoginViewBTN = onView(allOf(withId(R.id.signUpTextView), isDisplayed()))
        goBackToLoginViewBTN.perform(click())
    }

    @Test
    fun elementsExistTest() {
        onView(withId(R.id.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.email_et)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.password_et)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.retype_password)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun failedRegisterTest() {
        onView(allOf(withId(R.id.full_name))).perform(replaceText(""))
        onView(withId(R.id.email_et)).perform(replaceText("test@test.t"))
        onView(withId(R.id.password_et)).perform(replaceText("passwordTest"))
        onView(withId(R.id.retype_password)).perform(replaceText("passwordTest"))
        onView(allOf(withId(R.id.signUp))).perform(click())
        onView(withId(R.id.register_activity)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun failedRegisterTest2() {
        onView(allOf(withId(R.id.full_name))).perform(replaceText("Test Name"))
        onView(withId(R.id.email_et)).perform(replaceText("test@test.t"))
        onView(withId(R.id.password_et)).perform(replaceText("passwordTest"))
        onView(withId(R.id.retype_password)).perform(replaceText("password"))
        onView(allOf(withId(R.id.signUp))).perform(click())
        onView(withId(R.id.register_activity)).check(ViewAssertions.matches(isDisplayed()))
    }

}
