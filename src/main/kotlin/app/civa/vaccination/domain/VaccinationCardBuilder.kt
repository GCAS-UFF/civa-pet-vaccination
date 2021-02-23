package app.civa.vaccination.domain

import java.util.*

interface VaccinationCardBuilder {

    val id: UUID
    val petId: UUID
    val species: Species
    val applications: Applications

}
