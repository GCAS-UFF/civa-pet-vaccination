package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardPortIn {

    suspend fun createOne(petId: UUID, species: Species): String
    suspend fun findById(id: UUID): VaccinationCard?
}
