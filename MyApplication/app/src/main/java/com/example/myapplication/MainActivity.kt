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

        // fetch camera_Button access by its id
        val clickCamera = findViewById<Button>(R.id.camera_Button)
        // checking if camera permission is granted or not if not the we will ask for the permission at the start of the app
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
        }

        // when the camera button is clicked
        clickCamera.setOnClickListener {
            // again checking if the camera permission is given or not in case user denies at the start
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
            }
            else {
                // if the camera permission is given then start the camera for taking the picture
                val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takeIntent, REQUEST_CODE)
            }
        }
    }

    private fun fetchJson(){
        println("attempting to fetch json")

        // a temporary url for testing if our okhttp client is able to send request and fetch the data
//        val url = "https://jsonplaceholder.typicode.com/todos/1"

        // declaring url variable which contains the url where we will send the post request.
        val url = "https://59ea-104-28-219-119.in.ngrok.io"


        // fetching the image taken from the imageview
        val imageView = findViewById<ImageView>(R.id.imageView)
        println("extracted imageview")
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        println("created bitmap")
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        println("created bitmap compress")
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        println("created imageBytes")
        val postBodyText: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        println("created Base64 image string")

//        println(postBodyText)
//        val postBodyText = "Hello"
//        textView.text = imageString
//        val mediaType = MediaType.parse("text/plain; charset=utf-8")

        // declaring post body
        val mediaType = "text/plain; charset=utf-8".toMediaTypeOrNull()
        val postBody: RequestBody = RequestBody.create(mediaType, postBodyText)
        val request = Request.Builder()
            .url(url)
            .post(postBody)
            .build()


        val client = OkHttpClient()

        // we can not execute http request in the main thread so we need to use enqueue

        var body = "$"
        val textView = findViewById<TextView>(R.id.textView)
        println("Begin of http request")
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("trying to fetch json object string from the url")
                body = response.body?.string().toString()
                textView.text = body
                println(body)
            }
            override fun onFailure(call: Call, e: IOException) {
                textView.text = "Please Retry!"
                println("Failed to execute request")
            }
        })
        println("End of the function")
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

    // after image is taken we display it in the imageview variable and make http request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){     //means user had successfully taken picture from camera
            val imageTaken = data?.extras?.get("data") as Bitmap        // image we take comes as bitmap
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(imageTaken)

            println("Calling FetchJson function _____")
            fetchJson()
            println("fetchJson function ended _____")
        }
        else{
            Toast.makeText(this, "Image is not taken", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}