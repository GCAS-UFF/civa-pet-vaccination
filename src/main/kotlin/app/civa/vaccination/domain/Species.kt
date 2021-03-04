package app.civa.vaccination.domain

enum class  Species {
    CANINE, FELINE
}

infix fun Collection<Species>.matches(species: Species) =
    this.contains(species)
