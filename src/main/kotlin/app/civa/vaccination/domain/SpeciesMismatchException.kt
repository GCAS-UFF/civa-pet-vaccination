package app.civa.vaccination.domain

class SpeciesMismatchException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        private const val SPECIES_DOESNT_MATCH = "Species doesn't match vaccine's species"

        fun from(expected: Species, actual: Collection<Species>) =
            SpeciesMismatchException(
                message = SPECIES_DOESNT_MATCH,
                expected = "$expected",
                actual = "$actual"
            )

    }
}
