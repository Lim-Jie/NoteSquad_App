package com.example.authentication_user_test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username= findViewById<TextView>(R.id.EmailAddress)
        val password= findViewById<TextView>(R.id.Password)
        val LoginButton=findViewById<Button>(R.id.LoginButton)

        //event listener
        LoginButton.setOnClickListener {v->
            if(username.text.toString().equals("Li Jie") &&  password.text.toString().equals("123")){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                OpenHomePage() //Redirect to homepage when login successful

            }else{
                Toast.makeText(this, "Login Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun OpenHomePage(){
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }
}