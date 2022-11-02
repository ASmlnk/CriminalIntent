@file:Suppress("DEPRECATION")

package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment: DialogFragment() {

    interface Callbacks {                // интерфейс обратного вызова для передачи даты из диологового окна в CF
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener = DatePickerDialog.OnDateSetListener { _ : DatePicker, year: Int, month: Int, day: Int ->   //DatePicker  не используется, поэтому _
            val resultDate : Date = GregorianCalendar(year, month, day).time   // получаем объект Date из Calendar через св-во time
            targetFragment?.let { fragment ->            //тут находится экземпляр фрагмента который запустил диологовое окно, может быть  null
                (fragment as Callbacks).onDateSelected(resultDate)
            }
        } //слушатель для передачи даты в CF

        val date = arguments?.getSerializable(ARG_DATE) as Date //Полученые данные из аргументов приводим к типу Date
        val calendar = Calendar.getInstance()
        calendar.time = date                           //передаем полученые данные в объект Calendar
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(),    //контекстный объект необходимый для доступа к необходимым ресурсам
                                dateListener,         //слушатель дат
                                initialYear,
                                initialMonth,
                                initialDay)
    }

    companion object {
        //для передачи даты в DatePickerFragment через аргументы
        fun newInstance(date: Date) : DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}