package com.bignerdranch.android.criminalintent

class Zadanie2 {
}
data class Order (val name: String,
                  val startingPoint: String,      //начальная точка
                  val endPoint: String,
                //  val cargoVolumeM3: Int? = null,    //обьем груза
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

    abstract fun refuel()    //заправить
    abstract fun repair()    //отремонтировать
    abstract fun freePlace()
    abstract fun loadOrder (order: Order)  //разместить заказ
    abstract fun unloadOrder (order: Order)
    abstract fun checkOrder ()

}

//грузовой транспорт
class FreightTransport(_brand: String,
                       _model: String,
                       _yearOfIssue: Int,
                       _typeOfFuel: String,
                       _fuelConsumption: Double,
                       val bodyType: String,                              //тип груза
                       val carryingCapacity: Int) : Transport() {         //грузоподьемность

    override val brand = _brand
    override val model = _model
    override val yearOfIssue = _yearOfIssue
    override val typeOfFuel = _typeOfFuel
    override val fuelConsumption = _fuelConsumption
    private val orders: MutableSet<Order> = mutableSetOf()
    var remainingCarryingCapacity: Int = carryingCapacity
        get() = carryingCapacity - orders.sumBy{ it.cargoWeightKg!! }
        private set

    override fun refuel() {
        println("Бак $brand $model полностью заправлен")
    }
    override fun repair() {
        println("Выполнено техническое обслуживание и ремонт $brand $model")
    }
    override fun freePlace() {
        println("В транспорт можно погрузить еще $remainingCarryingCapacity кг ")
    }
    override fun loadOrder(order: Order) {
        if (!orders.contains(order)) {
            orders.add(order)
            println("Заказ ${order.name} загружен в $brand $model")
        } else {
            println("Заказ ${order.name} уже погружен в $brand $model")
        }

    }
    override fun unloadOrder(order: Order) {
       if (orders.contains(order)) {
           orders.remove(order)
           println("Заказ ${order.name} разгружен из $brand $model")
       } else {
           println("Заказа ${order.name} уже был разгружен")
       }
    }
    override fun checkOrder() {
        if (orders.isNullOrEmpty()) {
            println("список заказов пуст")
        } else {
            val list: List<Order> = orders.toList()
            var message = "Погружено ${list.size} заказов:\n "
            for (n in list) {
                message += "заказ ${n.name} из ${n.startingPoint} в ${n.endPoint} " +
                        "${n.cargoType} ${n.cargoWeightKg}кг\n"
            }
            println(message)
        }
    }

    fun listOrder(): List<Order> {
        return orders.toList()
    }
    fun sealing () {
        println("После погрузки заказа выполнена опломбировка кузова")
    }
}


//пасажирский транспорт
class PassengerTransport(_brand: String,
                         _model: String,
                         _yearOfIssue: Int,
                         _typeOfFuel: String,
                         _fuelConsumption: Double,
                         val numberOfSeats: Int) : Transport() {  //количество мест

    override val brand = _brand
    override val model = _model
    override val yearOfIssue = _yearOfIssue
    override val typeOfFuel = _typeOfFuel
    override val fuelConsumption = _fuelConsumption
    private val orders: MutableSet<Order> = mutableSetOf()
    var remainingNumberOfSeats: Int = numberOfSeats
            get() = numberOfSeats - orders.sumBy{ it.numberOfPassengers!! }
        private set

    override fun refuel() {
        println("Бак $brand $model полностью заправлен")
    }
    override fun repair() {
        println("Выполнено техническое обслуживание и ремонт $brand $model")
    }
    override fun freePlace() {
        println("В транспорте еще $remainingNumberOfSeats свободных мест ")
    }
    override fun loadOrder(order: Order) {
        if (!orders.contains(order)) {
            orders.add(order)
            println("Заказ ${order.name} загружен в $brand $model")
        } else {
            println("Заказ ${order.name} уже погружен в $brand $model")
        }
    }
    override fun unloadOrder(order: Order) {
        if (orders.contains(order)) {
            orders.remove(order)
            println("Заказ ${order.name} разгружен из $brand $model")
        } else {
            println("Заказа ${order.name} уже был разгружен")
        }
    }
    override fun checkOrder() {
        if (orders.isNullOrEmpty()) {
            println("список заказов пуст")
        } else {
            val list: List<Order> = orders.toList()
            var message = "Погружено ${list.size} заказов:\n "
            for (n in list) {
                message += "заказ ${n.name} из ${n.startingPoint} в ${n.endPoint} " +
                        "количество пасажиров ${n.numberOfPassengers} человек \n"
            }
            println(message)
        }
    }

    fun listOrder(): List<Order> {
        return orders.toList()
    }
    fun disinfection() {
        println("Перед выполнением заказа салон был продезинфецирован")
    }
}

//грузо-пасажирский транспорт
class CargoPassengerTransport(_brand: String,
                              _model: String,
                              _yearOfIssue: Int,
                              _typeOfFuel: String,
                              _fuelConsumption: Double,
                              val carryingCapacity: Int,
                              val numberOfSeats: Int) : Transport() {

    override val brand = _brand
    override val model = _model
    override val yearOfIssue = _yearOfIssue
    override val typeOfFuel = _typeOfFuel
    override val fuelConsumption = _fuelConsumption
    private val orders: MutableSet<Order> = mutableSetOf()
    var remainingNumberOfSeats: Int = numberOfSeats
        get() = numberOfSeats - orders.sumBy{ it.numberOfPassengers!! }
        private set
    var remainingCarryingCapacity: Int = carryingCapacity
        get() = carryingCapacity - orders.sumBy{ it.cargoWeightKg!! }
        private set

    override fun refuel() {
        println("Бак $brand $model полностью заправлен")
    }
    override fun repair() {
        println("Выполнено техническое обслуживание и ремонт $brand $model")
    }
    override fun freePlace() {
        println("В транспорт можно загрузить еще $remainingCarryingCapacity кг, а свобоных мест осталось $remainingNumberOfSeats  ")
    }
    override fun loadOrder(order: Order) {
        if (!orders.contains(order)) {
            orders.add(order)
            println("Заказ ${order.name} загружен в $brand $model")
        } else {
            println("Заказ ${order.name} уже погружен в $brand $model")
        }
    }
    override fun unloadOrder(order: Order) {
        if (orders.contains(order)) {
            orders.remove(order)
            println("Заказ ${order.name} разгружен из $brand $model")
        } else {
            println("Заказа ${order.name} уже был разгружен")
        }
    }
    override fun checkOrder() {
        if (orders.isNullOrEmpty()) {
            println("список заказов пуст")
        } else {
            val list: List<Order> = orders.toList()
            var message = "Погружено ${list.size} заказов:\n "
            for (n in list) {
                message += "заказ ${n.name} из ${n.startingPoint} в ${n.endPoint} " +
                        "количество пасажиров ${n.numberOfPassengers} человек " +
                        "с грузом ${n.cargoWeightKg}кг\n"
            }
            println(message)
        }
    }

    fun listOrder(): List<Order> {
        return orders.toList()
    }
    fun disinfection() {
        println("Перед выполнением заказа салон был продезинфецирован")
    }
}


val order1 = Order("Заказ1", "Минск", "Гомель", cargoWeightKg = 2450, cargoType = "Продукты" )
val order2 = Order("Заказ2", "Минск", "Гомель", cargoWeightKg = 3600, cargoType = "Продукты" )
val order3 = Order("Заказ3", "Минск", "Могилев", cargoWeightKg = 5450, cargoType = "Промтовары" )
val order4 = Order("Заказ4", "Гродно", "Минск", cargoWeightKg = 7000, cargoType = "Песок" )
val order5 = Order("Заказ5", "Минск", "Молодечно", cargoWeightKg = 8000, cargoType = "Щебень" )
val order6 = Order("Заказ6", "Минск", "Гомель", numberOfPassengers = 5)
val order7 = Order("Заказ7", "Гомель", "Жлобин", numberOfPassengers = 45)
val order8 = Order("Заказ8", "Светлогорск", "Гомель", numberOfPassengers = 55)
val order9 = Order("Заказ9", "Минск", "Гомель", cargoWeightKg = 1450, numberOfPassengers = 2 )
val order10 = Order("Заказ10", "Минск", "Гомель", cargoWeightKg = 750, numberOfPassengers = 4 )

val auto1 = FreightTransport("MAN", "TGX", 2016, "ДТ", 24.0, "Рефрижератор", 19000 )
val auto2 = FreightTransport("Volvo", "FH16", 2012, "ДТ", 26.2, "Рефрижератор", 30000 )
val auto3 = FreightTransport("Hyundai", "HD170", 2016, "ДТ", 25.0, "Тент", 11500 )
val auto4 = FreightTransport("ГАЗ", "33023", 2016, "ГАЗ", 15.0, "Тент", 1200 )
val auto5 = FreightTransport("Scania", "S730 V8", 2017, "ДТ", 42.0, "Цистерна", 40000 )
val auto6 = FreightTransport("Volvo", "FM", 2018, "ДТ", 43.0, "Цистерна", 70000 )
val auto7 = FreightTransport("КАМАЗ", "43118", 2005, "ДТ", 33.0, "Кузов", 10000 )
val auto8 = FreightTransport("MAN", "TGS 8x4", 2018, "ДТ", 37.0, "Кузов", 27000 )
val auto9 = PassengerTransport("Scania", "Irizar I6", 2016, "Дизель", 35.0,  55 )
val auto10 = PassengerTransport("MAN", "Lion’s Coach", 2016, "Дизель", 37.0,  60 )
val auto11 = PassengerTransport("Mercedes-Benz", "Sprinter", 2019, "Дизель", 14.0,  18 )
val auto12 = CargoPassengerTransport("Volkswagen", "Crafter", 2016, "Дизель", 9.5, 2000, 4 )
val auto13 = CargoPassengerTransport("Volkswagen", "Transporter T5 Kasten", 2019, "Дизель", 8.5, 1200, 4 )
val auto14 = CargoPassengerTransport("Mercedes-Benz", "Vito", 2014, "Дизель", 9.0, 1300, 4)

val listOrder = listOf<Order>(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10)
val listAutoPark = listOf<Transport>(auto1, auto2, auto3, auto4, auto5, auto6, auto7, auto8, auto9, auto10, auto11, auto12, auto13, auto14)

fun transportCategory (order: Order, transport: List<Transport>): List<Transport> {
  val c =  if ((order.numberOfPassengers == null || order.numberOfPassengers == 0) && order.cargoWeightKg != null && order.cargoType != null) {
      transport.filterIsInstance<FreightTransport>()
  } else if ((order.numberOfPassengers != null || order.numberOfPassengers != 0) && order.cargoWeightKg == null && order.cargoType == null) {
      transport.filterIsInstance<PassengerTransport>()
  } else {
      transport.filterIsInstance<CargoPassengerTransport>()
  }
return c
}
