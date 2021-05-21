package app.civa.vaccination.usecases

import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardPortOut {

    suspend fun createOne(petId: UUID, card: VaccinationCard): VaccinationCard?
    suspend fun findById(id: UUID): VaccinationCard?
    suspend fun existsByPetId(id: UUID): Boolean
}
