package com.bignerdranch.android.criminalintent

data class Otrezok (val startX: Int, val startY: Int, val finishX: Int, val finishY: Int) {
}


fun main() {
    val listOtrezok: List<Otrezok> = listOf(
        Otrezok(15,0,15,3210),
        Otrezok(0,15,6000,15),
        Otrezok(1500,0,1500,3210),
        Otrezok(15,1015,1500,1015),
        Otrezok(15,2015,1500,2015),
        Otrezok(15,3015,1500,3015),
        Otrezok(2550,0,2550,3210),
        Otrezok(1500,1415,2550,1415),
        Otrezok(1500,2815,2550,2815),
        Otrezok(3991,0,3991,3210),
        Otrezok(2550,515,3991,515),
        Otrezok(2550,1015,3991,1015),
        Otrezok(2550,1515,3991,1515),
        Otrezok(2550,2015,3991,2015),
        Otrezok(2550,2765,3991,2765),
        Otrezok(3250,2015,3250,2765),
        Otrezok(4789,0,4789,3210),
        Otrezok(3991,1515,4789,1515),
        Otrezok(3991,3015,4789,3015),
        Otrezok(5843,0,5843,3210),
        Otrezok(4789,1123,5843,1123),
        Otrezok(5316,15,5316,1123))

    val groupOtrezokXStart = listOtrezok.groupBy { it.startX == it.finishX}
    val groupOtrezokX = groupOtrezokXStart[true]
    val groupOtrezokX1 = groupOtrezokX?.groupBy {it.startX}
    val groupOtrezokYStart = listOtrezok.groupBy { it.startY == it.finishY}
    val groupOtrezokY = groupOtrezokYStart[true]
    val groupOtrezokY1 = groupOtrezokY?.groupBy {it.startY}


    println(groupOtrezokY1)

    //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 1
}