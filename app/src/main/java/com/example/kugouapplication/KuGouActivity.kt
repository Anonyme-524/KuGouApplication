package com.example.kugouapplication

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ku_gou.*
import kotlinx.android.synthetic.main.activity_song_list.*

class KuGouActivity : AppCompatActivity() {
    private var kgList = ArrayList<kgClass>()
    private val mediaPlayer = MediaPlayer()
    private var index = 0
    private fun initKGData() {
        repeat(2)
        {
            kgList.add(kgClass("小尖尖", "薛之谦", R.drawable.music_01, "music_01.mp3"))
            kgList.add(kgClass("彩卷", "薛之谦", R.drawable.music_02, "music_02.mp3"))
            kgList.add(kgClass("绅士", "薛之谦", R.drawable.music_03, "music_03.mp3"))
            kgList.add(kgClass("天后", "薛之谦", R.drawable.music_04, "music_04.mp3"))
            kgList.add(kgClass("天外来物", "薛之谦", R.drawable.music_05, "music_05.mp3"))
            kgList.add(kgClass("下雨了", "薛之谦", R.drawable.music_06, "music_06.mp3"))
            kgList.add(kgClass("演员", "薛之谦", R.drawable.music_07, "music_07.mp3"))
            kgList.add(kgClass("野心", "薛之谦", R.drawable.music_08, "music_08.mp3"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val window = this.window
        val decorView = window.decorView
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_ku_gou)
        initKGData()
        initMediaPlayer(kgList[index].music)
        Recycler_view.layoutManager = LinearLayoutManager(this)
        val adapter = KGAdapter(kgList)
        Recycler_view.adapter = adapter

        play.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                play.setImageResource(R.drawable.ic_zan)
                mediaPlayer.start()
            } else {
                play.setImageResource(R.drawable.ic_bo)
                mediaPlayer.pause()
            }
            musicRotate()
        }

        last_song.setOnClickListener {
            index--
            mediaPlayer.reset()
            if (index < 0) {
                index = kgList.size - 1
            }
            musicManager()
            musicRotate()
        }

        next_song.setOnClickListener {
            index++
            mediaPlayer.reset()
            if (index > kgList.size - 1) {
                index = 0
            }
            musicManager()
            musicRotate()
        }

    }

    private fun musicRotate() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
        anim.interpolator = LinearInterpolator()
        record.animation = anim
        anim.cancel()
    }

    private fun musicManager() {
        initMediaPlayer(kgList[index].music)
        song_title.text = kgList[index].songTitle
        singer.text = kgList[index].Singer
        record.setImageResource(kgList[index].image)
        mediaPlayer.start()
        play.setImageResource(R.drawable.ic_zan)

    }

    inner class KGAdapter(private val kgList: ArrayList<kgClass>) : RecyclerView.Adapter<KGAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val songTitle: TextView = view.findViewById(R.id.songTitle)
            val Singer: TextView = view.findViewById(R.id.Singer)

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_song_list, parent, false)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.setOnClickListener {
                index = viewHolder.adapterPosition
                val list = kgList[index]
                mediaPlayer.reset()
                initMediaPlayer(list.music)
                play.setImageResource(R.drawable.ic_zan)
                mediaPlayer.start()
                musicRotate()


                song_title.text = list.songTitle
                singer.text = list.Singer
                record.setImageResource(list.image)

            }
            return viewHolder
        }

        override fun getItemCount() = kgList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val kgClass = kgList[position]
            holder.songTitle.text = kgClass.songTitle
            holder.Singer.text = kgClass.Singer
        }
    }

    private fun initMediaPlayer(music: String) {
        val assetManager = assets
        val fd = assetManager.openFd(music)
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}