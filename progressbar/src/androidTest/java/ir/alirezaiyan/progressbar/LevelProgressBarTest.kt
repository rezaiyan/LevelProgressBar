package ir.alirezaiyan.progressbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Rule
import org.junit.Test

class LevelProgressBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // -----------------------------------------------------------------------
    // Display tests
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_defaultParams_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(level = 5, animated = false)
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_displaysCorrectLevel() {
        composeTestRule.setContent {
            LevelProgressBar(level = 7, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 7 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_levelZero_displaysZero() {
        composeTestRule.setContent {
            LevelProgressBar(level = 0, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 0 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_maxLevel_displaysMax() {
        composeTestRule.setContent {
            LevelProgressBar(level = 10, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 10 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_exceedsMax_clampsToMax() {
        composeTestRule.setContent {
            LevelProgressBar(level = 15, maxLevel = 10, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 10 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_negativeLevel_clampsToZero() {
        composeTestRule.setContent {
            LevelProgressBar(level = -3, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 0 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_customMaxLevel() {
        composeTestRule.setContent {
            LevelProgressBar(level = 3, maxLevel = 5, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 3 of 5")
            .assertIsDisplayed()
    }

    // -----------------------------------------------------------------------
    // State / mode tests
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_stepMode_hasStepState() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                mode = ProgressMode.Step,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "step")
            )
    }

    @Test
    fun levelProgressBar_continuousMode_hasContinuousState() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                mode = ProgressMode.Continuous,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "continuous")
            )
    }

    @Test
    fun levelProgressBar_disabled_hasDisabledState() {
        composeTestRule.setContent {
            LevelProgressBar(level = 5, enabled = false, animated = false)
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "disabled")
            )
    }

    // -----------------------------------------------------------------------
    // Colors API tests
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_customColors_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                colors = LevelProgressBarDefaults.colors(
                    progressColor = Color.Red,
                    trackColor = Color.LightGray,
                    backgroundColor = Color.DarkGray,
                    textColor = Color.Yellow,
                ),
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_customStrokeWidth_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                strokeWidth = 25.dp,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    // -----------------------------------------------------------------------
    // New parameter tests
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_customStrokeCap_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 6,
                strokeCap = StrokeCap.Butt,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_customArcGeometry_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 7,
                startAngle = 180f,
                sweepAngle = 180f,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_customTextStyle_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Light),
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_formatLevel_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 7,
                maxLevel = 10,
                formatLevel = { "$it/10" },
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_explicitSmallSize_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                modifier = Modifier.size(80.dp),
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    // -----------------------------------------------------------------------
    // Dynamic update tests
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_levelUpdate_updatesSemantics() {
        var level by mutableIntStateOf(3)

        composeTestRule.setContent {
            LevelProgressBar(level = level, animated = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Level 3 of 10")
            .assertIsDisplayed()

        level = 8

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Level 8 of 10")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_withAnimation_isDisplayed() {
        composeTestRule.setContent {
            LevelProgressBar(level = 6, animated = true)
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }

    @Test
    fun levelProgressBar_stepModeDisabled_hasDisabledState() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                mode = ProgressMode.Step,
                enabled = false,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "disabled")
            )
    }

    @Test
    fun levelProgressBar_allLevels_renderWithoutCrash() {
        composeTestRule.setContent {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                for (i in 0..10) {
                    LevelProgressBar(level = i, animated = false)
                }
            }
        }

        for (i in 0..10) {
            composeTestRule
                .onNodeWithContentDescription("Level $i of 10")
                .assertExists()
        }
    }

    // -----------------------------------------------------------------------
    // Semantics: progressBarRangeInfo
    // -----------------------------------------------------------------------

    @Test
    fun levelProgressBar_hasProgressBarRangeInfo() {
        composeTestRule.setContent {
            LevelProgressBar(level = 7, maxLevel = 10, animated = false)
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(
                        current = 7f,
                        range = 0f..10f,
                        steps = 9,
                    )
                )
            )
    }

    // -----------------------------------------------------------------------
    // Backward-compatible overload tests
    // -----------------------------------------------------------------------

    @Suppress("DEPRECATION")
    @Test
    fun levelProgressBar_legacyApi_isStepProgress_works() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                isStepProgress = true,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "step")
            )
    }

    @Suppress("DEPRECATION")
    @Test
    fun levelProgressBar_legacyApi_individualColors_works() {
        composeTestRule.setContent {
            LevelProgressBar(
                level = 5,
                progressColor = Color.Red,
                unProgressColor = Color.LightGray,
                backgroundColor = Color.DarkGray,
                textColor = Color.Yellow,
                animated = false,
            )
        }

        composeTestRule
            .onNodeWithTag("LevelProgressBar")
            .assertIsDisplayed()
    }
}
