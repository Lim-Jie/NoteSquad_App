package com.example.NoteSquad_TestApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button



class HomePage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val openNoteShareButton = findViewById<Button>(R.id.NoteShareButton)

        openNoteShareButton.setOnClickListener{v->
            OpenNoteShare()
        }
    }

    fun OpenNoteShare (){
        val intent=Intent(this,NoteSharepage::class.java)
        startActivity(intent)
    }






}


