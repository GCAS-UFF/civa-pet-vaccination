package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Inventory
import app.civa.vaccination.domain.Vaccine
import app.civa.vaccination.domain.VaccineControl
import java.util.*

interface InventoryUseCases {

    suspend fun createOne(vetId: UUID): String
    suspend fun findByVetId(vetId: UUID): Inventory
    suspend fun addStock(controlId: UUID): Boolean
    suspend fun retrieveVaccine(controlId: UUID): Vaccine
    suspend fun retrieveVaccine(name: String): Vaccine

}
