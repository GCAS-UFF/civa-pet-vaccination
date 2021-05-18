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
            vaccine = vaccine.mustBeValid(),
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

    infix fun accepts(visitor: VaccineApplicationVisitor) {
        visitor.seeId(id)
        visitor.seeVaccine(vaccine)
        visitor.seePetWeight(petWeight)
        visitor.seeCreatedOn(createdOn)
    }

    fun getKey() = vaccine.makeKey()

    infix fun mustMatch(species: Species) = apply {
        vaccine mustMatch species
    }

    infix fun mapStatusFrom(other: VaccineApplication) =
        createdOn mapStatus other.createdOn

    override fun toString() =
        "VaccineApplication(id=$id, " +
                "vaccine=$vaccine, " +
                "petWeight=$petWeight, " +
                "createdOn=$createdOn)"
}

infix fun Collection<VaccineApplication>?.minus(application: VaccineApplication) =
    this?.filter { it.id != application.id }
        ?: throw ApplicationNotFoundException from application.id

fun application(lambda: VaccineApplicationBuilder.() -> Unit): VaccineApplication {
    val builder = object : VaccineApplicationBuilder {
        override lateinit var id: UUID
        override lateinit var vaccine: Vaccine
        override lateinit var petWeight: PetWeight
        override lateinit var createdOn: ApplicationDateTime

        override fun build() = VaccineApplication(this)
    }
    builder.lambda()
    return builder.build()
}
