package com.example.npsapiproject

import android.media.Image
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class NPSData(
    val activities: List<Actvity>,
    val parkCode: String,
    val name: String,
    val fullName: String,
    val description: String,
    val contacts: Contact,
    val directionsInfo: String,
    val directionsUrl: String,
    val entranceFees: List<EntranceFee>,
    val images: List<Image>,
    val entrancePasses: List<EntrancePass>,
    val operatingHours: List<OperatingHour>,
    val url: String,
    val weatherInfo: String
) : Parcelable {

    @Parcelize
    data class Actvity(
        val name: String
    ) : Parcelable

    @Parcelize
    data class Contact(
        val phoneNumbers: List<PhoneNumber>,
        val emailAddresses: List<EmailAddress>
    ) : Parcelable

    //this class is inside of the Contact class
    @Parcelize
    data class PhoneNumber(
        val phoneNumber: String,
        val type: String
    ) : Parcelable

    //this class is inside of the Contact class
    @Parcelize
    data class EmailAddress(
        val emailAddress: String,
        val description: String
    ) : Parcelable

    @Parcelize
    data class EntranceFee(
        val cost: String,
        val description: String,
        val title: String
    ) : Parcelable

    @Parcelize
    data class EntrancePass(
        val cost: String,
        val description: String,
        val title: String
    ) : Parcelable

    @Parcelize
    data class Image(
        val credit: String,
        val title: String,
        val altText: String,
        val caption: String,
        val url: String
    ) : Parcelable

    @Parcelize
    data class OperatingHour(
        val description: String,
        val standardHours: Map<String, String>,
    ) : Parcelable

}
