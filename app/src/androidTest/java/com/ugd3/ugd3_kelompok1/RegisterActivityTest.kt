package com.ugd3.ugd3_kelompok1


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        pressBack()

        val materialButton = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.namaInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutNama),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("joel"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton2.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.emailInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("joel@gmail.com"), closeSoftKeyboard())


        val materialButton3 = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton3.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.passwordInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("12345678"), closeSoftKeyboard())


        val materialButton4 = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton4.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton5 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton5.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton6.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.nomorTeleponInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutNomorTelepon),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("08813254"), closeSoftKeyboard())


        val materialButton7 = onView(
            allOf(
                withId(R.id.btnDaftar), withText("Daftar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout4),
                        childAtPosition(
                            withId(R.id.activityRegister),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton7.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))
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

    fun waitFor(delay: Long): ViewAction?{
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for" + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

}
