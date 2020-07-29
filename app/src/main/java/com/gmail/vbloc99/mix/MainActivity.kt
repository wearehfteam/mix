package com.gmail.vbloc99.mix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Mlist:ArrayList<String> = ArrayList()
        Mlist.addAll(
            listOf(
            "believer",
            "save_me",
            "sign"
            ))
        val Slist = mutableListOf(
            "Dragon",
            "DEAMN",
            "DEAMN"
        )

        val adapter = ItemAdapter(Mlist, Slist, this)

        val listView: ListView = findViewById(R.id.listMusics)
        listView.adapter = adapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.i("hello", Mlist[position])
                val intent = Intent(this, Play::class.java)

                intent.putExtra("position", position);

                intent.putStringArrayListExtra("data", Mlist)
                this.startActivity(intent)
            }
    }

}