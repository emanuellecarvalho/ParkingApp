package parking

import util.Parameters
import util.getTimeInArrayFormat
import util.getTimeInMinutesFormat
import vehicle.Vehicle

data class Parking(var vehicles: MutableSet<Vehicle>) {
    private var report = Pair(0, 0)

    fun addVehicle(vehicle: Vehicle): Boolean {
        if (!this.isForbidden(vehicle)) {
            error("This vehicle type is not allowed to add in the com.meli.parking")
            return false
        }
        if (checkVehicle(vehicle)) {
            error("Sorry, the vehicle already is in the parking!")
            return false
        }

        return if (this.vehicles.size <= Parameters.TOTAL_VEHICLE_ALLOWED.value - 1) {
            if (this.vehicles.add(vehicle)) {
                println("######## Welcome to AlkeParking!-> ${vehicle.plate}\n ########")
                true

            } else {
                error("######## Sorry, the check-in failed! ########")
                false
            }

        } else {
            println("######## Sorry, the check-in failed ########")
            false
        }
    }

    fun checkOutVehicle(vehicle: Vehicle): Vehicle? {
        if (this.vehicles.size == 0) {
            onSuccess("The Parking is empty. You can not remove a vehicle!")
            return null
        }

        if (this.vehicles.remove(vehicle)) {
            onSuccess("The vehicle ${vehicle.plate} was removed!")

            val timeArray = getTimeInArrayFormat(vehicle.parkedTime)// format [1, 50] -> 1:50
            val timeInMinutes = getTimeInMinutesFormat(timeArray)// format minutes 90min
            val totalToPay = util.calculateFee(timeInMinutes, vehicle)

            onSuccess("Your fee is $$totalToPay. Come back soon!")

            val vehicleReport = report.first + 1
            val amount = report.second + totalToPay

            report = Pair(vehicleReport, amount)

            return vehicle
        } else {
            error("Can not remove the vehicle. There is a error!")
            return null
        }
    }

    private fun isForbidden(vehicle: Vehicle): Boolean {
        when (vehicle.type.type) {
            "moto" -> {
                return true
            }
            "minibus" -> {
                return true
            }
            "bus" -> {
                return true
            }
            "car" -> {
                return true
            }
            else -> {
                return false
            }
        }
    }

    fun listVehicles(): ArrayList<String> {
        val vehicleList = arrayListOf<String>()

        for (vehicle in this.vehicles) {
            vehicleList.add(vehicle.plate)
        }
        return vehicleList
    }

    fun financialReport(): String {
        return "${report.first} vehicles have checked out and have earnings of ${report.second}\n"
    }

    private fun checkVehicle(vehicle: Vehicle): Boolean {
        return this.vehicles.contains(vehicle)
    }

    fun getVehicleByPlate(plate: String): Vehicle? {
        return this.vehicles.find { it.plate == plate }
    }

    private fun onSuccess(msn: String) {
        println("######## $msn ########")
    }

    private fun error(msn: String){
        println("######## $msn ########")
    }
}