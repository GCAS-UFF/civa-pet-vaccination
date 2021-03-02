package app.civa.vaccination.domain

import java.util.*

class VaccinationCard
private constructor(
    private val id: UUID,
    private val petID: UUID,
    private val species: Species,
    private val applications: Applications
) {

    val size: Int
        get() = applications.countAll()

    constructor(petID: UUID, species: Species) : this(
        UUID.randomUUID(),
        petID,
        species,
        Applications()
    )

    constructor(builder: VaccinationCardBuilder) : this(
        builder.id,
        builder.petId,
        builder.species,
        builder.applications
    )

    fun accept(visitor: VaccinationCardVisitor) {
        visitor.seeId(id)
        visitor.seePetId(petID)
        visitor.seeSpecies(species)
        visitor.seeApplications(applications)
    }

    infix fun add(application: VaccineApplication) =
        applications add application.mustMatch(species)

    infix fun removeBy(applicationId: UUID) =
        applications deleteBy applicationId

    infix fun findBy(applicationId: UUID) =
        applications findBy applicationId

    override fun toString() =
        "VaccinationCard(id=$id, " +
        "petID=$petID, " +
        "species=$species, " +
        "applications=$applications, " +
        "size=$size)"

}
