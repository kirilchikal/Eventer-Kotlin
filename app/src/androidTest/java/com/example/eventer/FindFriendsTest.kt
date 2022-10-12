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
class FindFriendsTest {

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
        waitToAppear(R.id.nav_friends)
        onView(ViewMatchers.withId(R.id.nav_find_friends)).perform(ViewActions.click())

    }

    @Test
    fun elementsExistTest() {
        waitToAppear(R.id.toolbar_find_friends)
        onView(ViewMatchers.withId(R.id.toolbar_find_friends))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.et_find_friend_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.find_friends_btn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.rv_users_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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