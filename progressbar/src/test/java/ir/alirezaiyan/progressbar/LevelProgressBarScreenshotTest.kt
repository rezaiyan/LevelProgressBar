package ir.alirezaiyan.progressbar

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class LevelProgressBarScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun allLevels_continuous() {
        paparazzi.snapshot {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (level in 0..5) {
                        LevelProgressBar(
                            level = level,
                            modifier = Modifier.size(120.dp),
                            mode = ProgressMode.Continuous,
                            animated = false,
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (level in 6..10) {
                        LevelProgressBar(
                            level = level,
                            modifier = Modifier.size(120.dp),
                            mode = ProgressMode.Continuous,
                            animated = false,
                        )
                    }
                }
            }
        }
    }

    @Test
    fun allLevels_step() {
        paparazzi.snapshot {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (level in 0..5) {
                        LevelProgressBar(
                            level = level,
                            modifier = Modifier.size(120.dp),
                            mode = ProgressMode.Step,
                            animated = false,
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (level in 6..10) {
                        LevelProgressBar(
                            level = level,
                            modifier = Modifier.size(120.dp),
                            mode = ProgressMode.Step,
                            animated = false,
                        )
                    }
                }
            }
        }
    }

    @Test
    fun customColors() {
        paparazzi.snapshot {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LevelProgressBar(
                    level = 7,
                    modifier = Modifier.size(150.dp),
                    colors = LevelProgressBarDefaults.colors(
                        progressColor = Color(0xFF2196F3),
                        trackColor = Color(0xFFBBDEFB),
                        backgroundColor = Color(0xFF2196F3),
                    ),
                    animated = false,
                )
                LevelProgressBar(
                    level = 7,
                    modifier = Modifier.size(150.dp),
                    colors = LevelProgressBarDefaults.colors(
                        progressColor = Color(0xFFF44336),
                        trackColor = Color(0xFFFFCDD2),
                        backgroundColor = Color(0xFFF44336),
                    ),
                    animated = false,
                )
                LevelProgressBar(
                    level = 7,
                    modifier = Modifier.size(150.dp),
                    colors = LevelProgressBarDefaults.colors(
                        progressColor = Color(0xFFFF9800),
                        trackColor = Color(0xFFFFE0B2),
                        backgroundColor = Color(0xFFFF9800),
                    ),
                    animated = false,
                )
            }
        }
    }

    @Test
    fun customStrokeWidth() {
        paparazzi.snapshot {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LevelProgressBar(
                    level = 6,
                    modifier = Modifier.size(150.dp),
                    strokeWidth = 5.dp,
                    animated = false,
                )
                LevelProgressBar(
                    level = 6,
                    modifier = Modifier.size(150.dp),
                    strokeWidth = 15.dp,
                    animated = false,
                )
                LevelProgressBar(
                    level = 6,
                    modifier = Modifier.size(150.dp),
                    strokeWidth = 25.dp,
                    animated = false,
                )
            }
        }
    }

    @Test
    fun disabled() {
        paparazzi.snapshot {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LevelProgressBar(
                    level = 7,
                    modifier = Modifier.size(150.dp),
                    enabled = true,
                    animated = false,
                )
                LevelProgressBar(
                    level = 7,
                    modifier = Modifier.size(150.dp),
                    enabled = false,
                    animated = false,
                )
            }
        }
    }

    @Test
    fun darkBackground() {
        paparazzi.snapshot {
            Box(
                modifier = Modifier
                    .background(Color(0xFF121212))
                    .padding(16.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LevelProgressBar(
                        level = 5,
                        modifier = Modifier.size(150.dp),
                        colors = LevelProgressBarDefaults.colors(
                            progressColor = Color(0xFF4CAF50),
                            trackColor = Color(0xFF424242),
                            backgroundColor = Color(0xFF4CAF50),
                        ),
                        animated = false,
                    )
                    LevelProgressBar(
                        level = 5,
                        modifier = Modifier.size(150.dp),
                        colors = LevelProgressBarDefaults.colors(
                            progressColor = Color(0xFF4CAF50),
                            trackColor = Color(0xFF424242),
                            backgroundColor = Color(0xFF4CAF50),
                        ),
                        mode = ProgressMode.Step,
                        animated = false,
                    )
                }
            }
        }
    }
}
