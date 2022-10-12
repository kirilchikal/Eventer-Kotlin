package com.example.eventer


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class Testst {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUpBeforeTest() {      // to test MainActivity first should login
        onView(withId(R.id.email)).perform(replaceText("test1@test.kt"))
        onView(withId(R.id.password)).perform(replaceText("eventer"))
        onView(allOf(withId(R.id.login_btn))).perform(click())
    }

    @Test
    fun switchBetweenFragmentsTest() {
        waitToAppear(R.id.ic_account)
        onView(withId(R.id.ic_account)).perform(click())
        waitToAppear(R.id.ic_chats)
        onView(withId(R.id.ic_chats)).perform(click())
        waitToAppear(R.id.fragment_container)
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))     //check if account fragment switched
    }


    private fun waitToAppear(id: Int) {
        var found = false
        while (!found) {
            try {
                onView(withId(id))
                    .check(matches(isDisplayed()))
                found = true
            }
            catch (_: Exception) {
                // Execution goes here if the view isn't found
            }
        }
    }

}
