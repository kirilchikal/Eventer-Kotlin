package com.example.eventer


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun addDeleteTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("test1@test.kt"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("eventer"), closeSoftKeyboard())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.ic_account),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val navigationMenuItemView = onView(
            allOf(
                withId(R.id.nav_find_friends),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.profile_menu),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.find_friends_btn),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_find_friends),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.event_btn), withText("ADD"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.event_btn), withText("ADD"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_find_friends),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
            allOf(
                withId(R.id.nav_friends),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.profile_menu),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.fcd_delete_btn), withText("DELETE"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.fcd_delete_btn), withText("DELETE"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
