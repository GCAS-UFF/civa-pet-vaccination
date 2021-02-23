package app.civa.vaccination.domain

import java.util.*

interface VaccineApplicationVisitor {

    fun seeId(id: UUID)
    fun seeVaccine(vaccine: Vaccine)
    fun seePetWeight(petWeight: PetWeight)
    fun seeCreatedOn(createdOn: ApplicationDateTime)

}
