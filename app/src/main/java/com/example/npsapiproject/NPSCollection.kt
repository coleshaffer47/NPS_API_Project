package com.example.npsapiproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.backendless.persistence.JSONUpdateBuilder
import com.example.npsapiproject.databinding.ActivityNpsCollectionBinding


class NPSCollection : AppCompatActivity() {
    private lateinit var binding: ActivityNpsCollectionBinding
    var park: NPSAccount = NPSAccount()
    var nationalPark: NPSData? = null

    companion object {
        var EXTRA_USERID = "userid"
        var EXTRA_FULLNAME = "National Park"
        var EXTRA_BADGE = false
    }

    var user: NPSAccount? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID) ?: ""
        //park.makeMap()
        var user = Backendless.UserService.CurrentUser()
        var isToggled : Boolean


        //variable
        nationalPark = intent?.getParcelableExtra<NPSData>(NPSDetailActivity.EXTRA_FULLNAME)
        retrieveAllData(user.objectId)


        //simple
        binding.toggleButtonNpscollectionHasVisited.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) buttonView.setBackgroundColor(
                Color.rgb(
                    68,
                    156,
                    71
                )
            ) else buttonView.setBackgroundColor(
                Color.rgb(124, 94, 64)
            )

        })

        binding.textViewNpscollectionDescription.text = "Welcome to the account collection page! " +
                "Here, you can update all of your personal information about " + nationalPark?.fullName +
                "! You can leave personal notes about your visit in any ways you want. Leave a date for" +
                " the first time you visited the park, and hit the \"PARK UNVISITED\" switch to earn your" +
                " NPS Exploration Guide travel stamp! \nCollect them all!"
        binding.textViewNpscollectionTitle.text = "My " + nationalPark?.name + " Collection"

        binding.toggleButtonNpscollectionHasVisited.textOn = "   I've visited " +
                nationalPark?.name + "!   "


        //"Sunday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("sunday")

        //save
        binding.buttonCollectionApply.setOnClickListener {
            if (park.hasBadge == null) {
                park.hasBadge = mutableMapOf()
            }
            if (park.dateVisited == null) {
                park.dateVisited = mutableMapOf()

            }
            if (park.personalNote == null) {
                park.personalNote = mutableMapOf()
            }
            park.hasBadge?.put(
                nationalPark?.parkCode.toString(),
                binding.toggleButtonNpscollectionHasVisited.isChecked
            )
            park.dateVisited?.put(
                nationalPark?.parkCode.toString(),
                binding.editTextDateNpscollectionFirstVisit.text.toString()
            )
            park.personalNote?.put(
                nationalPark?.parkCode.toString(),
                binding.editTextTextMultiLineNpscollectionPersonalNotes.text.toString()
            )
            Log.d("NPS Collection", "onCreate Save Pressed:  $park")

//            val hasBadge = JSONUpdateBuilder.SET()
//                .addArgument()
//
            val npsaccount = mapOf(
                "hasBadge" to park.hasBadge,
                "dateVisited" to park.dateVisited,
                "personalNote" to park.personalNote,
                "objectId" to park.objectId,
                "ownerId" to park.ownerId
            )

            Backendless.Data.of("NPSAccount").save(npsaccount, object :
                AsyncCallback<Map<*, *>> {
                override fun handleResponse(response: Map<*, *>) {
                    Log.d("NPS Collection", "handleResponse: $response")
                    // set the result to ok and then pass along the updated
                    // park object in the intent

                    val detailIntent = Intent(this@NPSCollection, NPSActivity::class.java)
                    it.context.startActivity(detailIntent)
                    Toast.makeText(this@NPSCollection, "\uD83D\uDFE2 ${nationalPark?.name} collection info updated!", Toast.LENGTH_LONG).show()

                    finish()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    Log.d("NPS Collection", "handleFault: ${fault?.message}")
                }

            })

//            Backendless.Persistence.of("NPSAccount").save(park, object :
//                AsyncCallback<NPSAccount> {
//                override fun handleResponse(response: NPSAccount?) {
//                    Log.d("NPS Collection", "handleResponse: $response")
//                    finish()
//                }
//
//                override fun handleFault(fault: BackendlessFault?) {
//                    Log.d("NPS Collection", "handleFault: ${fault?.message}")
//                }
//
//            })
        }
    }

    fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(NPSAccount::class.java).find(queryBuilder, object :
            AsyncCallback<List<NPSAccount?>?> {
            override fun handleResponse(foundData: List<NPSAccount?>?) {
                // every loaded object from the "Contact" table is now an individual java.util.Map
                //loans = foundLoans
                Log.d("NPSCollection", "$foundData nationalPark: $nationalPark")
                user = foundData?.get(0)
                if(user != null) {
                    park = user!!
                }

                var date = user?.dateVisited?.get(nationalPark!!.parkCode)
                if (date != null)
                    binding.editTextDateNpscollectionFirstVisit.setText(date)

                var note = user?.personalNote?.get(nationalPark!!.parkCode)
                if (note != null)
                    binding.editTextTextMultiLineNpscollectionPersonalNotes.setText(note)

                binding.toggleButtonNpscollectionHasVisited.isChecked = user?.hasBadge?.get(nationalPark!!.parkCode) == true



//                binding.textViewNpsdetailDate.text = "First Visit Date is yet \nto be set!"
//                binding.textViewNpsdetailNote.text = "Notes about your visit are yet to be made!"
//                binding.imageViewNpsdetailMyStamp.setImageResource(R.drawable.nps_wood_cookie_not_found)

            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(LoginActivity.TAG, "handleFault: ${fault.message}")
            }
        })
    }
}