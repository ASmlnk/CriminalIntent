package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDetailViewModel() : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()   //Используем для добавление значения в LiveDate

    val crimeLiveData: LiveData<Crime?> = Transformations.switchMap(crimeIdLiveData) { crimeId ->
        crimeRepository.getCrime(crimeId)
    }
    //за crimeLiveData наблюдает CrimeFragment, c помощью
    // crimeRepository.getCrime(crimeId) идет запрос в БД
    // getCrime(crimeId) возвращает LiveData<Crime?>, т.е. принимается один объект ЛД, а возвращается другой
    //преобразование одной ЛД в другую с помощью значения crimeId
    //когда фрагмент сменит id, VM публикует новые данные о приступлении в существующем потоке данных

    fun  loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }
    //с помощью этой функции добавляем crimeId в LD, вызывается в onCreate CrimeFragment

    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}