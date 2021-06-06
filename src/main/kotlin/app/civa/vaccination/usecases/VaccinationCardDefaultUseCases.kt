package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import org.springframework.stereotype.Service
import java.util.*

@Service
class VaccinationCardDefaultUseCases(
    private val persistence: VaccinationCardPersistence
) : VaccinationCardUseCases {

    override suspend fun createOne(petId: UUID, species: Species): String {
        val card = VaccinationCard.of(petId to species)
        return persistence.createOne(petId, card).id.toString()
    }

    override suspend fun findById(cardId: UUID) = persistence findById cardId

    override suspend fun deleteOne(cardId: UUID) = persistence deleteById cardId

}
