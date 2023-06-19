package com.codelog.arke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.marginRight
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val cardView: CardView = findViewById(R.id.cardView)
        val view = LinearLayout(cardView.context)
        view.orientation = LinearLayout.HORIZONTAL

        val profilePic = ImageView(view.context)
        profilePic.setImageResource(R.mipmap.ic_launcher)

        val name = TextView(view.context)
        name.text = "Jaco Malan"

        view.addView(profilePic)
        view.addView(name)
        view.setOnClickListener(View.OnClickListener {
            val toast = Toast.makeText(it.context, "Hello World!", Toast.LENGTH_SHORT)
            toast.show()
        })

        cardView.addView(view)
    }
}