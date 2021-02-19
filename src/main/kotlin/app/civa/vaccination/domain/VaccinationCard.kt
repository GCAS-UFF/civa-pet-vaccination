package app.civa.vaccination.domain

import java.util.UUID

class VaccinationCard
private constructor(
    private val id: UUID,
    private val petID: UUID,
    private val species: Species,
    private val applications: Applications
) {

    constructor(petID: UUID, species: Species) : this(
        UUID.randomUUID(),
        petID,
        species,
        Applications()
    )

    fun addApplication(application: VaccineApplication) {
        application.mustMatchSpecies(species)
        applications.addEntry(application)
    }

    fun removeApplicationById(applicationId: UUID) {
        applications.deleteById(applicationId)
    }

    fun findApplicationById(applicationId: UUID) =
        applications.findById(applicationId)


    fun countAll() = applications.countAll()

}
