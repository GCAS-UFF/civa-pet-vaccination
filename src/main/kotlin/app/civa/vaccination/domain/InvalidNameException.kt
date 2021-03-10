package app.civa.vaccination.domain

class InvalidNameException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    constructor() : this(
        message = "Name must not be blank",
        expected = "Any string",
        actual = "\"\""
    )

}
