# LevelProgressBar

A Jetpack Compose circular progress bar with **Segmented** and **Continuous** modes.

[![License](https://img.shields.io/badge/License-Apache/2.0-blue.svg)](https://github.com/rezaiyan/LevelProgressBar/blob/master/LICENSE)

## Screenshots

### Continuous Mode
All levels from 0 to 10 rendered in continuous arc style:

<p align="center">
  <img src="./art/screenshot_continuous.png" alt="Continuous mode levels 0-10"/>
</p>

### Step Mode
All levels from 0 to 10 rendered in segmented step style:

<p align="center">
  <img src="./art/screenshot_step.png" alt="Step mode levels 0-10"/>
</p>

### Custom Colors
Easily theme the progress bar with any color combination:

<p align="center">
  <img src="./art/screenshot_colors.png" alt="Custom color themes"/>
</p>

### Stroke Width Variations
Control the arc thickness with the `strokeWidth` parameter:

<p align="center">
  <img src="./art/screenshot_stroke_widths.png" alt="Stroke width variations"/>
</p>

### Enabled vs Disabled
Visual feedback for enabled and disabled states (both continuous and step modes):

<p align="center">
  <img src="./art/screenshot_enabled_disabled.png" alt="Enabled vs disabled states"/>
</p>

### Dark Theme
Works on dark backgrounds:

<p align="center">
  <img src="./art/screenshot_dark_theme.png" alt="Dark theme"/>
</p>

## Installation

Add the dependency to your app `build.gradle` file:

```gradle
implementation "io.github.rezaiyan:levelprogressbar:1.0.3"
```

## Usage

```kotlin
LevelProgressBar(
    level = 7,
    modifier = Modifier.size(250.dp),
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `level` | `Int` | *required* | Current progress level |
| `modifier` | `Modifier` | `Modifier` | Layout modifier |
| `maxLevel` | `Int` | `10` | Maximum level cap |
| `progressColor` | `Color` | `Color.Green` | Completed progress arc color |
| `unProgressColor` | `Color` | `Color.Gray` | Remaining progress arc color |
| `backgroundColor` | `Color` | `progressColor` | Inner circle background color |
| `textColor` | `Color` | `Color.White` | Level number text color |
| `strokeWidth` | `Dp` | `10.dp` | Arc stroke width |
| `isStepProgress` | `Boolean` | `false` | Segmented step mode vs continuous arc |
| `enabled` | `Boolean` | `true` | Enabled/disabled state (disabled = 40% alpha) |
| `imageBitmap` | `ImageBitmap?` | `null` | Optional center image replacing the level text |
| `animated` | `Boolean` | `true` | Animate progress changes |

### Examples

**Step mode with custom colors:**
```kotlin
LevelProgressBar(
    level = 5,
    isStepProgress = true,
    progressColor = Color(0xFF2196F3),
    unProgressColor = Color(0xFFBBDEFB),
    backgroundColor = Color(0xFF2196F3),
)
```

**Disabled state:**
```kotlin
LevelProgressBar(
    level = 7,
    enabled = false,
)
```

**Custom stroke width:**
```kotlin
LevelProgressBar(
    level = 6,
    strokeWidth = 25.dp,
)
```

## Screenshot Testing

This project uses [Paparazzi](https://github.com/cashapp/paparazzi) for screenshot testing. Paparazzi runs on the JVM with no emulator required.

**Record golden screenshots:**
```bash
./gradlew :progressbar:recordPaparazziDebug
```

**Verify against golden screenshots:**
```bash
./gradlew :progressbar:verifyPaparazziDebug
```

Golden files are stored in `progressbar/src/test/snapshots/`.

## License

    Copyright 2020 alirezaiyann@gmail.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
