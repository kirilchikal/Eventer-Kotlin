package com.example.eventer

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
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
class AccountFragmentTest {
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
            .perform(click())
        waitToAppear(R.id.ic_account)
        onView(ViewMatchers.withId(R.id.ic_account)).perform(click())
        onView(ViewMatchers.withId(R.id.ic_account)).perform(click())
    }

    @Test
    fun elementsExistTest() {
        waitToAppear(R.id.profile_image)
        onView(ViewMatchers.withId(R.id.profile_image))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tv_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tv_fullname)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.profile_menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun logOutTest() {
        waitToAppear(R.id.nav_logout)
        onView(ViewMatchers.withId(R.id.nav_logout)).perform(click())
        waitToAppear(R.id.login_btn)
        onView(ViewMatchers.withId(R.id.login_btn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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