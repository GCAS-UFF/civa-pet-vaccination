package app.civa.vaccination.domain

import java.util.*

interface VaccineApplicationVisitor {

    infix fun seeId(id: UUID)
    infix fun seeVaccine(vaccine: Vaccine)
    infix fun seePetWeight(petWeight: PetWeight)
    infix fun seeCreatedOn(createdOn: ApplicationDateTime)

}
