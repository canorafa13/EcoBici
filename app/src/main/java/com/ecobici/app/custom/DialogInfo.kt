package com.ecobici.app.custom

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.ecobici.app.R
import com.ecobici.app.classes.CONST
import com.ecobici.app.classes.Stations
import com.ecobici.app.databinding.DialogFragmentBinding

class DialogInfo(context: Context, inflater: LayoutInflater, val action: Action) {
    private var binding: DialogFragmentBinding
    var dialog: AlertDialog? = null
    init {
        val builder = AlertDialog.Builder(context)
        val v: View = inflater.inflate(R.layout.dialog_fragment, null)
        binding = DialogFragmentBinding.bind(v)
        builder.setView(v)

        dialog = builder.create()
    }

    fun openInfoWindow(station: Stations) {
        /// Se muestra la pantalla de información
        binding.name.text = station.name
        binding.freeBikes.text = station.free_bikes.toString()
        binding.emptySlots.text = station.empty_slots.toString()
        binding.timestamp.text = station.timestamp.substring(0, 19)
        binding.address.text = "${station.extra.address}, C. P. ${station.extra.zip}"
        binding.openWaze.setOnClickListener {
            // Se intenta abrir una navegación con Waze
            val url = CONST.SERVER_WAZE + "?q=" + station.extra
                .address + "&ll=" + station.latitude + "," + station.longitude + "&navigate=yes"
            action.openWaze(url, "market://details?id=com.waze")
        }
        dialog?.show()
    }

    interface Action{
        fun openWaze(vararg url: String)
    }
}