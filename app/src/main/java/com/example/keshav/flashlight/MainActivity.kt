package com.example.keshav.flashlight


import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {


    private val TAG = "TorchLight"
    private val LEDOFF = 0
    private val LEDON = 1
    private var mLedState = LEDOFF
    private var mLedOnOff = false
    private var mp: MediaPlayer? = null
    private var camera: Camera? = null
    internal var params: Camera.Parameters? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hasFlash()

       val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton?.setOnClickListener(View.OnClickListener { onClickLedOnOffButton() })

    }

    private fun hasFlash() {

        mLedOnOff = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (!mLedOnOff) {
            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Error")
            alertDialog.setMessage("Device doesn't support Flash light")
            alertDialog.setPositiveButton("OK") { dialog, which -> finish() }
            alertDialog.show()
        }
        getCamera()
        return
    }



    private fun onClickLedOnOffButton() {
       playSound()

        if (mLedOnOff) {
            turnOnOffLed(LEDON);
            mLedOnOff = false
        } else {
           turnOnOffLed(LEDOFF);
            mLedOnOff = true
        }
    }

    private fun turnOnOffLed(on: Int) {

        if (camera == null || params == null) {
            return
        }

        if (on == LEDON) {
            params = camera!!.parameters
            params!!.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera!!.parameters = params
            camera!!.startPreview()

        } else {
            camera!!.parameters
            params!!.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera!!.parameters = params
            camera!!.stopPreview()

        }
        mLedState = on
    }

    private fun getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open()
                params = camera!!.parameters
            } catch (e: Exception) {

            }

        }
    }

    private fun releaseCamera() {

        if (camera != null) {
            camera!!.release()
            camera = null
        }
    }

    private fun playSound() {
        if (mLedState == 1) {
            mp = MediaPlayer.create(this@MainActivity, R.raw.light_switch_off)
        } else {
            mp = MediaPlayer.create(this@MainActivity, R.raw.light_switch_on)
        }
        mp!!.setOnCompletionListener { mp -> mp.release() }
        mp!!.start()
    }



    override fun onStop() {
        super.onStop()
        releaseCamera()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStart() {
        super.onStart()

    }


}
