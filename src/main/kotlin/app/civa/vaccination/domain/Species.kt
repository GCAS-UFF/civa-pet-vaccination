package app.civa.vaccination.domain

enum class Species {
    CANINE, FELINE;
}

infix fun Collection<Species>.mustMatch(species: Species) = apply {
    when (this.contains(species)) {
        false -> throw SpeciesMismatchException.from(species, this)
    }
}
