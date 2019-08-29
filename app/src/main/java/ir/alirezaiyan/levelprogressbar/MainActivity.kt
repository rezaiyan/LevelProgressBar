/*
 * Copyright 2016 alirezaiyann@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.alirezaiyan.levelprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        p1IsEnable.setOnCheckedChangeListener { _, b -> p1.setEnable(b) }
        p1IsStepBar.setOnCheckedChangeListener { _, b -> p1.setIsStep(b) }

        p1LevelSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (fromUser) {
                    p1.setProgressWithAnimation(progress.toFloat())
                } else
                    p1.setSpeed(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //write custom code to on start progress
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        p1LevelSeekStroke.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                p1.strokeWidth = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //write custom code to on start progress
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }
}
