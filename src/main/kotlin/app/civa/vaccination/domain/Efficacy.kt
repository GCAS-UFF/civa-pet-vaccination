package app.civa.vaccination.domain

class Efficacy
private constructor(
    private val species: Collection<Species>,
    private val agents: Collection<String>
) {

    constructor(builder: EfficacyBuilder) : this(
        builder.species,
        builder.agents
    ) {
        mustNotBeEmpty(builder.species, builder.agents)
    }

    fun accept(visitor: EfficacyVisitor) {
        visitor.seeSpecies(species)
        visitor.seeAgents(agents)
    }

    infix fun mustMatch(species: Species) = apply {
        this.species mustMatch species
    }
}

fun efficacy(lambda: EfficacyBuilder.() -> Unit): Efficacy {
    val builder = EfficacyEntityBuilder()
    builder.lambda()
    return builder.build()
}

private fun <T> mustNotBeEmpty(vararg values: Collection<T>) {
    values.forEach {
        if (it.isEmpty()) throw IllegalArgumentException()
    }
}
