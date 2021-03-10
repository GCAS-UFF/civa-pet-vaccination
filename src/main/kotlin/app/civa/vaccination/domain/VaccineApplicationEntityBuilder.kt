package app.civa.vaccination.domain

import java.util.*

class VaccineApplicationEntityBuilder : VaccineApplicationBuilder {

    override lateinit var id: UUID
    override lateinit var vaccine: Vaccine
    override lateinit var petWeight: PetWeight
    override lateinit var createdOn: ApplicationDateTime

    override fun build() = VaccineApplication(this)

    override fun toString() =
        "VaccineApplicationBuilder(id=$id, " +
        "vaccine=$vaccine, " +
        "petWeight=$petWeight, " +
        "createdOn=$createdOn)"
}
