package com.bignerdranch.android.criminalintent

class Zadanie1 {

    fun ticketCheck (ticketNumber: String): Boolean {
        val numberLength = ticketNumber.length

        when {
            ticketNumber.toIntOrNull() == null -> throw IllegalArgumentException ("Номер билета должен состоять из цифр")
            numberLength < 2 -> throw IllegalArgumentException ("Номер билета должен иметь не менне двух цифр")
            numberLength > 8 -> throw IllegalArgumentException ("Номер билета не должен иметь более восьми цифр")
            numberLength % 2 != 0 -> throw IllegalArgumentException("Номер билета должен состоять из четного числа цифр")
        }

        val listNumber: MutableList<Int> = mutableListOf()
        ticketNumber.forEach { listNumber.add(it.toString().toInt()) }
        val result = listNumber.chunked(numberLength/2) { it.sum() }
        return result.first() == result.last()
    }
}
