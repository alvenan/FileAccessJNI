package vendor.alvenan.fileaccessjni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import vendor.alvenan.fileaccessjni.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

        private lateinit var textBox: TextView
        private lateinit var filePath: EditText
        private lateinit var message: EditText
        private lateinit var writeBtn: Button
        private lateinit var readBtn: Button
        private lateinit var deleteBtn: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            textBox = findViewById(R.id.sample_text)
            filePath = findViewById(R.id.pathet)
            message = findViewById(R.id.textet)
            writeBtn = findViewById(R.id.writebtn)
            readBtn = findViewById(R.id.readbtn)
            deleteBtn = findViewById(R.id.deletebtn)
        }

        /**
         * A native method that is implemented by the 'fileaccessjni' native library,
         * which is packaged with this application.
         */
        external fun stringFromJNI(): String

        companion object {
            // Used to load the 'fileaccessjni' library on application startup.
            init {
                System.loadLibrary("fileaccessjni")
            }
        }
    }