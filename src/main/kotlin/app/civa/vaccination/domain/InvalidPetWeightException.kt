package app.civa.vaccination.domain

class InvalidPetWeightException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        private const val PET_WEIGHT_MUST_BE_POSITIVE =
            "Pet weight must be positive: 0 (ZERO) or greater"

        infix fun from(value: Double) = InvalidPetWeightException(
            message = PET_WEIGHT_MUST_BE_POSITIVE,
            expected = "$value to be bigger than 0.0",
            actual = "$value"
        )
    }
}
