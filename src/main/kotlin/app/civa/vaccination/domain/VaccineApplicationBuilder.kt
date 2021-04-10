package app.civa.vaccination.domain

import java.util.*

interface VaccineApplicationBuilder: Builder<VaccineApplication> {

    var id: UUID
    var vaccine: Vaccine
    var petWeight: PetWeight
    var createdOn: ApplicationDateTime

}
