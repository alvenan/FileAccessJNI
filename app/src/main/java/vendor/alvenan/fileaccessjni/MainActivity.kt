package vendor.alvenan.fileaccessjni

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

            writeBtn.setOnClickListener {
                if (checkPermission()) {
                    textBox.text = stringFromJNI()
                } else {
                    Log.i(TAG,"Acesso ao armazenamento não liberado, solicitando.")
                    requestPermission()
                }
            }

            readBtn.setOnClickListener {
                if (checkPermission()) {
                    textBox.text = stringFromJNI()
                } else {
                    Log.i(TAG,"Acesso ao armazenamento não liberado, solicitando.")
                    requestPermission()
                }
            }

            deleteBtn.setOnClickListener {
                if (checkPermission()) {
                    textBox.text = stringFromJNI()
                } else {
                    Log.i(TAG,"Acesso ao armazenamento não liberado, solicitando.")
                    requestPermission()
                }
            }
        }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager()
        else {
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        Log.d(TAG, "Solicitando permissão")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            intent.data = Uri.fromParts("package", this.packageName, null)
            storageActivityResultLauncher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_CODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val storageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i(TAG, "storageActivityResultLauncher: ")
            if (Environment.isExternalStorageManager()) {
                Log.i(TAG,"storageActivityResultLauncher:  Permissão de acesso ao armazenamento concedida.")
            } else {
                Log.i(TAG,"storageActivityResultLauncher: Permissão de acesso ao armazenamento negada....")
                Toast.makeText(this, "Permissão de acesso ao armazenamento negada....", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out   String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read) Log.d(TAG, "onRequestPermissionsResult:   Permissão de acesso ao armazenamento concedida.")
                else {
                    Log.d(TAG, "onRequestPermissionsResult: Permissão de acesso ao armazenamento negada....")
                    Toast.makeText(this, "Permissão de acesso ao armazenamento negada....", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /**
         * A native method that is implemented by the 'fileaccessjni' native library,
         * which is packaged with this application.
         */
        external fun stringFromJNI(): String

        companion object {
            private const val STORAGE_PERMISSION_CODE = 100
            private const val TAG = "FileAccessApp - Java"
            // Used to load the 'fileaccessjni' library on application startup.
            init {
                System.loadLibrary("fileaccessjni")
            }
        }
    }