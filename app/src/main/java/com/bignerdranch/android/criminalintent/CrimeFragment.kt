@file:Suppress("DEPRECATION")

package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 1
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val REQUEST_PHOTO_BIG = 2
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment: Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var crime: Crime
    private lateinit var photoFile: File
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
    private lateinit var callTheViolator: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoUri: Uri
    //private lateinit var observer: ViewTreeObserver
    private val photoSize = mutableMapOf<String, Int>( "width" to 0, "height" to 0 )


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
        suspectButton = view.findViewById(R.id.crime_suspect) as Button
        callTheViolator = view.findViewById(R.id.call_the_violator) as Button
        photoButton = view.findViewById(R.id.crime_camera) as ImageButton
        photoView = view.findViewById(R.id.crime_photo) as ImageView

        //observer = photoView.viewTreeObserver

        val observer = photoView.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                photoView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val width = photoView.width
                val height = photoView.measuredHeight
                updateSize(width, height)

                Log.d("My", "width = $width heidht = $height")

            }
        })









        /*observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                observer.removeOnGlobalLayoutListener(this)

                val width = photoView.width
                val height = photoView.measuredHeight

                Log.d("My", "width = $width heidht = $height")

            }
            })*/


            /*observer.removeOnGlobalLayoutListener{this}
            val width = photoView.width
            Log.d("My", "width = $width heidht = $height")
            //updateSize(width, height)
            val height = photoView.measuredHeight*/




        Log.d("My", "width = ${photoView.width} heidht = ")

















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
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent ->
                        /*startActivity(intent)*/    //значение по умолчанию
                        val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report)) //всегда преставляется возможность выбора внешней активити
                        startActivity(chooserIntent)
            }
        }
        callTheViolator.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + crime.phoneNumber)
            startActivity(intent)
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
                photoFile = crimeDetailViewModel.getPhotoFile(crime)
                photoUri = FileProvider.getUriForFile(requireActivity(), "com.bignerdranch.android.criminalintent.fileprovider", photoFile)
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
        suspectButton.apply {
            val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)  /*ContactsContract.Contacts.CONTENT_URI*/
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }

            val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
        }
        photoButton.apply {
            /*Проверка на наличие приложения камеры в ОС*/
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

            /*Если приложения камеры нет кнопка заблокируется*/
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                /*Дополниние с указанием пути к файлу*/
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                /*Для каждой активити которая может обработать интент captureImage устанавливаем флаг Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                * т.е. даем разрешение запасывать Uri (photoUri)*/
                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
        photoView.apply {
            setOnClickListener {
                if (photoFile.exists()) {
                    PhotoDialog.newInstance(photoFile).apply {
                        show(this@CrimeFragment.requireFragmentManager(), "dialog")
                    }
                }
            }
        }


    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()  //пропуск анимации флажка при повароте экрана
        if (crime.suspect.isNotEmpty()) {
            suspectButton.text = crime.suspect
            }
            if (crime.phoneNumber.isNotEmpty()) {
                callTheViolator.text = crime.phoneNumber
            }
        }

        updatePhotoView()

    }

    private fun updatePhotoView() {




        if (photoFile.exists()) {

            val width: Int = photoSize["width"]!!
            val height = photoSize["height"]!!


            val bitmap = getScaledBitmap(photoFile.path, width, height ) /*requireActivity()*/
            val matrix: Matrix = Matrix()
            if (bitmap.height < bitmap.width) {
                matrix.postRotate(90F)
            } else {
                matrix.postRotate(0F)
            }
            val b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            photoView.setImageBitmap(b)

        } else {
            photoView.setImageDrawable(null)
        }
    }

    private fun updateSize(width: Int, height: Int){
        photoSize["width"] = width
        photoSize["height"] = height

    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {

            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                //запрашиваем БД контактов и получаем объект курсор
                val contactUri: Uri? = data.data
                //Указать для каких полей ваш запрос должен возвращать значение.
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
                //Выполняемый здесь запрос - contactUri похож на предложение where
                val cursor = requireActivity().contentResolver.query(contactUri!!, queryFields, null, null, null)
                cursor?.use {
                    //Проверяем, что курсор содержит хотя бы один результат
                    if (it.count == 0) {
                        return
                    }
                    /*Первый столбец первой строки данных -
                            это имя вашего подазреваемого*/
                    it.moveToFirst()    //перемещает курсор в первую строку
                    val suspect = it.getString(0)  //получаем содержимое первого столбца в виде строки

                    val phoneNumber = it.getString(1)
                    
                    crime.phoneNumber = phoneNumber
                                                                    //эта строка будет именем подозреваемого
                    crime.suspect = suspect                     //вставляем эту строку в текст кнопки
                    crimeDetailViewModel.saveCrime(crime)        //полученую информацию сохроняем в нашей БД
                    suspectButton.text = suspect
                    callTheViolator.text = phoneNumber

                }
            }
            resultCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
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
            getString(R.string.crime_report_suspect, crime.suspect)
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


