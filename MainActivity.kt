package com.example.dannyhf

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startVoiceRecognition()
    }

    private fun startVoiceRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nl-NL")
        }

        speechRecognizer.setRecognitionListener(object : android.speech.RecognitionListener {
            override fun onResults(results: Bundle) {
                val spokenText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)?.lowercase(Locale.ROOT)
                processCommand(spokenText)
                startVoiceRecognition() // blijf luisteren
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                startVoiceRecognition()
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    private fun processCommand(command: String?) {
        when {
            command == null -> return
            "open youtube" in command -> {
                startActivity(packageManager.getLaunchIntentForPackage("com.google.android.youtube"))
            }
            "bel mama" in command -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = android.net.Uri.parse("tel:0612345678")
                startActivity(callIntent)
            }
            "start camera" in command -> {
                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                startActivity(intent)
            }
            else -> Toast.makeText(this, "Onbekend commando: $command", Toast.LENGTH_SHORT).show()
        }
    }
}
