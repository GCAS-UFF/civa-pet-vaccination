package app.civa.vaccination.adapter.out

import app.civa.vaccination.domain.Inventory
import app.civa.vaccination.domain.VaccinationCard
import app.civa.vaccination.usecases.InventoryPersistence
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.exists
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.NoSuchElementException

@Repository
class ReactiveMongoInventory(
    private val operations: ReactiveMongoOperations
) : InventoryPersistence {

    override suspend fun findByVetId(vetId: UUID): Inventory {
        val query = Query(where("vetId").isEqualTo(vetId))
        return operations.find<Inventory>(query)
            .awaitFirstOrElse { throw NoSuchElementException() }
    }

    override suspend fun createOne(vetId: UUID, inventory: Inventory): Inventory =
        when (this existsByVetId vetId) {
            true -> throw DuplicateKeyException("$vetId is already linked to an existing inventory")
            false -> operations.save(inventory).awaitFirst()
        }

    override suspend fun findById(inventoryId: UUID): Inventory {
        val query = Query(where("_id").isEqualTo(inventoryId))
        return operations.find<Inventory>(query)
            .awaitFirstOrElse { throw NoSuchElementException() }
    }

    suspend infix fun existsByVetId(vetId: UUID): Boolean {
        val query = Query(where("vetId").isEqualTo(vetId))
        return operations.exists<Inventory>(query).awaitFirst()
    }
}