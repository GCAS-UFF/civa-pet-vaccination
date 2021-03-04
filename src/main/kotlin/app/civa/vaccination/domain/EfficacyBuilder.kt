package app.civa.vaccination.domain

interface EfficacyBuilder : Builder<Efficacy> {

    var species: Collection<Species>
    var agents: Collection<String>

}
