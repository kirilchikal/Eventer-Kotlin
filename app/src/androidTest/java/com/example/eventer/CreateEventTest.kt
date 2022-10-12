package com.example.eventer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class CreateEventTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUpBeforeTest() {      // to test MainActivity first should login
        onView(withId(R.id.email)).perform(replaceText("test1@test.kt"))
        onView(withId(R.id.password)).perform(replaceText("eventer"))
        onView(allOf(withId(R.id.login_btn))).perform(click())
        waitToAppear(R.id.ic_account)
        onView(allOf(withId(R.id.fab))).perform(click())
        onView(allOf(withId(R.id.fab))).perform(click())
    }

    @Test
    fun toolBarExistTest() {
        waitToAppear(R.id.toolbar_create_event)
        onView(withId(R.id.toolbar_create_event)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.place)).check(matches(isDisplayed()))
        onView(withId(R.id.date)).check(matches(isDisplayed()))
        onView(withId(R.id.addMembers)).check(matches(isDisplayed()))
        onView(withId(R.id.n_members_add)).check(matches(isDisplayed()))
        onView(withId(R.id.create)).check(matches(isDisplayed()))
    }

    @Test
    fun addMembersTest() {
        waitToAppear(R.id.addMembers)
        onView(allOf(withId(R.id.addMembers))).perform(click())
        waitToAppear(R.id.toolbar_add_members)
        onView(withId(R.id.rv_members_list)).check(matches(isDisplayed()))
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
