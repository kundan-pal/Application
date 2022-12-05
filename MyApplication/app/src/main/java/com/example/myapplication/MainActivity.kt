package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException


// Declaring invariant in variable for request of camera access
private const val REQUEST_CODE : Int = 101

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting camera_Button access by its id
        val clickCamera = findViewById<Button>(R.id.camera_Button)
        // checking if camera permission is granted or not if not the we will ask for the permission at the start of the app
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
        }

        // when the camera button is clicked
        clickCamera.setOnClickListener {
//            Toast.makeText(this, "Camera button is clicked", Toast.LENGTH_SHORT).show()
            // again checking if the camera permission is given or not
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
            }
            else {
                // if the camera permission is given then start the camera for taking the picture
                val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takeIntent, REQUEST_CODE)
//                if(takeIntent.resolveActivity(packageManager)!=null){
//                    startActivityForResult(takeIntent, REQUEST_CODE)
//                }
            }
        }

//        println("function call to fetchJson")
//        fetchJson()
    }

    private fun fetchJson(){
        println("attempting to fetch json")
        Log.d("this", "attempting to fetch JSON")


//        val url = "https://api.letsbuildthatapp.com/youtube/home_feed"

//        val url = "https://jsonplaceholder.typicode.com/todos/1"

//        val url = "http://127.0.0.1:5000/returnjson"

//        val url = "http://10.0.2.2:8080"

        val url = "https://dcb3-2409-4073-119-f338-fc0b-5282-6515-e204.in.ngrok.io"


        val imageView = findViewById<ImageView>(R.id.imageView)
        println("extracted imageview")
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        println("created bitmap")
        val byteArrayOutputStream = ByteArrayOutputStream()
//        val bitmap = BitmapFactory.decodeResource(/* res = */ resources, /* id = */ R.drawable.ic_launcher_background)

//        println("created bitmap")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        println("created bitmap compress")
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        println("created imageBytes")
        val postBodyText: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        println("created string")
//        println(postBodyText)
//        val postBodyText = "Hello"

//        textView.text = imageString

//        val mediaType = MediaType.parse("text/plain; charset=utf-8")
        val mediaType = "text/plain; charset=utf-8".toMediaTypeOrNull()
        val postBody: RequestBody = RequestBody.create(mediaType, postBodyText)
        val request = Request.Builder()
            .url(url)
            .post(postBody)
            .build()

        val client = OkHttpClient()

        // we can not execute http request in the main thread so we need to use enqueue
//        client.newCall(request).execute()

        var body = "$"
        val textView = findViewById<TextView>(R.id.textView)
        println("Begin of functions")
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("trying to fetch json from the url")
                body = response.body?.string().toString()
                textView.text = body
                println(body)
            }
            override fun onFailure(call: Call, e: IOException) {
                textView.text = "Please Retry!"
                println("Failed to execute request")
            }
        })
//        if()
//
//        if(body != "$"){
//            textView.text = "hello world"
//        }


        println("End of the functions")
    }

    // optional function to make camera button enable if the permission is granted
    // because if permission is not granted and we try to start camera then app will crash
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val clickCamera = findViewById<Button>(R.id.camera_Button)
            clickCamera.isEnabled = true
        }
    }

    // after image is taken we display it in the imageview variable
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){  //means user had successfully taken picture from camera
            val imageTaken = data?.extras?.get("data") as Bitmap        // image we take comes as bitmap
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(imageTaken)

            println("function call to fetchJson")
            fetchJson()
            println("fetch json ended")
        }
        else{
            Toast.makeText(this, "Image is not taken", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}