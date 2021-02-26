package app.civa.vaccination.domain

import java.util.*

class VaccinationCardEntityBuilder : VaccinationCardBuilder {

    override lateinit var id: UUID
    override lateinit var petId: UUID
    override lateinit var species: Species
    override lateinit var applications: Applications

    fun id(id: UUID) =
        apply { this.id = id }

    fun petId(petId: UUID) =
        apply { this.petId = petId }

    fun species(species: Species) =
        apply { this.species = species }

    fun applications(applications: Applications) =
        apply { this.applications = applications }

    override fun build() = VaccinationCard(this)
}
