package com.gmail.vbloc99.mix

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView




class ItemAdapter(
    var Mlist: List<String>,
    var Slist: List<String>,
    context: Context?
) :
    BaseAdapter() {
    var context: Context? = null
    override fun getCount(): Int {
        return Mlist.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(
        i: Int,
        view: View?,
        viewGroup: ViewGroup
    ): View {
        val inflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val retView = inflater.inflate(R.layout.item, viewGroup, false)

        val music: TextView = retView.findViewById<TextView>(R.id.nameMusic)
        music.setText(Mlist[i])
        val singer: TextView = retView.findViewById<TextView>(R.id.nameSinger)
        singer.setText(Slist[i])
        val image: ImageView = retView.findViewById<ImageView>(R.id.imageView)
        image.setImageResource(R.mipmap.music)
        return retView
    }

    init {
        this.context = context
    }


}