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
implementation "io.github.rezaiyan:levelprogressbar:2.0.0"
```

## Quick Start

```kotlin
LevelProgressBar(
    level = 7,
)
```

## API

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `level` | `Int` | *required* | Current progress level, clamped to `0..maxLevel` |
| `modifier` | `Modifier` | `Modifier` | Layout modifier |
| `maxLevel` | `Int` | `10` | Maximum level (must be > 0) |
| `colors` | `LevelProgressBarColors` | `LevelProgressBarDefaults.colors()` | Color configuration |
| `strokeWidth` | `Dp` | `10.dp` | Arc stroke width |
| `mode` | `ProgressMode` | `Continuous` | `ProgressMode.Continuous` or `ProgressMode.Step` |
| `enabled` | `Boolean` | `true` | Disabled state reduces alpha to 40% |
| `animated` | `Boolean` | `true` | Animate level changes |
| `animationSpec` | `AnimationSpec<Float>` | `tween(1500ms)` | Custom animation spec |
| `content` | `@Composable ((Int) -> Unit)?` | `null` | Slot for custom center content (replaces default text) |

### Colors

Use `LevelProgressBarDefaults.colors()` to configure colors:

```kotlin
LevelProgressBar(
    level = 7,
    colors = LevelProgressBarDefaults.colors(
        progressColor = Color(0xFF2196F3),  // Filled arc
        trackColor = Color(0xFFBBDEFB),     // Unfilled arc
        backgroundColor = Color(0xFF2196F3), // Inner circle
        textColor = Color.White,             // Level number
    ),
)
```

### Progress Mode

```kotlin
// Continuous arc (default)
LevelProgressBar(
    level = 5,
    mode = ProgressMode.Continuous,
)

// Segmented steps
LevelProgressBar(
    level = 5,
    mode = ProgressMode.Step,
)
```

### Custom Center Content

Replace the default level text with any composable:

```kotlin
LevelProgressBar(
    level = 7,
) { currentLevel ->
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = Color.White,
    )
}
```

### Custom Animation

```kotlin
LevelProgressBar(
    level = 7,
    animationSpec = spring(dampingRatio = 0.6f),
)
```

### Defaults Object

All defaults are accessible via `LevelProgressBarDefaults`:

| Constant | Value |
|---|---|
| `MaxLevel` | `10` |
| `StrokeWidth` | `10.dp` |
| `Size` | `250.dp` |
| `Mode` | `ProgressMode.Continuous` |

## Migration from 1.x

The 1.x individual color parameters still work via a backward-compatible overload:

```kotlin
// 1.x style - still works
LevelProgressBar(
    level = 5,
    progressColor = Color.Red,
    unProgressColor = Color.LightGray,
    isStepProgress = true,
)

// 2.x style - recommended
LevelProgressBar(
    level = 5,
    colors = LevelProgressBarDefaults.colors(
        progressColor = Color.Red,
        trackColor = Color.LightGray,
    ),
    mode = ProgressMode.Step,
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
