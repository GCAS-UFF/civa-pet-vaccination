package app.civa.vaccination.domain

import java.util.*

interface VaccineApplicationBuilder: Builder<VaccineApplication> {

    val createdOn: ApplicationDateTime
    val petWeight: PetWeight
    val vaccine: Vaccine
    val id: UUID

}
