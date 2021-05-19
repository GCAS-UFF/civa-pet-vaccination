package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.Applications
import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import java.util.*

data class VaccinationCardPayloadOut(
    val id: UUID,
    val petID: UUID,
    val species: Species,
    val applications: Applications
) {
    constructor(builder: VaccinationCardPayloadOutBuilder) : this(
        builder.id,
        builder.petID,
        builder.species,
        builder.applications
    )

    companion object {
        infix fun from(vaccinationCard: VaccinationCard): VaccinationCardPayloadOut {
            val builder = VaccinationCardPayloadOutBuilder()
            vaccinationCard accepts builder
            return builder.build()
        }
    }
}

