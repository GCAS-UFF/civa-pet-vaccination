package app.civa.vaccination.domain

import java.util.*

class VaccineApplicationEntityBuilder : VaccineApplicationBuilder {

    override lateinit var id: UUID
    override lateinit var vaccine: Vaccine
    override lateinit var petWeight: PetWeight
    override lateinit var createdOn: ApplicationDateTime

    fun id(id: UUID) =
        apply { this.id = id }

    fun vaccine(vaccine: Vaccine) =
        apply { this.vaccine = vaccine }

    fun petWeight(petWeight: PetWeight) =
        apply { this.petWeight = petWeight }

    fun createdOn(dateTime: ApplicationDateTime) =
        apply { this.createdOn = dateTime }

    override fun build() = VaccineApplication(this)

    override fun toString() =
        "VaccineApplicationBuilder(id=$id, " +
        "vaccine=$vaccine, " +
        "petWeight=$petWeight, " +
        "createdOn=$createdOn)"
}