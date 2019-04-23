package com.example.saveexternal

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button
    private lateinit var txtUser: EditText
    private lateinit var txtPass: EditText
    private lateinit var lblUser: TextView
    private lateinit var lblPass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave = btn_save
        btnLoad = btn_load
        txtUser = txt_user
        txtPass = txt_pass
        lblUser = lbl_user
        lblPass = lbl_pass

        btnSave.setOnClickListener{
            if(isExternalAvailable() && checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                try {
                    var textFile = File(Environment.getExternalStorageDirectory(), "credenciales.txt")
                    var fos = FileOutputStream(textFile)
                    var string = txtUser.text.toString() + "," + txtPass.text.toString()
                    fos.write(string.toByteArray())
                    fos.close()

                    Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show()

                }catch (e: IOException){
                    println(e)
                }
            }else{
                Toast.makeText(this, "Canout use External Storage", Toast.LENGTH_SHORT).show()

            }
        }

        btnLoad.setOnClickListener{

            if(isExternalAvailable() && checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                var sb = StringBuilder()
                try {
                    var textFile = File(Environment.getExternalStorageDirectory(), "credenciales.txt")
                    var fis = FileInputStream(textFile)

                    if(fis != null){
                        var isr = InputStreamReader(fis)
                        var buff = BufferedReader(isr)

                        sb.append(buff.readLine())
                    }

                    fis.close()

                    val strings = sb.split(",")

                    lblUser.text = strings[0]
                    lblPass.text = strings[1]

                    Toast.makeText(this, "Data Collected", Toast.LENGTH_SHORT).show()

                }catch (e: IOException){
                    println(e)
                }
            }else{
                Toast.makeText(this, "Canout use External Storage", Toast.LENGTH_SHORT).show()

            }

        }

    }

    fun isExternalAvailable(): Boolean{
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true
        }else{
            return false
        }
    }

    fun checkPermission(permission: String): Boolean{
        var check = ContextCompat.checkSelfPermission(this,permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }

}
