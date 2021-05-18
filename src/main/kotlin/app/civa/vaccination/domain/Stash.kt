package app.civa.vaccination.domain

import java.util.*

class Stash : HashMap<String, Collection<VaccineControl>>() {

    fun add(vaccine: Vaccine, quantity: Int = 1): VaccineControl {
        val control = VaccineControl.from(quantity, vaccine)

        this.merge(vaccine.makeKey(), setOf(control)) { A, B -> A union B }

        return control
    }

    infix fun findVaccineByName(vaccineName: String) =
        this[vaccineName]
            ?: throw VaccineControlNotFoundException of vaccineName

    infix fun findVaccineControlById(id: UUID) =
        this.flatMap { it.value }
            .firstOrNull { it.id == id }
            ?: throw VaccineControlNotFoundException from id

    fun increaseStock(id: UUID, quantity: Int = 1): VaccineControl {
        val control = this.findVaccineControlById(id)
        val updatedControl = control.increaseBy(quantity)
        this updateInventoryWith updatedControl
        return updatedControl
    }

    fun retrieve(id: UUID, quantity: Int = 1): Vaccine {
        val control = this.findVaccineControlById(id)
        val (updatedControl, vaccine) = control.retrieve(quantity)
        this updateInventoryWith updatedControl
        return vaccine
    }

    private infix fun updateInventoryWith(updatedControl: VaccineControl) {
        val vaccineName = updatedControl.getVaccineKey()

        this[vaccineName] = this[vaccineName]!!
            .map { if (it.id == updatedControl.id) updatedControl else it }
    }
}
