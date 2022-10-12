package com.example.eventer

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ChangePasswordTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUpBeforeTest() {
        onView(ViewMatchers.withId(R.id.email))
            .perform(ViewActions.replaceText("test1@test.kt"))
        onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.replaceText("eventer"))
        onView(Matchers.allOf(ViewMatchers.withId(R.id.login_btn)))
            .perform(ViewActions.click())
        waitToAppear(R.id.ic_account)
        onView(ViewMatchers.withId(R.id.ic_account)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.ic_account)).perform(ViewActions.click())
        waitToAppear(R.id.nav_change_password)
        onView(ViewMatchers.withId(R.id.nav_change_password)).perform(ViewActions.click())
    }

    @Test
    fun elementsExistTest() {
        waitToAppear(R.id.toolbar_change_password)
        onView(ViewMatchers.withId(R.id.toolbar_change_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.ti_current_password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.et_current_password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.ti_confirm_password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun failedOperationTest() {
        waitToAppear(R.id.toolbar_change_password)
        onView(Matchers.allOf(ViewMatchers.withId(R.id.et_current_password))).perform(ViewActions.replaceText("fakePass"))
        onView(ViewMatchers.withId(R.id.et_new_password)).perform(ViewActions.replaceText(""))
        onView(Matchers.allOf(ViewMatchers.withId(R.id.btn_change_password))).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.bottomNavigationView)).check(ViewAssertions.doesNotExist())     // check whether MainActivity is not displayed
    }

    @Test
    fun failedOperationTest2() {
        waitToAppear(R.id.toolbar_change_password)
        onView(Matchers.allOf(ViewMatchers.withId(R.id.et_current_password))).perform(ViewActions.replaceText("fakePass"))
        onView(ViewMatchers.withId(R.id.et_new_password)).perform(ViewActions.replaceText("fakePass2"))
        onView(Matchers.allOf(ViewMatchers.withId(R.id.btn_change_password))).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.bottomNavigationView)).check(ViewAssertions.doesNotExist())     // check whether MainActivity is not displayed
    }

    private fun waitToAppear(id: Int) {
        var found = false
        while (!found) {
            try {
                Espresso.onView(ViewMatchers.withId(id))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                found = true
            }
            catch (_: Exception) {
                // Execution goes here if the view isn't found
            }
        }
    }

}