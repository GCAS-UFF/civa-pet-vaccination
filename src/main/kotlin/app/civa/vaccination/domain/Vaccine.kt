package app.civa.vaccination.domain

import java.util.*

class Vaccine
private constructor(
    private val name: Name,
    private val efficacy: Efficacy,
    private val fabrication: Fabrication
) {

    constructor(builder: VaccineBuilder) : this(
        builder.name,
        builder.efficacy,
        builder.fabrication
    )

    infix fun accept(visitor: VaccineVisitor) {
        visitor.seeName(name)
        visitor.seeEfficacy(efficacy)
        visitor.seeFabrication(fabrication)
    }

    infix fun pairNameWith(application: VaccineApplication) =
        name pairWith application

    infix fun mustMatch(species: Species) =
        efficacy mustMatch species

    fun mustBeValid() = apply { fabrication.mustBeValid() }

    fun apply(weight: PetWeight) = application {
        id = UUID.randomUUID()
        createdOn = ApplicationDateTime.now()
        petWeight = weight
        vaccine = this@Vaccine.mustBeValid()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vaccine

        if (name != other.name) return false
        if (efficacy != other.efficacy) return false
        if (fabrication != other.fabrication) return false

        return true
    }

    override fun hashCode() =
        31 * (31 * name.hashCode() + efficacy.hashCode()) + fabrication.hashCode()

}

fun vaccine(lambda: VaccineBuilder.() -> Unit): Vaccine {
    val builder = VaccineEntityBuilder()
    builder.lambda()
    return builder.build()
}
