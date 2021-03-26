package app.civa.vaccination.domain

interface EfficacyVisitor {

    infix fun seeSpecies(species: Collection<Species>)
    infix fun seeAgents(agents: Collection<String>)
}
