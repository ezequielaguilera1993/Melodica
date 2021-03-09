package com.example.melodica

import android.content.pm.PackageManager
import android.graphics.Color.BLACK
import android.graphics.Color.parseColor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


open class MainActivity : AppCompatActivity() {
    var b = 0
    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null
    var a: Int? = 0
    var array = arrayListOf<Int>()
    lateinit var mp: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )
        mp = MediaPlayer.create(this, R.raw.tonoe)
        mp.isLooping = true


//Global Scope todo lo que se ejecue aca se ejcacute hasta que se destuya el app


        /* Log.i("Printer", "${Color.parseColor("#ffffff")}")
         Log.i("Printer", "${Color.parseColor("#fefffe")}")
         Log.i("Printer", "${Color.parseColor("#fdfffd")}")
         Log.i("Printer", "${Color.parseColor("#fcfffc")}")
         Log.i("Printer", "${Color.parseColor("#fbfffb")}")
         Log.i("Printer", "${Color.parseColor("#fafffa")}")
         Log.i("Printer", "${Color.parseColor("#f0fff0")}")
         Log.i("Printer", "${Color.parseColor("#00ff00")}")*/



        start.setOnClickListener()
        {
            mp.start()
//            mp.setVolume(0f, 0f)
            startRecording()
            GlobalScope.launch(Dispatchers.Default) {
                while (true) {

                    a = recorder?.maxAmplitude!!
                    Log.i("|||a", "${a}")
                    /***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/

                    Thread.sleep(50)
                    array.add((a!!).toInt())
                    var maxVol = 32767

                    var volume = (a!!.toFloat() / maxVol)


                    Log.i("|||volume", "${volume}")
                    /***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/

                    withContext(Dispatchers.Main) {

                        if (array.count() == 10) {
                            b =
                                (array[0] + array[1] + array[2] + array[3] + array[4] + array[5] + array[6] + array[7] + array[8] + array[9]) / 10
                            textView2.text = ((b.toDouble() / maxVol) * 255.0).toInt().toString()

                            array.clear()

                            if (b1.isPressed) {
                                mp.setVolume(volume, volume)
                                if (b < maxVol / 10) {
                                    back.setBackgroundColor(parseColor("#000000"))
                                } else back.setBackgroundColor(-1 - (((b.toFloat() / maxVol) * 255).toInt() * 65537))

                            }
                        }

                        """$a||$b""".also { textView.text = it }


//                        Log.i("b Printer", "$b")
//                        Log.i("color Printer", "${(b.toFloat() / 32767)*255*65537}")

                        if (!b1.isPressed) {
                            mp.setVolume(0f, 0f)
                            back.setBackgroundColor(parseColor("#000000"))
                        }


                    }
                }

            }
        }

        stopRecording.setOnClickListener()
        {
            stopRecording()
//stopRecording()
        }

    }


    private fun startRecording() {

        start.isEnabled = false
        stopRecording.isEnabled = true
        //Creating file
        try {
            audioFile = File.createTempFile("audio", "tmp", cacheDir)
        } catch (e: IOException) {
            Log.e(MainActivity::class.simpleName, e.message ?: e.toString())
            return
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = MediaRecorder()
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            setAudioSamplingRate(48000)
            setAudioEncodingBitRate(48000)
            prepare()
            start()
        }

        //startDrawing()
//    timer.start() //actualiza el textView
    }

    private fun stopRecording() {
        start.isEnabled = true
        stopRecording.isEnabled = false
        //stopping recorder
        recorder?.apply {
            stop()
            release()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
    }

    fun btnIsPressed(view: View): Boolean {

//Log.i("|||view.isPressed", "${view.isPressed} ${view.id} ")/***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/
        return (view.isPressed)

    }
}


/*
var s=0
for (x in -1 downTo -16711936 step 65537) {
    Log.i("Printer", "$x")
    back.setBackgroundColor(x)
    Thread.sleep(50)
s++
}
Log.i("Printer","$s")

back.setBackgroundColor(-16777216)*/


/*var audioTrack = AudioTrack1(
    AudioManager.STREAM_MUSIC,
   500, AudioFormat.CHANNEL_OUT_MONO,
    AudioFormat.ENCODING_PCM_16BIT, 10,
    AudioTrack1.MODE_STATIC)


audioTrack.write(buffer, 0, buffer.length);
audioTrack.play();*/