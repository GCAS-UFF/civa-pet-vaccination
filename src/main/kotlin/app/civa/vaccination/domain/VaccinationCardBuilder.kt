package app.civa.vaccination.domain

import java.util.*

interface VaccinationCardBuilder : Builder<VaccinationCard>{

    var id: UUID
    var petId: UUID
    var species: Species
    var applications: Applications

}
