package app.civa.vaccination.domain

import java.util.*

class ApplicationNotFoundException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        private const val APPLICATION_NOT_FOUND = "Vaccine Application not found"

        infix fun from(applicationId: UUID) = ApplicationNotFoundException(
            message = APPLICATION_NOT_FOUND,
            expected = applicationId.toString(),
            actual = "null"
        )
    }

}
