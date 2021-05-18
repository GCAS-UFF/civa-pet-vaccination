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

    infix fun accepts(visitor: VaccineVisitor) {
        visitor.seeName(name)
        visitor.seeEfficacy(efficacy)
        visitor.seeFabrication(fabrication)
    }

    fun makeKey() = this.name.key

    infix fun apply(petWeight: PetWeight) =
        VaccineApplication.from(vaccine = this, petWeight)

    infix fun pairNameWith(application: VaccineApplication) =
        name pairWith application

    infix fun pairNameWith(control: VaccineControl) =
        name pairWith control

    infix fun mustMatch(species: Species) = apply {
        efficacy mustMatch species
    }

    fun mustBeValid() = apply {
        fabrication.mustBeValid()
    }
}

fun vaccine(lambda: VaccineBuilder.() -> Unit): Vaccine {
    val builder = object : VaccineBuilder {
        override lateinit var name: Name
        override lateinit var efficacy: Efficacy
        override lateinit var fabrication: Fabrication

        override fun build() = Vaccine(this)
    }
    builder.lambda()
    return builder.build()
}
