package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.VALID

class InvalidApplicationException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        infix fun from(status: DateTimeStatus) = InvalidApplicationException(
            message = status.message,
            expected = VALID.message,
            actual = "$status"
        )
    }
}
