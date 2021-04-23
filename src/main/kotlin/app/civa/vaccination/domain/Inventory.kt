package app.civa.vaccination.domain

import java.util.*

class Inventory : HashMap<String, Collection<VaccineControl>>() {
    fun add(vaccine: Vaccine, quantity: Int = 1) {
        val (vaccineName, control) = VaccineControl.from(quantity, vaccine).toPair()

        this.merge(vaccineName, setOf(control)) { A, B -> A union B }
    }

    infix fun findVaccineByName(vaccineName: String) =
        this[vaccineName]
            ?: throw VaccineControlNotFoundException of vaccineName

    infix fun findVaccineControlById(id: UUID) =
        this.flatMap { it.value }
            .firstOrNull { it.id == id }
            ?: throw VaccineControlNotFoundException from id

    fun increaseStock(quantity: Int = 1, id: UUID) {
        val (vaccineName, control) = this.findVaccineControlById(id).toPair()

        this.updateInventory(vaccineName, control.increaseBy(quantity))
    }

    fun retrieve(quantity: Int = 1, id: UUID): Vaccine {
        val (vaccineName, control) = this.findVaccineControlById(id).toPair()
        val (updatedControl, vaccine) = control.retrieve(quantity)

        this.updateInventory(vaccineName, updatedControl)

        return vaccine
    }

    private fun updateInventory(vaccineName: String, updatedControl: VaccineControl) {
        this[vaccineName] = this[vaccineName]!!
            .map { if (it.id == updatedControl.id) updatedControl else it }
    }
}
