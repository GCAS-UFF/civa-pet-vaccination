package app.civa.vaccination.adapter.out

import app.civa.vaccination.domain.Inventory
import app.civa.vaccination.usecases.InventoryPersistence
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReactiveMongoInventory(
    private val operations: ReactiveMongoOperations
) : InventoryPersistence {

    override suspend fun findByVetId(vetId: UUID): Inventory {
        val query = Query(where("vetId").isEqualTo(vetId))
        return operations.find<Inventory>(query)
            .awaitFirstOrElse { throw NoSuchElementException() }
    }
}