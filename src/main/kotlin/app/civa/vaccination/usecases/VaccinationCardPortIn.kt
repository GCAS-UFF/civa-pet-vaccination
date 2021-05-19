package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono
import java.util.*

interface VaccinationCardPortIn {

    fun createOne(entry: Pair<UUID, Species>): Mono<VaccinationCard>

}
