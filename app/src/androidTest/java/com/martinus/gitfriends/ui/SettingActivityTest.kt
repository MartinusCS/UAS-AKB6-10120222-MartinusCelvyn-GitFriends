package com.martinus.gitfriends.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.martinus.gitfriends.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(SettingActivity::class.java)
    }

    @Test
    fun changeMode() {
        Espresso.onView(ViewMatchers.withId(R.id.switch_theme)).perform(ViewActions.click())
    }
}