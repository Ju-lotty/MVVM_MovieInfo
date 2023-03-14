package com.example.movie_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.movie_app.details.DetailMovie

class MainActivity : AppCompatActivity() {
    private val btn: Button by lazy {
        findViewById(R.id.btn)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            val intent = Intent(this, DetailMovie::class.java)
            intent.putExtra("id", 315162)
            this.startActivity(intent)
        }
    }
}