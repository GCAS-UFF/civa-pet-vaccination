package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class VaccinationCardService(
    private val vaccinationCardPortOut: VaccinationCardPortOut
) : VaccinationCardPortIn {

    override fun createOne(entry: Pair<UUID, Species>): Mono<VaccinationCard> {
        return Mono.just(VaccinationCard.of(entry))
            .flatMap { vaccinationCardPortOut.createOne(it) }
    }

}
