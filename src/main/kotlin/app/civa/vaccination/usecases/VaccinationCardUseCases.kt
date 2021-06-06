package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardUseCases {

    suspend fun createOne(petId: UUID, species: Species): String
    suspend fun findById(cardId: UUID): VaccinationCard
    suspend fun deleteOne(cardId: UUID): VaccinationCard

}
