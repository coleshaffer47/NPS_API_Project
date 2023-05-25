package com.example.npsapiproject

import android.content.Intent
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class NPSAdapter(var dataSet: NPSDataWrapper):
    RecyclerView.Adapter<NPSAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewNPS: TextView
        val textViewNumber: TextView
        val textViewEmail: TextView
        val layout: ConstraintLayout
        val userId: String

        init {
            textViewNPS = view.findViewById(R.id.textView_NPSItem_NPS)
            textViewEmail = view.findViewById(R.id.textView_NPSItem_email)
            textViewNumber = view.findViewById(R.id.textView_NPSItem_phoneNumber)
            layout = view.findViewById(R.id.layout_NPSItem)
            userId = NPSActivity.USER_ID
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_nps, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val NPS = dataSet.data[position]
        val contacts = NPS.contacts

        //NAME DISPLAY
        viewHolder.textViewNPS.text = NPS.fullName

        //PHONE NUMBER DISPLAY
        val voiceNums = contacts.phoneNumbers.filter { it.type == "Voice" }
        if(voiceNums.isNotEmpty() && voiceNums[0].phoneNumber.substring(3,4) != "-"
            && voiceNums[0].phoneNumber.substring(0,1) != "("
            && voiceNums[0].phoneNumber.substring(3,4) != "/") {
            viewHolder.textViewNumber.text =
                "Phone Number: (" + voiceNums[0].phoneNumber.substring(0, 3) + ") " +
                        voiceNums[0].phoneNumber.substring(3, 6) + "-" +
                        voiceNums[0].phoneNumber.substring(6)
        } else if(voiceNums.isNotEmpty()) {
            viewHolder.textViewNumber.text = "Phone Number: " + voiceNums[0].phoneNumber
        }
        else {
            viewHolder.textViewNumber.text = "(No Phone Number Available)"
        }

        //EMAIL DISPLAY
        val emailNums = contacts.emailAddresses.filter { it.description == "" }
        if(emailNums.isNotEmpty()) {
            viewHolder.textViewEmail.text =emailNums[0].emailAddress
        } else {
            viewHolder.textViewEmail.text = "(No Email Available)"
        }

        viewHolder.layout.setOnClickListener{
            val detailIntent = Intent(it.context, NPSDetailActivity::class.java)
            detailIntent.putExtra(NPSDetailActivity.EXTRA_FULLNAME, NPS)
            detailIntent.putExtra(NPSDetailActivity.EXTRA_USERID, viewHolder.userId)

            it.context.startActivity(detailIntent)
        }

    }


    override fun getItemCount(): Int {
        return dataSet.data.size
    }
}