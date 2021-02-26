package app.civa.vaccination.domain

import java.util.*

interface VaccinationCardVisitor {

    fun seeId(id: UUID)
    fun seePetId(petID: UUID)
    fun seeSpecies(species: Species)
    fun seeApplications(applications: Applications)

}
