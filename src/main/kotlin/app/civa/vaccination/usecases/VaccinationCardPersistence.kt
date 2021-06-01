package app.civa.vaccination.usecases

import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardPersistence {

    suspend fun createOne(petId: UUID, card: VaccinationCard): VaccinationCard?
    suspend infix fun findById(cardId: UUID): VaccinationCard?
    suspend infix fun existsByPetId(petId: UUID): Boolean
}
