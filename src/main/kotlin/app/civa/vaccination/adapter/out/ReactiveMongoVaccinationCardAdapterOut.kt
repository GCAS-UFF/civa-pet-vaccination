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
import java.lang.IllegalArgumentException
import java.util.*

@Repository
class ReactiveMongoVaccinationCardAdapterOut(
    private val operations: ReactiveMongoOperations
) : VaccinationCardPortOut {

    override suspend fun createOne(petId: UUID, card: VaccinationCard): VaccinationCard? {
        return if (existsByPetId(petId)) {
            throw IllegalArgumentException()
        } else {
            operations.save<VaccinationCard>(card).awaitFirst()
        }
    }

    override suspend fun findById(id: UUID): VaccinationCard? {
        return operations.find<VaccinationCard>(
            Query(where("_id").isEqualTo(id))
        ).awaitFirstOrNull()
    }

    override suspend fun existsByPetId(id: UUID): Boolean {
        return operations.exists<VaccinationCard>(
            Query(where("petId").isEqualTo(id))
        ).awaitFirst()
    }
}