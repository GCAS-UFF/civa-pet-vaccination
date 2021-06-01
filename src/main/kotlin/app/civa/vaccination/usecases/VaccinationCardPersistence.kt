package app.civa.vaccination.usecases

import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardPersistence {

    suspend fun createOne(petId: UUID, card: VaccinationCard): VaccinationCard?
    suspend fun findById(cardId: UUID): VaccinationCard?
    suspend fun existsByPetId(petId: UUID): Boolean
}
