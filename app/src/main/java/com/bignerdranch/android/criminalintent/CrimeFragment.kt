@file:Suppress("DEPRECATION")

package com.bignerdranch.android.criminalintent

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 1
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment: Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var reportButton: Button

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this) [CrimeDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        // пришел из хост активити через функцию newInstance(crimeId: UUID)
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        timeButton = view.findViewById(R.id.crime_time) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        reportButton = view.findViewById(R.id.crime_report) as Button


        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {           //передаем дату а диологовое окно
            setTargetFragment(this@CrimeFragment, REQUEST_DATE)  //назначаем CrimeFragment целевым фрагментом для DPF
            show(this@CrimeFragment.requireFragmentManager() , DIALOG_DATE)
            }                                                     //появляется диалоговое окно с выбором даты
        }
        timeButton.setOnClickListener {
            TimePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_TIME)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }
        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT,
                         getString(R.string.crime_report_subject))
            }.also { intent ->
                        /*startActivity(intent)*/    //значение по умолчанию
                        val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report)) //всегда преставляется возможность выбора внешней активити
                        startActivity(chooserIntent)
            }
        }

        /*dateButton.apply {         Блокировка кнопки
            text = crime.date.toString()
            isEnabled = false
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { crime ->
            crime?.let {
                this.crime = crime
                updateUI()
            }   //наблюдаем за ЛД, придет объект crime из БД по id и
                // с помощью updateUI заполним представление
        })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()  //пропуск анимации флажка при повароте экрана
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        var suspect = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect)
        }

        return getString(R.string.crime_report,
                         crime.title,
                         dateString,
                         solvedString,
                         suspect)
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }  //Реализация интерфейса Callbacks из DPF
    override fun onTimeSelected(time: Date) {
        crime.date = time
        updateUI()
    }
}

