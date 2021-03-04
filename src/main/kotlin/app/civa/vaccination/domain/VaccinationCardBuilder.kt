package app.civa.vaccination.domain

import java.util.*

interface VaccinationCardBuilder : Builder<VaccinationCard>{

    val id: UUID
    val petId: UUID
    val species: Species
    val applications: Applications

}
