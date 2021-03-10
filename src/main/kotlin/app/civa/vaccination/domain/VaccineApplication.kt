package app.civa.vaccination.domain

import java.util.*

class VaccineApplication
private constructor(
    val id: UUID,
    private val vaccine: Vaccine,
    private val petWeight: PetWeight,
    private val createdOn: ApplicationDateTime
) {

    companion object {

        fun from(vaccine: Vaccine, petWeight: PetWeight) = VaccineApplication(
            id = UUID.randomUUID(),
            vaccine,
            petWeight,
            createdOn = ApplicationDateTime.now()
        )
    }

    constructor(builder: VaccineApplicationBuilder) : this(
        builder.id,
        builder.vaccine,
        builder.petWeight,
        builder.createdOn
    )

    infix fun accept(visitor: VaccineApplicationVisitor) {
        visitor.seeId(id)
        visitor.seeVaccine(vaccine)
        visitor.seePetWeight(petWeight)
        visitor.seeCreatedOn(createdOn)
    }

    fun toPair() = vaccine pairNameWith this

    infix fun mustMatch(species: Species) =
        apply { vaccine mustMatch species }

    infix fun getStatusFrom(other: VaccineApplication) =
        createdOn mapStatus other.createdOn

    override fun toString() =
        "VaccineApplication(id=$id, " +
        "vaccine=$vaccine, " +
        "petWeight=$petWeight, " +
        "createdOn=$createdOn)"
}

infix fun Collection<VaccineApplication>?.minus(application: VaccineApplication) =
    this?.filter { it.id != application.id }

fun application(lambda: VaccineApplicationEntityBuilder.() -> Unit): VaccineApplication {
    val builder = VaccineApplicationEntityBuilder()
    builder.lambda()
    return builder.build()
}
