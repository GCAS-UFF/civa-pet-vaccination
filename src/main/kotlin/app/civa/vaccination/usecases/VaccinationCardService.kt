package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import org.springframework.stereotype.Service
import java.util.*

@Service
class VaccinationCardService(
    private val vaccinationCardPortOut: VaccinationCardPortOut
) : VaccinationCardPortIn {

    override suspend fun createOne(petId: UUID, species: Species): String {
        val card = VaccinationCard.of(petId to species)
        vaccinationCardPortOut.createOne(petId, card)
        return card.id.toString()
    }

    override suspend fun findById(id: UUID) =
        vaccinationCardPortOut.findById(id)

}
