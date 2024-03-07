package com.example.mybot
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mybot.databinding.ActivityMainBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private val apiKey = "apikey"
    private lateinit var binding: ActivityMainBinding
    private lateinit var generativeModel:GenerativeModel // Placeholder for GenerativeModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generativeModel = GenerativeModel(modelName = "gemini-pro", apiKey = apiKey)
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("My Bot")
        progressDialog.setMessage("Generating...")
        binding.generateBtn.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

            if (binding.etPrompt.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter text please", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                lifecycleScope.launch {
                    try {
                        val prompt = binding.etPrompt.text.toString()
                        val response = withContext(Dispatchers.IO) {
                            generativeModel.generateContent(prompt)
                        }
                        progressDialog.hide()
                        binding.promptAnswer.text = response.text
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        binding.cleanBtn.setOnClickListener {
            binding.etPrompt.text.clear()
        }

        binding.copyBtn.setOnClickListener {
            val textToCopy = binding.promptAnswer.text.toString()
            if (textToCopy.isEmpty()) {
                Toast.makeText(this, "No text to copy", Toast.LENGTH_SHORT).show()
            } else {
                copyToClipboard(this, textToCopy)
            }
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
