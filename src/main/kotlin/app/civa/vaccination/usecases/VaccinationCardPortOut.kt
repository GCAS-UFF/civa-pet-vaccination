package app.civa.vaccination.usecases

import app.civa.vaccination.domain.VaccinationCard
import reactor.core.publisher.Mono

interface VaccinationCardPortOut {

    fun createOne(vaccinationCard: VaccinationCard): Mono<VaccinationCard>

}
