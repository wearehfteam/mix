package com.gmail.vbloc99.mix

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.create
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import java.lang.String
import java.util.*
import kotlin.collections.ArrayList


class Play : AppCompatActivity() {

    var mp: MediaPlayer? = null
    var startTime = 0
    var finalTime = 0
    var seekbar: SeekBar? = null
    var oneTimeOnly = 0
    var myHandler: Handler = Handler()
    var pause: ImageView? = null
    var next: ImageView? = null
    var previous: ImageView? = null
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
        gridView.adapter = btnAdapter

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
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mp!!.start()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.progress = progress
            }
        })

        //play control
        nameSong = findViewById(R.id.song)
        nameSong!!.text = Mlist!!.get(position)
        initControlButton()
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

    override fun onBackPressed() {
        stop()
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        return
    }

    private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mp!!.currentPosition
            seekbar!!.progress = startTime
            myHandler.postDelayed(this, 100)
        }
    }

    fun initControlButton() {
        pause = findViewById(R.id.btnPause)
        pause!!.setImageResource(R.drawable.pause)

        next = findViewById(R.id.btnNext)
        next!!.setImageResource(R.drawable.next)

        previous = findViewById(R.id.btnPrevious)
        previous!!.setImageResource(R.drawable.previous)

    }
    fun initSeekbar() {
        finalTime = mp!!.duration
        startTime = mp!!.currentPosition

        seekbar = findViewById(R.id.seekBar)

        if (oneTimeOnly == 0) {
            seekbar!!.max = finalTime
            oneTimeOnly = 1
        }
        seekbar!!.progress = startTime
    }

}

