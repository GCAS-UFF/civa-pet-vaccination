package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Inventory
import java.util.*

interface InventoryPersistence {

    suspend fun findByVetId(vetId: UUID): Inventory
    suspend fun findById(inventoryId: UUID): Inventory
    suspend fun createOne(vetId: UUID, inventory: Inventory): Inventory

}
