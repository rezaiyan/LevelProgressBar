package ir.alirezaiyan.levelprogressbar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class DemoScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun demoScreen_progressBarIsDisplayed() {
        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun demoScreen_controlsAreVisible() {
        composeTestRule
            .onNodeWithText("Is Enable")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Is Step Bar")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Level: 6")
            .assertIsDisplayed()
    }

    @Test
    fun demoScreen_strokeWidthLabelIsVisible() {
        composeTestRule
            .onNodeWithText("Stroke width: 10")
            .assertIsDisplayed()
    }

    @Test
    fun demoScreen_initialLevelIsSix() {
        composeTestRule
            .onNodeWithText("Level: 6")
            .assertIsDisplayed()
    }

    @Test
    fun demoScreen_toggleEnableCheckbox() {
        composeTestRule
            .onNodeWithText("Is Enable")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun demoScreen_toggleStepBarCheckbox() {
        composeTestRule
            .onNodeWithText("Is Step Bar")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }
}
