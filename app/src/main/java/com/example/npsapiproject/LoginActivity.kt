package com.example.npsapiproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.npsapiproject.databinding.ActivityNpsLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        // the values to send in intents are called Extras
        // and have the EXTRA_BLAH format for naming the key
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val EXTRA_USERID = "userID"
        val TAG = "loginActivity"
    }

    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent to do whatever we need with the returned info
            binding.editTextLoginUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(EXTRA_PASSWORD))
        }
    }

    private lateinit var binding: ActivityNpsLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNpsLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.BACKENDLESS_API_KEY)

        // logging in to backendless
        binding.buttonLoginLogin.setOnClickListener {
            // do not forget to call Backendless.initApp in the app initialization code

            // do not forget to call Backendless.initApp in the app initialization code
            Backendless.UserService.login(
                binding.editTextLoginUsername.text.toString(),
                binding.editTextLoginPassword.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in

                        if (user != null) {
                            launchNPSActivity(user)
                        }
                        Log.d(TAG, "handleResponse: ${user?.getProperty("username")}")
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d(TAG, "handleFault: ${fault.message}")
                    }
                })

        }

        //launch the registration activity
        binding.textViewLoginSignup.setOnClickListener {
            // 1. Create an intent object with the current activity and the
            // destination activity's class.
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            // 2. Optionally add information to send with the intent
            // ley value pairs just like with bundles
            registrationIntent.putExtra(EXTRA_USERNAME,
                binding.editTextLoginUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD,
                binding.editTextLoginPassword.text.toString())

            // 3a. Launch the new activity using the intent
            //startActivity(registrationIntent)
            // 3b. Launch the activity for a result using the variable from the
            // register for result contract above
            startRegistrationForResult.launch(registrationIntent)
        }

    }

    private fun launchNPSActivity(user: BackendlessUser?) {
        val npsIntent = Intent(this, NPSActivity::class.java)

        if(user != null) {
            val userId = user.objectId
            npsIntent.putExtra(EXTRA_USERID, userId)
            Toast.makeText(this, "\uD83D\uDFE2 Welcome back, ${binding.editTextLoginUsername.text}!", Toast.LENGTH_LONG).show()

        }

        startRegistrationForResult.launch(npsIntent)
        finish()
    }

    override fun onBackPressed() {
        launchNPSActivity(null)
        super.onBackPressed()
    }
}