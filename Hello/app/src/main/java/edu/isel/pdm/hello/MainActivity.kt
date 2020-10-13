package edu.isel.pdm.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("HELLO_APP", "onCreate() on instance ${hashCode()}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("HELLO_APP", "onStart() on instance ${hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("HELLO_APP", "onResume() on instance ${hashCode()}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("HELLO_APP", "onPause() on instance ${hashCode()}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("HELLO_APP", "onStop() on instance ${hashCode()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HELLO_APP", "onDestroy() on instance ${hashCode()}")
    }
}