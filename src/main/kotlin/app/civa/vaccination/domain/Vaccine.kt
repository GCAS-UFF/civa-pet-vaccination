package app.civa.vaccination.domain

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

    infix fun apply(petWeight: PetWeight) = VaccineApplication.from(
        vaccine = this, petWeight
    )

    infix fun pairNameWith(application: VaccineApplication) =
        name pairWith application

    infix fun mustMatch(species: Species) = apply {
        efficacy mustMatch species
    }

    fun mustBeValid() = apply { fabrication.mustBeValid() }

}

fun vaccine(lambda: VaccineBuilder.() -> Unit): Vaccine {
    val builder = VaccineEntityBuilder()
    builder.lambda()
    return builder.build()
}
