package app.civa.vaccination.domain

import java.util.*

class VaccineApplication
private constructor(
    val id: UUID,
    private val vaccine: Vaccine,
    private val petWeight: PetWeight,
    private val createdOn: ApplicationDateTime
) {

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
        apply {  vaccine.mustMatch(species) }

    infix fun mapStatusFrom(other: VaccineApplication) =
        createdOn.mapStatusFrom(other.createdOn)

    infix fun happenedRecentlyIn(applications: Collection<VaccineApplication>?) =
        applications?.any { it == this }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VaccineApplication
        return when {
            other.vaccine != vaccine -> false
            createdOn != other.createdOn -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = vaccine.hashCode()
        result = 31 * result + createdOn.hashCode()
        return result
    }

    override fun toString() =
        "VaccineApplication(id=$id, " +
        "vaccine=$vaccine, " +
        "petWeight=$petWeight, " +
        "createdOn=$createdOn)"

}

fun application(lambda: VaccineApplicationEntityBuilder.() -> Unit): VaccineApplication {
    val builder = VaccineApplicationEntityBuilder()
    builder.lambda()
    return builder.build()
}
