package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.*
import java.util.*

data class VaccinationCardPayloadOut(
    val id: UUID,
    val petID: UUID,
    val species: Species,
    val applications: Applications
) {
    companion object {

        infix fun from(vaccinationCard: VaccinationCard): VaccinationCardPayloadOut {
            val builder = object : VaccinationCardVisitor, Builder<VaccinationCardPayloadOut> {
                lateinit var id: UUID
                lateinit var petID: UUID
                lateinit var species: Species
                lateinit var applications: Applications

                override fun seeId(id: UUID) {
                    this.id = id
                }

                override fun seePetId(petID: UUID) {
                    this.petID = petID
                }

                override fun seeSpecies(species: Species) {
                    this.species = species
                }

                override fun seeApplications(applications: Applications) {
                    this.applications = applications
                }

                override fun build() = VaccinationCardPayloadOut(id, petID, species, applications)
            }

            vaccinationCard accepts builder
            return builder.build()
        }
    }
}
