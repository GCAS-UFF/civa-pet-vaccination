package app.civa.vaccination.adapter.out

import app.civa.vaccination.domain.VaccinationCard
import app.civa.vaccination.usecases.VaccinationCardPortOut
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.exists
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReactiveMongoVaccinationCardAdapterOut(
    private val operations: ReactiveMongoOperations
) : VaccinationCardPortOut {

    override suspend fun createOne(petId: UUID, card: VaccinationCard) =
        when (this existsByPetId petId) {
            true -> null
            false -> operations.save<VaccinationCard>(card).awaitFirst()
        }

    override suspend fun findById(id: UUID) =
        operations.find<VaccinationCard>(
            Query(where("_id").isEqualTo(id))
        ).awaitFirstOrNull()


    override suspend infix fun existsByPetId(id: UUID): Boolean =
        operations.exists<VaccinationCard>(
            Query(where("petId").isEqualTo(id))
        ).awaitFirst()
}

