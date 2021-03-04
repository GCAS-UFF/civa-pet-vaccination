package app.civa.vaccination.domain

class Efficacy
private constructor(
    private val species: Collection<Species>,
    private val agents: Collection<String>
) {

    constructor(builder: EfficacyBuilder) : this(
        builder.species,
        builder.agents
    )

    fun accept(visitor: EfficacyVisitor) {
        visitor.seeSpecies(species)
        visitor.seeAgents(agents)
    }

    companion object {

        const val SPECIES_DOESNT_MATCH = "Species doesn't match vaccine's species"
    }

    infix fun mustMatch(species: Species) {
        when (this.species matches species) {
            false -> throw IllegalStateException(SPECIES_DOESNT_MATCH)
        }
    }

}

fun efficacy(lambda: EfficacyBuilder.() -> Unit): Efficacy {
    val builder = EfficacyEntityBuilder()
    builder.lambda()
    return builder.build()
}
