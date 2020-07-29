package com.gmail.vbloc99.mix

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaPlayer.create
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*


class Play : AppCompatActivity() {
    var mRecorder: MediaRecorder? = null
    val REQUEST_AUDIO_PERMISSION_CODE = 1
    var mFileName: kotlin.String? = null
    var checked = false;


    var mp: MediaPlayer? = null
    var finalTime = 0
    var seekbar: SeekBar? = null
    var maxVolume = 100
    var sbVolume: SeekBar? = null
    var oneTimeOnly = 0
    var myHandler: Handler = Handler()
    var pause: ImageView? = null
    var next: ImageView? = null
    var previous: ImageView? = null
    var listSong: ImageView? = null
    var nameSong: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val list = Arrays.asList(
            R.raw.bubbles, R.raw.clay, R.raw.confetti, R.raw.corona,
            R.raw.dotted_spiral, R.raw.flash_2, R.raw.glimmer,
            R.raw.pinwheel, R.raw.prism_1, R.raw.prism_2,
            R.raw.prism_3, R.raw.silent, R.raw.splits, R.raw.squiggle,
            R.raw.suspension, R.raw.timer, R.raw.ufo, R.raw.veil,
            R.raw.wipe, R.raw.zig_zag, R.raw.pad1,
            R.raw.pad13, R.raw.pad14, R.raw.pad16, R.raw.pad17
        )

        val btnAdapter = ButtonAdapter(list, this)
        val gridView = findViewById<GridView>(R.id.gridView)
        val rdb = findViewById(R.id.recordButton) as RadioButton

        gridView.adapter = btnAdapter

        record(rdb);

        var position = intent.getIntExtra("position",0)
        val Mlist = intent.getStringArrayListExtra("data")

        if (Mlist !== null) {
            stop()
            start(position, Mlist)
        }

        initSeekbar()

        myHandler.postDelayed(UpdateSongTime, 100)

        seekbar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mp!!.seekTo(seekBar.progress)
                pause!!.setImageResource(R.drawable.pause)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mp!!.start()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.progress = progress
            }
        })

        sbVolume!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val log1 = (progress.toDouble()/maxVolume.toDouble()).toFloat()

                mp!!.setVolume(log1, log1) //set volume takes two paramater
                seekBar.progress = progress
            }
        })

        //play control
        nameSong = findViewById(R.id.song)
        nameSong!!.text = Mlist!!.get(position)
        initControlButton()

        listSong!!.setOnClickListener {
            ListSong()
        }

        pause!!.setOnClickListener {
            pause()
        }
        next!!.setOnClickListener {
            position++
            if (position >= Mlist.size ){
                position = 0
            }
            nameSong!!.text = Mlist[position]
            start(position, Mlist)
        }
        previous!!.setOnClickListener {
            position--
            if (position < 0){
                position = Mlist.size - 1
            }
            nameSong!!.text = Mlist.get(position)
            if (Mlist != null) {
                start(position, Mlist)
            }
        }


    }

    fun stop() {
        if (mp != null) {
            mp!!.stop()
            mp!!.release()
            mp = null
        }

    }

    fun pause() {
        if (mp!!.isPlaying) {
            mp!!.pause()
            pause!!.setImageResource(R.drawable.play)
        } else {
            pause!!.setImageResource(R.drawable.pause)
            mp!!.start()
        }
    }

    fun start(position: Int, Mlist: ArrayList<kotlin.String>) {
        stop()
        val name = Mlist.get(position)
        var resID = resources.getIdentifier(name, "raw", packageName)
        mp = create(this, resID)
        mp!!.start()
        mp!!.setVolume(10.toFloat(),10.toFloat())
    }

    fun ListSong() {
        stop()
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            if (mp != null){
                val startTime = mp!!.currentPosition
                seekbar!!.progress = startTime
                myHandler.postDelayed(this, 100)
                if (startTime == finalTime){
                    pause!!.setImageResource(R.drawable.play)
                }
            }

        }
    }

    fun initControlButton() {
        listSong = findViewById(R.id.btnListSong)
        listSong!!.setImageResource(R.drawable.list_song)

        pause = findViewById(R.id.btnPause)
        pause!!.setImageResource(R.drawable.pause)

        next = findViewById(R.id.btnNext)
        next!!.setImageResource(R.drawable.next)

        previous = findViewById(R.id.btnPrevious)
        previous!!.setImageResource(R.drawable.previous)

    }
    fun initSeekbar() {
        finalTime = mp!!.duration

        seekbar = findViewById(R.id.seekBar)
        sbVolume = findViewById(R.id.sbVolume)

        if (oneTimeOnly == 0) {
            seekbar!!.max = finalTime
            oneTimeOnly = 1
        }
        seekbar!!.progress = 0

        sbVolume!!.max = maxVolume
        sbVolume!!.progress = maxVolume
    }

    fun record(rdb: RadioButton) {
        rdb.setOnClickListener { v ->
            run {
                checked = !checked
                var rdb = v as RadioButton
                v.isChecked = checked

                if (checked) {
                    if (CheckPermissions()) {
                        mRecorder = MediaRecorder()

                        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.VOICE_PERFORMANCE)
                        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                        mFileName = getExternalFilesDir(null)!!.absolutePath;
                        mFileName += "/AudioRecording" + System.currentTimeMillis().toString() + ".3gp";

                        mRecorder!!.setOutputFile(mFileName);

                        try {
                            mRecorder!!.prepare()
                        } catch (e: IOException) {
                            Log.e("MyActivity", "prepare() failed")
                        }
                        mRecorder!!.start()

                        Toast.makeText(
                            applicationContext,
                            "Recording Started",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        RequestPermissions();
                    }
                } else {
                    mRecorder!!.stop();
                    mRecorder!!.reset();
                    mRecorder!!.release();
                    mRecorder = null
                    Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG)
                        .show();
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out kotlin.String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.size > 0) {
                val permissionToRecord =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun CheckPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun RequestPermissions() {
        ActivityCompat.requestPermissions(
            this@Play,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_AUDIO_PERMISSION_CODE
        )
    }
}

