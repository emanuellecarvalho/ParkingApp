package prompt

import parking.Parking
import util.Options
import vehicle.TypeVehicle
import vehicle.Vehicle
import java.util.*
import kotlin.random.Random

fun start(parking: Parking){

    while (true) {
        val scan = Scanner(System.`in`)

        println("--- MENU: Choose a option ---")
        for (option in Options.values()) {
            println("${option.action} -> ${option.description}")
        }

        lineDivisor() // line divisor

        val optionGetLine = scan.next().uppercase()
        println(optionGetLine)

        when (optionGetLine) {
            Options.ADD_N.action -> {
                createVehicleByPrompt(parking)
            }
            Options.ADD_D.action -> {
                createVehiclesDynamically(parking, 6)
            }
            Options.SHOW_L.action -> {
                showListOfVehicles(parking)
            }
            Options.CHECK_OUT.action -> {
                checkOut(parking)
            }
            Options.REPORT_F.action -> {
                generateFinancialReport(parking)
            }
            else -> {
                println("Invalid option")
            }
        }
    }
}

fun createVehiclesDynamically(parking: Parking, numberOfVehicles: Int) {
    for (number in 1..numberOfVehicles) {
        for (type in TypeVehicle.values()) {
            val card = Random(999).toString()
            val vehicle = Vehicle(plate = "GOTS2${number + type.ordinal}", type = type, discountCard = card)
            parking.addVehicle(vehicle)
        }
    }
}

//method show a list of vehicles of the parking
fun showListOfVehicles(parking: Parking) {
    for (vehicle in parking.listVehicles()) {
        println(vehicle)
    }
}

//method creates (add) a vehicle with command line
fun createVehicleByPrompt(parking: Parking) {
    val listOfTypes = arrayListOf<TypeVehicle>()

    val scan = Scanner(System.`in`)
    println("Please enter with a plate number of car:")

    val plateNumber = scan.next().uppercase()
    println("The plate number is: $plateNumber")

    lineDivisor() // line divisor

    println("Have you got a discount card? If you have, type it. Or not, just type ENTER.")
    var carDiscount: String? = scan.next()
    if (carDiscount.equals("")) carDiscount = null

    lineDivisor() // divisor line

    println("Now, choose a type of vehicle:")
    for (type in TypeVehicle.values()) {
        listOfTypes.add(type)
        println("$type -> ${type.ordinal}")
    }
    println("Type:")

    while (true) {
        var typeOfVehicle: Int?

        try {
            typeOfVehicle = scan.next().toInt()
            val vehicle = Vehicle(plateNumber, listOfTypes[typeOfVehicle], carDiscount)
            if (parking.addVehicle(vehicle)) {
                break

            }else{
                start(parking)
            }

        } catch (e: java.lang.NumberFormatException) {
            println("Type a valid value format: is an integer number!")

        } catch (_: java.lang.IndexOutOfBoundsException) {
            println("Type of vehicle is invalid. Choose a valid code of vehicle!")
        }
    }
}

fun checkOut(parking: Parking){
    val scan = Scanner(System.`in`)
    println("Enter with license plate:")
    val plate = scan.next()

    val vehicle = parking.getVehicleByPlate(plate)

    if (vehicle != null){
        parking.checkOutVehicle(vehicle)

    }else{
        println("We can not checkout this vehicle")
        start(parking)
    }

}

fun generateFinancialReport(parking: Parking){
    val report = parking.financialReport()
    lineDivisor()
    println("FINANCIAL REPORT")
    println(report)
    lineDivisor()
    start(parking)
}

fun lineDivisor() {
    println("------------------------")
}
