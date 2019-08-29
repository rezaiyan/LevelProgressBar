package ir.alirezaiyan.levelprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        p1IsEnable.setOnCheckedChangeListener { compoundButton, b -> p1.setEnable(b) }
        p1IsStepBar.setOnCheckedChangeListener { compoundButton, b -> p1.setIsStep(b) }

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
