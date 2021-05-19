package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.Species
import java.util.*

data class VaccinationCardPayloadIn(val id: UUID, val species: Species) {
    fun toPair() = id to species
}
