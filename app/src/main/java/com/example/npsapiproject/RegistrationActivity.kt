package com.example.npsapiproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.npsapiproject.databinding.ActivityNpsRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNpsRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieve any information from the intent using the extra's keys
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        // prefill the username & password fields
        // for EditTexts, you actually have to use the setText functions
        binding.editTextRegistrationUsername.setText(username)
        binding.editTextRegistrationPassword.setText(password)
        binding.textView4.text = "Create an account here to participate in the \"My Collection\" " +
                "section of each National Park!\n\n- Your Username must be at least 3 \ncharacters long and not already taken." +
                "\n- Your Password must be at least 8 \ncharacters long, have 1 at least digit, \nand 1 at least 1 capital letter." +
                "\n- Your email must be through Gmail \nand not already used for an account. \n\nIf account creation does" +
                " not work, check to make sure these criteria are all correct."

        // register an account
        binding.confirmButton.setOnClickListener {
            var canConfirm = RegistrationUtil.validateUsername(binding.editTextRegistrationUsername.text.toString()) &&
                    RegistrationUtil.validatePassword(binding.editTextRegistrationPassword.text.toString(),
                        binding.editTextRegistrationVerify.text.toString()) &&
                    RegistrationUtil.validateEmail(binding.editTextRegistrationEmail.text.toString())

            Log.d("RegistrationtActivity", "onCreate: Passwords ${RegistrationUtil.validatePassword(binding.editTextRegistrationPassword.text.toString(),
                binding.editTextRegistrationVerify.text.toString())}")
            Log.d("RegistrationtActivity", "onCreate: Username ${RegistrationUtil.validateUsername(binding.editTextRegistrationUsername.text.toString())}")
            Log.d("RegistrationtActivity", "onCreate: Email ${RegistrationUtil.validateEmail(binding.editTextRegistrationEmail.text.toString())}")

            // the apply lambda will call the functions inside it on the object
            // that apply is called on
            if (canConfirm) {

                val user = BackendlessUser()
                user.setProperty("username", binding.editTextRegistrationUsername.text.toString())
                user.setProperty("password", binding.editTextRegistrationPassword.text.toString())
                user.setProperty("email", binding.editTextRegistrationEmail.text.toString())
                Toast.makeText(this, "\uD83D\uDFE2 Account creation success!", Toast.LENGTH_LONG).show()


                Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        val account = NPSAccount()
                        account.ownerId = user.objectId
                        val npsaccount = mapOf(
                            "hasBadge" to account.hasBadge,
                            "dateVisited" to account.dateVisited,
                            "personalNote" to account.personalNote,
                            "objectId" to account.objectId,
                            "ownerId" to account.ownerId
                        )
                        Backendless.Data.of("NPSAccount").save(npsaccount, object :
                            AsyncCallback<Map<*, *>> {
                            override fun handleResponse(response: Map<*, *>) {
                                val resultIntent = Intent().apply {
                                    // apply { putExtra() } is doing the same thing as resultIntent.putExtra()
                                    putExtra(
                                        LoginActivity.EXTRA_USERNAME,
                                        binding.editTextRegistrationUsername.text.toString()

                                    )
                                    putExtra(
                                        LoginActivity.EXTRA_PASSWORD,
                                        binding.editTextRegistrationPassword.text.toString()
                                    )
                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d("NPS Collection", "handleFault: ${fault?.message}")
                            }

                        })

                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        Log.d(LoginActivity.TAG, "handleFault: ${fault?.message}")
                    }

                })

            }




            //need to figure out how to get user input into parameters
            /*if (!RegistrationUtil.validateName(binding.editTextTextPersonName.toString()))
                canConfirm = false
            if (RegistrationUtil.validateUsername(binding.editTextRegistrationUsername.toString()) == false)
                canConfirm = false
            if (RegistrationUtil.validateEmail(binding.editTextTextEmailAddress.toString()) == false)
                canConfirm = false
            if (RegistrationUtil.validatePassword(binding.editTextTextPassword.toString(),
                    binding.editTextTextPassword2.toString()) == false)
                canConfirm = false
            if (canConfirm == false)
                binding.invalidSubmission.setText("Something is invalid.")
            //change a textView to show what they may be missing

             */
        }


    }
}