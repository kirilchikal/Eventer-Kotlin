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
class EditProfileTest {
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
        waitToAppear(R.id.nav_edit_profile)
        onView(ViewMatchers.withId(R.id.nav_edit_profile)).perform(ViewActions.click())

    }

    @Test
    fun elementsExistTest() {
        waitToAppear(R.id.toolbar_edit_profile)
        onView(ViewMatchers.withId(R.id.toolbar_edit_profile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.old_image)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.upload_image)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.btn_save_changes)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.edit_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.edit_fullname)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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