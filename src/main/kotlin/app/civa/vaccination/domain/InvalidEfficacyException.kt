package app.civa.vaccination.domain

class InvalidEfficacyException (
    override val message: String,
    override val expected: String,
    override val actual: String
): DomainException(message, expected, actual) {

    constructor() : this(
        message = "Collection must not be empty",
        expected = "Collection with at least one element",
        actual = "Empty collection"
    )

}