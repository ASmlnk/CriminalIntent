package com.bignerdranch.android.criminalintent

class Zadanie1 {

    fun ticketCheck (ticketNumber: Int): Boolean {
        val numberLength = ticketNumber.toString().length

        if (numberLength < 2) {
            throw IllegalArgumentException ("Номер билета должен иметь не менне двух цифр")
        } else if (numberLength > 8) {
            throw IllegalArgumentException ("Номер билета не должен иметь более восьми цифр")
        } else if (numberLength % 2 != 0) {
            throw IllegalArgumentException("Номер билета должен состоять из четного числа цифр")
        }

        val listNumber: MutableList<Int> = mutableListOf()
        var numberForLoop: Int = ticketNumber

        for (i in 1..numberLength) {
            listNumber.add(numberForLoop % 10)
            numberForLoop /= 10
        }
        val result = listNumber.chunked(numberLength/2) { it.sum() }
        return result.first() == result.last()
    }
}
