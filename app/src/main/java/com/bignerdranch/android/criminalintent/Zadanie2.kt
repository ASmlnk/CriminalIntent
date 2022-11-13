package com.bignerdranch.android.criminalintent

class Zadanie2 {
}
data class Order (val startingPoint: String,      //начальная точка
                  val endPoint: String,
                  val cargoVolumeM3: Int? = null,    //обьем груза
                  val cargoWeightKg: Int? = null,     //вес груза
                  val cargoType: String? = null,      //тип груза
                  val numberOfPassengers: Int? = null) {    //количество пасажиров


}

abstract class Transport {
    abstract val brand: String   //бренд
    abstract val model: String   //модель
    abstract val yearOfIssue: Int  //год выпуска
    abstract val typeOfFuel: String  //тип топлива
    abstract val fuelConsumption: Double  //расход топлива
    abstract val orders: MutableList<Order>
   // abstract fun refuel()    //заправить
   // abstract fun repair()    //отремонтировать
   // abstract fun placeAnOrder ()  //разместить заказ
}

//грузовой транспорт
class FreightTransport(override val brand: String,
                       override val model: String,
                       override val yearOfIssue: Int,
                       override val typeOfFuel: String,
                       override val fuelConsumption: Double,
                       val bodyType: String,
                       val bodyVolume: Int,                                //обьем груза
                       val carryingCapacity: Int) : Transport() {         //грузоподьемность

    override val orders: MutableList<Order> = mutableListOf()
    var remainingBodyVolume: Int = bodyVolume
    var remainingCarryingCapacity: Int = carryingCapacity
    }


//пасажирский транспорт
class PassengerTransport(override val brand: String,
                                  override val model: String,
                                  override val yearOfIssue: Int,
                                  override val typeOfFuel: String,
                                  override val fuelConsumption: Double,
                                  val numberOfSeats: Int) : Transport() {  //количество мест

    override val orders: MutableList<Order> = mutableListOf()
    var remainingNumberOfSeats: Int = numberOfSeats
}

//грузо-пасажирский транспорт
class CargoPassengerTransport(override val brand: String,
                              override val model: String,
                              override val yearOfIssue: Int,
                              override val typeOfFuel: String,
                              override val fuelConsumption: Double,
                              val carryingCapacity: Int,
                              val numberOfSeats: Int) : Transport() {

    override val orders: MutableList<Order> = mutableListOf()
    val remainingNumberOfSeats: Int = numberOfSeats
    val remainingCarryingCapacity: Int = carryingCapacity
   // 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до
}

