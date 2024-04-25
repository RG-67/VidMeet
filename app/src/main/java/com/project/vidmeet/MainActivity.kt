package com.project.vidmeet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.project.vidmeet.databinding.ActivityMainBinding
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        val serverUrl = URL("https://meet.jit.si")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {
            val defaultOption: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOption)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        binding.createOrJoinBtn.setOnClickListener {
            if (nullCheck(it)) {
                val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                    .setRoom(binding.edtCode.text.toString())
                    .setFeatureFlag("invite.enabled", false)
                    .build()
                JitsiMeetActivity.launch(this, options)
            }
        }

        binding.shareBtn.setOnClickListener {
            if (nullCheck(it)) {
                val intent = Intent()
                intent.setAction(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Enter this code to join the meeting: " + binding.edtCode.text.toString()
                )
                intent.setType("text/plain")
                startActivity(intent)
            }
        }

    }

    private fun nullCheck(view: View): Boolean {
        return if (binding.edtCode.text.toString().isNotEmpty()) {
            true
        } else {
            binding.edtCode.error = ""
            Snackbar.make(view, "Enter code", Snackbar.LENGTH_SHORT).show()
            false
        }
    }

}