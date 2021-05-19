package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.Applications
import app.civa.vaccination.domain.Builder
import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCardVisitor
import java.util.*

class VaccinationCardPayloadOutBuilder : VaccinationCardVisitor,
    Builder<VaccinationCardPayloadOut> {

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

    override fun build() = VaccinationCardPayloadOut(this)
}
