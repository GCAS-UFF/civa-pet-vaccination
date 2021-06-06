package app.civa.vaccination.adapter.out

import app.civa.vaccination.domain.VaccinationCard
import app.civa.vaccination.usecases.VaccinationCardPersistence
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.exists
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReactiveMongoVaccinationCard(
    private val operations: ReactiveMongoOperations
) : VaccinationCardPersistence {

    override suspend fun createOne(petId: UUID, card: VaccinationCard): VaccinationCard =
        when (this existsByPetId petId) {
            true -> throw DuplicateKeyException("$petId is already linked to an existing card")
            false -> operations.save(card).awaitFirst()
        }

    override suspend infix fun findById(cardId: UUID): VaccinationCard {
        val query = Query(where("_id").isEqualTo(cardId))
        return operations.find<VaccinationCard>(query)
            .awaitFirstOrElse { throw NoSuchElementException() }
    }

    override suspend fun deleteById(cardId: UUID): VaccinationCard {
        val query = Query(where("_id").isEqualTo(cardId))
        return operations.findAndRemove<VaccinationCard>(query)
            .awaitFirstOrElse { throw NoSuchElementException("$cardId not found") }
    }

    override suspend infix fun existsByPetId(petId: UUID): Boolean {
        val query = Query(where("petId").isEqualTo(petId))
        return operations.exists<VaccinationCard>(query).awaitFirst()
    }
}
