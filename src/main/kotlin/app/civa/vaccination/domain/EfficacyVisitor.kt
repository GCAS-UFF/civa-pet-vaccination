package app.civa.vaccination.domain

interface EfficacyVisitor {

    fun seeSpecies(species: Collection<Species>)
    fun seeAgents(agents: Collection<String>)

}
