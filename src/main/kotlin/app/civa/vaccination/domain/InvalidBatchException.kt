package app.civa.vaccination.domain

class InvalidBatchException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        infix fun from(value: String) =
            InvalidBatchException(
                message = "Batch doesn't match required pattern",
                expected = "ddd/dd",
                actual = value
            )
    }
}
