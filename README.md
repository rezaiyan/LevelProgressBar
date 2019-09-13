# LevelProgressBar
A custom view with circular step progress level

[![Download](https://api.bintray.com/packages/rezaiyan/Android/levelprogressbar/images/download.svg)](https://bintray.com/rezaiyan/Android/levelprogressbar/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LevelProgressBar-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7831)
[![Build Status](https://travis-ci.org/rezaiyan/LevelProgressBar.svg?branch=master)](https://travis-ci.org/rezaiyan/LevelProgressBar)
[![License](https://img.shields.io/badge/License-Apache/2.0-blue.svg)](https://github.com/badoo/Reaktive/blob/master/LICENSE)


<p align="center">
  <img src="./art/preview.png" height="400" width="220"/>
</p>

## Installation

Add the dependency to your app build.gradle file:

```
implementation "ir.alirezaiyan:levelprogressbar:1.0.2"
```

## Usage

Add `LevelProgressBar`:

```xml
   <ir.alirezaiyan.progressbar.LevelProgressBar
            android:id="@+id/p1"
            android:layout_width="150dp"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="20dp"
            android:layout_height="150dp"
            app:spb_background_progress_color="#E22D9D"
            app:spb_is_enable="true"
            app:spb_unprogress_color="#E6E6E6"
            app:spb_stroke_with="10"
            app:spb_is_step_progress="true"
            app:spb_level="level6"
            app:spb_text_level_color="#fff"
            app:spb_text_title_color="#040504"
            app:spb_src="@drawable/ic_level"/>
```

License
--------

    Copyright 2016 alirezaiyann@gmail.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
