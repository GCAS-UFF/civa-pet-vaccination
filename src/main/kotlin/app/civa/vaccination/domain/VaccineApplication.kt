package app.civa.vaccination.domain

import java.util.*

class VaccineApplication
private constructor(
    val id: UUID,
    private val vaccine: Vaccine,
    private val petWeight: PetWeight,
    private val createdOn: ApplicationDateTime
) {

    constructor(vaccine: Vaccine, petWeight: PetWeight) : this(
        UUID.randomUUID(),
        vaccine,
        petWeight,
        ApplicationDateTime.now()
    ) {
        vaccine.mustBeValid()
    }

    constructor(builder: VaccineApplicationBuilder) : this(
        builder.id,
        builder.vaccine,
        builder.petWeight,
        builder.createdOn
    )

    fun accept(visitor: VaccineApplicationVisitor) {
        visitor.seeId(id)
        visitor.seeVaccine(vaccine)
        visitor.seePetWeight(petWeight)
        visitor.seeCreatedOn(createdOn)
    }

    fun toPair(): Pair<String, VaccineApplication> {
        val builder = StringBuilder()
        vaccine.accept { builder.append(it) }
        val vaccineName = builder.toString()

        return vaccineName to this
    }

    fun mustMatchSpecies(species: Species) =
        vaccine.mustMatchSpecies(species)

    fun mapStatus(other: VaccineApplication) =
        createdOn.mapStatus(other.createdOn)

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
