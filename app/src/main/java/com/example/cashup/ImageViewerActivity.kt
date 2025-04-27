package com.example.cashup

//---------------------------------------- START OF IMPORTS -------------------------------------//
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//---------------------------------------- END OF IMPORTS -------------------------------------//

//***************************************************** START OF CODE ***********************************************************//

class ImageViewerActivity : AppCompatActivity() {

    // Companion object to hold constants like Intent extras
    companion object {
        const val EXTRA_IMAGE_URI = "IMAGE_URI" // Key for the image URI passed in the Intent
        private const val TAG = "ImageViewerActivity" // Tag for logging
    }

    //--------------------------- START OF CLASS BODY -------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        //------------------- UI ELEMENT INITIALIZATION ----------------------//
        val imageView = findViewById<ImageView>(R.id.fullscreen_image)
        val backButton = findViewById<ImageButton>(R.id.back_button_poe)
        //--------------------------------------------------------------------//

        //------------------- BACK BUTTON LISTENER ---------------------------//
        backButton.setOnClickListener {
            finish() // Close this activity when the back button is pressed
        }
        //--------------------------------------------------------------------//

        //------------------- INTENT DATA HANDLING ---------------------------//
        // Get the image URI string from the intent using the constant key
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)

        // ---> Logging to make sure the URI is received correctly <---
        Log.d(TAG, "Received URI string from Intent: $imageUriString")
        //--------------------------------------------------------------------//

        //------------------- IMAGE LOADING LOGIC ----------------------------//
        if (imageUriString != null) {
            try {
                val uri = Uri.parse(imageUriString)

                // Logging to make sure the URI is parsed correctly
                Log.d(TAG, "Parsed URI: $uri")

                // Set the image using the URI
                imageView.setImageURI(uri)

                // Checking if loading actually worked by verifying the drawable
                if (imageView.drawable == null) {
                    Toast.makeText(
                        this,
                        "Failed to load image. Check permissions or URI.",
                        Toast.LENGTH_LONG
                    ).show()
                    // ---> Logging <---
                    Log.e(
                        TAG,
                        "Drawable is null after setting URI: $uri. Checking stream access..."
                    )

                    // Attempt to access the content stream directly for diagnostics
                    try {
                        contentResolver.openInputStream(uri)?.use {
                            // Successfully opened stream, indicating the URI is likely valid but ImageView failed
                            Log.d(
                                TAG,
                                "Successfully opened input stream for URI: $uri"
                            )
                        }
                            ?: Log.e(
                                TAG,
                                "Input stream was null for URI: $uri"
                            ) // Stream was null
                    } catch (streamError: Exception) {
                        // Failed to open stream, indicating a problem with the URI or permissions
                        Log.e(
                            TAG,
                            "FAILED to open input stream for URI: $uri",
                            streamError
                        )
                    }
                    // ---> END OF ADDED CHECK <---

                } else {
                    // Image loaded successfully
                    Log.d(
                        TAG,
                        "Successfully set image URI and drawable is not null."
                    )
                }

            } catch (e: Exception) {
                // Catch potential errors during URI parsing or setting the image
                Log.e(TAG, "Error parsing URI or setting image", e)
                Toast.makeText(
                    this,
                    "Error displaying image. Invalid URI?",
                    Toast.LENGTH_LONG
                ).show()
                finish() // Close if there's an error loading
            }
        } else {
            // Handle the case where no URI was passed in the intent
            Toast.makeText(this, "No image URI provided", Toast.LENGTH_SHORT)
                .show()
            Log.e(TAG, "Intent did not contain $EXTRA_IMAGE_URI extra.")
            finish() // Close the activity as there's nothing to display
        }
        //--------------------------------------------------------------------//
    }
}
//***************************************************** END OF CODE ***********************************************************//
