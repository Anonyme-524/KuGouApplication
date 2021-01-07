package com.example.kugouapplication

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var time = 7
    private val timer = Timer()
    private val task: TimerTask =object : TimerTask(){
        override fun run() {
            runOnUiThread {
                time--
                button.text="$time 跳过"
            }
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
        setContentView(R.layout.activity_main)
        handler.postDelayed({
            init()
        },7500)

        handler.postDelayed({
            imageView2.setImageResource(R.drawable.logo2)
            button.visibility = View.VISIBLE
        },2000)

        timer.schedule(task,1000,1000)

        button.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            init()
        }
        initMediaPlayer()
        mediaPlayer.start()
    }
    private fun init()
    {
        val intent = Intent(this,KuGouActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun initMediaPlayer() {
        val assetManager = assets
        val fd = assetManager.openFd("kugou.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor,fd.startOffset,fd.length)
        mediaPlayer.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}