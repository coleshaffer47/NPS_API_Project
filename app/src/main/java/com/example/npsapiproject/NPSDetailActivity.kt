package com.example.npsapiproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.npsapiproject.LoginActivity.Companion.EXTRA_USERNAME
import com.example.npsapiproject.databinding.ActivityNpsDetailBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NPSDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNpsDetailBinding
    var nationalPark : NPSData? = null

    companion object {
        val EXTRA_FULLNAME = "National Park"
        var EXTRA_USERID = "userid"
        val EXTRA_HASVISITED = false
    }

    var user: NPSAccount? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsDetailBinding.inflate(layoutInflater)
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID) ?: ""
//        EXTRA_USERID = userId
        setContentView(binding.root)


        //onClick opened or closed
        var toDoOnClick = false
        var collectOnClick = false

        //variable
        nationalPark = intent?.getParcelableExtra(EXTRA_FULLNAME)

        //visibility
        binding.textViewNpsdetailActivityDesc.visibility = View.GONE
        binding.textViewNpsdetailCollectionText.visibility = View.GONE
        binding.textViewNpsdetailNote.visibility = View.GONE
        binding.textViewNpsdetailNoteTitle.visibility = View.GONE
        binding.textViewNpsdetailDate.visibility = View.GONE
        binding.textViewNpsdetailDateTitle.visibility = View.GONE
        binding.textViewNpsdetailEdit.visibility = View.GONE
        binding.imageViewNpsdetailMyStamp.visibility = View.GONE

        //simple
        binding.textViewNpsdetailFullName.text = nationalPark?.fullName
        binding.textViewNpsdetailDescription.text = nationalPark?.description
        binding.textViewNpsdetailActivityTitle.text = "What Does " + nationalPark?.name + " Have To Offer? (+)"
        binding.textViewNpsdetailCollection.text = "Your " + nationalPark?.name + " Collection (+)"
        binding.textViewFormore.text =
            "For more information about " + nationalPark?.name + ", please visit:"
        binding.textViewUrl.text = nationalPark?.url


        binding.buttonNpsdetailDetailedInformation.setOnClickListener {
            val detailIntent = Intent(this, NPSDetailActivityFurther::class.java).apply {
                putExtra(EXTRA_FULLNAME, nationalPark)
            }

            it.context.startActivity(detailIntent)
        }

        //default image
        var imageNumber = 0
        Picasso.get().load(nationalPark!!.images[imageNumber].url).placeholder(R.drawable.ic_baseline_download_24).resize(450, 250)
            .into(binding.imageViewNpsdetailImages)
        binding.textViewNpsdetailImageTitle.text =
            "\"" + nationalPark!!.images[imageNumber].title + "\" " + nationalPark!!.images[imageNumber].credit
        binding.textViewNpsdetailImageDescription.text =
            nationalPark!!.images[imageNumber].altText + " || " + nationalPark!!.images[imageNumber].caption

        //previous image
        binding.textViewNpsdetailPrevious.setOnClickListener {
            imageNumber -= 1
            if(imageNumber < 0) {
                imageNumber = nationalPark?.images!!.size - 1
                Picasso.get().load(nationalPark!!.images[imageNumber].url).placeholder(R.drawable.ic_baseline_download_24).resize(450, 250)
                    .into(binding.imageViewNpsdetailImages)
                binding.textViewNpsdetailImageTitle.text =
                    "\"" + nationalPark!!.images[imageNumber].title + "\" " + nationalPark!!.images[imageNumber].credit
                binding.textViewNpsdetailImageDescription.text =
                    nationalPark!!.images[imageNumber].altText + " || " + nationalPark!!.images[imageNumber].caption

            }
            else {
                Picasso.get().load(nationalPark!!.images[imageNumber].url).placeholder(R.drawable.ic_baseline_download_24).resize(450, 250)
                    .into(binding.imageViewNpsdetailImages)
                binding.textViewNpsdetailImageTitle.text =
                    "\"" + nationalPark!!.images[imageNumber].title + "\" " + nationalPark!!.images[imageNumber].credit
                binding.textViewNpsdetailImageDescription.text =
                    nationalPark!!.images[imageNumber].altText + " || " + nationalPark!!.images[imageNumber].caption
            }
        }

        //next image
        binding.textViewNpsdetailNext.setOnClickListener {
            imageNumber += 1
            if(imageNumber > nationalPark?.images!!.size - 1) {
                imageNumber = 0
                Picasso.get().load(nationalPark!!.images[imageNumber].url).placeholder(R.drawable.ic_baseline_download_24).resize(450, 250)
                    .into(binding.imageViewNpsdetailImages)
                binding.textViewNpsdetailImageTitle.text =
                    "\"" + nationalPark!!.images[imageNumber].title + "\" " + nationalPark!!.images[imageNumber].credit
                binding.textViewNpsdetailImageDescription.text =
                    nationalPark!!.images[imageNumber].altText + " || " + nationalPark!!.images[imageNumber].caption
            }
            else {
                Picasso.get().load(nationalPark!!.images[imageNumber].url).placeholder(R.drawable.ic_baseline_download_24).resize(450, 250)
                    .into(binding.imageViewNpsdetailImages)
                binding.textViewNpsdetailImageTitle.text =
                    "\"" + nationalPark!!.images[imageNumber].title + "\" " + nationalPark!!.images[imageNumber].credit
                binding.textViewNpsdetailImageDescription.text =
                    nationalPark!!.images[imageNumber].altText + " || " + nationalPark!!.images[imageNumber].caption
            }

        }

        //Activity
        binding.textViewNpsdetailActivityTitle.setOnClickListener {
            if (toDoOnClick == false) {
                binding.textViewNpsdetailActivityTitle.text = "What Does " + nationalPark!!.name + " Have To Offer? (-)"
                binding.textViewNpsdetailActivityDesc.visibility = View.VISIBLE

                var w = 0
                var activityString = ""
                while(w < nationalPark!!.activities.size - 1) {
                    activityString += nationalPark!!.activities[w].name + ", "
                    w++
                }
                activityString += nationalPark!!.activities[w].name + " are all available at " + nationalPark!!.name + "."

                binding.textViewNpsdetailActivityDesc.text = activityString
                toDoOnClick = true
            }
            else {
                binding.textViewNpsdetailActivityTitle.text = "What Does " + nationalPark!!.name + " Have To Offer? (+)"
                binding.textViewNpsdetailActivityDesc.visibility = View.GONE
                toDoOnClick = false
            }
        }

        //Collection
        binding.textViewNpsdetailCollection.setOnClickListener {
            if (collectOnClick == false) {
                binding.textViewNpsdetailCollection.text = "Your " + nationalPark?.name + " Collection (-)"
                binding.textViewNpsdetailCollectionText.visibility = View.VISIBLE
                binding.textViewNpsdetailNote.visibility = View.VISIBLE
                binding.textViewNpsdetailNoteTitle.visibility = View.VISIBLE
                binding.textViewNpsdetailDate.visibility = View.VISIBLE
                binding.textViewNpsdetailDateTitle.visibility = View.VISIBLE
                binding.textViewNpsdetailEdit.visibility = View.VISIBLE
                binding.imageViewNpsdetailMyStamp.visibility = View.VISIBLE

                nationalPark?.parkCode?.let { getStampByParkCodeApiCall(it, 5) }
                var user = Backendless.UserService.CurrentUser()
                if(user != null) {

                    retrieveAllData(user.objectId)

//                    binding.textViewNpsdetailDate.text = "First Visit Date is yet \nto be set!"
//                    binding.textViewNpsdetailNote.text = "Notes about your visit are yet to be made!"
//                    binding.imageViewNpsdetailMyStamp.setImageResource(R.drawable.nps_wood_cookie_not_found)


                }

                collectOnClick = true
            }
            else {
                binding.textViewNpsdetailCollection.text = "Your " + nationalPark?.name + " Collection (+)"
                binding.textViewNpsdetailCollectionText.visibility = View.GONE
                binding.textViewNpsdetailNote.visibility = View.GONE
                binding.textViewNpsdetailNoteTitle.visibility = View.GONE
                binding.textViewNpsdetailDate.visibility = View.GONE
                binding.textViewNpsdetailDateTitle.visibility = View.GONE
                binding.textViewNpsdetailEdit.visibility = View.GONE
                binding.imageViewNpsdetailMyStamp.visibility = View.GONE

                collectOnClick = false
            }
        }

        binding.textViewNpsdetailEdit.setOnClickListener {
            if (Backendless.UserService.CurrentUser() == null) {
                Toast.makeText(this, "\uD83D\uDD34 You need to log-in to an account first!", Toast.LENGTH_LONG).show()
            }
            else {
                val detailIntentCollection = Intent(this, NPSCollection::class.java).apply {
                    putExtra(EXTRA_FULLNAME, nationalPark)
                }
                it.context.startActivity(detailIntentCollection)
            }

        }

    }

    private fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(NPSAccount::class.java).find(queryBuilder, object :
            AsyncCallback<List<NPSAccount?>?> {
            override fun handleResponse(foundData: List<NPSAccount?>?) {
                // every loaded object from the "Contact" table is now an individual java.util.Map
                //loans = foundLoans
                Log.d("detailActivity", "$foundData")

                user = foundData?.get(0)
                if (user?.hasBadge?.get(nationalPark!!.parkCode) == true) {
                    Log.d("detailActivity", "here")
                    binding.imageViewNpsdetailMyStamp.setImageResource(R.drawable.nps_wood_cookie)
                }
                else {
                    binding.imageViewNpsdetailMyStamp.setImageResource(R.drawable.nps_wood_cookie_not_found)
                }

                var note = user?.personalNote?.get(nationalPark!!.parkCode)
                if (note != null && note != "")
                    binding.textViewNpsdetailNote.text = note
                else {
                    binding.textViewNpsdetailNote.text = "Notes about your visit are yet to be made!"
                }

                var date = user?.dateVisited?.get(nationalPark!!.parkCode)
                if (date != null && date != "")
                    binding.textViewNpsdetailDate.text = date
                else {
                    binding.textViewNpsdetailDate.text = "First Visit Date is yet \nto be set!"
                }

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

    private fun getStampByParkCodeApiCall(parkCode: String, limit: Int) {
        val NPSDataService =
            RetrofitHelper.getInstance().create(NPSDataService::class.java)

        val NPSDataCall =
            NPSDataService.getStamps( Constants.API_KEY, parkCode, limit)

        NPSDataCall.enqueue(object: Callback<NPSStampDataWrapper> {
            override fun onResponse(call: Call<NPSStampDataWrapper>, response: Response<NPSStampDataWrapper>) {

                if (response.body() != null) {
                    //!! asserts that something that is nullables wont be null
                    Log.d("detail", "onResponse: ${response.raw()}")
                    var s = 0
                    var stampDetailString = "If you have a National Park Passport, " +
                            "you can get your collectable stamp at "
                    while(s < response.body()!!.data.size - 1) {
                        stampDetailString += response.body()!!.data[s].label + ", "
                        s++
                    }
                    if (response.body()!!.data.size == 1) {
                        stampDetailString += response.body()!!.data[s].label +
                                ". Other unlisted locations may be available as well."
                    }
                    else {
                        stampDetailString += "or " + response.body()!!.data[s].label +
                                ". Other unlisted locations may be available as well."
                    }

                    if (stampDetailString == "")
                        binding.textViewNpsdetailCollectionText.text =
                            "This National Park does not offer NPS Passport stamps."
                    else
                        binding.textViewNpsdetailCollectionText.text = stampDetailString

                }
            }

            override fun onFailure(call: Call<NPSStampDataWrapper>, t: Throwable) {

                Log.d("detail", "onResponse: ${t.message}")

            }
        })
    }
}