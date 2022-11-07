@file:Suppress("DEPRECATION")

package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_TIME = "time"

class TimePickerFragment: DialogFragment() {

    interface Callbacks {
        fun onTimeSelected(time: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay: Int, minute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val resultTime: Date = calendar.time  //приводим результат к типу Date

            targetFragment?.let {
                (it as Callbacks).onTimeSelected(resultTime)
            }
        }

        val time = arguments?.getSerializable(ARG_TIME) as Date
       // val calendar = Calendar.getInstance()
        calendar.time = time
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(),
                                timeListener,
                                hourOfDay,
                                minute,
                                true) //true=показ в режиме 24час, false = в режиме PM/AM
    }

    companion object {
        fun newInstance(time: Date) : TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}