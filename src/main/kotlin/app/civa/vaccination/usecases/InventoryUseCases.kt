package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Vaccine
import app.civa.vaccination.domain.VaccineControl
import java.util.*

interface InventoryUseCases {

    suspend fun createOne(vetId: UUID): String?
    suspend fun findById(controlId: UUID): VaccineControl?
    suspend fun addStock(controlId: UUID): Boolean
    suspend fun retrieveVaccine(controlId: UUID): Vaccine?
    suspend fun retrieveVaccine(name: String): Vaccine?

}
