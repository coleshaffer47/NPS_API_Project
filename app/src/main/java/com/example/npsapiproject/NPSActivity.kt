package com.example.npsapiproject

import android.content.Intent
import android.net.wifi.rtt.CivicLocationKeys.STATE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.example.npsapiproject.databinding.ActivityNpsListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NPSActivity : AppCompatActivity() {
    companion object {
        const val TAG = "NPSActivity"
        var USER_ID = "userId"
    }
    private lateinit var binding: ActivityNpsListBinding
    lateinit var adapter: NPSAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getNationalParkbyParkCodeApiCall(Constants.PARK_LIST.joinToString(","), Constants.NUM_PARKS)

    }

    override fun onStart() {
        super.onStart()
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID) ?: ""
        //USER_ID = userId
    }

    private fun getNationalParkbyParkCodeApiCall(parkCode: String, limit: Int) {
        val NPSDataService =
            RetrofitHelper.getInstance().create(NPSDataService::class.java)

        val NPSDataCall =
            NPSDataService.getNationalPark( Constants.API_KEY, parkCode, limit)

        NPSDataCall.enqueue(object: Callback<NPSDataWrapper> {
            override fun onResponse(call: Call<NPSDataWrapper>, response: Response<NPSDataWrapper>) {
                Log.d(TAG, "onResponse: ${response.body()?.data?.filter { it.fullName.contains("Big") }}")

                if (response.body() != null) {
                    //!! asserts that something that is nullables wont be null

                    adapter = NPSAdapter(response.body()!!)
                    binding.recyclerViewNPSList.adapter = adapter
                    binding.recyclerViewNPSList.layoutManager =
                        LinearLayoutManager(this@NPSActivity)
                } else {
                    Log.d(TAG, "Body is NULL")
                }
            }

            override fun onFailure(call: Call<NPSDataWrapper>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nps_menu, menu)
        return true
    }

    private fun displayInfoDialogue() {
        AlertDialog.Builder(this)
            .setTitle("Welcome to the National Parks Service Exploration Guide!")
            .setMessage("In this list of all major 63 National Parks, you'll find " +
                    "all kinds of information and resources needed to either plan a " +
                    "visit, or simply learn more about the park.\n\nEach park has a " +
                    "detailed description section, which includes necessary travel info," +
                    " like park fees, hours of operation, real-time park alerts, and more." +
                    "\n\nIf you create an account (found in the top right corner), you can use" +
                    " the \"Collections\" tab found in each park's page. There you can leave notes " +
                    "about your visit, the date you first visited it, and you can even earn a " +
                    "collectible badge if you've visited the park!")
            .setPositiveButton("OKAY!", {dialog, id -> })
            .create()
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        val id = item.itemId
        if (id == R.id.nps_menu_about) {
            displayInfoDialogue()
            return true
        }
        else if (id == R.id.nps_menu_login) {
            if(Backendless.UserService.CurrentUser() == null)
                    launchLogin()
                else
                    Toast.makeText(this, "\uD83D\uDD34 You are already logged in!\nLogging out will be available one day...", Toast.LENGTH_LONG).show()

                true
        }
        return super.onOptionsItemSelected(item)
//        return when (item.itemId) {
//            R.id.nps_menu_login -> {
//                if(Backendless.UserService.CurrentUser() == null)
//                    launchLogin()
//                else
//                    Toast.makeText(this, "\uD83D\uDD34 You are already logged in!", Toast.LENGTH_LONG).show()
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
    }

    private fun launchLogin() {
        val detailIntent = Intent(this, LoginActivity::class.java)

        startActivity(detailIntent)
        finish()
    }
}

