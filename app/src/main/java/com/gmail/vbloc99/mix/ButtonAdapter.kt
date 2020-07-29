package com.gmail.vbloc99.mix

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView

class ButtonAdapter(
    var BList: List<Int>,
    context: Context?
) :
    BaseAdapter() {
    var context: Context? = null
    var viewGroup: ViewGroup? = null
    var columns = 5
    override fun getCount(): Int {
        return BList.size
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
        this.viewGroup = viewGroup
        val inflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val retView = inflater.inflate(R.layout.button, viewGroup, false)
        val button = retView.findViewById<ImageView>(R.id.button)
        val mp =
            arrayOf(MediaPlayer.create(context, BList[i]))
        button.setOnClickListener {
            if (mp[0]!!.isPlaying) {
                mp[0]!!.stop()
                mp[0]!!.reset()
                mp[0]!!.release()
                mp[0] = null
                mp[0] = MediaPlayer.create(context, BList[i])
            }
            mp[0]!!.start()
//            lightPattern3(i)
            if(i==4 || i==0 ) lightPattern4(i)
            else if(i==24) lightPattern3(i)
            else {
                val ranPattern = (0..4).random()
                when(ranPattern){
                    0 -> lightPattern0(i)
                    1 -> lightPattern1(i)
                    2 -> lightPattern2(i)
                    3 -> lightPattern3(i)
                    4 -> lightPattern4(i)
                }
            }
        }
        return retView
    }
    private fun lightPattern0(i : Int) {
        val color = (1..5).random()
        setColorAtPos(i,color)
    }

    private fun lightPattern1(i : Int) {
        val color = (1..5).random()
        setColorAtPos(i,color)
        for(time in (1..columns)){
            Handler(Looper.getMainLooper()).postDelayed({
                rowLight(i, time, color)
                colLight(i, time, color)
            }, (time*100).toLong())
        }
    }

    private fun lightPattern2(i : Int) {
        val color = (1..5).random()
        setColorAtPos(i,color)
        for(time in (1 until columns)){
            Handler(Looper.getMainLooper()).postDelayed({
                if((i-time)/columns == i/columns) colLight(i-time, time, color)
                if((i+time)/columns == i/columns)colLight(i+time, time, color)
            }, (time*100).toLong())
        }
    }

    private fun lightPattern3(i : Int) {
        val array = arrayListOf<Int>(0,1,2,3,4,9,14,19,24,23,22,21,20,15,10,5,6,7,8,13,18,17,16,11,12)
        val color = (1..5).random()


        for((index, value) in array.withIndex()){
            Handler(Looper.getMainLooper()).postDelayed({
                setColorAtPos(value, color)
            }, (index*50).toLong())
        }

    }

    private fun lightPattern4(i : Int) {
        var array = arrayListOf<Int>(0,1,2,3,4,9,14,19,24,23,22,21,20,15,10,5,6,7,8,13,18,17,16,11,12)
        var size = array.size
        val color = (1..5).random()


        for((index, value) in array.withIndex()){
            Handler(Looper.getMainLooper()).postDelayed({
                setColorAtPos(value, color)
            }, ((size-index)*50).toLong())
        }

    }

    private fun rowLight(pos: Int, dis: Int, color: Int){
        if (((pos - dis)/columns).toInt() == (pos/columns).toInt()){
            setColorAtPos(pos-dis,color)
        }
        if (((pos + dis)/columns).toInt() == (pos/columns).toInt()){
            setColorAtPos(pos+dis,color)
        }
    }

    private fun colLight(pos: Int, dis: Int, color: Int){
        if (((pos - dis*columns)%columns).toInt() == (pos%columns).toInt()){
            setColorAtPos(pos-dis*columns,color)
        }
        if (((pos + dis*columns)%columns).toInt() == (pos%columns).toInt()){
            setColorAtPos(pos+dis*columns,color)
        }
    }




    private fun setColorAtPos(pos: Int, colorNum: Int) {
        if (pos < 0 || pos >= 25) return;

        val view = this.viewGroup?.getChildAt(pos)
        val btn = view?.findViewById<ImageView>(R.id.button)



        val color = when (colorNum) {
            1 -> R.drawable.blue
            2 -> R.drawable.darkgreen
            3 -> R.drawable.green
            4 -> R.drawable.orange
            5 -> R.drawable.pink
            else ->R.drawable.gray
        }
        btn?.setBackgroundResource(color)
        if (colorNum != 0){
            Handler(Looper.getMainLooper()).postDelayed({
                btn?.setBackgroundResource(R.drawable.gray)
            }, 500)
        }


    }


    private fun playSound(mp: Array<MediaPlayer?>, i: Int) {

    }

    init {
        this.context = context
    }
}