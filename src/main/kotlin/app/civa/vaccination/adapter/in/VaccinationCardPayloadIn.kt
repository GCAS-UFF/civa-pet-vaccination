package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.Species
import java.util.*

data class VaccinationCardPayloadIn(val petId: UUID, val species: Species)
