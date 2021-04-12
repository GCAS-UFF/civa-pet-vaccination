package app.civa.vaccination.domain

import java.util.*

class VaccinationCard
private constructor(
    private val id: UUID,
    private val petID: UUID,
    private val species: Species,
    private val applications: Applications
) {
    companion object {
        infix fun of(petEntry: Pair<UUID, Species>): VaccinationCard {
            val (petId, species) = petEntry
            return VaccinationCard(
                UUID.randomUUID(),
                petId,
                species,
                Applications()
            )
        }
    }

    constructor(builder: VaccinationCardBuilder) : this(
        builder.id,
        builder.petId,
        builder.species,
        builder.applications
    )

    infix fun accepts(visitor: VaccinationCardVisitor) {
        visitor.seeId(id)
        visitor.seePetId(petID)
        visitor.seeSpecies(species)
        visitor.seeApplications(applications)
    }

    infix fun add(application: VaccineApplication) =
        applications add application.mustMatch(species)

    infix fun deleteBy(applicationId: UUID) =
        applications deleteBy applicationId

    infix fun findBy(applicationId: UUID) =
        applications findBy applicationId

    override fun toString() =
        "VaccinationCard(id=$id, " +
                "petID=$petID, " +
                "species=$species, " +
                "applications=$applications"
}

fun vaccinationCard(lambda: VaccinationCardBuilder.() -> Unit): VaccinationCard {
    val builder = object : VaccinationCardBuilder {
        override lateinit var id: UUID
        override lateinit var petId: UUID
        override lateinit var species: Species
        override lateinit var applications: Applications

        override fun build() = VaccinationCard(this)
    }
    builder.lambda()
    return builder.build()
}
