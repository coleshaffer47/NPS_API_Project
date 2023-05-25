package com.example.npsapiproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.npsapiproject.databinding.ActivityNpsDetail2Binding
import com.example.npsapiproject.databinding.ActivityNpsDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NPSDetailActivityFurther : AppCompatActivity() {
    private lateinit var binding: ActivityNpsDetail2Binding

    companion object {
        val EXTRA_FULLNAME = "National Park"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //onClicks opened or closed
        var directionsOnClick = false
        var hoursOnClick = false
        var feesOnClick = false
        var weatherOnClick = false
        var alertOnClick = false

        //visibility
        binding.textViewNpsdetail2DirectionsInfo.visibility = View.GONE
        binding.textViewNpsdetail2DirectionsUrl.visibility = View.GONE
        binding.textViewNpsdetail2DirectionsLink.visibility = View.GONE
        binding.textViewNpsdetail2OpenHours.visibility = View.GONE
        binding.textViewNpsdetail2HoursDescription.visibility = View.GONE
        binding.textViewNpsdetail2FeeType.visibility = View.GONE
        binding.textViewNpsdetail2RegularFees.visibility = View.GONE
        binding.textViewNpsdetail2EntrancePasses.visibility = View.GONE
        binding.textViewNpsdetail2EntrancePass.visibility = View.GONE
        binding.textViewNpsdetail2StandardHours.visibility = View.GONE
        binding.textViewNpsdetail2WeatherDescription.visibility = View.GONE
        binding.textViewNpsdetail2Alerts.visibility = View.GONE

        //variable
        val nationalPark = intent?.getParcelableExtra<NPSData>(EXTRA_FULLNAME)
        Log.d("NPS2", "onCreate: $nationalPark")

        //simple
        binding.textViewNpsdetail2FurtherInfo.text = "Detailed " + nationalPark?.name + " Information"
        binding.textViewNpsdetail2InfoDescription.text =
            "One of, if not the most important steps, to visiting a National Park " +
                    "is planning ahead. This section contains vital information " +
                    "regarding everything you need to know about planning a visit " +
                    "to " + nationalPark?.name + ". This includes Directional Information, " +
                    "Operating Hours, Park Fees, and more."
        binding.textViewNpsdetail2ForMoreInfo.text =
            "For more information about " + nationalPark?.name + ", please visit:"
        binding.textViewNpsdetail2Url.text = nationalPark?.url

        //contact information
        val checkPhoneNumber = nationalPark?.contacts?.phoneNumbers?.filter { it.type == "Voice" }
        if (checkPhoneNumber != null) {
            if(checkPhoneNumber.isNotEmpty() && checkPhoneNumber[0].phoneNumber.substring(3,4) != "-"
                && checkPhoneNumber[0].phoneNumber.substring(0,1) != "("
                && checkPhoneNumber[0].phoneNumber.substring(3,4) != "/") {
                binding.textViewNpsdetail2PhoneNumber.text =
                    "Phone Number: (" + checkPhoneNumber[0].phoneNumber.substring(0, 3) + ") " +
                            checkPhoneNumber[0].phoneNumber.substring(3, 6) + "-" +
                            checkPhoneNumber[0].phoneNumber.substring(6)
            } else if(checkPhoneNumber.isNotEmpty()) {
                binding.textViewNpsdetail2PhoneNumber.text = "Phone Number: " + checkPhoneNumber[0].phoneNumber
            } else {
                binding.textViewNpsdetail2PhoneNumber.text =  "Phone Number: (No Phone Number Available)"
            }
        }

        val checkEmail = nationalPark?.contacts?.emailAddresses?.filter { it.description == "" }
        if (checkEmail != null) {
            if(checkEmail.isNotEmpty()) {
                binding.textViewNpsdetail2Email.text = "Email Address: " + checkEmail[0].emailAddress
            } else {
                binding.textViewNpsdetail2Email.text =  "Email Address: (No Email Available)"
            }
        }

        //Directional Information
        binding.textViewNpsdetail2DirectionsTitle.setOnClickListener{
            if (directionsOnClick == false) {
                binding.textViewNpsdetail2DirectionsTitle.text = "Directional Information (-)"

                binding.textViewNpsdetail2DirectionsInfo.visibility = View.VISIBLE
                binding.textViewNpsdetail2DirectionsInfo.text = nationalPark?.directionsInfo

                binding.textViewNpsdetail2DirectionsUrl.visibility = View.VISIBLE
                binding.textViewNpsdetail2DirectionsUrl.text =
                    "For more directional information, please visit:"
                binding.textViewNpsdetail2DirectionsLink.visibility = View.VISIBLE
                binding.textViewNpsdetail2DirectionsLink.text = nationalPark?.directionsUrl
                directionsOnClick = true
            }
            else if (directionsOnClick == true) {
                binding.textViewNpsdetail2DirectionsTitle.text = "Directional Information (+)"

                binding.textViewNpsdetail2DirectionsInfo.visibility = View.GONE
                binding.textViewNpsdetail2DirectionsUrl.visibility = View.GONE
                binding.textViewNpsdetail2DirectionsLink.visibility = View.GONE


                directionsOnClick = false
            }
        }

        //Fee Information
        binding.textViewNpsdetail2FeesTitle.setOnClickListener {
            if (feesOnClick == false){
                binding.textViewNpsdetail2FeesTitle.text = "Park Fees (-)"
                binding.textViewNpsdetail2FeeType.visibility = View.VISIBLE
                binding.textViewNpsdetail2RegularFees.visibility = View.VISIBLE
                binding.textViewNpsdetail2EntrancePasses.visibility = View.VISIBLE
                binding.textViewNpsdetail2EntrancePass.visibility = View.VISIBLE

                var i = 0
                var feeString = ""
                while (i < nationalPark?.entranceFees?.size!!) {
                    feeString += "" + nationalPark.entranceFees[i].title + " ($" +
                            nationalPark.entranceFees[i].cost + ")\n\"" +
                            nationalPark.entranceFees[i].description + "\""
                    if (i != nationalPark.entranceFees.size - 1)
                        feeString += "\n\n"
                    i++
                }
                if (feeString != "")
                    binding.textViewNpsdetail2FeeType.text = feeString
                else
                    binding.textViewNpsdetail2FeeType.text = "(No information found, visit the " +
                            "National Parks website for more information. See the link at the " +
                            "bottom of the page.)"

                /////

                var k = 0
                var passString = ""
                while (k < nationalPark?.entrancePasses?.size!!) {
                    passString += "" + nationalPark.entrancePasses[k].title + " ($" +
                            nationalPark.entrancePasses[k].cost + ")\n\"" +
                            nationalPark.entrancePasses[k].description + "\""
                    if (k != nationalPark.entrancePasses.size - 1)
                        passString += "\n\n"
                    k++
                }
                if (passString != "")
                    binding.textViewNpsdetail2EntrancePass.text = passString
                else
                    binding.textViewNpsdetail2EntrancePass.text = "(No information found, visit the " +
                            "National Parks website for more information. See the link at the " +
                            "bottom of the page.)"

                /////

                feesOnClick = true
            }
            else if (feesOnClick == true) {
                binding.textViewNpsdetail2FeesTitle.text = "Park Fees (+)"

                binding.textViewNpsdetail2FeeType.visibility = View.GONE
                binding.textViewNpsdetail2RegularFees.visibility = View.GONE
                binding.textViewNpsdetail2EntrancePasses.visibility = View.GONE
                binding.textViewNpsdetail2EntrancePass.visibility = View.GONE

                feesOnClick = false
            }
        }

        //Operational Information
        binding.textViewNpsdetail2HoursTitle.setOnClickListener {
            if (hoursOnClick == false) {
                binding.textViewNpsdetail2HoursTitle.text = "Operating Hours (-)"

                binding.textViewNpsdetail2OpenHours.visibility = View.VISIBLE
                binding.textViewNpsdetail2OpenHours.text =
                    "Sunday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("sunday") +
                            "\nMonday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("monday") +
                            "\nTuesday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("tuesday") +
                            "\nWednesday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("wednesday") +
                            "\nThursday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("thursday") +
                            "\nFriday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("friday") +
                            "\nSaturday: " + nationalPark?.operatingHours?.get(0)?.standardHours?.get("saturday")

                binding.textViewNpsdetail2HoursDescription.visibility = View.VISIBLE
                binding.textViewNpsdetail2StandardHours.visibility = View.VISIBLE

                binding.textViewNpsdetail2HoursDescription.text = nationalPark?.operatingHours?.get(0)?.description

                hoursOnClick = true
            }
            else if (hoursOnClick == true) {
                binding.textViewNpsdetail2HoursTitle.text = "Operating Hours (+)"

                binding.textViewNpsdetail2OpenHours.visibility = View.GONE
                binding.textViewNpsdetail2HoursDescription.visibility = View.GONE
                binding.textViewNpsdetail2StandardHours.visibility = View.GONE

                hoursOnClick = false
            }
        }

        //Weather Information
        binding.textViewNpsdetail2WeatherTitle.setOnClickListener {
            if (weatherOnClick == false){
                binding.textViewNpsdetail2WeatherTitle.text = "Weather Expectations (-)"
                binding.textViewNpsdetail2WeatherDescription.visibility = View.VISIBLE

                binding.textViewNpsdetail2WeatherDescription.text = nationalPark?.weatherInfo

                weatherOnClick = true
            }
            else if (weatherOnClick == true) {
                binding.textViewNpsdetail2WeatherTitle.text = "Weather Expectations (+)"

                binding.textViewNpsdetail2WeatherDescription.visibility = View.GONE

                weatherOnClick = false
            }
        }

        //ALERTS
        nationalPark?.parkCode?.let { getAlertByParkCodeApiCall(it, 100, 1) }

        binding.textViewNpsdetail2AlertTitle.setOnClickListener {
            if (alertOnClick == false) {
                binding.textViewNpsdetail2AlertTitle.text = "ANNOUNCEMENTS (-)"
                binding.textViewNpsdetail2Alerts.visibility = View.VISIBLE

                nationalPark?.parkCode?.let { getAlertByParkCodeApiCall(it, 100, 2) }

                alertOnClick = true
            }
            else if (alertOnClick == true) {
                binding.textViewNpsdetail2AlertTitle.text = "ANNOUNCEMENTS (+)"
                binding.textViewNpsdetail2Alerts.visibility = View.GONE

                alertOnClick = false
            }
        }

    }

    private fun getAlertByParkCodeApiCall(parkCode: String, limit: Int, commander: Int) {
        val NPSDataService =
            RetrofitHelper.getInstance().create(NPSDataService::class.java)

        val NPSDataCall =
            NPSDataService.getAlerts( Constants.API_KEY, parkCode, limit)

        NPSDataCall.enqueue(object: Callback<NPSAlertDataWrapper> {
            override fun onResponse(call: Call<NPSAlertDataWrapper>, response: Response<NPSAlertDataWrapper>) {

                if (response.body() != null) {
                    //!! asserts that something that is nullables wont be null
                    Log.d("furtherdetail", "onResponseFurther: ${response.raw()}")
                    var a = 0
                    var alertDetailString = ""
                    while(a < response.body()!!.data.size) {
                        alertDetailString += response.body()!!.data[a].title + "\n\"" +
                                response.body()!!.data[a].description + "\"\n(Category: " +
                                response.body()!!.data[a].category + ")"
                        if (a != response.body()!!.data.size - 1)
                            alertDetailString += "\n------------\n"
                        a++
                    }

                    if (commander == 1) {
                        if (alertDetailString == "")
                            binding.textViewNpsdetail2AlertTitle.visibility = View.GONE
                        else
                            binding.textViewNpsdetail2AlertTitle.visibility = View.VISIBLE

                    }
                    else if (commander == 2)
                        binding.textViewNpsdetail2Alerts.text = alertDetailString
                }
            }

            override fun onFailure(call: Call<NPSAlertDataWrapper>, t: Throwable) {

                Log.d("furtherdetail", "onResponse: ${t.message}")

            }
        })
    }
}